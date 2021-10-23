package ee.taltech.publicapplication.game.handler;

import ee.taltech.publicapplication.game.handler.event_handler.abstract_parent.AbstractEventHandler;
import ee.taltech.publicapplication.game.handler.event_handler.abstract_parent.AbstractProcessor;
import ee.taltech.publicapplication.game.model.Room;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.FluxSink;

public abstract class Handler<Processor extends AbstractProcessor<EmittedEvent>, EmittedEvent> {

    protected final AbstractEventHandler<Processor, EmittedEvent> eventHandler;

    public Handler(AbstractEventHandler<Processor, EmittedEvent> eventHandler) {
        this.eventHandler = eventHandler;
    }

    public DirectProcessor<EmittedEvent> getProcessor(Long roomId) {
        return eventHandler.getDirectProcessor(roomId);
    }

    public FluxSink<EmittedEvent> getEvents(Long roomId) {
        return eventHandler.getEventsInputStream(roomId);
    }

    public Room emit(Room room, EmittedEvent value) {
        getEvents(room.getId()).next(value);
        return room;
    }

    public Room completeProcessor(Room room) {
        Long roomId = room.getId();
        if (eventHandler.contains(roomId)) {
            getEvents(roomId).complete();
            eventHandler.removeProcessor(roomId);
        }
        return room;
    }

    public abstract Room clean(Room room);

}
