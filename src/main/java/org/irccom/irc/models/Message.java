package org.irccom.irc.models;

public class Message {
    private String nickname;
    private String message;

    public String getNickname() {
        return nickname;
    }

    public Message(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public Message() {
    }
    public Message(String message) {
        this.message = message;
    }

}
