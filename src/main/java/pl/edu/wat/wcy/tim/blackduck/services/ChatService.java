package pl.edu.wat.wcy.tim.blackduck.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.tim.blackduck.DTOs.ChatConversationDTO;
import pl.edu.wat.wcy.tim.blackduck.DTOs.ChatMessageDTO;
import pl.edu.wat.wcy.tim.blackduck.DTOs.UserDTO;
import pl.edu.wat.wcy.tim.blackduck.exceptions.MessageMalformedException;
import pl.edu.wat.wcy.tim.blackduck.exceptions.UserNotFoundException;
import pl.edu.wat.wcy.tim.blackduck.models.ChatConversation;
import pl.edu.wat.wcy.tim.blackduck.models.ChatMessage;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.repositories.ChatConversationRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.ChatMessageRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.security.JwtProvider;
import pl.edu.wat.wcy.tim.blackduck.util.ObjectMapper;

import java.util.List;

@Service
public class ChatService implements IChatService {

    private final ChatConversationRepository conversationRepository;
    private final ChatMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Autowired
    public ChatService(ChatConversationRepository conversationRepository,
                       ChatMessageRepository messageRepository,
                       UserRepository userRepository,
                       JwtProvider jwtProvider){
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void receiveMessage(ChatMessageDTO dto, String token) throws UserNotFoundException, MessageMalformedException {

        User user = userRepository.findByUsername(jwtProvider.resolveUsername(token)).orElseThrow(()-> new UserNotFoundException("User not found"));

        ChatMessage message = ObjectMapper.chatMessageFromDto(dto, userRepository);
        ChatConversation conversation = conversationRepository.findByUser1AndUser2(message.getFromUser(), message.getToUser());

        if(!conversation.getCid().equals(message.getCid()) || message.getCid().equals(""))
            throw new MessageMalformedException("Conversation not found -> ConservationId: " + message.getCid());
        if(message.getFromUser() != user || message.getToUser() == user) throw new MessageMalformedException("You shall not pass");

        messageRepository.save(message);

        //TODO: notify "User toUser about message"

    }

    @Override
    public List<ChatMessageDTO> getExistingMessages(String token) throws UserNotFoundException {
        User user = userRepository.findByUsername(jwtProvider.resolveUsername(token)).orElseThrow(
                ()-> new UserNotFoundException("User not found"));
        List<ChatMessage> messages = messageRepository.findByToUserOrFromUser(user, user);


        return ObjectMapper.dtosFromChatMessages(messages);
    }

    @Override
    public ChatConversationDTO establishConversation(String token, UserDTO userDTO) throws UserNotFoundException {
        User user = userRepository.findByUsername(jwtProvider.resolveUsername(token)).orElseThrow(
                ()-> new UserNotFoundException("User not found"));
        User user1 = userRepository.findById(userDTO.getUserId()).orElseThrow(
                ()-> new UserNotFoundException("User not found -> UserId: " + userDTO.getUserId()));

        ChatConversation conversation = conversationRepository.findByUser1AndUser2(user, user1);
        if(conversation == null){
            conversation = new ChatConversation(user, user1);
            conversationRepository.save(conversation);
        }

        return new ChatConversationDTO(conversation);
    }

    @Override
    public List<ChatConversationDTO> getExistingConversations(String token) throws UserNotFoundException {
        User user = userRepository.findByUsername(jwtProvider.resolveUsername(token)).orElseThrow(
                ()-> new UserNotFoundException("User not found"));
        List<ChatConversation> conversations = conversationRepository.findByUser1OrUser2(user, user);
        List<ChatConversationDTO> dtos = ObjectMapper.dtosFromChatConversations(conversations);
        return dtos;
    }

}
