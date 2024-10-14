package org.bilyoner.exception;

public class CustomException extends RuntimeException {

    private int statusCode;

    public CustomException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}