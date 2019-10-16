package pl.edu.wat.wcy.tim.blackduck.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
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

}