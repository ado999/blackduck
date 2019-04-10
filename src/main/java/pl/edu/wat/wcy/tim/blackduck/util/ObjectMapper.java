package pl.edu.wat.wcy.tim.blackduck.util;

import pl.edu.wat.wcy.tim.blackduck.DTOs.ChatConversationDTO;
import pl.edu.wat.wcy.tim.blackduck.DTOs.ChatMessageDTO;
import pl.edu.wat.wcy.tim.blackduck.DTOs.UserDTO;
import pl.edu.wat.wcy.tim.blackduck.exceptions.UserNotFoundException;
import pl.edu.wat.wcy.tim.blackduck.models.ChatConversation;
import pl.edu.wat.wcy.tim.blackduck.models.ChatMessage;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;

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


    public static ChatMessage chatMessageFromDto(ChatMessageDTO dto, UserRepository userRepository) throws UserNotFoundException {
        User fromUser = userRepository.findById(dto.getFromUserId()).orElseThrow(() -> new UserNotFoundException("User not found -> UserId: " + dto.getFromUserId()));
        User toUser = userRepository.findById(dto.getToUserId()).orElseThrow(() -> new UserNotFoundException("User not found -> UserId: " + dto.getToUserId()));

        ChatMessage message = new ChatMessage(fromUser, toUser, dto.getMessage(), dto.getCid());

        return message;
    }

    public static ChatMessageDTO dtoFromChatMessage(ChatMessage message){
        return new ChatMessageDTO(message);
    }

    public static List<ChatMessageDTO> dtosFromChatMessages(List<ChatMessage> messages){
        ArrayList<ChatMessageDTO> dtos = new ArrayList<>();

        for(ChatMessage message : messages){
            dtos.add(dtoFromChatMessage(message));
        }
        return dtos;
    }

    public static ChatConversationDTO dtoFromChatConversation(ChatConversation conversation){
        return new ChatConversationDTO(conversation);
    }

    public static List<ChatConversationDTO> dtosFromChatConversations(List<ChatConversation> conversations){
        ArrayList<ChatConversationDTO> dtos = new ArrayList<>();

        for(ChatConversation conversation : conversations){
            dtos.add(dtoFromChatConversation(conversation));
        }
        return dtos;
    }

}
