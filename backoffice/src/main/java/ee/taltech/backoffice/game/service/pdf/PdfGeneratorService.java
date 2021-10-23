package ee.taltech.backoffice.game.service.pdf;


import ee.taltech.backoffice.game.model.dto.QuestionStatisticsDto;
import ee.taltech.backoffice.game.model.dto.RoomStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PdfGeneratorService {

    private final ThymeleafTemplater thymeleafTemplater;
    private final FlyingSaucerPdfCreator fLyingSaucesPdfCreator;

    public void generatePdf(RoomStatistics roomStatistics, List<QuestionStatisticsDto> questionStatisticsDtos, OutputStream outputStream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        thymeleafTemplater.process(roomStatistics, questionStatisticsDtos, buffer);
        fLyingSaucesPdfCreator.createPdf(new ByteArrayInputStream(buffer.toByteArray()), outputStream);
    }

}
