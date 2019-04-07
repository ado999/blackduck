package pl.edu.wat.wcy.tim.blackduck.util;

import pl.edu.wat.wcy.tim.blackduck.DTOs.UserDTO;
import pl.edu.wat.wcy.tim.blackduck.messaging.DTOs.ChatMessageDTO;
import pl.edu.wat.wcy.tim.blackduck.messaging.models.ChatMessage;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.services.implementations.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    public static ChatMessage messageFromDto(ChatMessageDTO dto, UserService userService){
        ChatMessage message = new ChatMessage(
                userService.getUser(dto.getAuthorUserId()),
                userService.getUser(dto.getRecipentUserId()),
                dto.getContent());
        return message;
    }

    public static List<ChatMessageDTO> dtosfromMessages(List<ChatMessage> messages){
        List<ChatMessageDTO> dtos = new ArrayList<>();
        for(ChatMessage message: messages){
            dtos.add(new ChatMessageDTO(message));
        }
        return dtos;
    }

}
