package pl.edu.wat.wcy.tim.blackduck.requests;

import lombok.Data;

@Data
public class FolderRequest {
    private String folderName;
    private String description;
    private boolean isPrivate;
}
