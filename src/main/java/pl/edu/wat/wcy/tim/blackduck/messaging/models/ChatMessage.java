package pl.edu.wat.wcy.tim.blackduck.messaging.models;

import pl.edu.wat.wcy.tim.blackduck.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "authorUserId")
    private User authorUser;

    @OneToOne
    @JoinColumn(name = "recopentUserId")
    private User recipentUser;

    @NotNull
    private Date timeSent;

    @NotNull
    private String content;

    public ChatMessage(){};

    public ChatMessage(User author, User recipent, String content){
        authorUser = author;
        recipentUser = recipent;
        this.content = content;
        this.timeSent = new Date();
    }

    public long getId() {
        return id;
    }

    public User getAuthorUser() {
        return authorUser;
    }

    public User getRecipentUser() {
        return recipentUser;
    }

    public Date getTimeSent() {
        return timeSent;
    }

    public String getContent() {
        return content;
    }
}
