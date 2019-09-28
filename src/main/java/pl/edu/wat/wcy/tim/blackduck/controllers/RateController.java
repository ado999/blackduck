package pl.edu.wat.wcy.tim.blackduck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.tim.blackduck.repositories.ImageRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.RateRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.RateRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.ResponseMessage;
import pl.edu.wat.wcy.tim.blackduck.services.RateService;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/rates")
public class RateController {

    @Autowired
    RateRepository rateRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    RateService rateService;

    public RateController() {
    }

    @PostMapping("/sendRate")
    public ResponseEntity<?> sendRate(@Valid @RequestBody RateRequest rateRequest){
        ResponseMessage responseMessage = rateService.sendRate(rateRequest);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

}
