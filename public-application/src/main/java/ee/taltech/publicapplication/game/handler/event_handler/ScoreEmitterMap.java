package ee.taltech.publicapplication.game.handler.event_handler;

import ee.taltech.publicapplication.game.handler.event_handler.abstract_parent.AbstractEventHandler;
import ee.taltech.publicapplication.game.handler.event_handler.abstract_parent.AbstractProcessor;
import org.springframework.stereotype.Component;

@Component
public class ScoreEmitterMap
        extends AbstractEventHandler<ScoreEmitterMap.ScoreEvaluatedProcessor, Boolean> {

    public static class ScoreEvaluatedProcessor extends AbstractProcessor<Boolean> {
    }

    public void addProcessor(Long romId) {
        addProcessor(romId, new ScoreEvaluatedProcessor());
    }

}
