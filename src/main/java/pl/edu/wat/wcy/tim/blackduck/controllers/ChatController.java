package pl.edu.wat.wcy.tim.blackduck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.tim.blackduck.DTOs.ChatConversationDTO;
import pl.edu.wat.wcy.tim.blackduck.DTOs.ChatMessageDTO;
import pl.edu.wat.wcy.tim.blackduck.DTOs.UserDTO;
import pl.edu.wat.wcy.tim.blackduck.exceptions.MessageMalformedException;
import pl.edu.wat.wcy.tim.blackduck.exceptions.UserNotFoundException;
import pl.edu.wat.wcy.tim.blackduck.requests.LoginRequest;
import pl.edu.wat.wcy.tim.blackduck.services.ChatService;

import java.security.Principal;
import java.util.List;

@RestController
public class ChatController {

    private ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService){
        this.chatService = chatService;
    }

    @PostMapping("/chat/sendMessage")
    public ResponseEntity receiveMessage(@RequestBody ChatMessageDTO chatMessageDTO, @RequestHeader(name = "Authorization") String token) throws UserNotFoundException, MessageMalformedException {
        chatService.receiveMessage(chatMessageDTO, token);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/chat/getMessages")
    public ResponseEntity getMessages(@RequestHeader(name = "Authorization") String token) throws UserNotFoundException {
        List<ChatMessageDTO> messages = chatService.getExistingMessages(token);

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping("/chat/establishConversation")
    public ResponseEntity establishConversation(@RequestBody UserDTO userDTO, @RequestHeader(name = "Authorization") String token) throws UserNotFoundException {
        ChatConversationDTO conservationDto = chatService.establishConversation(token, userDTO);

        return new ResponseEntity<>(conservationDto, HttpStatus.OK);
    }

    @GetMapping("/chat/getExistingConversations")
    public ResponseEntity getExistingConversations(@RequestHeader(name = "Authorization") String token, Principal principal) throws UserNotFoundException {
        List<ChatConversationDTO> dtos = chatService.getExistingConversations(token);
        System.out.println(principal.getName());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping("/chat/test")
    public ResponseEntity test(@RequestBody LoginRequest dto){
        chatService.test(dto);
        return new ResponseEntity(HttpStatus.OK);
    }

}
