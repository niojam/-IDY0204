package ee.taltech.publicapplication.game.handler.room;

import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.handler.Handler;
import ee.taltech.publicapplication.game.handler.event_handler.RoomStatusUpdatedWatcher;
import ee.taltech.publicapplication.game.model.Room;
import ee.taltech.publicapplication.game.model.RoomStatus;
import ee.taltech.publicapplication.game.model.dto.AnswerDto;
import ee.taltech.publicapplication.game.model.dto.PlayerDto;
import ee.taltech.publicapplication.game.model.dto.QuestionDto;
import ee.taltech.publicapplication.game.model.dto.QuizDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ee.taltech.publicapplication.common.Constants.QUIZ_FINISHED;
import static ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest.Code.BUSINESS_LOGIC_EXCEPTION;
import static ee.taltech.publicapplication.game.model.RoomStatus.OPEN;
import static java.lang.String.format;

@Component
public class RoomHandler extends Handler<RoomStatusUpdatedWatcher.RoomStatusUpdateProcessor, RoomStatus> {

    private final Map<Long, InMemoryRoom> rooms = new HashMap<>();
    private final Map<String, Long> roomPinToRoomIdMap = new HashMap<>();

    public RoomHandler(RoomStatusUpdatedWatcher roomStatusUpdatedWatcher) {
        super(roomStatusUpdatedWatcher);
    }

    public Mono<PlayerDto> addPlayer(Long roomId, Long playerId, String username) {
        InMemoryRoom room = rooms.get(roomId);
        if (room == null) {
            return Mono.error(new BadRequest(
                    BUSINESS_LOGIC_EXCEPTION,
                    format("Room with id=%s is not available", roomId)));
        }
        PlayerDto newPlayer = new PlayerDto()
                .setUsername(username)
                .setPlayerId(playerId);

        room.getPlayers().put(playerId, newPlayer);
        return Mono.just(newPlayer);
    }

    public void removePlayer(Long roomId, Long playerId) {
        // remove from players
        getInMemoryRoom(roomId).getPlayers().remove(playerId);
    }

    public Room setup(Room room) {
        rooms.put(room.getId(),
                new InMemoryRoom()
                        .setAuthorId(room.getAuthorId())
                        .setRoomPin(room.getPin())
                        .setRoomStatus(OPEN));
        roomPinToRoomIdMap.put(room.getPin(), room.getId());
        eventHandler.addProcessor(room.getId());
        return room;
    }

    public Room setRoomStatus(Room room, RoomStatus roomStatus) {
        getInMemoryRoom(room.getId()).setRoomStatus(roomStatus);
        return room;
    }

    public boolean contains(Long roomId) {
        return rooms.containsKey(roomId);
    }

    public RoomStatus getRoomStatus(Long roomId) {
        return getInMemoryRoom(roomId).getRoomStatus();
    }

    public Set<String> getAllRoomPins() {
        return roomPinToRoomIdMap.keySet();
    }

    public Long getRoomId(String roomPin) {
        return Optional.ofNullable(roomPinToRoomIdMap.get(roomPin))
                .orElseThrow(() -> new BadRequest(
                        BUSINESS_LOGIC_EXCEPTION,
                        format("No room found with roomPin=%s", roomPin)));
    }

