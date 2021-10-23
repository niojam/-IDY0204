package ee.taltech.backoffice.game.service;

import ee.taltech.backoffice.game.controller.errorHandling.BadRequest;
import ee.taltech.backoffice.game.model.Answer;
import ee.taltech.backoffice.game.model.dto.AnswerDto;
import ee.taltech.backoffice.game.model.mapper.AnswerMapper;
import ee.taltech.backoffice.game.repository.AnswerRepository;
import ee.taltech.backoffice.game.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {

}
