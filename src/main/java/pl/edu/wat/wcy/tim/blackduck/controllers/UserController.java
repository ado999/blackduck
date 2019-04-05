package pl.edu.wat.wcy.tim.blackduck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.wat.wcy.tim.blackduck.requests.LoginRequest;
import pl.edu.wat.wcy.tim.blackduck.requests.SignUpRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.LoginResponse;
import pl.edu.wat.wcy.tim.blackduck.services.implementations.UserDetailsServiceImpl;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserDetailsServiceImpl userService;



    @GetMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest){

        LoginResponse response = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        boolean result = userService.signup(signUpRequest);

        if(result)
        return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }

}
