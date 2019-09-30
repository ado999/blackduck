package pl.edu.wat.wcy.tim.blackduck.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class PostRequest {

    private String title;

    private String body; // file zakodowany

    private Integer folderId;

    private String description;
}
