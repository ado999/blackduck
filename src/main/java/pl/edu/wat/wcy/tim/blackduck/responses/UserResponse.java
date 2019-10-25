package pl.edu.wat.wcy.tim.blackduck.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserResponse {

    private String uuid;

    private String displayName;

    private String fullName;

    private Date creationDate;

    private String profilePhotoUrl;

    private String profileBackgroundUrl;

    private String description;

    private String email;

    private Date lastActivity;

    private boolean followed = false;

    public UserResponse(
            String uuid,
            String displayName,
            String fullName,
            Date creationDate,
            String profilePhotoUrl,
            String profileBackgroundUrl,
            String description,
            String email,
            Date lastActivity
    ) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.fullName = fullName;
        this.creationDate = creationDate;
        this.profilePhotoUrl = profilePhotoUrl;
        this.profileBackgroundUrl = profileBackgroundUrl;
        this.description = description;
        this.email = email;
        this.lastActivity = lastActivity;
    }
}