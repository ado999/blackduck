package pl.edu.wat.wcy.tim.blackduck.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.tim.blackduck.models.Hashtag;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {
    Hashtag findByName (String name);
}
