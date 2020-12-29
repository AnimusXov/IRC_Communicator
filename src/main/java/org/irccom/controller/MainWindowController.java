package org.irccom.controller;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.sun.xml.internal.ws.resources.ServerMessages;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import net.engio.mbassy.listener.Handler;
import org.irccom.controller.custom.NoSelectionModel;
import org.irccom.irc.Connect;
import org.irccom.irc.models.CurrentChannel;
import org.irccom.irc.models.Message;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.ServerMessage;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.client.library.event.channel.ChannelNamesUpdatedEvent;
import org.kitteh.irc.client.library.event.channel.RequestedChannelJoinCompleteEvent;
import org.kitteh.irc.client.library.event.client.ClientNegotiationCompleteEvent;
import org.kitteh.irc.client.library.event.helper.ServerMessageEvent;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;


public class MainWindowController   {


    public Label labelRightBottom;
    public Label labelLeftBottom;
    public Label serverNameLabel;
    public Label usersTableLabel;
    public Label motdLabel;
    Client client = Connect.client.getClient();
    Hashtable<Channel, CurrentChannel> setOfMessageObsList = new Hashtable<>();
    Channel pointerCurrentChannel;
    @FXML
    ObservableList<Message> serverMessages = FXCollections.observableArrayList();
    @FXML
    public ObservableList<Message> messageObsList = FXCollections.observableArrayList();
    @FXML
    public ObservableList<Message> privateMessageObsList = FXCollections.observableArrayList();
    @FXML
    public ObservableSet<String> userObsList = FXCollections.observableSet();
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





    // register a listener for MainWindowController
    public void registerListener(){
       client.getEventManager().registerEventListener(this);
    }


    @Handler
    private  void onChannelNamesUpdated(ChannelNamesUpdatedEvent event){
        // new Thread
            Platform.runLater(() -> {

            });
    }



   /* @FXML
    private void onMouseClicked(){
        Optional <Channel> channel  = client.getChannel(channelsList.getSelectionModel().getSelectedItem());
        channel.ifPresent(x -> pointerCurrentChannel = x);
    } */

    // After Complete join to the channel
    @Handler
    public void onChannelJoin(RequestedChannelJoinCompleteEvent event){

        ObservableList<Message> newObsList = FXCollections.observableArrayList();
        CurrentChannel cc = new CurrentChannel(newObsList,event.getChannel());
        setOfMessageObsList.put(event.getChannel(),cc);


            Platform.runLater(() -> {
                    channelObsList.add(event.getChannel().getName());
                    setChannels(channelObsList);
            });
        pointerCurrentChannel = client.getChannels().stream().findFirst().get();
    }
    // Load obs list into ListView for channels
    public void setChannels(ObservableList<String> list){
        channelsList.setItems(list);
        channelsList.refresh();
    }
    // Load obs list into ListView for users
    public void setUsers(ObservableList<String> list){
        userList.setItems(list);
        userList.refresh();
    }


    // on message event handler
    @Handler
    public void onChannelMessage(ChannelMessageEvent event ) {

        new Thread(() -> {
            if (event.getActor().getNick().equals(event.getActor().getClient().getNick()))
                return;
        });

        Platform.runLater(() -> {
            setOfMessageObsList.get(event.getChannel()).getMsgObsList().add(new Message(event.getActor().getNick(), event.getMessage()));
            chat_window.setItems(setOfMessageObsList.get(pointerCurrentChannel).getMsgObsList());
        });
        {

            chat_window.refresh();
        }
    }

    // Switching channels via double click, load proper user and message lists
    private void setCustomCellFactory(){

        // Setup the CellFactory
      /*  chat_window.setCellFactory(listView -> new ListCell<Message>() {
            private ImageView imageView = new ImageView();
            @Override
            protected void updateItem(Message message, boolean empty) {
                super.updateItem(message, empty);

                if (empty) {
                    setGraphic(null);
                } else {

                    setGraphic(imageView);
                }
            }
        }); */
        channelsList.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                pointerCurrentChannel = client.getChannel(channelsList.getSelectionModel().getSelectedItem()).get();
                chat_window.setItems(setOfMessageObsList.get(pointerCurrentChannel).getMsgObsList());
                userObsList.clear();
                userObsList.addAll(pointerCurrentChannel.getNicknames());
                userList.setItems(FXCollections.observableArrayList(userObsList));
                usersTableLabel.setText(userObsList.size() + " Użytkowników");
                if(pointerCurrentChannel.getTopic().getValue().isPresent())
                motdLabel.setText(pointerCurrentChannel.getTopic().getValue().get());
            }
        });
    }


    // Sending input from input field to the server
    @FXML
    private void onEnterKeyPress(){
        typeField.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                String message = typeField.getText();
                client.sendMessage(pointerCurrentChannel,message);
                setOfMessageObsList.get(pointerCurrentChannel).getMsgObsList().add(new Message(client.getNick(), message));
                // clear text from input field and refresh the message view
                typeField.clear();
            }
        });
    }
    // Local time clock implementation
    @FXML
    private void beginClock() {

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            labelRightBottom.setText(LocalDateTime.now().format(format));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
   String url = " https://i.imgur.com/YphY8uD.jpg";
    public void test() throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        Image image = new Image(connection.getInputStream());
        ImageView imageView = new ImageView();
        imageView.setSmooth(true);
        imageView.setPreserveRatio(true);
        imageView.setImage(image);
        setOfMessageObsList.get(pointerCurrentChannel).getMsgObsList().add(new Message(imageView));

    }



    @Handler
    public void onServerMessage(ServerMessageEvent event ) {
        Platform.runLater(() -> {
           serverMessages.add(new Message(event.getSource().getMessage()));
        });
    }

    @Handler
    public void onServerJoin(ClientNegotiationCompleteEvent event ) {
        Platform.runLater(() -> {


        chat_window.setItems(serverMessages);
        serverNameLabel.setText(event.getServerInfo().getAddress().get());
        chat_window.refresh();

        });
    }




    // Initialize method
    @FXML
    public  void initialize() throws IOException {
        registerListener();
        setCustomCellFactory();
        beginClock();
        typeField.requestFocus();
        chat_window.setSelectionModel(new NoSelectionModel<>());
    }
}


