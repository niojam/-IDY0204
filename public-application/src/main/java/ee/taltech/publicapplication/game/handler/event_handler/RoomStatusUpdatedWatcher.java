package ee.taltech.publicapplication.game.handler.event_handler;

import ee.taltech.publicapplication.game.handler.event_handler.abstract_parent.AbstractEventHandler;
import ee.taltech.publicapplication.game.handler.event_handler.abstract_parent.AbstractProcessor;
import ee.taltech.publicapplication.game.model.RoomStatus;
import org.springframework.stereotype.Component;

@Component
public class RoomStatusUpdatedWatcher
        extends AbstractEventHandler<RoomStatusUpdatedWatcher.RoomStatusUpdateProcessor, RoomStatus> {

    public static class RoomStatusUpdateProcessor extends AbstractProcessor<RoomStatus> {
    }

    public void addProcessor(Long romId) {
        addProcessor(romId, new RoomStatusUpdateProcessor());
    }

}
