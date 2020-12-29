package org.irccom.irc.models;


import javafx.scene.image.ImageView;

public class Message {
    private String nickname;
    private String message;
    private ImageView image;

    public Message() {
    }

    public Message(String message) {
        this.message = message;
    }

    public Message(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }

    public Message(ImageView image) {
        this.image = image;
    }

    @Override
    public String toString() {
        if(nickname != null)
        return "<" + nickname + ">" + " " + message;
        return message;
    }

}
