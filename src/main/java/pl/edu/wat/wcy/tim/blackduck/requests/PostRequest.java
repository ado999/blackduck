package pl.edu.wat.wcy.tim.blackduck.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostRequest {

    private String title;

    private MultipartFile file;

    private MultipartFile vidPhoto;

    private Integer folderId;

    private String description;
}
