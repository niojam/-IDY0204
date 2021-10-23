package ee.taltech.publicapplication.game.handler.playerScores;

import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.handler.Handler;
import ee.taltech.publicapplication.game.handler.event_handler.ScoreEmitterMap;
import ee.taltech.publicapplication.game.handler.playerScores.quetstionTypeStrategy.QuestionTypeStrategy;
import ee.taltech.publicapplication.game.handler.playerScores.quetstionTypeStrategy.QuestionTypeStrategyFactory;
import ee.taltech.publicapplication.game.handler.playerScores.timeAlgorithmStrategy.TimeAlgorithmStrategy;
import ee.taltech.publicapplication.game.handler.playerScores.timeAlgorithmStrategy.TimeAlgorithmStrategyFactory;
import ee.taltech.publicapplication.game.handler.room.InMemoryQuestion;
import ee.taltech.publicapplication.game.handler.room.RoomHandler;
import ee.taltech.publicapplication.game.model.Room;
import ee.taltech.publicapplication.game.model.Score;
import ee.taltech.publicapplication.game.model.dto.give_answer.GiveAnswerRequest;
import ee.taltech.publicapplication.game.security.user_details.PublicAppUserDetails;
import ee.taltech.publicapplication.game.service.ScoreService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

import static ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest.Code.BUSINESS_LOGIC_EXCEPTION;
import static java.lang.String.format;

@Component
public class ScoreHandler extends Handler<ScoreEmitterMap.ScoreEvaluatedProcessor, Boolean> {

    private final RoomHandler roomHandler;
    private final ScoreService scoreService;
    private final Map<Long, Set<Long>> playersAnswered = new HashMap<>(); // <roomId, Set<playerId>>
    private final Map<Long, Map<Long, InMemoryScore>> scores = new HashMap<>(); // <roomId, <playerId, Score>>

    private final QuestionTypeStrategyFactory questionTypeStrategyFactory;
    private final TimeAlgorithmStrategyFactory timeAlgorithmStrategyFactory;

    public ScoreHandler(ScoreEmitterMap scoreEmitterMap,
                        RoomHandler roomHandler,
                        ScoreService scoreService, QuestionTypeStrategyFactory questionTypeStrategyFactory, TimeAlgorithmStrategyFactory timeAlgorithmStrategyFactory) {
        super(scoreEmitterMap);
        this.roomHandler = roomHandler;
        this.scoreService = scoreService;
        this.questionTypeStrategyFactory = questionTypeStrategyFactory;
        this.timeAlgorithmStrategyFactory = timeAlgorithmStrategyFactory;
    }

    public void setup(Long roomId) {
        scores.put(roomId, new HashMap<>());
        eventHandler.addProcessor(roomId);
        playersAnswered.put(roomId, new HashSet<>());
    }

    public Long createPlayerScore(Long roomId, Long playerId, String username) {
        getScore(roomId)
                .put(playerId,
                        new InMemoryScore()
                                .setPlayerId(playerId)
                                .setUsername(username));
        return roomId;
    }

    public InMemoryScore getScore(Long roomId, Long playerId) {
        return Optional.ofNullable(getScore(roomId).get(playerId))
                .orElseThrow(() -> new BadRequest(
                        BUSINESS_LOGIC_EXCEPTION,
                        format("No score found for roomId=%d and player=%d", roomId, playerId)
                ));
    }

