package pl.edu.wat.wcy.tim.blackduck.exceptions;

public class TheSameUserException extends Exception {

    public TheSameUserException(){
        super();
    }

    public TheSameUserException(String message){
        super(message);
    }

    public TheSameUserException(String message, Throwable cause){
        super(message, cause);
    }

}
