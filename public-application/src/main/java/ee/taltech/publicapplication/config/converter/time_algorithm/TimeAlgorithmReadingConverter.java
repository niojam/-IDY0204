package ee.taltech.publicapplication.config.converter.time_algorithm;

import ee.taltech.publicapplication.game.model.TimeAlgorithm;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;

@ReadingConverter
public class TimeAlgorithmReadingConverter implements Converter<String, TimeAlgorithm> {

    @Override
    public TimeAlgorithm convert(@NonNull String source) {
        return TimeAlgorithm.valueOf(source);
    }



}
