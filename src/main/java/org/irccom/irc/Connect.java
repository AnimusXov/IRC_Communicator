package org.irccom.irc;

import org.irccom.sqlite.model.Server;
import org.irccom.sqlite.model.User;
import org.kitteh.irc.client.library.Client;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Connect {
    public Connect() {
    }
    public static Client client;
    Client.Builder builder = Client.builder();

    public void connect(Server server, User user,boolean isUserInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        builder.listeners().input(line -> System.out.println(sdf.format(new Date()) + ' ' + "[I] " + line));
        builder.listeners().output(line -> System.out.println(sdf.format(new Date()) + ' ' + "[O] " + line));
        builder.listeners().exception(Throwable::printStackTrace);

        if(!server.getPort().equals("6697")){
            if(isUserInfo)
           buildInsecureConnection(user,server);
            else
                buildDefaultInsecureConnection(user,server);
        }
else {
            if (!isUserInfo) {
                buildDefaultConnect(server, user);
            } else {
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
                if (user.getChannels() != null) {
                    for (String channel : user.getChannels().split(",")) {
                        client.addChannel(channel);
                    }
                }
            }
        }
    }
    public void buildInsecureConnection(User user, Server server){
        Client.builder()
                .nick(user.getNickname())
                .name(user.getUsername())
                .realName(user.getRealname())
                .server().port((Integer.parseInt(server.getPort())), Client.Builder.Server.SecurityType.INSECURE)
                .host(server.getIp())
                .password(user.getPassword())
                .then()
                .buildAndConnect();
    }

    public void buildDefaultInsecureConnection(User user, Server server){
        client = Client.builder()
                .nick(user.getNickname())
                .name(user.getUsername())
                .server().port((Integer.parseInt(server.getPort())), Client.Builder.Server.SecurityType.INSECURE)
                .host(server.getIp())
                .then()
                .buildAndConnect();
    }

    public void buildDefaultConnect(Server server, User user){
        client = Client.builder()
                .nick(user.getNickname())
                .name(user.getUsername())
				.realName("KhIRC")
                .server()
                .host(server.getIp())
                .then()
                .buildAndConnect();
    }
private boolean isNamesEmpty(User user){
	return user.getNickname().isEmpty() || user.getUsername().isEmpty();
}
private User setDefaultNames(User user){
    	if(isNamesEmpty(user)){
    		user.setNickname("Guest1234");
    		user.setUsername("Guest12345");
	    }
    	return user;
}

}
