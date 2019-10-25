package pl.edu.wat.wcy.tim.blackduck.responses;

public class FirebaseMessageResponse {
    private String to;
    private Message data;

    public void setTo(String to){
        this.to = "/topics/" + to;
    }

    public void setChatMessageResponse(ChatMessageResponse message){
        Message m = new Message();
        m.setChatMessageResponse(message);
        this.data = m;
    }

    class Message{
        private ChatMessageResponse chatMessageResponse;

        public void setChatMessageResponse(ChatMessageResponse cmr){
            this.chatMessageResponse = cmr;
        }
    }
}
