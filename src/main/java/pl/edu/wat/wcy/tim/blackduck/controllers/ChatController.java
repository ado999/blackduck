package pl.edu.wat.wcy.tim.blackduck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.tim.blackduck.exceptions.UserNotFoundException;
import pl.edu.wat.wcy.tim.blackduck.requests.ChatMessageRequest;
import pl.edu.wat.wcy.tim.blackduck.requests.GetMessagesRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.ChatConversationResponse;
import pl.edu.wat.wcy.tim.blackduck.responses.ChatMessageResponse;
import pl.edu.wat.wcy.tim.blackduck.services.ChatService;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
public class ChatController {

    private ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService){
        this.chatService = chatService;
    }

    @PostMapping("/chat/sendMessage")
    public ResponseEntity sendMessage(@RequestBody ChatMessageRequest chatMessageRequest, HttpServletRequest req)
            throws UserNotFoundException {
        try {
            ChatMessageResponse response = chatService.sendMessage(chatMessageRequest, req);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/chat/messages")
    public ResponseEntity getMessages(@RequestBody GetMessagesRequest getMessagesRequest, HttpServletRequest req) {
        List<ChatMessageResponse> messages;
        try {
            messages = chatService.getExistingMessages(getMessagesRequest, req);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/chat/conversations")
    public ResponseEntity getExistingConversations(HttpServletRequest req) {

        List<ChatConversationResponse> dtos;
        try {
            dtos = chatService.getExistingConversations(req);
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
