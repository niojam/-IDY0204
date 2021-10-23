package ee.taltech.publicapplication.game.service;

import ee.taltech.publicapplication.game.model.PlayedAnswer;
import ee.taltech.publicapplication.game.model.PlayedQuestion;
import ee.taltech.publicapplication.game.model.dto.AnswerDto;
import ee.taltech.publicapplication.game.repository.PlayedAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayedAnswerService {

    private final PlayedAnswerRepository playedAnswerRepository;

    public Flux<PlayedAnswer> saveAnswers(List<AnswerDto> answers, List<PlayedQuestion> playedQuestions) {
        List<PlayedAnswer> playedAnswers = answers.stream()
                .map(answer -> new PlayedAnswer()
                        .setParentAnswerId(answer.getId())
                        .setText(answer.getText())
                        .setIsCorrect(answer.getIsCorrect())
                        .setQuestionId(findQuestionId(playedQuestions, answer.getQuestionId())))
                .collect(Collectors.toList());
        return playedAnswerRepository.saveAll(playedAnswers);
    }

    private Long findQuestionId(List<PlayedQuestion> playedQuestions, Long parentQuestionId) {
        return playedQuestions.stream()
                .filter(question -> question.getParentQuestionId().equals(parentQuestionId))
                .findFirst()
                .orElseThrow()
                .getId();
    }

}
