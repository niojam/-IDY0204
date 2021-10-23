package ee.taltech.backoffice.game.service.pdf;

import ee.taltech.backoffice.game.model.dto.QuestionStatisticsDto;
import ee.taltech.backoffice.game.model.dto.RoomStatistics;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class ThymeleafTemplater {

    private TemplateEngine templateEngine;

    public ThymeleafTemplater() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.XML);
        templateResolver.setPrefix("/pdf-templates/");
        templateResolver.setSuffix(".xhtml");
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());

        templateResolver.setCacheable(true);

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    public void process(RoomStatistics roomStatistics, List<QuestionStatisticsDto> questionStatisticsDtos, OutputStream outputStream) {
        Context context = new Context();
        context.setVariable("roomStatistics", roomStatistics);
        context.setVariable("questionStatistics", questionStatisticsDtos);

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        templateEngine.process("statistics", context, writer);
    }

}
