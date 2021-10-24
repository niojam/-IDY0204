package ee.taltech.backoffice.game.service;

import com.github.dockerjava.api.exception.BadRequestException;
import ee.taltech.backoffice.game.controller.errorHandling.BadRequest;
import ee.taltech.backoffice.game.model.Quiz;
import ee.taltech.backoffice.game.model.Room;
import ee.taltech.backoffice.game.model.RoomStatus;
import ee.taltech.backoffice.game.model.dto.QuestionDto;
import ee.taltech.backoffice.game.model.dto.QuizDetails;
import ee.taltech.backoffice.game.model.dto.QuizDto;
import ee.taltech.backoffice.game.model.mapper.QuizMapper;
import ee.taltech.backoffice.game.repository.QuizRepository;
import ee.taltech.backoffice.game.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper = Mappers.getMapper(QuizMapper.class);
    private final QuestionService questionService;
    private final RoomRepository roomRepository;

    public QuizDto getQuiz(Long id, Long authorId) {
        return null;

    }

    public List<QuizDetails> getQuizzes(Long authorId) {
        return null;
    }


    public void deleteQuiz(Long quizId, Long authorId) {
    }

    public QuizDto createQuiz(QuizDto quizDto, Long userId) {
       return null; // Mehod to test
    }

    public Quiz getQuiz(Long id) {
        return null;
    }


}

