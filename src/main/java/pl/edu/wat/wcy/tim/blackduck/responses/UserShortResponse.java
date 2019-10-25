package pl.edu.wat.wcy.tim.blackduck.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class UserShortResponse {

    private String username;

    private String fullName;

    private String profilePhotoUrl;

    private Date lastActivity;
}