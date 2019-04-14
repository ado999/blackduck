package pl.edu.wat.wcy.tim.blackduck.services;

import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.tim.blackduck.DTOs.UserDTO;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.requests.SignUpRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.LoginResponse;

import java.util.Set;

@Service
public interface IUserService {
    LoginResponse login(String username, String password);
    boolean signup(SignUpRequest request);
    Set<UserDTO> getFollowedUsers(UserDTO dto);

    User getUser(String username);

    User getUser(int userId);
}
