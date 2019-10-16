package pl.edu.wat.wcy.tim.blackduck.requests;

import lombok.Data;

@Data
public class PostRequestPreuploaded {

    private String title;

    private String file;

    private String vidPhoto;

    private Integer folderId;

    private String description;
}
