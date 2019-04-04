package pl.edu.wat.wcy.tim.blackduck.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.models.UserPrinciple;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.responses.JwtResponse;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));

        return UserPrinciple.build(user);
    }

    public JwtResponse login(String username, String password){
        User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));
        user = userRepository.findByUsernameAndPassword(user.getUsername(), password).orElseThrow(
                () -> new BadCredentialsException("Password incorrect for -> username or email : " + username)
        );
        JwtResponse response = new JwtResponse("token", user.getUsername(), null);
        return response;
    }
}