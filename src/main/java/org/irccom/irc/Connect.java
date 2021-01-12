package org.irccom.irc;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.irccom.model.Server;
import org.irccom.model.User;
import org.kitteh.irc.client.library.Client;

import javax.jws.soap.SOAPBinding;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class Connect {




    public Connect() {
    }




    public static Client client;
   // public static @NonNull Optional<org.kitteh.irc.client.library.element.User> user_;


    Client.Builder builder = Client.builder();



    public void connect(Server server, User user) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        builder.listeners().input(line -> System.out.println(sdf.format(new Date()) + ' ' + "[I] " + line));
        builder.listeners().output(line -> System.out.println(sdf.format(new Date()) + ' ' + "[O] " + line));
        builder.listeners().exception(Throwable::printStackTrace);

        client = Client.builder()
                .nick(user.getNickname())
                .name(user.getUsername())
                .realName(user.getRealname())
                .server()
                .host(server.getIp())
                .password(user.getPassword())
                .then()
                .buildAndConnect();
       // user_ = client.getUser();
        System.out.println(client.getChannels().isEmpty());
        for (String channel : user.getChannels().split(",")) {
            client.addChannel(channel);
        }
    }
    public void defaultConnect(Server server, User user){
        client = Client.builder()
                .nick(user.getNickname())
                .name(user.getUsername())
                .server()
                .host(server.getIp())
                .then()
                .buildAndConnect();
        for (String channel : user.getChannels().split(",")) {
            client.addChannel(channel);
        }
        //user_ = client.getUser();
        System.out.println(client.getChannels().isEmpty());

    }



}
