package ee.taltech.publicapplication.config.converter.questionType;

import ee.taltech.publicapplication.game.model.QuestionType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;

@ReadingConverter
public class QuestionTypeReadingConverter implements Converter<String, QuestionType> {

    @Override
    public QuestionType convert(@NonNull String source) {
        return QuestionType.valueOf(source);
    }



}
