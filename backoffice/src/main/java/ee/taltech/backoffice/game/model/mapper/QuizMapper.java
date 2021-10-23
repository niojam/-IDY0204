package ee.taltech.backoffice.game.model.mapper;

import ee.taltech.backoffice.game.model.Quiz;
import ee.taltech.backoffice.game.model.dto.QuizDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper
public interface QuizMapper extends AbstractMapper<Quiz, QuizDto> {

    @Override
    @Mapping(target = "questions", ignore = true)
    QuizDto toDto(Quiz source);

    @Override
    Quiz toEntity(QuizDto dto);
}
