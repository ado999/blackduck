package pl.edu.wat.wcy.tim.blackduck.messaging.DTOs;

public class ChatChannelEstablishedDTO {
    private String uuid;
    private String userOneName;
    private String userTwoName;

    public ChatChannelEstablishedDTO(){}

    public ChatChannelEstablishedDTO(String uuid, String userOneName, String userTwoName){
        this.uuid = uuid;
        this.userOneName = userOneName;
        this.userTwoName = userTwoName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUserOneName() {
        return userOneName;
    }

    public String getUserTwoName() {
        return userTwoName;
    }
}
