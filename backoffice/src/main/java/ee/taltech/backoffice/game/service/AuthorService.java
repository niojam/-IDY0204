package ee.taltech.backoffice.game.service;

import ee.taltech.backoffice.game.model.Author;
import ee.taltech.backoffice.game.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public Author createIfNotExist(String oid, String email) {
        return authorRepository.findByOid(oid)
                .orElseGet(() -> authorRepository.save(new Author()
                        .setOid(oid)
                        .setUsername(email)));
    }

}
