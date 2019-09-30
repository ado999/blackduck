package pl.edu.wat.wcy.tim.blackduck.models;

import lombok.Data;
import pl.edu.wat.wcy.tim.blackduck.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "chat_messages")
@Data
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String message;

    @NotNull
    @OneToOne
    @JoinColumn(name = "fromUser")
    private User fromUser;

    @NotNull
    @OneToOne
    @JoinColumn(name = "toUser")
    private User toUser;

    @NotNull
    private Date date;

    @NotNull
    private String cid;

    public ChatMessage(User fromUser, User toUser, String message, String cid){
       this.fromUser = fromUser;
       this.toUser = toUser;
       this.message = message;
       this.cid = cid;
       this.date = new Date();
    }

    public ChatMessage(){}


}
