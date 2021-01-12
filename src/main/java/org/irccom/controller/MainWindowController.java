package org.irccom.controller;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import javafx.util.Duration;
import net.engio.mbassy.listener.Handler;
import org.irccom.irc.Connect;
import org.irccom.irc.models.CurrentChannel;
import org.irccom.irc.models.Message;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.client.library.event.channel.ChannelTopicEvent;
import org.kitteh.irc.client.library.event.channel.RequestedChannelJoinCompleteEvent;
import org.kitteh.irc.client.library.event.user.UserAccountStatusEvent;
import org.kitteh.irc.client.library.event.user.UserNickChangeEvent;
import org.irccom.irc.listener.IRCHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;


public class MainWindowController   {


    public Label labelRightBottom;
    public Label labelLeftBottom;
    public Label serverNameLabel;
    public Label usersTableLabel;
    public Label motdLabel;
    public JFXTabPane upperTabPane;
    Client client = Connect.client.getClient();
    Hashtable<Channel, CurrentChannel> setOfMessageObsList = new Hashtable<>();
    Hashtable<String, Tab> hashTableOfTabs = new Hashtable<>();
    Channel pointerCurrentChannel;
    String userColor = "#2196f3";
    String otherUserColor = "#424242";
    IRCHandler IRCHandler = new IRCHandler(this);
    //NonNull Optional<User> user = Connect.user_;



    @FXML
    ObservableList<Message> serverMessages = FXCollections.observableArrayList();
    @FXML
    public ObservableList<Message> messageObsList = FXCollections.observableArrayList();
    @FXML
    public ObservableSet<String> userObsList = FXCollections.observableSet();
    @FXML
    public ObservableList<String> channelObsList =  FXCollections.observableArrayList();
    @FXML
    public ObservableList<Message> messageObservableList = FXCollections.observableArrayList();
    @FXML
    public JFXListView<Message> messageJFXListView;
    @FXML
    public JFXListView<String> channelsList;
    @FXML
    public JFXListView<String> userList;
    @FXML
    public JFXTextField typeField;





    // register a listener for MainWindowController
    public void registerListener(){
       client.getEventManager().registerEventListener(this);
       client.getEventManager().registerEventListener(IRCHandler);
    }

