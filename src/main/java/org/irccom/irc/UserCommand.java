package org.irccom.irc;

import org.kitteh.irc.client.library.Client;

public enum UserCommand {
    //LEAVE, WHO, WHOIS, MSG, QUERY, QUIT, NICK, ME, TOPIC, KICK, INVITE, PING, IGNORE, NOTICE,
    JOIN {
        @Override
        public void join (String channel,Client client){
            client.addChannel(channel);
        }
    },

    ;

    public abstract void join(String channel, Client client);
}

