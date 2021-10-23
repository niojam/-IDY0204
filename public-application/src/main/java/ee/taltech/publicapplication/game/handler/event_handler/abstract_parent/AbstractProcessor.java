package ee.taltech.publicapplication.game.handler.event_handler.abstract_parent;

import lombok.Getter;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.FluxSink;

@Getter
public class AbstractProcessor<EmittedEvent> {

    private final DirectProcessor<EmittedEvent> directProcessor = DirectProcessor.create();
    private final FluxSink<EmittedEvent> eventsInputStream = directProcessor.sink(FluxSink.OverflowStrategy.DROP);

}
