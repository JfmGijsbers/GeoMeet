package com.group02tue.geomeet;

public class BaseMessage {
    String message;
    User sender;
    long createdAt;

    public BaseMessage(String message, User sender, long createdAt) {
        this.message = message;
        this.sender = sender;
        this.createdAt = createdAt;
    }
}
class User {
    String username;
    public User(String username) {
        this.username = username;
    }
}
