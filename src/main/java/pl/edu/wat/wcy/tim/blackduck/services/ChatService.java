package pl.edu.wat.wcy.tim.blackduck.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.tim.blackduck.requests.ChatMessageRequest;
import pl.edu.wat.wcy.tim.blackduck.requests.GetMessagesRequest;
import pl.edu.wat.wcy.tim.blackduck.responses.ChatConversationResponse;
import pl.edu.wat.wcy.tim.blackduck.responses.ChatMessageResponse;
import pl.edu.wat.wcy.tim.blackduck.DTOs.UserDTO;
import pl.edu.wat.wcy.tim.blackduck.exceptions.MessageMalformedException;
import pl.edu.wat.wcy.tim.blackduck.exceptions.UserNotFoundException;
import pl.edu.wat.wcy.tim.blackduck.models.ChatConversation;
import pl.edu.wat.wcy.tim.blackduck.models.ChatMessage;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.repositories.ChatConversationRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.ChatMessageRepository;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.LoginRequest;
import pl.edu.wat.wcy.tim.blackduck.security.JwtProvider;
import pl.edu.wat.wcy.tim.blackduck.util.ObjectMapper;
import pl.edu.wat.wcy.tim.blackduck.util.RequestValidationComponent;
import pl.edu.wat.wcy.tim.blackduck.util.ResponseMapper;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService implements IChatService {

    private final ChatConversationRepository conversationRepository;
    private final ChatMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final SimpMessagingTemplate messagingTemplate;
    private final RequestValidationComponent validationComponent;
    private final ResponseMapper responseMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public ChatService(ChatConversationRepository conversationRepository,
                       ChatMessageRepository messageRepository,
                       UserRepository userRepository,
                       JwtProvider jwtProvider,
                       SimpMessagingTemplate messagingTemplate,
                       RequestValidationComponent validationComponent,
                       ResponseMapper responseMapper,
                       ObjectMapper objectMapper
    ){
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.messagingTemplate = messagingTemplate;
        this.validationComponent = validationComponent;
        this.responseMapper = responseMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public ChatMessageResponse sendMessage(ChatMessageRequest request , HttpServletRequest req) throws UserNotFoundException, AuthenticationException {

        User user = validationComponent.validateRequest(req);

        ChatMessage message = objectMapper.toObject(request);
        message.setFromUser(user);
        Optional<ChatConversation> chatConversationOpt =
                conversationRepository.findByUser1AndUser2(message.getFromUser(), message.getToUser());

        if(!chatConversationOpt.isPresent()){
            createConversation(message);
        }

        messageRepository.save(message);

        String uid = message.getToUser().getUuid();
        if(uid != null && !uid.equals(""))
        messagingTemplate.convertAndSend(
                "/topic/greetings",
                responseMapper.toResponse(message));

        return responseMapper.toResponse(message);
    }

    @Override
    public List<ChatMessageResponse> getExistingMessages(GetMessagesRequest getMessagesRequest, HttpServletRequest req) throws AuthenticationException {
        User user = validationComponent.validateRequest(req);
        Optional<User> toUserOpt = userRepository.findByUsername(getMessagesRequest.getToUserUsername());

        if(toUserOpt.isPresent()){
            List<ChatMessage> messages = messageRepository.findAllByFromUserAndToUser(user, toUserOpt.get());

            return messages.stream()
                    .map(responseMapper::toResponse)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<ChatConversationResponse> getExistingConversations(HttpServletRequest req) throws AuthenticationException {

        User user = validationComponent.validateRequest(req);

        List<ChatConversation> conversations = conversationRepository.findByUser1OrUser2(user, user);
        return conversations.stream()
                .map(responseMapper::toResponse)
                .collect(Collectors.toList());
    }

    private void createConversation(ChatMessage message) throws UserNotFoundException {
        ChatConversation conversation = new ChatConversation(
                message.getFromUser(),
                message.getToUser()
        );
        conversationRepository.save(conversation);
    }

    public void test(LoginRequest dto) {
        messagingTemplate.convertAndSend("/socket-publisher/"+dto.getUsername(),dto.getPassword());
        System.out.println("test");
    }
}
