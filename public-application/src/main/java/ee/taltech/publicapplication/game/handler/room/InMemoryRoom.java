package ee.taltech.publicapplication.game.handler.room;

import ee.taltech.publicapplication.game.model.RoomStatus;
import ee.taltech.publicapplication.game.model.dto.PlayerDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class InMemoryRoom {

    private Long authorId;
    private String roomPin;
    private RoomStatus roomStatus;
    private InMemoryQuiz quiz;
    private Long currentQuestionId;
    private Map<Long, PlayerDto> players = new HashMap<>();

}
