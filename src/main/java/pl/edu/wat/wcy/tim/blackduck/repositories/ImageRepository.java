package pl.edu.wat.wcy.tim.blackduck.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.tim.blackduck.models.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    Image findByPath(String path);
}
