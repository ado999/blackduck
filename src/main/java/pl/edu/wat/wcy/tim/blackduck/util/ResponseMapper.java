package pl.edu.wat.wcy.tim.blackduck.util;

import org.springframework.stereotype.Component;
import pl.edu.wat.wcy.tim.blackduck.models.*;
import pl.edu.wat.wcy.tim.blackduck.responses.*;

import java.util.stream.Collectors;

@Component
public class ResponseMapper {
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getUuid(),
                user.getUsername(),
                user.getFullName(),
                user.getCreationDate(),
                user.getProfilePhotoUrl(),
                user.getProfileBacgroundUrl(),
                user.getDescription(),
                user.getPhoneNumber(),
                user.getFolders().stream().map(this::toResponse).collect(Collectors.toList()),
                user.getFollowers().stream().map(this::toShortResponse).collect(Collectors.toList()),
                user.getFollowedUsers().stream().map(this::toShortResponse).collect(Collectors.toList())
        );
    }

    public FolderResponse toResponse(Folder dir){
        return new FolderResponse(
                toShortResponse(dir.getOwner()),
                dir.getFolderName(),
                dir.getDescription(),
                dir.getContentList().stream().map(this::toResponse).collect(Collectors.toList())
        );
    }

    public PostResponse toResponse(Post post) {
        return new PostResponse(
                post.getTitle(),
                post.getContentUrl(),
                toResponse(post.getContentType()),
                toShortResponse(post.getAuthor()),
                post.getCreationDate(),
                post.getDescription(),
                post.getRootFolder(),
                post.getComments().stream().map(this::toResponse).collect(Collectors.toList()),
                post.getRates().stream().map(this::toResponse).collect(Collectors.toList())

        );
    }

    private CommentResponse toResponse(Comment comment){
        return new CommentResponse(
                toShortResponse(comment.getAuthor()),
                comment.getContent(),
                comment.getCreationDate()
        );
    }

    private RateResponse toResponse(Rate rate){
        return new RateResponse(
                rate.getRate(),
                toShortResponse(rate.getFromUser())
        );
    }

    private UserShortResponse toShortResponse(User user){
        return new UserShortResponse(
                user.getUuid(),
                user.getUsername(),
                user.getProfilePhotoUrl()
        );
    }

    private ContentTypeResponse toResponse(ContentType contentType){
        return ContentTypeResponse.valueOf(contentType.name());
    }
}
