package pl.edu.wat.wcy.tim.blackduck.requests;

import lombok.Data;

@Data
public class EditProfileRequest {

    private String profilePicture;

    private String backgroundPicture;

    private String fullName;

    private String description;

    private String email;
}
