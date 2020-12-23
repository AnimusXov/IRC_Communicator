package org.irccom.listener;


import net.engio.mbassy.listener.Handler;
import org.irccom.irc.Connect;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.defaults.listener.AbstractDefaultListenerBase;
import org.kitteh.irc.client.library.defaults.listener.DefaultListeners;
import org.kitteh.irc.client.library.element.MessageReceiver;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.client.library.event.helper.MessageEvent;
import org.kitteh.irc.client.library.event.user.PrivateMessageEvent;
import org.kitteh.irc.client.library.event.user.PrivateNoticeEvent;
import org.kitteh.irc.client.library.util.Listener;

import java.util.Objects;

public class MessageListener {
    Connect conn;

    @Handler
    public void onChannelMessage(ChannelMessageEvent event) {
        if(event.getActor().getNick().equals(event.getActor().getClient().getNick()))
            return;
        String message = event.getMessage();
        System.out.println(message);
    }



}
