package org.irccom.irc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import net.engio.mbassy.listener.Handler;
import org.irccom.controller.MainWindowController;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Connect {




    public Connect() {
    }




    public static Client client;


    Client.Builder builder = Client.builder();



    public void connect(String nick, String ip) {


        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        builder.listeners().input(line -> System.out.println(sdf.format(new Date()) + ' ' + "[I] " + line));
        builder.listeners().output(line -> System.out.println(sdf.format(new Date()) + ' ' + "[O] " + line));
        builder.listeners().exception(Throwable::printStackTrace);

        client = Client.builder()
                .nick(nick)
                .server()
                .host(ip)
                .then()
                .buildAndConnect();
        client.addChannel("#test999");
        client.addChannel("#test998");



       System.out.println(client.getClient().getChannels().isEmpty());
        System.out.println(client.getChannels().isEmpty());


    }
  /*  @Handler
    public void onChannelMessage(@org.jetbrains.annotations.NotNull ChannelMessageEvent event ) {
        if (event.getActor().getNick().equals(event.getActor().getClient().getNick()))
            return;
        System.out.println(event.getMessage());
    }*/


}
