package pl.edu.wat.wcy.tim.blackduck.messaging.services;

import com.google.common.collect.Lists;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import pl.edu.wat.wcy.tim.blackduck.Exceptions.IsSameUserException;
import pl.edu.wat.wcy.tim.blackduck.messaging.DTOs.ChatChannelInitDTO;
import pl.edu.wat.wcy.tim.blackduck.messaging.DTOs.ChatMessageDTO;
import pl.edu.wat.wcy.tim.blackduck.messaging.DTOs.NotificationDTO;
import pl.edu.wat.wcy.tim.blackduck.messaging.models.ChatChannel;
import pl.edu.wat.wcy.tim.blackduck.messaging.models.ChatMessage;
import pl.edu.wat.wcy.tim.blackduck.messaging.repositories.ChatChannelRepository;
import pl.edu.wat.wcy.tim.blackduck.messaging.repositories.ChatMessageRepository;
import pl.edu.wat.wcy.tim.blackduck.models.User;
import pl.edu.wat.wcy.tim.blackduck.services.implementations.UserService;
import pl.edu.wat.wcy.tim.blackduck.util.ObjectMapper;

import java.util.List;

@Deprecated
public class ChatService extends ChannelInterceptorAdapter {

    private final int MAX_PAGEABLE_CHAT_MESSAGES = 100;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatChannelRepository chatChannelRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    public ChatService(){}

    //_______________________________________________________________________________
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent){
        StompHeaderAccessor stompDetails = StompHeaderAccessor.wrap(message);

        if(stompDetails.getCommand() == null) return;

        switch (stompDetails.getCommand()){
            case CONNECT:
                break;
            case CONNECTED:
                userService.setPresence(stompDetails.getUser().getName(), true);
                break;
            case DISCONNECT:
                userService.setPresence(stompDetails.getUser().getName(), false);
                break;
        }
    }

    private String getExistingChannel(ChatChannelInitDTO chatChannelInitDTO){
        List<ChatChannel> channels = chatChannelRepository.findExistingChannel(
                chatChannelInitDTO.getUserOneId(),
                chatChannelInitDTO.getUserTwoId());
        return (channels != null && !channels.isEmpty()) ? channels.get(0).getUuid() : null;
    }

    private String newChatSession(ChatChannelInitDTO chatChannelInitDTO)
            throws BeansException {
        ChatChannel channel = new ChatChannel(
                userService.getUser(chatChannelInitDTO.getUserOneId()),
                userService.getUser(chatChannelInitDTO.getUserTwoId()));
        chatChannelRepository.save(channel);
        return channel.getUuid();
    }

    public String establishChatSession(ChatChannelInitDTO chatChannelInitDTO)
            throws IsSameUserException, BeansException {
        if (chatChannelInitDTO.getUserOneId() == chatChannelInitDTO.getUserTwoId()) {
            throw new IsSameUserException();
        }

        String uuid = getExistingChannel(chatChannelInitDTO);

        return (uuid != null) ? uuid : newChatSession(chatChannelInitDTO);
    }

    public void submitMessage(ChatMessageDTO chatMessageDTO)
            throws BeansException {
        ChatMessage chatMessage = ObjectMapper.messageFromDto(chatMessageDTO, userService);

        chatMessageRepository.save(chatMessage);

        User fromUser = userService.getUser(chatMessage.getAuthorUser().getId());
        User recipientUser = userService.getUser(chatMessage.getRecipentUser().getId());

        notifyUser(recipientUser,
                new NotificationDTO(
                        "ChatMessageNotification",
                        fromUser.getUsername() + " has sent you a message",
                        chatMessage.getAuthorUser().getId()
                )
        );
    }

    private void notifyUser(User recipientUser, NotificationDTO notification) {
        if (userService.isPresent(recipientUser)) {
            simpMessagingTemplate
                    .convertAndSend("/topic/user.notification." + recipientUser.getId(), notification);
        }
    }

    public List<ChatMessageDTO> getExistingChatMessages(String channelUuid) {
        ChatChannel channel = chatChannelRepository.getChannelDetails(channelUuid);

        List<ChatMessage> chatMessages =
                chatMessageRepository.getExistingChatMessages(
                        channel.getUserOne().getId(),
                        channel.getUserTwo().getId(),
                        new PageRequest(0, MAX_PAGEABLE_CHAT_MESSAGES)
                );
        List<ChatMessage> messagesByLatest = Lists.reverse(chatMessages);

        return ObjectMapper.dtosfromMessages(messagesByLatest);
    }






}