    // Updating user's nickname when changed in the current, no point updating in every channel
    @Handler
    private  void onChannelNameUpdated(UserNickChangeEvent event){

            Platform.runLater(() -> {
                if(userObsList.contains(event.getOldUser().getNick())) {
                    userObsList.remove(event.getOldUser().getNick());
                    userObsList.add(event.getNewUser().getNick());
                    userList.setItems(FXCollections.observableArrayList(userObsList));
                    userList.refresh();
                }
            });
    }
    @Handler
    private  void onChannelTopicUpdated(ChannelTopicEvent event){
        Platform.runLater(() -> {
            if(pointerCurrentChannel!=null)
            if(pointerCurrentChannel.equals(event.getChannel())) {
                motdLabel.setText(event.getNewTopic().getValue().get());
            }
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


    // on message event listener
    @Handler
    public void onChannelMessage(ChannelMessageEvent event ) {
        Platform.runLater(() -> {
            setOfMessageObsList.get(event.getChannel()).getMsgObsList().add(new Message(event.getActor().getNick(), event.getMessage()));
            messageJFXListView.setItems(setOfMessageObsList.get(pointerCurrentChannel).getMsgObsList());
        });
        {
            messageJFXListView.refresh();
        }
    }

    public void addMessageToList(Message message, Channel channel){
        Platform.runLater(() -> {
            setOfMessageObsList.get(channel).getMsgObsList().add(message)
            setOfMessageObsList.get(event.getChannel()).getMsgObsList().add(new Message(event.getActor().getNick(), event.getMessage()));
            messageJFXListView.setItems(setOfMessageObsList.get(pointerCurrentChannel).getMsgObsList());
        });
        {
            messageJFXListView.refresh();
        }
    }
    private void setCustomCellFactoryChatList(){

        messageJFXListView.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> param) {
                return new JFXListCell<Message>(){
                    @Override
                    public void updateItem(Message item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null || item.getMessage() == null) {
                            setText(null);
                        } else if(item.getNickname() == null) {
                            setText(item.getMessage()); // no formatting here yet
                        }
                        else{
                            setText(null);
                            Text nickname = new Text();
                            Text message = new Text();
                            TextFlow messageCell = new TextFlow(nickname,message);
                            if(!item.getNickname().equals(client.getClient().getNick())) {
                                nickname.setStyle("-fx-fill:#424242;-fx-font-weight:bold;");
                            }
                            else {
                                nickname.setStyle("-fx-fill:#2196f3;-fx-font-weight:bold;");
                            }
                            nickname.setText("<" + item.getNickname() + "> ");
                            message.setText(item.getMessage());
                            setGraphic(messageCell);
                        }
                    }
                };
            }
        });
    }
          /*chat_window.setCellFactory(param -> new ListCell<Message>() {

            @Override
            protected void updateItem(Message item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getMessage() == null) {
                    setText(null);
                } else if(item.getNickname() == null) {
                    setText(item.getMessage());
                }
                else{
                    setText("<" + item.getNickname() + "> " + item.getMessage());
                }

            }
        });*/

       /* ListCell<Message> cell = new ListCell<>();

        chat_window.setCellFactory(lv -> {

            final ClipboardContent clipboardContent = new ClipboardContent();
            ContextMenu contextMenu = new ContextMenu();

        MenuItem copyToClipboard = new MenuItem();
            copyToClipboard.textProperty().bind(Bindings.format("Kopiuj \"%s\"", cell.itemProperty()));
            copyToClipboard.setOnAction(event -> {
                String item = cell.getItem().getMessage();
                clipboardContent.putString(item);
                Clipboard.getSystemClipboard().setContent(clipboardContent);
        });

        contextMenu.getItems().add(copyToClipboard);
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell;
        });
            } */


    // Switching channels via double click, load proper user and message lists
    private void setCustomCellFactory(){
        channelsList.setCellFactory(lv -> {

            {
                ListCell<String> cell = new ListCell<>();;
                ContextMenu contextMenu = new ContextMenu();

                MenuItem deleteItem = new MenuItem();
                deleteItem.textProperty().

                        bind(Bindings.format("Odłącz \"%s\"", cell.itemProperty()));
                deleteItem.setOnAction(event ->

                {
                    channelsList.getItems().remove(cell.getItem());
                    client.removeChannel(channelsList.getItems().get(cell.getIndex()));
                });
                contextMenu.getItems().

                        addAll(deleteItem);

                cell.textProperty().
                        bind(cell.itemProperty());

                cell.emptyProperty().

                        addListener((obs, wasEmpty, isNowEmpty) ->

                        {

                            if (isNowEmpty) {

                                cell.setContextMenu(null);

                            } else {

                                cell.setContextMenu(contextMenu);
                            }
                        });

                return cell;
            }
        });


                channelsList.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                motdLabel.setText("");
                pointerCurrentChannel = client.getChannel(channelsList.getSelectionModel().getSelectedItem()).get();
                messageJFXListView.setItems(setOfMessageObsList.get(pointerCurrentChannel).getMsgObsList());
                userObsList.clear();
                userObsList.addAll(pointerCurrentChannel.getNicknames());
                userList.setItems(FXCollections.observableArrayList(userObsList));
                usersTableLabel.setText(userObsList.size() + " Użytkowników"); // Get the number of users in channel user switched to
                if(pointerCurrentChannel.getTopic().getValue().isPresent())
                motdLabel.setText(pointerCurrentChannel.getTopic().getValue().get()); // Get the topic for channel user switched to
            }
        });
    }


    // Sending input from input field to the server
    @FXML
    private void onEnterKeyPress(){
        typeField.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                String message = typeField.getText();
                client.sendCtcpMessage(pointerCurrentChannel,message);
                client.sendRawLine(message);
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
    /*
    public void test() throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        Image image = new Image(connection.getInputStream());
        ImageView imageView = new ImageView();
        imageView.setSmooth(true);
        imageView.setPreserveRatio(true);
        imageView.setImage(image);
        setOfMessageObsList.get(pointerCurrentChannel).getMsgObsList().add(new Message(imageView));

    } */




    public void newServerMessage(Message serverMessage){
        Platform.runLater(() -> {
            serverMessages.add(serverMessage);
        });
    }


    @Handler
    public void onAccountStatusEvent(UserAccountStatusEvent event){

    }


    public void setMessageListAndServerLabel(String serverName){
        Platform.runLater(() -> {
        messageJFXListView.setItems(serverMessages);
        serverNameLabel.setText(serverName);
        messageJFXListView.refresh();
        });
    }

    public void createNewTab(Message message){
        Platform.runLater(() -> {
            if(!hashTableOfTabs.containsKey(message.getNickname())){
                Tab tab = new Tab();
                tab.setText(message.getNickname());
                hashTableOfTabs.put(message.getNickname(),tab);
                upperTabPane.getTabs().add(tab);
            }
        });
    }




    // Initialize method
    @FXML
    public  void initialize() throws IOException {


        registerListener();
        setCustomCellFactoryChatList();
        setCustomCellFactory();
        beginClock();
        typeField.requestFocus();
    }
}


