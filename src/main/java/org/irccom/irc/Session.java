package org.irccom.irc;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.User;

import java.util.*;
import java.util.stream.Collectors;

public class Session {
    Channel channel;

   /* public List<String> getUsers(Set<Channel> channel){

         if(channel.iterator().hasNext()) {
             return channel.stream().iterator().next().getNicknames();
         }
         return null;
    } */


    public List<String> getUsers(String channel, Client client) {
        if (client.getChannel(channel).isPresent()) {
            Channel ircChannel = client.getChannel(channel).get();
            List<User> listOfUsers = ircChannel.getUsers();

            return listOfUsers.stream()
                    .map(User::getNick)
                    .filter(s -> !s.equals(client.getNick()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    public Set<Channel> getChannels(Client client){
        return client.getChannels();
    }
}
