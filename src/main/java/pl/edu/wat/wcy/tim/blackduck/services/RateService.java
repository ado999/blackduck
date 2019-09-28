package pl.edu.wat.wcy.tim.blackduck.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.edu.wat.wcy.tim.blackduck.models.Image;
import pl.edu.wat.wcy.tim.blackduck.models.Rate;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.repositories.ImageRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.RateRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.RateRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.ResponseMessage;

import javax.validation.Valid;
import java.util.Optional;

@Service
public class RateService {

    @Autowired
    RateRepository rateRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    public ResponseMessage sendRate (@Valid @RequestBody RateRequest rateRequest){

        Optional<User> user = userRepository.findByUsername(rateRequest.getUsername());
        Optional<Image> image = imageRepository.findById(Integer.parseInt(rateRequest.getImageId()));

        Rate rate = rateRepository.findRateByUserAndImage(user, image);

        if (rate==null){
            Rate rates = new Rate(Double.parseDouble(rateRequest.getRate()), user.get(), image);
            rateRepository.save(rates);
        }else{
            Rate rates = rateRepository.findRateByUserAndImage(user, image);
            rates.setRate(Double.parseDouble(rateRequest.getRate()));
            rateRepository.save(rates);
        }

        return new ResponseMessage("ok");
    }
}
