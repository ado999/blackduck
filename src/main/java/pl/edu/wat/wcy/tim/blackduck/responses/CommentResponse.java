package pl.edu.wat.wcy.tim.blackduck.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.wat.wcy.tim.blackduck.models.Post;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

  //  private PostResponse post;

    private UserShortResponse author;

    private String content;

    private Date creationDate;
}