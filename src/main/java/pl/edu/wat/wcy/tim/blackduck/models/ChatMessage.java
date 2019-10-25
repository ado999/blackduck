package pl.edu.wat.wcy.tim.blackduck.models;

import lombok.Data;
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

    public ChatMessage(User fromUser, User toUser, String message){
       this.fromUser = fromUser;
       this.toUser = toUser;
       this.message = message;
       this.date = new Date();
    }

    public ChatMessage(){}
}
