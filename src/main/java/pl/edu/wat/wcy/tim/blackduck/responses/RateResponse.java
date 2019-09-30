package pl.edu.wat.wcy.tim.blackduck.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RateResponse {

    private int rate; // int in 1..5

    private UserShortResponse fromUser;
}