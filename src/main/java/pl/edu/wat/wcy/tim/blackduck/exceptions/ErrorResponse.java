package pl.edu.wat.wcy.tim.blackduck.exceptions;

import java.util.Date;

public class ErrorResponse {

    private String message;
    private Date timestamp;

    public ErrorResponse(String message){
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
