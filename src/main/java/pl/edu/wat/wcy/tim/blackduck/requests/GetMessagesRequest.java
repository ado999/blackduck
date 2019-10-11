package pl.edu.wat.wcy.tim.blackduck.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetMessagesRequest {
    private String toUserUsername;
}
