package pl.edu.wat.wcy.tim.blackduck.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.wat.wcy.tim.blackduck.exceptions.UserNotFoundException;
import pl.edu.wat.wcy.tim.blackduck.models.*;
import pl.edu.wat.wcy.tim.blackduck.repositories.UserRepository;
import pl.edu.wat.wcy.tim.blackduck.requests.*;
import pl.edu.wat.wcy.tim.blackduck.responses.UserShortResponse;

import java.util.Date;
import java.util.Optional;

@Component
public class ObjectMapper {

    private final UserRepository userRepository;

    @Autowired
    public ObjectMapper(
            UserRepository userRepository
    ){
        this.userRepository = userRepository;
    }


    public ChatMessage toObject(ChatMessageRequest request) throws UserNotFoundException {
        User toUser = userRepository.findByUsername(request.getToUser()).orElseThrow(() -> new UserNotFoundException("User not found"));

        ChatMessage message = new ChatMessage(null, toUser, request.getMessage());

        return message;
    }

    public User toObject(SignUpRequest request){
        return new User(
                request.getUsername(),
                request.getFullName(),
                request.getEmail(),
                request.getPassword(),
                request.getDescription(),
                new Date(),
                null,
                null
        );
    }


    public static Post toObject(PostRequest request) {
        return new Post(
                request.getTitle(),
                null,
                null,
                null,
                null,
                new Date(),
                request.getDescription(),
                null
        );
    }

    public static Post toObject(PostRequestPreuploaded request) {
        return new Post(
                request.getTitle(),
                null,
                null,
                null,
                null,
                new Date(),
                request.getDescription(),
                null
        );
    }

    public static Folder toObject(FolderRequest request){
        return new Folder(
                null,
                request.getFolderName(),
                request.getDescription()
        );
    }

    public static Comment toObject (CommentRequest request){
        return new Comment(
                null,
                null,
                request.getContent(),
                new Date()
        );
    }

    public static Rate toObject (RateRequest request){
        return new Rate(
                request.getRate(),
                null,
                null
        );
    }
}
