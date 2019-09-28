package pl.edu.wat.wcy.tim.blackduck.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.tim.blackduck.models.Image;
import pl.edu.wat.wcy.tim.blackduck.models.Rate;
import pl.edu.wat.wcy.tim.blackduck.models.User;

import java.util.Optional;

@Repository
public interface RateRepository extends JpaRepository<Rate, Integer> {
    Rate findRateById (Integer id);
    Rate findRateByUser (User user);
    Rate findRateByImage (Image image);
    Rate findRateByUserAndImage (Optional<User> user, Optional<Image> image);

}
