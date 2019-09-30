package pl.edu.wat.wcy.tim.blackduck.requests;

import lombok.Data;
import pl.edu.wat.wcy.tim.blackduck.responses.UserShortResponse;

@Data
public class FolderRequest {
    private String folderName;
    private String description;
}
