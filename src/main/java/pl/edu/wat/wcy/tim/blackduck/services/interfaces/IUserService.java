package pl.edu.wat.wcy.tim.blackduck.services.interfaces;

import pl.edu.wat.wcy.tim.blackduck.DTOs.UserDTO;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.requests.SignUpRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.LoginResponse;

import java.util.Set;

public interface IUserService {
    LoginResponse login(String username, String password);
    boolean signup(SignUpRequest request);
    Set<UserDTO> getFollowedUsers(UserDTO dto);

    User getUser(String username);

    User getUser(int userId);

    void setPresence(String username, boolean isPresent);

    boolean isPresent(User recipientUser);
}
