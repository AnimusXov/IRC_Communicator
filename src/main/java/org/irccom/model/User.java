package org.irccom.model;

public class User {
    int id;
    String nickname;
    String alt_nickname;
    String realname;
    String username;
    String password;
    String channels;


    public User(int id, String nickname, String alt_nickname, String realname, String username, String password, String channels) {
        this.id = id;
        this.nickname = nickname;
        this.alt_nickname = alt_nickname;
        this.realname = realname;
        this.username = username;
        this.password = password;
        this.channels = channels;
    }

    public User(int id, String nickname, String alt_nickname, String realname, String username, String password) {
        this.id = id;
        this.nickname = nickname;
        this.alt_nickname = alt_nickname;
        this.realname = realname;
        this.username = username;
        this.password = password;
    }

    public User(String nickname, String alt_nickname, String realname, String username, String password) {
        this.nickname = nickname;
        this.alt_nickname = alt_nickname;
        this.realname = realname;
        this.username = username;
        this.password = password;
    }

    public User(String channels) {
        this.channels = channels;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAlt_nickname() {
        return alt_nickname;
    }

    public void setAlt_nickname(String alt_nickname) {
        this.alt_nickname = alt_nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }
}
