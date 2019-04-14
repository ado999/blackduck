package pl.edu.wat.wcy.tim.blackduck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.tim.blackduck.DTOs.UserDTO;
import pl.edu.wat.wcy.tim.blackduck.requests.LoginRequest;
import pl.edu.wat.wcy.tim.blackduck.requests.SignUpRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.LoginResponse;
import pl.edu.wat.wcy.tim.blackduck.services.UserService;

import javax.validation.Valid;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService userService;



    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest){

        LoginResponse response = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        userService.signup(signUpRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/followed")
    public ResponseEntity<UserDTO> getFollowedUsers(@RequestBody UserDTO dto){
        return new ResponseEntity(userService.getFollowedUsers(dto), HttpStatus.OK);
    }

}
