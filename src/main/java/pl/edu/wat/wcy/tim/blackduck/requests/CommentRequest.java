package pl.edu.wat.wcy.tim.blackduck.requests;

import javax.validation.constraints.NotBlank;

public class CommentRequest {
    @NotBlank
    private String description;
    private String username;
    private String imageId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
