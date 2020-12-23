package org.irccom.controller;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import net.engio.mbassy.listener.Handler;
import org.irccom.irc.Connect;
import org.irccom.irc.Session;
import org.irccom.irc.models.CurrentChannel;
import org.irccom.irc.models.Message;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.defaults.element.DefaultChannel;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.client.library.event.channel.ChannelNamesUpdatedEvent;
import org.kitteh.irc.client.library.event.channel.RequestedChannelJoinCompleteEvent;

import java.util.Hashtable;
import java.util.Optional;


public class MainWindowController   {



    Client client = Connect.client.getClient();
    Hashtable<String, Channel> setOfChannels;
    Hashtable<Channel, ObservableList<Message>> setOfMessageObsList;
    Channel pointerCurrentChannel;

    @FXML
    public ObservableList<Message> messageObsList = FXCollections.observableArrayList();
    @FXML
    public ObservableList<Message> privateMessageObsList = FXCollections.observableArrayList();
    @FXML
    public ObservableList<String> userObsList = FXCollections.observableArrayList();
    @FXML
    public ObservableList<String> channelObsList =  FXCollections.observableArrayList();
    @FXML
    public JFXListView<Message> chat_window;
    @FXML
    public JFXListView<String> channelsList;
    @FXML
    public JFXListView<String> userList;
    @FXML
    public JFXTextField typeField;


    private void setObsListForChannel(Channel channel){

        ObservableList<Message> privateMessageObsList =  FXCollections.observableArrayList();
        setOfMessageObsList.put(channel,privateMessageObsList);
    }



    public void registerListener(){
       client.getEventManager().registerEventListener(this);
    }


    @Handler
    private  void onChannelNamesUpdated(ChannelNamesUpdatedEvent event){
        // new Thread
            Platform.runLater(() -> {
                userObsList.addAll(event.getChannel().getNicknames());
                setUsers(userObsList);
            });
    }

    @FXML
    private void onMouseClicked(){
        Optional <Channel> channel  = client.getChannel(channelsList.getSelectionModel().getSelectedItem());
        channel.ifPresent(x -> pointerCurrentChannel = x);
    }


    @Handler
    public void onChannelJoin(RequestedChannelJoinCompleteEvent event){
        new Thread(() ->{
            setObsListForChannel(event.getChannel());
        });
            Platform.runLater(() -> {
                    channelObsList.add(event.getChannel().getName());
                    setChannels(channelObsList);
            });
    }

    public void setChannels(ObservableList<String> list){
        channelsList.setItems(list);
        channelsList.refresh();
    }
    public void setUsers(ObservableList<String> list){
        userList.setItems(list);
        userList.refresh();
        System.out.println(userObsList);
    }



    @Handler
    public void onChannelMessage(ChannelMessageEvent event )
    {
        new Thread(() -> {
            if (event.getActor().getNick().equals(event.getActor().getClient().getNick()))
                return;
            if(!(pointerCurrentChannel == null))
            privateMessageObsList = setOfMessageObsList.get(pointerCurrentChannel);

        });

        Platform.runLater(() -> {
          privateMessageObsList.add(new Message(event.getActor().getNick(),event.getMessage()));
        }); {

        chat_window.setItems(privateMessageObsList);
        chat_window.refresh();
    }




    }

    @FXML
    public  void initialize() throws InterruptedException {
        registerListener();
        new Thread(() ->{
            client.getChannels().stream().findFirst().ifPresent(x -> pointerCurrentChannel = x);

        });
    }
}


