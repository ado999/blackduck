package pl.edu.wat.wcy.tim.blackduck.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.security.JwtProvider;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Component
public class RequestValidationComponent {

    private UserRepository userRepository;

    private JwtProvider jwtProvider;

    @Autowired
    public RequestValidationComponent(
            JwtProvider jwtProvider,
            UserRepository userRepository
    ){
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    public User validateRequest(HttpServletRequest req) throws AuthenticationException {
        Optional<User> user = userRepository.findByUsername(
                jwtProvider.getUserNameFromJwtToken(jwtProvider.resolveToken(req))
        );
        if (!user.isPresent()) {
            throw new AuthenticationException("User not found");
        }
        User usr = user.get();
        usr.setLastActivityDate(new Date());
        userRepository.save(usr);
        return usr;
    }

}
