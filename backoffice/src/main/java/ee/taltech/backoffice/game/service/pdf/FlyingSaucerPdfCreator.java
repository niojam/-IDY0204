package ee.taltech.backoffice.game.service.pdf;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class FlyingSaucerPdfCreator {

    public void createPdf(InputStream inputStream, OutputStream outputStream) {
        try {
            ITextRenderer renderer = new ITextRenderer();

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(inputStream);

            renderer.getSharedContext()
                    .setUserAgentCallback(new ClasspathResourceResolvingAgent(
                            renderer.getOutputDevice(),
                            renderer.getSharedContext()));
            renderer.setDocument(document, "classpath::pdf-templates/statistics.xhtml");
            renderer.layout();
            renderer.createPDF(outputStream, true);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
