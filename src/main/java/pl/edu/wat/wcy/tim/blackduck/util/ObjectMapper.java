package pl.edu.wat.wcy.tim.blackduck.util;

import pl.edu.wat.wcy.tim.blackduck.DTOs.UserDTO;
import pl.edu.wat.wcy.tim.blackduck.models.User;

import java.util.HashSet;
import java.util.Set;

public class ObjectMapper {

    public static UserDTO dtoFromUser(User user){
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());

        return dto;
    }

    public static Set<UserDTO> dtosFromUsers(Set<User> users){
        Set<UserDTO> dtos = new HashSet<>();
        for(User user: users){
            dtos.add(dtoFromUser(user));
        }

        return dtos;
    }


}