    public void addQuiz(Long roomId, QuizDto quizDto) {
        InMemoryRoom inMemoryRoom = getInMemoryRoom(roomId);
        InMemoryQuiz inMemoryQuiz = new InMemoryQuiz();

        inMemoryQuiz.setSimplifiedQuizDto(new QuizDto()
                .setFirstQuestionId(quizDto.getFirstQuestionId())
                .setName(quizDto.getName())
                .setQuestions(quizDto
                        .getQuestions()
                        .stream()
                        .map(questionDto -> new QuestionDto()
                                .setId(questionDto.getId())
                                .setNextQuestionId(questionDto.getNextQuestionId())
                                .setQuestionType(questionDto.getQuestionType())
                                .setAnswers(questionDto
                                        .getAnswers()
                                        .stream()
                                        .map(answerDto -> new AnswerDto().setId(answerDto.getId()))
                                        .collect(Collectors.toList())))
                        .collect(Collectors.toList())));

        quizDto.getQuestions()
                .forEach(question -> {
                    InMemoryQuestion inMemoryQuestion = new InMemoryQuestion();

                    inMemoryQuestion.setCorrectAnswers(question.getAnswers()
                            .stream()
                            .filter(AnswerDto::getIsCorrect)
                            .map(AnswerDto::getId)
                            .collect(Collectors.toSet()));
                    inMemoryQuestion.setNextQuestionId(question.getNextQuestionId() != null ? question.getNextQuestionId() : QUIZ_FINISHED);
                    inMemoryQuestion.setReward(question.getReward());
                    inMemoryQuestion.setQuestionType(question.getQuestionType());
                    inMemoryQuestion.setTimeAlgorithm(question.getTimeAlgorithm());

                    inMemoryQuiz.getInMemoryQuestionMap().put(question.getId(), inMemoryQuestion);
                });
        inMemoryRoom.setQuiz(inMemoryQuiz);
        inMemoryRoom.setCurrentQuestionId(quizDto.getFirstQuestionId());
    }

    public Long setNextQuestionId(Long roomId) {
        InMemoryRoom room = getInMemoryRoom(roomId);
        Long nextCurrentQuestionId = getInMemoryQuestion(roomId, room.getCurrentQuestionId()).getNextQuestionId();
        room.setCurrentQuestionId(nextCurrentQuestionId);
        if (nextCurrentQuestionId.equals(QUIZ_FINISHED)) {
            return null;
        }
        return nextCurrentQuestionId;
    }

    public void removeRoomPin(Long roomId) {
        roomPinToRoomIdMap.keySet().stream()
                .filter(pin -> roomPinToRoomIdMap
                        .get(pin)
                        .equals(roomId)).findFirst()
                .ifPresent(roomPinToRoomIdMap::remove);
    }

    public void removeInMemoryRoom(Long roomId) {
        rooms.remove(roomId);
    }

    private InMemoryRoom getInMemoryRoom(Long roomId) {
        return Optional.ofNullable(rooms.get(roomId))
                .orElseThrow(() -> new BadRequest(
                        BUSINESS_LOGIC_EXCEPTION,
                        format("No room found with roomId=%d", roomId)));
    }

    public Long getCurrentQuestionId(Long roomId) {
        return getInMemoryRoom(roomId).getCurrentQuestionId();
    }

    public QuizDto getSimplifiedQuiz(Long roomId) {
        return getInMemoryRoom(roomId).getQuiz().getSimplifiedQuizDto();
    }

    public Integer getPlayersCount(Long roomId) {
        return getInMemoryRoom(roomId).getPlayers().size();
    }

    public boolean isCurrentQuestionId(Long roomId, Long questionId) {
        if (!questionId.equals(getCurrentQuestionId(roomId))) {
            throw new BadRequest(
                    BUSINESS_LOGIC_EXCEPTION,
                    format("Illegal questionId=%d", questionId));
        }
        return true;
    }

    public Room clean(Room room) {
        Long roomId = room.getId();
        removeRoomPin(roomId);
        removeInMemoryRoom(roomId);
        completeProcessor(room);
        return room;
    }

    public InMemoryQuestion getInMemoryQuestion(Long roomId, Long questionId) {
        return getInMemoryRoom(roomId).getQuiz().getInMemoryQuestionMap().get(questionId);
    }

    public InMemoryQuestion getCurrentMemoryQuestion(Long roomId) {
        InMemoryRoom inMemoryRoom = getInMemoryRoom(roomId);
        return inMemoryRoom.getQuiz().getInMemoryQuestionMap().get(inMemoryRoom.getCurrentQuestionId());
    }

    public Room setQuestionStartedAt(Room room, long currentTimeMillis) {
        getInMemoryQuestion(room.getId(), getInMemoryRoom(room.getId()).getCurrentQuestionId())
                .setStartedAt(currentTimeMillis);
        return room;
    }

}
