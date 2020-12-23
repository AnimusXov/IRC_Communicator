package org.irccom.irc.models;

public class Message {
    private String nickname;
    private String message;

    public Message() {
    }

    public Message(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }

    @Override
    public String toString() {
        return "<" + nickname + ">" + " " + message;
    }
}
