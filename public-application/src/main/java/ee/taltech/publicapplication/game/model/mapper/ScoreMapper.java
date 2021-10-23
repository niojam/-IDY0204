package ee.taltech.publicapplication.game.model.mapper;

import ee.taltech.publicapplication.game.handler.playerScores.InMemoryScore;
import ee.taltech.publicapplication.game.model.dto.ScoreDto;
import org.mapstruct.Mapper;

@Mapper
public interface ScoreMapper extends AbstractMapper<InMemoryScore, ScoreDto> {
}
