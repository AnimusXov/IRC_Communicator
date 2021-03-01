package org.irccom.irc.model;

import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.irccom.controller.model.PrefixUser;

@Getter
@Setter
public class Message {
    private String time;
    private String nickname;
    private String message;
    private PrefixUser prefixUser;
    private ImageView image;
    boolean hasImage;

public Message(){

}


public String getNickname() {
        return nickname;
    }

    public Message(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }



public Message( String message, PrefixUser prefixUser ){
	this.message = message;
	this.prefixUser = prefixUser;
}

public Message( String nickname, String message, PrefixUser prefixUser ){
	this.nickname = nickname;
	this.message = message;
	this.prefixUser = prefixUser;
}


    public Message(String message) {
        this.message = message;
    }

}
