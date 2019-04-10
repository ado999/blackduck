package pl.edu.wat.wcy.tim.blackduck.DTOs;

import pl.edu.wat.wcy.tim.blackduck.models.ChatMessage;

import java.util.Date;

public class ChatMessageDTO {

    private int id;
    private String message;
    private int fromUserId;
    private int toUserId;
    private Date date;
    private String cid;

    public ChatMessageDTO(int id, String message, int fromUserId,
                           int toUserId, Date date, String cid){
        this.id = id;
        this.message = message;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.date = date;
        this.cid = cid;
    }

    public ChatMessageDTO(ChatMessage message){
        this.id = message.getId();
        this.message = message.getMessage();
        this.fromUserId = message.getFromUser().getId();
        this.toUserId = message.getToUser().getId();
        this.date = message.getDate();
        this.cid = message.getCid();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
