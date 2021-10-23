package ee.taltech.publicapplication.game.handler.event_handler;

import ee.taltech.publicapplication.game.handler.event_handler.abstract_parent.AbstractEventHandler;
import ee.taltech.publicapplication.game.handler.event_handler.abstract_parent.AbstractProcessor;
import ee.taltech.publicapplication.game.model.dto.watch_players.WatchPlayerResponse;
import org.springframework.stereotype.Component;

@Component
public class PlayerWatcher
        extends AbstractEventHandler<PlayerWatcher.PlayerWatcherProcessor, WatchPlayerResponse> {

    public static class PlayerWatcherProcessor extends AbstractProcessor<WatchPlayerResponse> {
    }

    public void addProcessor(Long romId) {
        addProcessor(romId, new PlayerWatcherProcessor());
    }

}
