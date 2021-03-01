package org.irccom.irc.listener;

import com.google.common.collect.ImmutableMap;
import lombok.Setter;
import net.engio.mbassy.listener.Handler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.irccom.controller.MainWindowController;
import org.irccom.controller.factory.MessageCompomentFactory;
import org.irccom.controller.model.PrefixUser;
import org.irccom.helper.Validator;
import org.irccom.irc.Connect;
import org.irccom.irc.model.Message;
import org.irccom.irc.model.User;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.mode.ChannelUserMode;
import org.kitteh.irc.client.library.event.channel.*;
import org.kitteh.irc.client.library.event.client.ClientNegotiationCompleteEvent;
import org.kitteh.irc.client.library.event.client.NickRejectedEvent;
import org.kitteh.irc.client.library.event.connection.ClientConnectionClosedEvent;
import org.kitteh.irc.client.library.event.connection.ClientConnectionEstablishedEvent;
import org.kitteh.irc.client.library.event.helper.ServerMessageEvent;
import org.kitteh.irc.client.library.event.user.*;
import org.kitteh.irc.client.library.feature.network.ClientConnection;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.SortedSet;

public class IRCHandler {
    private final MainWindowController controller;
    private final ImmutableMap<String, Integer> ctcpQueries = ImmutableMap.of("VERSION", 1, "TIME ", 2, "FINGER ", 3,"PING",4,"PONG",5);
    Client client = Connect.client.getClient();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public IRCHandler(MainWindowController controller) {
        this.controller = controller;
    }

    // Handles private messages
    @Handler
    public void onPrivateMessage( PrivateMessageEvent event) {
        if(!(event.getActor().getNick().isEmpty() && event.getMessage().isEmpty())) {
	        Message message = new Message();
	        message.setNickname(event.getActor().getNick());
	        message.setMessage(event.getMessage());
	        message.setTime(LocalDateTime.now().format(formatter));
	        controller.createNewTab(message);
        }
    }
    // Handles events related to joining a server
    @Handler
    public void onServerJoin(ClientNegotiationCompleteEvent event ) {
       controller.setMessageListAndServerLabel(event.getServerInfo().getAddress().get());
    }
    // Handles messages from server
    @Handler
    public void onServerMessage(ServerMessageEvent event ) {
            Message message = new Message();
            message.setMessage(event.getSource().getMessage());
            message.setTime(LocalDateTime.now().format(formatter));
	        controller.newServerMessage(message);
         

    }
    // Handles messages from a channel
    @Handler
    
    public void onChannelMessage(ChannelMessageEvent event ) throws IOException{
	    new PrefixUser();
	    PrefixUser temp;
	    Message message = new Message();
	    System.out.println(Validator.isUrlValid(event.getMessage()));
	    if(Validator.isUrlValid(event.getMessage())){
		     message.setImage(new MessageCompomentFactory().getImageFromWeb(event.getMessage()));
		     message.setHasImage(true);
	    }
    	if( getUserModes(event).isPresent() && !getUserModes(event).get().isEmpty() ) {
		     temp = new PrefixUser(event.getActor(), getUserModes(event).get().first().getNickPrefix());
	    }
    	else{
		     temp = new PrefixUser(event.getActor(), ' ');
	    }
	    message.setNickname(event.getActor().getNick());
	    message.setMessage(event.getMessage());
	    message.setPrefixUser(temp);
	    message.setTime(LocalDateTime.now().format(formatter));
	    controller.addMessageToList(message, event.getChannel());
    }

    @NonNull
    private Optional<SortedSet<ChannelUserMode>> getUserModes( ChannelMessageEvent event ){
    	return event.getChannel().getUserModes(event.getActor());
    }
    
    //
    @Handler
    public  void onUserQuitServer(UserQuitEvent event){
    }
    @Handler
    public void onUserInviteToChannel(ChannelInviteEvent event){
    }
    @Handler
    public void onUserDepartChannel(ChannelPartEvent event){
    }
    @Handler
    public void onConnectionClosed( ClientConnectionClosedEvent event ){
    }
    @Handler
    public void onConnectionEstablished( ClientConnectionEstablishedEvent event ){
    }
    @Handler
    public void onNicknameRejected( NickRejectedEvent event){
    }
    @Handler
    public void onAccountStatusEvent(UserAccountStatusEvent event){
    }
    // Handles events related to user changing its nickname
    @Handler
    private  void onUserNicknameChanged(UserNickChangeEvent event){
        controller.updateUserList(event.getOldUser(),event.getNewUser());
    }
    // Handles events related to channel's topic change
    @Handler
    private  void onChannelTopicUpdated(ChannelTopicEvent event){
       controller.updateChannelTopic(event.getChannel(),event.getNewTopic().getValue().get());
    }
    // Handles events after user joins to a channel
    @Handler
    public void onChannelJoin(RequestedChannelJoinCompleteEvent event){
		    controller.updateChannelListAndMessageSet(event.getChannel());
    }
    @Handler
    public void onUserChannelJoin(ChannelJoinEvent event){
    	if (!event.getUser().getNick().equals(client.getNick()) && controller.pointerCurrentChannel.equals(event.getChannel())){
    	controller.updateUserJoin(event.getUser());
	    }
    }
    @Handler
    public void onUserKick( ChannelKickEvent event ){
    if(controller.pointerCurrentChannel.equals(event.getChannel())){
    	controller.updateUserKick(event.getUser());
    }
    }
    @Handler
    public void onKnock(ChannelKnockEvent event){
    }
  
   
    

    @Handler
    public void onCTCPQuery(PrivateCtcpQueryEvent event){
    	if(event.getReply().isPresent()) {
		    switch ( ctcpQueries.get(event.getCommand()) ) {
			    case 1:
				    event.setReply("KhIRC");
				    break;
			    case 2:
				    event.setReply(LocalTime.now().toString());
				    break;
			    case 3:
				    event.setReply("");
				    break;
			    case 4:
				    event.setReply("PONG :ping");
				    break;
			    case 5:
				    event.setReply("PING :pong");
				    break;
		    }
	    }
        }

}
