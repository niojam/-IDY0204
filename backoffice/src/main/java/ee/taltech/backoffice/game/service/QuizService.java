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

}

