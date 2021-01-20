package org.irccom.irc.model;

public class User {
    private String nickname;
    private String modes;

    public User(String nickname, String modes) {
        this.nickname = nickname;
        this.modes = modes;
    }

    public User(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getModes() {
        return modes;
    }

    public void setModes(String modes) {
        this.modes = modes;
    }
}
