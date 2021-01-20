package org.irccom.irc.model;

public class Message {
    private String nickname;
    private String message;
    private String role;

    public String getNickname() {
        return nickname;
    }

    public Message(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }

    public Message(String nickname, String message, String role) {
        this.nickname = nickname;
        this.message = message;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public String getRole() {
        return role;
    }

    public Message(String message) {
        this.message = message;
    }

}
