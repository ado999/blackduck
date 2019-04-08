package pl.edu.wat.wcy.tim.blackduck.exceptions;

import java.util.Date;

public class ExceptionResponse {

    private String message;
    private Date timestamp;

    public ExceptionResponse(String message){
        this.message = message;
        this.timestamp = new Date();
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
