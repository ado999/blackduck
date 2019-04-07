package pl.edu.wat.wcy.tim.blackduck.messaging.controllers;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.edu.wat.wcy.tim.blackduck.Exceptions.IsSameUserException;
import pl.edu.wat.wcy.tim.blackduck.messaging.DTOs.ChatChannelEstablishedDTO;
import pl.edu.wat.wcy.tim.blackduck.messaging.DTOs.ChatChannelInitDTO;
import pl.edu.wat.wcy.tim.blackduck.messaging.DTOs.ChatMessageDTO;
import pl.edu.wat.wcy.tim.blackduck.messaging.services.ChatService;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.services.implementations.UserService;

import java.util.List;

@Controller
public class MessageController {

    private ChatService chatService;
    private UserService userService;

    @Autowired
    public MessageController(ChatService chatService, UserService userService){
        this.chatService = chatService;
        this.userService = userService;
    }

    @MessageMapping("/private.chat.{channelId}")
    @SendTo("/topic/private.chat.{channelId}")
    public ChatMessageDTO chatMessage(@DestinationVariable String channelId, ChatMessageDTO message)
            throws BeansException {
        chatService.submitMessage(message);

        return message;
    }

    @PutMapping("/api/private-chat/channel")
    public ResponseEntity<String> establishChatChannel(@RequestBody ChatChannelInitDTO chatChannelInitDTO)
            throws IsSameUserException {
        String channelUuid = chatService.establishChatSession(chatChannelInitDTO);
        User userOne = userService.getUser(chatChannelInitDTO.getUserOneId());
        User userTwo = userService.getUser(chatChannelInitDTO.getUserTwoId());

        ChatChannelEstablishedDTO establishedChatChannel = new ChatChannelEstablishedDTO(
                channelUuid,
                userOne.getUsername(),
                userTwo.getUsername()
        );

        return new ResponseEntity(establishedChatChannel, HttpStatus.OK);
    }

    @GetMapping("/api/private-chat/channel/{channelUuid}")
    public ResponseEntity<String> getExistingChatMessages(@PathVariable("channelUuid") String channelUuid) {
        List<ChatMessageDTO> messages = chatService.getExistingChatMessages(channelUuid);

        return new ResponseEntity(messages, HttpStatus.OK);
    }


}
