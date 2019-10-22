package pl.edu.wat.wcy.tim.blackduck.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserResponse {

    private String uuid; //not displayed anywhere, identifies user, generated, immutable, unique

    private String displayName; // eg. "AZxBlue96",

    private String fullName; // eg. "Jan Kowalski"

    private Date creationDate; // date of creation

    private String profilePhotoUrl;

    private String profileBackgroundUrl;

    private String description; // eg. "Influencer, Horse lover, \n Warsaw, PL"

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