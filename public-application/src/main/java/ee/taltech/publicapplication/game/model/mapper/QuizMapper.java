package ee.taltech.publicapplication.game.model.mapper;

import ee.taltech.publicapplication.game.model.Quiz;
import ee.taltech.publicapplication.game.model.dto.QuizDto;
import org.mapstruct.Mapper;

@Mapper
public interface QuizMapper extends AbstractMapper<Quiz, QuizDto> {
}
