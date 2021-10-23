package ee.taltech.publicapplication.game.handler.black_list_handler;

import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.model.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest.Code.BUSINESS_LOGIC_EXCEPTION;
import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class BlackListHandler {

    private final Map<Long, Set<Long>> blackList = new HashMap<>();

    public Room setup(Room room) {
        blackList.put(room.getId(), new HashSet<>());
        return room;
    }

    public void addPlayer(Long roomId, Long playerId) {
        getBlackList(roomId).add(playerId);
    }

    public Boolean containsPlayer(Long roomId, Long playerId) {
        return getBlackList(roomId).contains(playerId);
    }

    public Room clean(Room room) {
        blackList.remove(room.getId());
        return room;
    }

    private Set<Long> getBlackList(Long roomId) {
        if (blackList.containsKey(roomId)) {
            return blackList.get(roomId);
        } else {
            throw new BadRequest(
                    BUSINESS_LOGIC_EXCEPTION,
                    format("No black-list for with roomId=%d found", roomId));
        }
    }

}
