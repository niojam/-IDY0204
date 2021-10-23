package ee.taltech.backoffice.game.model.mapper;

import ee.taltech.backoffice.game.model.Room;
import ee.taltech.backoffice.game.model.dto.RoomDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface RoomMapper extends AbstractMapper<Room, RoomDto>{


    List<RoomDto> toDtoList(List<Room> rooms);

    @Override
    @Mapping(target = "quizName", ignore = true)
    RoomDto toDto(Room source);
}
