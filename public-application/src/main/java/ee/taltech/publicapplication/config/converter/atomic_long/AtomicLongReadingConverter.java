package ee.taltech.publicapplication.config.converter.atomic_long;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;

import java.util.concurrent.atomic.AtomicLong;

@ReadingConverter
public class AtomicLongReadingConverter implements Converter<Long, AtomicLong> {

    @Override
    public AtomicLong convert(@NonNull Long source) {
        return new AtomicLong(source);
    }

}
