package ee.taltech.publicapplication.game.model.mapper;

import ee.taltech.publicapplication.game.model.Answer;
import ee.taltech.publicapplication.game.model.dto.AnswerDto;
import org.mapstruct.Mapper;

@Mapper
public interface AnswerMapper extends AbstractMapper<Answer, AnswerDto> {

}
