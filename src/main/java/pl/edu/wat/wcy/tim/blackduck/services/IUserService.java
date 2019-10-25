package pl.edu.wat.wcy.tim.blackduck.services;

import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.requests.SignUpRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.LoginResponse;
import pl.edu.wat.wcy.tim.blackduck.responses.UserResponse;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

@Service
public interface IUserService {
    LoginResponse login(String username, String password);
    boolean signup(SignUpRequest request);

    UserResponse getUser(String username, HttpServletRequest req) throws AuthenticationException;

    User getUser(int userId);
}
