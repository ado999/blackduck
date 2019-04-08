package pl.edu.wat.wcy.tim.blackduck.DTOs;

import pl.edu.wat.wcy.tim.blackduck.models.ChatConversation;

public class ChatConversationDTO {

    private String cid;

    private int user1Id;

    private int user2Id;

    public ChatConversationDTO(ChatConversation conversation){
        this.cid = conversation.getCid();
        this.user1Id = conversation.getUser1().getId();
        this.user2Id = conversation.getUser2().getId();
    }

    public ChatConversationDTO(){}

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }
}
