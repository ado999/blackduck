package pl.edu.wat.wcy.tim.blackduck.responses;

public class CommentResponse {
    private String id;
    private String description;
    private String date;
    private String user;
    private String image;

    public CommentResponse(String id, String description, String date, String user, String image) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.user = user;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
