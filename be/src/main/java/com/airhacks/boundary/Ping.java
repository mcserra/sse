package com.airhacks.boundary;

public class Ping {
    private final String message;

    public Ping(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
