package pl.edu.wat.wcy.tim.blackduck.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.wat.wcy.tim.blackduck.models.Role;
import pl.edu.wat.wcy.tim.blackduck.models.RoleName;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.models.UserPrinciple;
import pl.edu.wat.wcy.tim.blackduck.repositories.RoleRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.SignUpRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.LoginResponse;
import pl.edu.wat.wcy.tim.blackduck.responses.UserResponse;
import pl.edu.wat.wcy.tim.blackduck.responses.UserShortResponse;
import pl.edu.wat.wcy.tim.blackduck.security.JwtProvider;
import pl.edu.wat.wcy.tim.blackduck.util.ObjectMapper;
import pl.edu.wat.wcy.tim.blackduck.util.RequestValidationComponent;
import pl.edu.wat.wcy.tim.blackduck.util.ResponseMapper;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    ResponseMapper responseMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RequestValidationComponent validationComponent;


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
        String uniqueId = UUID.randomUUID().toString();
        user.setUuid(uniqueId);
        user.setLastActivityDate(new Date());
        userRepository.save(user);


        LoginResponse response = new LoginResponse("Bearer " + jwt, responseMapper.toResponse(user));
        return response;
    }

    @Override
    public boolean signup(SignUpRequest request) throws IllegalArgumentException {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        // Creating user's account
        User user = objectMapper.toObject(request);
        user.setPassword(encoder.encode(request.getPassword()));
        user.setProfilePhotoUrl("https://www.medaid.co.uk/wp-content/uploads/2019/04/default.jpg");
        user.setProfileBacgroundUrl("http://www.allwhitebackground.com/images/2/2270.jpg");
        user.setLastActivityDate(new Date());

        Set<Role> roles = new HashSet();
        roles.add(roleRepository.findByName(RoleName.USER).orElseThrow(
                () -> new RuntimeException("User Role not found.")
        ));
        user.setRoles(roles);
        userRepository.save(user);

        return true;
    }

    public void updateDescription(String description, HttpServletRequest req) throws AuthenticationException {
        validationComponent.validateRequest(req);

        Optional<User> user = userRepository.findByUsername(jwtProvider.getUserNameFromJwtToken(jwtProvider.resolveToken(req)));

        User update = user.get();

        update.setDescription(description);

        userRepository.save(update);
    }

    public void updatePassword(String password, HttpServletRequest req) throws AuthenticationException {
        validationComponent.validateRequest(req);

        Optional<User> user = userRepository.findByUsername(jwtProvider.getUserNameFromJwtToken(jwtProvider.resolveToken(req)));

        User update = user.get();

        String newPass = encoder.encode(password);
        update.setPassword(newPass);

        userRepository.save(update);
    }

    public void updateProfilePicture(MultipartFile file, HttpServletRequest req) throws AuthenticationException {
        validationComponent.validateRequest(req);

        Optional<User> user = userRepository.findByUsername(jwtProvider.getUserNameFromJwtToken(jwtProvider.resolveToken(req)));

        User update = user.get();

        String fileTypeP = file.getOriginalFilename().split("\\.")[1];
        if (fileTypeP.equals("png") || fileTypeP.equals("jpg")) {
            store(file);
            String url = ServletUriComponentsBuilder.fromCurrentContextPath().path(user.get().getUsername()).path("/").path(file.getOriginalFilename()).toUriString();
            update.setProfilePhotoUrl(url);
        } else {
            throw new AuthenticationException("File not recognized");
        }
        userRepository.save(update);
    }

    public void updateBackgroundPicture(MultipartFile file, HttpServletRequest req) throws AuthenticationException {
        validationComponent.validateRequest(req);

        Optional<User> user = userRepository.findByUsername(jwtProvider.getUserNameFromJwtToken(jwtProvider.resolveToken(req)));

        User update = user.get();

        String fileTypeP = file.getOriginalFilename().split("\\.")[1];
        if (fileTypeP.equals("png") || fileTypeP.equals("jpg")) {
            store(file);
            String url = ServletUriComponentsBuilder.fromCurrentContextPath().path(user.get().getUsername()).path("/").path(file.getOriginalFilename()).toUriString();
            update.setProfileBacgroundUrl(url);
        } else {
            throw new AuthenticationException("File not recognized");
        }
        userRepository.save(update);
    }


    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));
    }

    @Override
    public User getUser(int userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> user Id : " + userId));
    }

    public List<UserResponse> getUserSearch(String text) {
        List<User> results = new ArrayList<>();
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getUsername().contains(text) || user.getUsername().equalsIgnoreCase(text)) {
                results.add(user);
            }
        }
        if (results.size() == 0) {
            throw new IllegalArgumentException("Post not found");
        }

        List<UserResponse> pr = new ArrayList<>();
        for (User user : results) {
            pr.add(responseMapper.toResponse((user)));
        }
        return pr;
    }

    public List<UserShortResponse> followers(HttpServletRequest req) throws AuthenticationException {
        User user = validationComponent.validateRequest(req);

        return userRepository
                .findAllByFollowedUsersContains(user)
                .stream()
                .map(it -> responseMapper.toShortResponse(it))
                .collect(Collectors.toList());
    }

    public List<UserShortResponse> followedUsers(HttpServletRequest req) throws AuthenticationException {
        User user = validationComponent.validateRequest(req);

        return user.getFollowedUsers()
                .stream()
                .map(it -> responseMapper.toShortResponse(it))
                .collect(Collectors.toList());
    }


    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final Path rootLocation = Paths.get("\\upload-dir");

    public void store(MultipartFile file) {
        try {
            String location = System.getProperty("user.dir") + rootLocation + "\\" + file.getOriginalFilename();
            //System.out.println(location);
            File fileToSave = new File(location);
            OutputStream os = new FileOutputStream(fileToSave);
            os.write(file.getBytes());
            os.close();
//        this.rootLocation.resolve(file.getOriginalFilename())
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}


