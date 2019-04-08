package pl.edu.wat.wcy.tim.blackduck.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "chat_conversations")
public class ChatConversation {

    @Id
    @NotNull
    private String cid;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_1")
    private User user1;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_2")
    private User user2;


    public ChatConversation(){}

    public ChatConversation(User user1, User user2){
        this.user1 = user1;
        this.user2 = user2;
        this.cid = UUID.randomUUID().toString();
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public String getCid() {
        return cid;
    }
}
