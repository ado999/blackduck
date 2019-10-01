package pl.edu.wat.wcy.tim.blackduck.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class PostRequest {

    private String title;

  //  private String body; // file zakodowany

    private MultipartFile file;

    private Integer folderId;

    private String description;
}
