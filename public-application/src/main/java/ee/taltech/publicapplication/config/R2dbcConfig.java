package ee.taltech.publicapplication.config;

import ee.taltech.publicapplication.config.converter.atomic_long.AtomicLongReadingConverter;
import ee.taltech.publicapplication.config.converter.atomic_long.AtomicLongWritingConverter;
import ee.taltech.publicapplication.config.converter.questionType.QuestionTypeReadingConverter;
import ee.taltech.publicapplication.config.converter.questionType.QuestionTypeWritingConverter;
import ee.taltech.publicapplication.config.converter.room_status.RoomStatusReadingConverter;
import ee.taltech.publicapplication.config.converter.room_status.RoomStatusWritingConverter;
import ee.taltech.publicapplication.config.converter.time_algorithm.TimeAlgorithmReadingConverter;
import ee.taltech.publicapplication.config.converter.time_algorithm.TimeAlgorithmWritingConverter;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.lang.NonNull;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class R2dbcConfig extends AbstractR2dbcConfiguration {

    private final ConnectionFactory configuration;

    @NonNull
    @Override
    public ConnectionFactory connectionFactory() {
        return configuration;
    }

    @NonNull
    @Override
    protected List<Object> getCustomConverters() {
        return List.of(
                new AtomicLongReadingConverter(), new AtomicLongWritingConverter(),
                new TimeAlgorithmReadingConverter(), new TimeAlgorithmWritingConverter(),
                new RoomStatusReadingConverter(), new RoomStatusWritingConverter(),
                new QuestionTypeReadingConverter(), new QuestionTypeWritingConverter());
    }

}
