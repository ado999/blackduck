package pl.edu.wat.wcy.tim.blackduck.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/")
    public ResponseEntity test(){
        return new ResponseEntity("<h1 style=\"color:cyan;font-size:220px;\">CHUJÓW STO</h1><h1>Blackduck backend pozdrawia :)</h1>", HttpStatus.OK);
    }

}
