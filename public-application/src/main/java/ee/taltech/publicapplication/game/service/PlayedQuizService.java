package ee.taltech.publicapplication.game.service;

import ee.taltech.publicapplication.game.handler.answer_handler.AnswerHandler;
import ee.taltech.publicapplication.game.model.PlayedAnswer;
import ee.taltech.publicapplication.game.model.PlayedQuiz;
import ee.taltech.publicapplication.game.model.Room;
import ee.taltech.publicapplication.game.model.dto.AnswerDto;
import ee.taltech.publicapplication.game.model.dto.QuestionDto;
import ee.taltech.publicapplication.game.model.dto.QuizDto;
import ee.taltech.publicapplication.game.repository.PlayedQuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayedQuizService {

    private final PlayedQuizRepository playedQuizRepository;
    private final QuizService quizService;
    private final PlayedQuestionService playedQuestionService;
    private final PlayedAnswerService playedAnswerService;
    private final AnswerHandler answerHandler;

    public Mono<Room> saveQuiz(Room room) {
        return Flux.from(quizService.findById(room.getQuizId()))
                .flatMap(quizDto -> saveQuiz(quizDto, room))
                .collectList()
                .flatMap(playedAnswers -> answerHandler.saveRoomAnswers(room, playedAnswers))
                .map(any -> room);
    }

    private Flux<PlayedAnswer> saveQuiz(QuizDto quiz, Room room) {
        PlayedQuiz playedQuiz = new PlayedQuiz()
                .setName(quiz.getName())
                .setAuthorId(quiz.getAuthorId());
        return Flux.from(playedQuizRepository.save(playedQuiz))
                .flatMap(savedQuiz -> {
                    room.setPlayedQuizId(savedQuiz.getId());
                    return playedQuestionService.saveQuestions(quiz.getQuestions(), savedQuiz.getId());
                })
                .buffer()
                .flatMap(questions -> playedAnswerService.saveAnswers(getAnswers(quiz), questions));
    }

    private List<AnswerDto> getAnswers(QuizDto quizDto) {
        return quizDto.getQuestions().stream()
                .map(QuestionDto::getAnswers)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

}
