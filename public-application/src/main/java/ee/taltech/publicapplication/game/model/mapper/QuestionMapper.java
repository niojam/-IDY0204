package ee.taltech.publicapplication.game.model.mapper;

import ee.taltech.publicapplication.game.model.Question;
import ee.taltech.publicapplication.game.model.dto.QuestionDto;
import org.mapstruct.Mapper;

@Mapper
public interface QuestionMapper extends AbstractMapper<Question, QuestionDto> {

}
