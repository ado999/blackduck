package pl.edu.wat.wcy.tim.blackduck.messaging.DTOs;


public class NotificationDTO {
    private String type;
    private String content;
    private int fromUserId;

    public NotificationDTO(String type, String content, int fromUserId){
        this.type = type;
        this.content = content;
        this.fromUserId = fromUserId;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public int getFromUserId() {
        return fromUserId;
    }
}
