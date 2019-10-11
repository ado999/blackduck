package pl.edu.wat.wcy.tim.blackduck.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageRequest {

    private String message;
    private String toUser;
}
