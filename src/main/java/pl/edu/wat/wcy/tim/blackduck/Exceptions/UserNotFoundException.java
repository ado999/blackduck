package pl.edu.wat.wcy.tim.blackduck.Exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() { super(); }
    public UserNotFoundException(String message) { super(message);  }
    public UserNotFoundException(String message, Throwable cause) { super(message, cause); }
    public UserNotFoundException(Throwable cause) { super(cause); }
}
