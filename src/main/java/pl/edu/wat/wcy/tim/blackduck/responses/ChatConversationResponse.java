package pl.edu.wat.wcy.tim.blackduck.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatConversationResponse {

    private String cid;

    private UserShortResponse user1;

    private UserShortResponse user2;
}
