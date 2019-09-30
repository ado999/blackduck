package pl.edu.wat.wcy.tim.blackduck.requests;

import lombok.Data;

@Data
public class CommentRequest {
    private Integer postId;
    private String content;
}
