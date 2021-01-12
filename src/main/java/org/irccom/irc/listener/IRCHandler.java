package org.irccom.irc.listener;

import javafx.application.Platform;
import net.engio.mbassy.listener.Handler;
import org.irccom.controller.MainWindowController;
import org.irccom.irc.models.Message;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.client.library.event.client.ClientNegotiationCompleteEvent;
import org.kitteh.irc.client.library.event.helper.ServerMessageEvent;
import org.kitteh.irc.client.library.event.user.PrivateMessageEvent;

public class IRCHandler {
    private final MainWindowController controller;

    public IRCHandler(MainWindowController controller) {
        this.controller = controller;
    }

    @Handler
    public void privmsg(PrivateMessageEvent event) {
        controller.createNewTab(new Message(event.getActor().getNick(),event.getMessage()));
    }

    @Handler
    public void onServerJoin(ClientNegotiationCompleteEvent event ) {
       controller.setMessageListAndServerLabel(event.getServerInfo().getNetworkName().get());
    }
    @Handler
    public void onServerMessage(ServerMessageEvent event ) {
        Platform.runLater(() -> {
            controller.newServerMessage(new Message(event.getSource().getMessage()));
        });
    }
    @Handler
    public void onChannelMessage(ChannelMessageEvent event ) {
        Platform.runLater(() -> {
         controller.addMessageToList(new Message(event.getActor().getNick(), event.getMessage()),event.getChannel());
        });

    }
}
