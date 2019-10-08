package pl.edu.wat.wcy.tim.blackduck.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupResponse {
    private boolean success;
    private String message;
}
