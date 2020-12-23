package org.irccom.irc.models;

import javafx.collections.ObservableList;
import org.kitteh.irc.client.library.element.Channel;

import java.util.HashSet;

public class CurrentChannel {
    private ObservableList<Message> msgObsList;
    private Message msg;


    public CurrentChannel(ObservableList<Message> msgObsList, Message msg) {
        this.msgObsList = msgObsList;
        this.msg = msg;
    }
}
