package ee.taltech.backoffice.game.service;

import ee.taltech.backoffice.game.controller.errorHandling.BadRequest;
import ee.taltech.backoffice.game.model.Question;
import ee.taltech.backoffice.game.model.Quiz;
import ee.taltech.backoffice.game.model.dto.AnswerDto;
import ee.taltech.backoffice.game.model.dto.QuestionDto;
import ee.taltech.backoffice.game.model.mapper.QuestionMapper;
import ee.taltech.backoffice.game.repository.QuestionRepository;
import ee.taltech.backoffice.game.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {


    public static final int MIN_CORRECT_ANSWERS_COUNT = 1;
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;
    private final QuestionMapper questionMapper = Mappers.getMapper(QuestionMapper.class);
    private final AnswerService answerService;

    public QuestionDto getQuestion(Long id) {
        return null;
    }

    public List<QuestionDto> getQuestionsForQuiz(Long id) {
        return null;
    }


    public List<QuestionDto> saveQuestions(List<QuestionDto> questionDtos) {
        return null;

    }

    public QuestionDto editQuestion(QuestionDto questionDto) {
        return null;

    }

    public void deleteQuestion(Long id, Long quizId) {
    }
    public void deleteQuestions(Long quizId) {
    }

    public QuestionDto addQuestion(QuestionDto question, Long quizId) {
        return null;
    }


}