    public List<InMemoryScore> getTopNScores(Long roomId, Integer n) {
        return getScore(roomId).values().stream()
                .sorted(Comparator.comparing(InMemoryScore::getTotalScore).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public Room clearRoomPlayersAnswered(Room room) {
        Long roomId = room.getId();
        Set<Long> playersAnswered = getPlayersAnswered(roomId);
        Map<Long, InMemoryScore> roomScore = getScore(roomId);
        InMemoryQuestion inMemoryQuestion = roomHandler.getCurrentMemoryQuestion(roomId);

        roomScore.keySet().stream()
                .filter(playerId -> !playersAnswered.contains(playerId))
                .map(roomScore::get)
                .forEach(inMemoryScore -> inMemoryScore.wronglyAnswered(inMemoryQuestion.getCorrectAnswers().size()));
        playersAnswered.clear();
        getScore(roomId).values().forEach(score -> score.setReward(0L));
        return room;
    }

    public Mono<GiveAnswerRequest> updatePlayerScore(PublicAppUserDetails userDetails, GiveAnswerRequest answerRequest) {
        Set<Long> answerIds = answerRequest.getAnswerIds();
        Long questionId = answerRequest.getQuestionId();
        Long playerId = userDetails.getUserId();
        Long roomId = userDetails.getRoomId();

        Set<Long> playersAnswered = getPlayersAnswered(roomId);
        if (playersAnswered.contains(playerId)) {
            return Mono.error(new BadRequest(
                    BUSINESS_LOGIC_EXCEPTION,
                    format("Player with playerId=%d has already answered", playerId)));
        }

        InMemoryScore playerScore = getScore(roomId, playerId);

        InMemoryQuestion inMemoryQuestion = roomHandler.getInMemoryQuestion(roomId, questionId);

        QuestionTypeStrategy questionTypeStrategy = questionTypeStrategyFactory.getStrategy(inMemoryQuestion.getQuestionType());
        TimeAlgorithmStrategy timeAlgorithmStrategy = timeAlgorithmStrategyFactory.getStrategy(inMemoryQuestion.getTimeAlgorithm());

        QuestionScore questionScore = questionTypeStrategy.calculateReward(inMemoryQuestion.getReward(), inMemoryQuestion.getCorrectAnswers(), answerIds);
        long rewardWithPenalty = timeAlgorithmStrategy.calculateReward(
                questionScore.getReward(),
                System.currentTimeMillis() - inMemoryQuestion.getStartedAt());

        playerScore.setReward(rewardWithPenalty);
        playerScore.increaseScore(rewardWithPenalty);
        playerScore.correctlyAnswered(questionScore.getCorrectlyAnswered());
        playerScore.wronglyAnswered(questionScore.getWronglyAnswered());

        getPlayersAnswered(roomId).add(userDetails.getUserId());
        return Mono.just(answerRequest);
    }

    public Room clean(Room room) {
        Long roomId = room.getId();
        removePlayersAnswered(roomId);
        removeRomeScore(roomId);
        completeProcessor(room);
        return room;
    }

    public void removePlayerStatistics(Long roomId, Long playerId) {
        getScore(roomId).remove(playerId);
    }

    public Mono<Room> saveRoomScore(Room room) {
        Long roomId = room.getId();
        return scoreService
                .saveAll(getScore(roomId).values()
                        .stream()
                        .map(inMemoryScore -> new Score()
                                .setCorrectAnswers(inMemoryScore.getCorrectAnswers())
                                .setWrongAnswers(inMemoryScore.getWrongAnswers())
                                .setPlayerId(inMemoryScore.getPlayerId())
                                .setRoomId(roomId)
                                .setScore(inMemoryScore.getTotalScore()))
                        .collect(Collectors.toList()))
                .collectList()
                .map(any -> room);
    }

    private void removeRomeScore(Long roomId) {
        scores.remove(roomId);
    }

    private void removePlayersAnswered(Long roomId) {
        playersAnswered.remove(roomId);
    }

    private Map<Long, InMemoryScore> getScore(Long roomId) {
        return Optional.ofNullable(scores.get(roomId))
                .orElseThrow(() -> new BadRequest(
                        BUSINESS_LOGIC_EXCEPTION,
                        format("No score found for roomId=%d", roomId)
                ));
    }

    private Set<Long> getPlayersAnswered(Long roomId) {
        return Optional.ofNullable(playersAnswered.get(roomId))
                .orElseThrow(() -> new BadRequest(
                        BUSINESS_LOGIC_EXCEPTION,
                        format("No playersAnswered found for roomId=%d", roomId)
                ));
    }

}
