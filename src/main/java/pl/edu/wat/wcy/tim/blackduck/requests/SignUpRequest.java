package pl.edu.wat.wcy.tim.blackduck.requests;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(max = 60)
    @Email
    private String email;


    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @Size(max = 50)
    private String description;


}
