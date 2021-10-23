package ee.taltech.backoffice.game.model.mapper;

import ee.taltech.backoffice.game.model.Question;
import ee.taltech.backoffice.game.model.dto.QuestionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface QuestionMapper extends AbstractMapper<Question, QuestionDto> {

    List<Question> toQuestionList(List<QuestionDto> questions);

    List<QuestionDto> toQuestionDtoList(List<Question> questions);

    @Override
    @Mapping(target = "answers", ignore = true)
    QuestionDto toDto(Question source);

    @Override
    @Mapping(target = "questionType", source = "questionType", defaultValue = "SINGLE_MATCH")
    @Mapping(target = "timeAlgorithm", source = "timeAlgorithm", defaultValue = "CONSTANT")
    @Mapping(target = "timer", source = "timer", defaultValue = "45L")
    @Mapping(target = "reward", source = "reward", defaultValue = "100L")
    Question toEntity(QuestionDto dto);

}
