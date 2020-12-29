package org.irccom.model;

public class Server {
    private int id;
    private String name;
    private String ip;
    private String port;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }


    public Server(int id, String name, String ip, String port) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.port = port;
    }


    @Override
    public String toString() {
        return name;
    }
}
