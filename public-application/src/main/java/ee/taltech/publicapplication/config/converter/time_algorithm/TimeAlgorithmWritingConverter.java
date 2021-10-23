package ee.taltech.publicapplication.config.converter.time_algorithm;

import ee.taltech.publicapplication.game.model.TimeAlgorithm;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class TimeAlgorithmWritingConverter implements Converter<TimeAlgorithm, String> {

    @Override
    public String convert(TimeAlgorithm source) {
        return source.toString();
    }

}
