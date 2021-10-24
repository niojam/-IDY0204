package ee.taltech.backoffice.game.service;

import ee.taltech.backoffice.game.model.Quiz;
import ee.taltech.backoffice.game.model.dto.QuestionDto;
import ee.taltech.backoffice.game.model.dto.QuizDto;
import ee.taltech.backoffice.game.repository.QuizRepository;
import ee.taltech.backoffice.game.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {

    QuizService quizService;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestionService questionService;
    @Mock
    private RoomRepository roomRepository;

    @BeforeEach
    public void beforeEach() {
        quizService = new QuizService(quizRepository, questionService, roomRepository);
    }


    @Test
    public void testSaveQuizNoQuestions() {
        QuizDto testQuizDto = new QuizDto().setName("megaquiz").setAuthorId(-1L);
        when(quizRepository.save(any(Quiz.class))).thenReturn(new Quiz(testQuizDto));
        QuizDto createdQuiz = quizService.createQuiz(testQuizDto, -1L);
        assertThat(createdQuiz.getName()).isEqualTo(testQuizDto.getName());
    }

    @Test
    public void testSaveQuizWithQuestions() {
        List<QuestionDto> questionDtos = new ArrayList<>(Collections.singletonList(
                new QuestionDto()
                        .setTitle("first")
                        .setText("question text")
                        .setNextQuestionId(null)
        ));
        QuizDto testQuizDto = new QuizDto().setName("megaquiz").setAuthorId(-1L).setQuestions(questionDtos);
        when(quizRepository.save(any(Quiz.class))).thenReturn(new Quiz(testQuizDto));
        when(questionService.saveQuestions(anyList())).thenReturn(questionDtos);
        QuizDto createdQuiz = quizService.createQuiz(testQuizDto, -1L);
        assertThat(createdQuiz.getName()).isEqualTo(testQuizDto.getName());
        assertThat(createdQuiz.getQuestions().size()).isEqualTo(1);
    }


}
