package pl.edu.wat.wcy.tim.blackduck.messaging.DTOs;

import pl.edu.wat.wcy.tim.blackduck.messaging.models.ChatMessage;

public class ChatMessageDTO {

    private int authorUserId;
    private int recipentUserId;
    private String content;

    public ChatMessageDTO(){}

    public ChatMessageDTO(int from, int to, String text){
        authorUserId = from;
        recipentUserId = to;
        content = text;
    }

    public ChatMessageDTO(ChatMessage message){
        this.authorUserId = message.getAuthorUser().getId();
        this.recipentUserId = message.getRecipentUser().getId();
        this.content = message.getContent();
    }

    public int getAuthorUserId() {
        return authorUserId;
    }

    public int getRecipentUserId() {
        return recipentUserId;
    }

    public String getContent() {
        return content;
    }
}
