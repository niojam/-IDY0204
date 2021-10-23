package ee.taltech.publicapplication.config.converter.questionType;

import ee.taltech.publicapplication.game.model.QuestionType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class QuestionTypeWritingConverter implements Converter<QuestionType, String> {

    @Override
    public String convert(QuestionType source) {
        return source.toString();
    }

}
