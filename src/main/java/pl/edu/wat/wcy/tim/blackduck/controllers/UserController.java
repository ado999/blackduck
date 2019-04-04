package pl.edu.wat.wcy.tim.blackduck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.wat.wcy.tim.blackduck.models.UserPrinciple;
import pl.edu.wat.wcy.tim.blackduck.requests.LoginRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.JwtResponse;
import pl.edu.wat.wcy.tim.blackduck.security.JwtProvider;
import pl.edu.wat.wcy.tim.blackduck.services.UserService;
import pl.edu.wat.wcy.tim.blackduck.services.implementations.UserDetailsServiceImpl;

@RestController
public class UserController {

    @Autowired
    private UserDetailsServiceImpl userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest){
        JwtResponse response = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);

        return new ResponseEntity<>(new JwtResponse(jwt), HttpStatus.OK);
    }



}
