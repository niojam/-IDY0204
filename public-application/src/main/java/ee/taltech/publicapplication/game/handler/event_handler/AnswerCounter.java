package ee.taltech.publicapplication.game.handler.event_handler;

import ee.taltech.publicapplication.game.handler.event_handler.abstract_parent.AbstractEventHandler;
import ee.taltech.publicapplication.game.handler.event_handler.abstract_parent.AbstractProcessor;
import org.springframework.stereotype.Component;

@Component
public class AnswerCounter
        extends AbstractEventHandler<AnswerCounter.AnswerCounterProcessor, Long> {

    @Override
    public void addProcessor(Long roomId) {
        addProcessor(roomId, new AnswerCounterProcessor());
    }

    public static class AnswerCounterProcessor extends AbstractProcessor<Long> {

    }
}
