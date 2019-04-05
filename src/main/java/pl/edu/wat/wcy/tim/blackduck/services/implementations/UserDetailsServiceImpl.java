package pl.edu.wat.wcy.tim.blackduck.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.wat.wcy.tim.blackduck.models.Role;
import pl.edu.wat.wcy.tim.blackduck.models.RoleName;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.models.UserPrinciple;
import pl.edu.wat.wcy.tim.blackduck.repositories.RoleRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.SignUpRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.LoginResponse;
import pl.edu.wat.wcy.tim.blackduck.security.JwtProvider;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    PasswordEncoder encoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));

        return UserPrinciple.build(user);
    }

    public LoginResponse login(String username, String password){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);

        User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));


        LoginResponse response = new LoginResponse("Bearer " + jwt, user.getUsername());
        return response;
    }

    public boolean signup(SignUpRequest request){
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Fail -> Username is already taken!");
        }

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Fail -> Email is already in use!");
        }

        // Creating user's account
        User user = new User(request.getUsername(), request.getEmail(), encoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet();
        roles.add(roleRepository.findByName(RoleName.USER).orElseThrow(
                () -> new RuntimeException("Fail! -> Cause: User Role not found.")
        ));
        user.setRoles(roles);
        userRepository.save(user);

        return true;
    }

}