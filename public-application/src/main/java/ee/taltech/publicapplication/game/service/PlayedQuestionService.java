package ee.taltech.publicapplication.game.service;

import ee.taltech.publicapplication.game.model.PlayedQuestion;
import ee.taltech.publicapplication.game.model.dto.QuestionDto;
import ee.taltech.publicapplication.game.repository.PlayedQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayedQuestionService {

    private final PlayedQuestionRepository playedQuestionRepository;
    private final PlayedAnswerService playedAnswerService;

    public Flux<PlayedQuestion> saveQuestions(List<QuestionDto> questions, Long playedQuizId) {
        List<PlayedQuestion> playedQuestions = questions.stream().map(question -> new PlayedQuestion()
                .setQuestionType(question.getQuestionType())
                .setQuizId(playedQuizId)
                .setReward(question.getReward())
                .setText(question.getText())
                .setTimer(question.getTimer())
                .setTimeAlgorithm(question.getTimeAlgorithm())
                .setTitle(question.getTitle())
                .setParentQuestionId(question.getId()))
                .collect(Collectors.toList());

        return playedQuestionRepository.saveAll(playedQuestions);
    }

}