package pl.edu.wat.wcy.tim.blackduck.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.wat.wcy.tim.blackduck.models.Post;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateResponse {

    private int rate;

    private UserShortResponse fromUser;
}