package ee.taltech.backoffice.game.service.pdf;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextUserAgent;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ClasspathResourceResolvingAgent extends ITextUserAgent {

    public ClasspathResourceResolvingAgent(ITextOutputDevice outputDevice, SharedContext sharedContext) {
        super(outputDevice);
        setSharedContext(sharedContext);
    }

    @Override
    public String resolveURI(String uri) {
        return uri;
    }

    @Override
    protected InputStream resolveAndOpenStream(String uri) {
        InputStream is;
        URL url;
        try {
            URI checkedUri = new URI(uri);
            ResourceLoader loader = new DefaultResourceLoader();
            if (checkedUri.getScheme() == null) {
                uri = "classpath:pdf-templates/" + uri;
            }

            url = loader.getResource(uri).getURL();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        try {
            is = url.openStream();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return is;
    }

}
