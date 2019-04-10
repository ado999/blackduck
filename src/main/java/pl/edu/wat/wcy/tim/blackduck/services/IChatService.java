package pl.edu.wat.wcy.tim.blackduck.services;

import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.tim.blackduck.DTOs.ChatConversationDTO;
import pl.edu.wat.wcy.tim.blackduck.DTOs.ChatMessageDTO;
import pl.edu.wat.wcy.tim.blackduck.DTOs.UserDTO;
import pl.edu.wat.wcy.tim.blackduck.exceptions.MessageMalformedException;
import pl.edu.wat.wcy.tim.blackduck.exceptions.UserNotFoundException;

import java.util.List;

@Service
public interface IChatService {

    void receiveMessage(ChatMessageDTO dto, String token) throws UserNotFoundException, MessageMalformedException;

    List<ChatMessageDTO> getExistingMessages(String token) throws UserNotFoundException;

    ChatConversationDTO establishConversation(String token, UserDTO userDTO) throws UserNotFoundException;

    List<ChatConversationDTO> getExistingConversations(String token) throws UserNotFoundException;
}
