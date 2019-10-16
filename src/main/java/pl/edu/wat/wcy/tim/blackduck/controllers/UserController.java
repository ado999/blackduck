package pl.edu.wat.wcy.tim.blackduck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.wcy.tim.blackduck.requests.EditProfileRequest;
import pl.edu.wat.wcy.tim.blackduck.requests.LoginRequest;
import pl.edu.wat.wcy.tim.blackduck.requests.SignUpRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.LoginResponse;
import pl.edu.wat.wcy.tim.blackduck.responses.SignupResponse;
import pl.edu.wat.wcy.tim.blackduck.responses.UserResponse;
import pl.edu.wat.wcy.tim.blackduck.responses.UserShortResponse;
import pl.edu.wat.wcy.tim.blackduck.services.UserService;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){

        LoginResponse response = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        try {
            userService.signup(signUpRequest);
            return new ResponseEntity<>(new SignupResponse(true, null), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(new SignupResponse(false, e.getMessage()), HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(new SignupResponse(false, "Wystąpił nieznany błąd"), HttpStatus.OK);
        }
    }

    @GetMapping("/followers")
    public ResponseEntity followers(HttpServletRequest req){
        try {
            return new ResponseEntity<>(userService.followers(req), HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/followedUsers")
    public ResponseEntity followedUsers(HttpServletRequest req){
        try {
            return new ResponseEntity<>(userService.followedUsers(req), HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/updateD")
    public ResponseEntity updateDescription (@RequestBody String description, HttpServletRequest req){
        try {
            userService.updateDescription(description, req);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/updatePP")
    public ResponseEntity updateProfilePicture (@RequestParam("file") MultipartFile file, HttpServletRequest req){
        try {
            userService.updateProfilePicture(file, req);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/updateBP")
    public ResponseEntity updateBackgroundPicture (@RequestParam("file") MultipartFile file, HttpServletRequest req){
        try {
            userService.updateBackgroundPicture(file, req);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/updateP")
    public ResponseEntity updatePassword(@RequestParam("password") String password, HttpServletRequest req){
        try {
            userService.updatePassword(password, req);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/search/{text}")
    public ResponseEntity getSearch(@PathVariable String text) {
        return new ResponseEntity<>(userService.getUserSearch(text), HttpStatus.OK);
    }

    @PostMapping("/editProfile")
    public ResponseEntity editProfile(@RequestBody EditProfileRequest profileRequest, HttpServletRequest req){
        try {
            return new ResponseEntity<>(userService.editProfile(profileRequest, req), HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
