package ee.taltech.publicapplication.config.converter.room_status;

import ee.taltech.publicapplication.game.model.RoomStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class RoomStatusWritingConverter implements Converter<RoomStatus, String> {

    @Override
    public String convert(RoomStatus source) {
        return source.toString();
    }

}
