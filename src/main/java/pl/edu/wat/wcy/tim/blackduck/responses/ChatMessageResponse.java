package pl.edu.wat.wcy.tim.blackduck.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageResponse {

    private int id;
    private String message;
    private UserShortResponse fromUser;
    private UserShortResponse toUser;
    private Date date;
}
