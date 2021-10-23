package ee.taltech.publicapplication.config.converter.atomic_long;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.concurrent.atomic.AtomicLong;

@WritingConverter
public class AtomicLongWritingConverter implements Converter<AtomicLong, Long> {

    @Override
    public Long convert(AtomicLong source) {
        return source.get();
    }

}
