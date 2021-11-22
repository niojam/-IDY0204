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
        Quiz quiz = quizRepository.findByIdAndAuthorId(id, authorId)
                .orElseThrow(() -> new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION, "Quiz not found"));
        return new QuizDto(quiz).setQuestions(questionService.getQuestionsForQuiz(quiz.getId()));
    }

    public List<QuizDetails> getQuizzes(Long authorId) {
        List<QuizDetails> quizDetails = new ArrayList<>();
        List<Quiz> quizzes = quizRepository.findByAuthor(authorId);
        quizzes.forEach(quiz -> quizDetails.add(new QuizDetails()
                .setQuizId(quiz.getId())
                .setQuizName(quiz.getName())
                .setImageId(quiz.getImageId())));
        return quizDetails;
    }


    public void deleteQuiz(Long quizId, Long authorId) {
        Quiz quizToDelete = quizRepository.findByIdAndAuthorId(quizId, authorId)
                .orElseThrow(() -> new BadRequest(BadRequest.Code.INVALID_ARGUMENT_EXCEPTION,
                        String.format("Quiz with id %d was not found", quizId)));
        List<Room> rooms = roomRepository.findByQuizId(quizId);
        if (rooms.stream().allMatch(room -> room.getStatus().equals(RoomStatus.FINISHED)
                || room.getStatus().equals(RoomStatus.ABORTED))) {
            quizRepository.delete(quizToDelete);
        } else {
            throw new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION, "There are active rooms that use this quiz");
        }
    }

    public QuizDto createQuiz(QuizDto quizDto, Long userId) {
        quizDto.setAuthorId(userId);
        Quiz quizToSave = quizMapper.toEntity(quizDto);
        Quiz savedQuiz = quizRepository.save(quizToSave);
        quizDto.getQuestions().forEach(q -> q.setQuizId(savedQuiz.getId()));
        List<QuestionDto> questions = questionService.saveQuestions(quizDto.getQuestions());
        if (!questions.isEmpty()) {
            savedQuiz.setFirstQuestionId(questions.get(0).getId());
            quizDto.setFirstQuestionId(questions.get(0).getId());
        }
        quizRepository.save(savedQuiz);
        return quizDto.setId(savedQuiz.getId()).setQuestions(questions);
    }

    public Quiz getQuiz(Long id) {
        return quizRepository
                .findById(id)
                .orElseThrow(() -> new BadRequestException("no quiz found"));
    }

}

