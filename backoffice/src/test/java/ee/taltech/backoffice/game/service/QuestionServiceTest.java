package ee.taltech.backoffice.game.service;


import ee.taltech.backoffice.game.controller.errorHandling.BadRequest;
import ee.taltech.backoffice.game.model.Question;
import ee.taltech.backoffice.game.model.QuestionType;
import ee.taltech.backoffice.game.model.Quiz;
import ee.taltech.backoffice.game.model.dto.AnswerDto;
import ee.taltech.backoffice.game.model.dto.QuestionDto;
import ee.taltech.backoffice.game.repository.QuestionRepository;
import ee.taltech.backoffice.game.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    private QuestionService questionService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerService answerService;

    @Mock
    private QuizRepository quizRepository;

    private List<AnswerDto> answerDtoList;


    @BeforeEach
    public void beforeEach() {
        questionService = new QuestionService(questionRepository, quizRepository, answerService);
        answerDtoList = new ArrayList<>(Arrays.asList(
                new AnswerDto().setText("first").setIsCorrect(false),
                new AnswerDto().setText("second").setIsCorrect(false),
                new AnswerDto().setText("third").setIsCorrect(true),
                new AnswerDto().setText("fourth").setIsCorrect(false)
        ));
    }

    @Test
    public void testSaveQuestions() {
        List<QuestionDto> questionDtos = new ArrayList<>(Arrays.asList(
                new QuestionDto()
                        .setText("text")
                        .setTitle("title")
                        .setAnswers(answerDtoList)
                        .setQuestionType(QuestionType.SINGLE_MATCH),
                new QuestionDto()
                        .setText("textTwo")
                        .setTitle("titleTwo")
                        .setAnswers(answerDtoList)
                        .setQuestionType(QuestionType.SINGLE_MATCH),
                new QuestionDto()
                        .setText("textThree")
                        .setTitle("titleThree")
                        .setAnswers(answerDtoList)
                        .setQuestionType(QuestionType.SINGLE_MATCH)
        ));
        List<Question> questions = new ArrayList<>(Arrays.asList(
                new Question()
                        .setId(1L)
                        .setQuizId(1L)
                        .setText("text")
                        .setTitle("title")
                        .setQuestionType(QuestionType.SINGLE_MATCH),
                new Question()
                        .setId(2L)
                        .setQuizId(1L)
                        .setText("textTwo")
                        .setTitle("titleTwo")
                        .setQuestionType(QuestionType.SINGLE_MATCH),
                new Question()
                        .setId(3L)
                        .setQuizId(1L)
                        .setText("textThree")
                        .setTitle("titleThree")
                        .setQuestionType(QuestionType.SINGLE_MATCH)
        ));
        when(questionRepository.saveAll(anyList())).thenReturn(questions);
        List<QuestionDto> result = questionService.saveQuestions(questionDtos);
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.stream().noneMatch(q -> q.getId() == null)).isTrue();
    }


    @Test
    public void testSaveQuestionsAtLeastOneCorrectAnswer() {
        List<AnswerDto> answerDtoListWithoutCorrect = new ArrayList<>(Arrays.asList(
                new AnswerDto().setText("1").setIsCorrect(false),
                new AnswerDto().setText("2").setIsCorrect(false),
                new AnswerDto().setText("3").setIsCorrect(false),
                new AnswerDto().setText("4").setIsCorrect(false)
        ));

        List<QuestionDto> questionDtos = new ArrayList<>(Arrays.asList(
                new QuestionDto()
                        .setText("text")
                        .setTitle("title")
                        .setAnswers(answerDtoListWithoutCorrect)
                        .setQuestionType(QuestionType.SINGLE_MATCH),
                new QuestionDto()
                        .setText("textTwo")
                        .setTitle("titleTwo")
                        .setAnswers(answerDtoList)
                        .setQuestionType(QuestionType.SINGLE_MATCH),
                new QuestionDto()
                        .setText("textThree")
                        .setTitle("titleThree")
                        .setAnswers(answerDtoList)
                        .setQuestionType(QuestionType.SINGLE_MATCH)
        ));
        assertThrows(BadRequest.class, () -> questionService.saveQuestions(questionDtos));
    }

    @Test
    public void testSaveQuestionsOnlyOneQuestion() {
        List<QuestionDto> questionDtos = new ArrayList<>(List.of(
                new QuestionDto()
                        .setText("text")
                        .setTitle("title")
                        .setAnswers(answerDtoList)
                        .setQuestionType(QuestionType.SINGLE_MATCH)
        ));
        List<Question> questions = new ArrayList<>(List.of(
                new Question()
                        .setId(1L)
                        .setQuizId(1L)
                        .setText("text")
                        .setTitle("title")
                        .setQuestionType(QuestionType.SINGLE_MATCH)));
        when(questionRepository.saveAll(anyList())).thenReturn(questions);
        List<QuestionDto> result = questionService.saveQuestions(questionDtos);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.stream().noneMatch(q -> q.getId() == null)).isTrue();
    }

    @Test
    public void testDeleteOneQuestion() {
        List<Question> questions = new ArrayList<>(Collections.singletonList(
                new Question().setQuizId(-1L).setId(-1L).setText("text")
        ));
        when(questionRepository.findByQuizId(-1L)).thenReturn(questions);
        questionService.deleteQuestions(-1L);
        verify(questionRepository, times(1))
                .deleteAll(questions);
    }

    @Test
    public void testDeleteQuestions() {
        List<Question> questions = new ArrayList<>(List.of(
                new Question().setQuizId(-1L).setId(-1L).setText("text"),
                new Question().setQuizId(-1L).setId(-2L).setText("text2")
        ));
        when(questionRepository.findByQuizId(-1L)).thenReturn(questions);
        questionService.deleteQuestions(-1L);
        verify(questionRepository, times(1))
                .deleteAll(questions);
    }


    @Test
    public void testEditQuestion() {
        QuestionDto returnedQuestionDto = questionService.editQuestion(
                new QuestionDto()
                        .setId(100L)
                        .setAnswers(List.of(new AnswerDto()
                                .setId(-1L)
                                .setText("Something")
                                .setQuestionId(100L)
                                .setIsCorrect(true))));
        assertThat(returnedQuestionDto.getId()).isEqualTo(100L);
    }

    @Test
    public void testEditQuestionNoCorrectAnswer() {
        assertThrows(BadRequest.class, () -> questionService.editQuestion(
                new QuestionDto()
                        .setId(100L)
                        .setAnswers(List.of(new AnswerDto()
                                .setId(-1L)
                                .setText("Something")
                                .setQuestionId(100L)
                                .setIsCorrect(false)))));
    }

    @Test
    public void testEditQuestionDoesNotExit() {
        when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BadRequest.class, () -> questionService.editQuestion(
                new QuestionDto()
                        .setId(100L)
                        .setAnswers(List.of(new AnswerDto()
                                .setId(-1L)
                                .setText("Something")
                                .setQuestionId(100L)
                                .setIsCorrect(false)))));
    }


}
