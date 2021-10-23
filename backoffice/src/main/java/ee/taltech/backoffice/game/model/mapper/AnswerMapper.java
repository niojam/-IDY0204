package ee.taltech.backoffice.game.model.mapper;

import ee.taltech.backoffice.game.model.Answer;
import ee.taltech.backoffice.game.model.dto.AnswerDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AnswerMapper extends AbstractMapper<Answer, AnswerDto> {

    List<Answer> toAnswerList(List<AnswerDto> answers);

    List<AnswerDto> toAnswerDtoList(Iterable<Answer> answers);

    @Override
    AnswerDto toDto(Answer source);

    @Override
    Answer toEntity(AnswerDto dto);
}
