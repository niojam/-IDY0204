package ee.taltech.publicapplication.game.handler.event_handler.abstract_parent;

import ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.FluxSink;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static ee.taltech.publicapplication.game.controller.errorHandling.exception.BadRequest.Code.BUSINESS_LOGIC_EXCEPTION;
import static java.lang.String.format;

public abstract class AbstractEventHandler<Processor extends AbstractProcessor<EmittedEvent>, EmittedEvent> {

    private final Map<Long, Processor> eventProcessorMap = new ConcurrentHashMap<>();

    public abstract void addProcessor(Long roomId);

    public DirectProcessor<EmittedEvent> getDirectProcessor(Long roomId) {
        return getProcessor(roomId).getDirectProcessor();
    }

    public FluxSink<EmittedEvent> getEventsInputStream(Long roomId) {
        return getProcessor(roomId).getEventsInputStream();
    }

    public void complete(Long roomId) {
        getEventsInputStream(roomId).complete();
    }

    public boolean contains(Long roomId) {
        return eventProcessorMap.containsKey(roomId);
    }

    protected void addProcessor(Long roomId, Processor processor) {
        if (eventProcessorMap.containsKey(roomId)) {
            throw new BadRequest(
                    BUSINESS_LOGIC_EXCEPTION,
                    format("You are already listening to roomId=%d", roomId)
            );
        }
        eventProcessorMap.put(roomId, processor);
    }

    public void removeProcessor(Long roomId) {
        eventProcessorMap.remove(roomId);
    }

    protected Processor getProcessor(Long roomId) {
        return Optional.ofNullable(eventProcessorMap.get(roomId))
                .orElseThrow(() -> new BadRequest(
                        BUSINESS_LOGIC_EXCEPTION,
                        format("No sse processor found for roomId=%d", roomId)));
    }

}
