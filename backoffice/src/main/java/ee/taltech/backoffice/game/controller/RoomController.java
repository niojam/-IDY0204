package ee.taltech.backoffice.game.controller;


import ee.taltech.backoffice.game.model.dto.RoomDto;
import ee.taltech.backoffice.game.service.RoomService;
import ee.taltech.backoffice.security.filter.user_details.BackofficeUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/rooms")
    public List<RoomDto> getAuthorRooms(@AuthenticationPrincipal BackofficeUserDetails userDetails) {
        return roomService.getAuthorRooms(userDetails.getUserId());
    }

    @DeleteMapping
    public void deleteRoom(@RequestParam Long roomId, @AuthenticationPrincipal BackofficeUserDetails userDetails) {
        roomService.deleteRoom(roomId);
    }
}
