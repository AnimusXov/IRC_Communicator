package org.irccom.irc.model;

import javafx.collections.ObservableList;
import org.kitteh.irc.client.library.element.Channel;

public class CurrentChannel {
    private ObservableList<Message> msgObsList;
    private Channel channel;

    public ObservableList<Message> getMsgObsList() {
        return msgObsList;
    }

    public void setMsgObsList(ObservableList<Message> msgObsList) {
        this.msgObsList = msgObsList;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public CurrentChannel(ObservableList<Message> msgObsList, Channel channel) {
        this.msgObsList = msgObsList;
        this.channel = channel;
    }
}
