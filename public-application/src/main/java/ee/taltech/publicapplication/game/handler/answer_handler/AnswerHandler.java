package ee.taltech.publicapplication.game.handler.answer_handler;

import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.handler.Handler;
import ee.taltech.publicapplication.game.handler.event_handler.AnswerCounter;
import ee.taltech.publicapplication.game.model.AnswerFrequency;
import ee.taltech.publicapplication.game.model.PlayedAnswer;
import ee.taltech.publicapplication.game.model.Room;
import ee.taltech.publicapplication.game.model.dto.QuizDto;
import ee.taltech.publicapplication.game.service.AnswerFrequencyService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest.Code.BUSINESS_LOGIC_EXCEPTION;
import static java.lang.String.format;

@Component
public class AnswerHandler extends Handler<AnswerCounter.AnswerCounterProcessor, Long> {

    private final AnswerFrequencyService answerFrequencyService;
    private final Map<Long, Map<Long, AnswerFrequency>> answers = new HashMap<>(); // <roomId, <answerId, amountOfAnswers>>


    public AnswerHandler(AnswerCounter answerCounter, AnswerFrequencyService answerFrequencyService) {
        super(answerCounter);
        this.answerFrequencyService = answerFrequencyService;
    }

    public void setup(Long roomId, QuizDto quizDto) {
        Map<Long, AnswerFrequency> answersPerRoom = new HashMap<>();
        quizDto.getQuestions().forEach(question ->
                question.getAnswers().forEach(answer -> answersPerRoom.put(answer.getId(),
                        new AnswerFrequency()
                                .setAnswerId(answer.getId())
                                .setFrequency(new AtomicLong(0L))
                                .setQuestionId(answer.getQuestionId())
                                .setRoomId(roomId))));

        eventHandler.addProcessor(roomId);
        answers.put(roomId, answersPerRoom);
    }

    public void registerAnswer(Long roomId, Set<Long> answerIds) {
        answerIds.forEach(answerId -> {
            Map<Long, AnswerFrequency> answersMap = getAnswersMap(roomId);
            if (answersMap.containsKey(answerId)) {
                answersMap.get(answerId).incrementFrequency();
            }
        });

        eventHandler.getEventsInputStream(roomId).next(1L);
    }

    public Mono<Room> saveRoomAnswers(Room room, List<PlayedAnswer> playedAnswerList) {
        Long roomId = room.getId();
        return answerFrequencyService.saveAll(getAnswersMap(roomId).values().stream()
                .map(answerFrequency -> composeAnswerFrequency(answerFrequency, playedAnswerList))
                .collect(Collectors.toList()))
                .collectList()
                .map(any -> room);
    }

    public Map<Long, Long> getAnswersStatistics(Long roomId, Long questionId) {
        return getAnswersMap(roomId).values().stream()
                .filter(frequency -> frequency.getQuestionId().equals(questionId))
                .collect(HashMap::new,
                        (m, c) -> m.put(c.getAnswerId(), c.getFrequency().get()),
                        (m, u) -> {
                        });
    }

    public Room clean(Room room) {
        answers.remove(room.getId());
        completeProcessor(room);
        return room;
    }

    private Map<Long, AnswerFrequency> getAnswersMap(Long roomId) {
        return Optional.ofNullable(answers.get(roomId))
                .orElseThrow(() -> new BadRequest(
                        BUSINESS_LOGIC_EXCEPTION,
                        format("No answers found for roomId=%d", roomId)
                ));
    }

    private AnswerFrequency composeAnswerFrequency(AnswerFrequency answerFrequency, List<PlayedAnswer> playedAnswers) {
        PlayedAnswer playedAnswer = findPlayedAnswer(answerFrequency.getAnswerId(), playedAnswers);
        return new AnswerFrequency()
                .setAnswerId(playedAnswer.getId())
                .setQuestionId(playedAnswer.getQuestionId())
                .setRoomId(answerFrequency.getRoomId())
                .setFrequency(answerFrequency.getFrequency());

    }

    private PlayedAnswer findPlayedAnswer(Long parentAnswerId, List<PlayedAnswer> playedAnswers) {
        return playedAnswers.stream()
                .filter(playedAnswer -> playedAnswer.getParentAnswerId().equals(parentAnswerId))
                .findFirst()
                .orElseThrow();
    }

}
