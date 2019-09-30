package pl.edu.wat.wcy.tim.blackduck.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
public class UserShortResponse {

    private String uuid; //not displayed anywhere, identifies user, generated, immutable, unique

    private String username;

    private String profilePhotoUrl;
}