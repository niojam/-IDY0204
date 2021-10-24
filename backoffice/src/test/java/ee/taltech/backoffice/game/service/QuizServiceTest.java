package ee.taltech.backoffice.game.service;

import ee.taltech.backoffice.game.model.Quiz;
import ee.taltech.backoffice.game.model.dto.QuizDto;
import ee.taltech.backoffice.game.repository.QuizRepository;
import ee.taltech.backoffice.game.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


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
    public void testSaveQuiz() {
        QuizDto testQuizDto = new QuizDto().setName("megaquiz").setAuthorId(-1L);
        when(quizRepository.save(any(Quiz.class))).thenReturn(new Quiz(testQuizDto));
        QuizDto createdQuiz = quizService.createQuiz(testQuizDto, -1L);
        assertThat(createdQuiz.getName()).isEqualTo(testQuizDto.getName());
    }


}
