package pl.edu.wat.wcy.tim.blackduck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.tim.blackduck.requests.RateRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.RateResponse;
import pl.edu.wat.wcy.tim.blackduck.services.RateService;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/rates")
public class RateController {

    RateService rateService;

    @Autowired
    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @PostMapping
    public ResponseEntity sendRate (@Valid @RequestBody RateRequest request, HttpServletRequest req){
        try {
            rateService.add(request, req);
            return new ResponseEntity(HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

//    @GetMapping("/{id}")
//    public ResponseEntity getRate(@PathVariable Integer id, HttpServletRequest req){
//        try {
//            RateResponse response = rateService.getRate(id, req);
//            return new ResponseEntity(response, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
//        }
//    }
}
