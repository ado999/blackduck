package pl.edu.wat.wcy.tim.blackduck.messaging.DTOs;

public class ChatChannelInitDTO {
    private int userOneId;
    private int userTwoId;

    private ChatChannelInitDTO(){}

    public int getUserOneId() {
        return userOneId;
    }

    public int getUserTwoId() {
        return userTwoId;
    }
}
