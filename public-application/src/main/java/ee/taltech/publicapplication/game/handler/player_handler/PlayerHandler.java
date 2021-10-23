package ee.taltech.publicapplication.game.handler.player_handler;

import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import ee.taltech.publicapplication.game.handler.Handler;
import ee.taltech.publicapplication.game.handler.event_handler.PlayerWatcher;
import ee.taltech.publicapplication.game.model.Room;
import ee.taltech.publicapplication.game.model.dto.PlayerDto;
import ee.taltech.publicapplication.game.model.dto.watch_players.WatchPlayerResponse;
import org.springframework.stereotype.Component;

import java.util.*;

import static ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest.Code.BUSINESS_LOGIC_EXCEPTION;
import static java.lang.String.format;

@Component
public class PlayerHandler extends Handler<PlayerWatcher.PlayerWatcherProcessor, WatchPlayerResponse> {

    private final Map<Long, Set<String>> roomIdToRoomUsernames = new HashMap<>();

    public PlayerHandler(PlayerWatcher playerWatcher) {
        super(playerWatcher);
    }

    public Room setup(Room room) {
        roomIdToRoomUsernames.put(room.getId(), new HashSet<>());
        eventHandler.addProcessor(room.getId());
        return room;
    }

    public PlayerDto addPlayer(Long roomId, PlayerDto player) {
        getUsernames(roomId).add(player.getUsername());

        eventHandler.getEventsInputStream(roomId).next(
                new WatchPlayerResponse()
                        .setPlayerId(player.getPlayerId())
                        .setUsername(player.getUsername())
                        .setStatus(WatchPlayerResponse.Status.ADDED)
        );

        return player;
    }

    public Boolean isUsernameNotAvailable(Long roomId, String username) {
        return getUsernames(roomId).contains(username);
    }

    private Set<String> getUsernames(Long roomId) {
        return Optional.ofNullable(roomIdToRoomUsernames.get(roomId))
                .orElseThrow(() -> new BadRequest(
                        BUSINESS_LOGIC_EXCEPTION,
                        format("No usernames found for roomId=%d", roomId)));
    }

    public void removeUsernames(Long roomId) {
        roomIdToRoomUsernames.remove(roomId);
    }

    public void removePlayer(Long roomId, Long playerId, String username) {
        // make username available
        getUsernames(roomId).remove(username);
    }

    public Room clean(Room room) {
        completeProcessor(room);
        removeUsernames(room.getId());
        return room;
    }

}
