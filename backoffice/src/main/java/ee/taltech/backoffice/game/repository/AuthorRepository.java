package ee.taltech.backoffice.game.repository;

import ee.taltech.backoffice.game.model.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {

    Optional<Author> findByOid(String oid);

}
