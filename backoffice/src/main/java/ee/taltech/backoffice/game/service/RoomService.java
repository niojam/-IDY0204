package ee.taltech.backoffice.game.service;

import ee.taltech.backoffice.game.controller.errorHandling.BadRequest;
import ee.taltech.backoffice.game.model.Room;
import ee.taltech.backoffice.game.model.dto.RoomDto;
import ee.taltech.backoffice.game.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<RoomDto> getAuthorRooms(Long authorId) {
        return roomRepository.findAuthorRooms(authorId);
    }

    public void deleteRoom(Long roomId) {
        Room roomToDelete = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new BadRequest(BadRequest.Code.INVALID_ARGUMENT_EXCEPTION, "Room not found"));
        roomRepository.delete(roomToDelete);
    }

}
