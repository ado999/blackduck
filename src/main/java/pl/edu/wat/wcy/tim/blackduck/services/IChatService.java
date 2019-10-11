package pl.edu.wat.wcy.tim.blackduck.services;

import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.tim.blackduck.requests.ChatMessageRequest;
import pl.edu.wat.wcy.tim.blackduck.requests.GetMessagesRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.ChatConversationResponse;
import pl.edu.wat.wcy.tim.blackduck.responses.ChatMessageResponse;
import pl.edu.wat.wcy.tim.blackduck.DTOs.UserDTO;
import pl.edu.wat.wcy.tim.blackduck.exceptions.MessageMalformedException;
import pl.edu.wat.wcy.tim.blackduck.exceptions.UserNotFoundException;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface IChatService {

    ChatMessageResponse sendMessage(ChatMessageRequest request , HttpServletRequest req) throws UserNotFoundException, AuthenticationException;

    List<ChatMessageResponse> getExistingMessages(GetMessagesRequest getMessagesRequest, HttpServletRequest req) throws UserNotFoundException, AuthenticationException;

    List<ChatConversationResponse> getExistingConversations(HttpServletRequest req) throws AuthenticationException;
}
