package ee.taltech.backoffice.game.service;

import com.github.dockerjava.api.exception.BadRequestException;
import ee.taltech.backoffice.game.model.Image;
import ee.taltech.backoffice.game.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ImageService {

    public final ImageRepository repository;


    public Image getImage(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() ->
                        new BadRequestException(format("No image found with id=%d", id)));
    }

    public Image saveImage(String fileName, byte[] content, String contentType) {
        return repository.save(new Image()
                .setContent(content)
                .setFileName(fileName)
                .setContentType(contentType));
    }

}
