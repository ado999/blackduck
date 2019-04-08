package pl.edu.wat.wcy.tim.blackduck.services;

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
import pl.edu.wat.wcy.tim.blackduck.DTOs.UserDTO;
import pl.edu.wat.wcy.tim.blackduck.models.Role;
import pl.edu.wat.wcy.tim.blackduck.models.RoleName;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.models.UserPrinciple;
import pl.edu.wat.wcy.tim.blackduck.repositories.RoleRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.SignUpRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.LoginResponse;
import pl.edu.wat.wcy.tim.blackduck.security.JwtProvider;
import pl.edu.wat.wcy.tim.blackduck.util.ObjectMapper;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements UserDetailsService, IUserService {

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

    @Override
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

    @Override
    public boolean signup(SignUpRequest request){
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken!");
        }

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        // Creating user's account
        User user = new User(request.getUsername(), request.getEmail(), encoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet();
        roles.add(roleRepository.findByName(RoleName.USER).orElseThrow(
                () -> new RuntimeException("User Role not found.")
        ));
        user.setRoles(roles);
        userRepository.save(user);

        return true;
    }

    @Override
    public Set<UserDTO> getFollowedUsers(UserDTO dto){
        User user = userRepository.findByIdAndUsername(dto.getUserId(), dto.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username or email : " + dto.getUsername()));
        return ObjectMapper.dtosFromUsers(user.getFollowedUsers());
    }

    @Override
    public User getUser(String username){
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));
    }

    @Override
    public User getUser(int userId){
        return userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> user Id : " + userId));
    }

    @Override
    public void setPresence(String username, boolean isPresent) {
        User user = getUser(username);
        user.setPresent(isPresent);
        userRepository.save(user);
    }

    @Override
    public boolean isPresent(User recipientUser) {
        return recipientUser.isPresent();
    }
}