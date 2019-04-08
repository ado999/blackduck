package pl.edu.wat.wcy.tim.blackduck.exceptions;

public class MessageMalformedException extends Exception {

    public MessageMalformedException(){
        super();
    }

    public MessageMalformedException(String message){
        super(message);
    }

    public MessageMalformedException(String message, Throwable cause){
        super(message, cause);
    }

}
