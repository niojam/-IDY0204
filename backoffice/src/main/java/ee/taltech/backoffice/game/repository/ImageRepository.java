package ee.taltech.backoffice.game.repository;

import ee.taltech.backoffice.game.model.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {

}
