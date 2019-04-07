package pl.edu.wat.wcy.tim.blackduck.messaging.models;

import pl.edu.wat.wcy.tim.blackduck.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "chat_channels")
public class ChatChannel {

    @Id
    @NotNull
    private String uuid;

    @OneToOne
    @JoinColumn(name = "userOne")
    private User userOne;

    @OneToOne
    @JoinColumn(name = "userTwo")
    private User userTwo;

    public ChatChannel(){}

    public ChatChannel(User userOne, User userTwo){
        this.uuid = UUID.randomUUID().toString();
        this.userOne = userOne;
        this.userTwo = userTwo;
    }

    public String getUuid() {
        return uuid;
    }

    public User getUserOne() {
        return userOne;
    }

    public User getUserTwo() {
        return userTwo;
    }
}
