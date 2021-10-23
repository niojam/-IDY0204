package ee.taltech.backoffice.game.controller;

import ee.taltech.backoffice.game.model.Image;
import ee.taltech.backoffice.game.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<Resource> getImage(@RequestParam Long id) {
        Image image = imageService.getImage(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .header(CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                .body(new ByteArrayResource(image.getContent()));
    }

    @PostMapping
    public Long saveImage(@RequestParam MultipartFile file) throws IOException {
        return imageService.saveImage(
                file.getOriginalFilename(),
                file.getBytes(),
                file.getContentType()).getId();
    }

}
