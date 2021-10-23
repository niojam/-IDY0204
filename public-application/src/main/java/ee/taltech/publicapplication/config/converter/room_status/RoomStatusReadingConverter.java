package ee.taltech.publicapplication.config.converter.room_status;

import ee.taltech.publicapplication.game.model.RoomStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;

@ReadingConverter
public class RoomStatusReadingConverter implements Converter<String, RoomStatus> {

    @Override
    public RoomStatus convert(@NonNull String source) {
        return RoomStatus.valueOf(source);
    }

}
