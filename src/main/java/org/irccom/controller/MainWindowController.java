package org.irccom.controller;

import com.jfoenix.controls.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTreeTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import javafx.util.Duration;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import lombok.SneakyThrows;
import org.irccom.controller.factory.JFXDialogFactory;
import org.irccom.controller.factory.MessageCompomentFactory;
import org.irccom.controller.factory.SceneFactory;
import org.irccom.controller.model.PrefixUser;
import org.irccom.controller.preset.Config;
import org.irccom.helper.Validator;
import org.irccom.irc.Connect;
import org.irccom.irc.listener.IRCHandler;
import org.irccom.irc.model.CurrentChannel;
import org.irccom.irc.model.Message;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.defaults.element.mode.DefaultChannelMode;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.element.mode.ChannelMode;
import org.kitteh.irc.client.library.element.mode.ModeStatus;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Hashtable;
import java.util.stream.Collectors;

public class MainWindowController   {

private final org.irccom.controller.factory.JFXDialogFactory JFXDialogFactory = new JFXDialogFactory(this);
public Label labelRightBottom;
    public Label labelLeftBottom;
    public Label serverNameLabel;
    public Label usersTableLabel;
    public Label motdLabel;
    private final Tab serverTab = new Tab();
    private final Tab channelTab = new Tab();
    public JFXTabPane upperTabPane;
    public Button addNewChannelButton;
    public MenuBar toolbar;
    public BorderPane borderPane;
    public StackPane stackRootPane;
    public ChoiceBoxTreeTableCell<String,Integer> commandComboBox;
    public MenuButton commandMenu;
    public Label channelNameLabel;
    
    public MenuItem privateMessage = new MenuItem();
    public MenuItem whoIs = new MenuItem();
    public MenuItem kick = new MenuItem();
    public MenuItem ban = new MenuItem();
    public MenuItem op = new MenuItem();
    public ContextMenu userListContextMenu = new ContextMenu();
    
    
    public MenuItem copy = new MenuItem();
    public ContextMenu messageListContextMenu = new ContextMenu();
  
    public MenuItem preferenceMenuItem;
    Client client = Connect.client.getClient();
    Hashtable<Channel, CurrentChannel> setOfMessageObsList = new Hashtable<>();
    Hashtable<String, ObservableList<Message>> setOfPrivateMessageObsLists = new Hashtable<>();
    Hashtable<String, Tab> hashTableOfTabs = new Hashtable<>();
    public Channel pointerCurrentChannel;
    Config config = new Config();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    IRCHandler IRCHandler = new IRCHandler(this);



   

 
    @FXML
    ObservableList<Message> serverMessages = FXCollections.observableArrayList();
    @FXML
    public ObservableList<Message> messageObsList = FXCollections.observableArrayList();
    @FXML
    public ObservableSet<PrefixUser> userObsList = FXCollections.observableSet();
    @FXML
    public ObservableList<String> channelObsList =  FXCollections.observableArrayList();
    @FXML
    public ObservableList<Message> messageObservableList = FXCollections.observableArrayList();
    @FXML
    public JFXListView<Message> messageJFXListView;
    @FXML
    public ListView<String> channelsList;
    @FXML
    public JFXListView<PrefixUser> userList;
    @FXML
    public TextField typeField;

    // register a listener for MainWindowController
    public void registerListeners(){
    	// register the listeners for IRC-related events
       client.getEventManager().registerEventListener(IRCHandler);
       // Listening to actions performed on tabs
        registerTabPaneListener();
    }
    private void registerTabPaneListener(){

        upperTabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            int selectedIndex = upperTabPane.getSelectionModel().getSelectedIndex();
            switch (selectedIndex){
                case 0:
                    messageJFXListView.setItems(serverMessages);
	                channelNameLabel.setText("");
	                usersTableLabel.setText("");
	                userList.setVisible(false);
                    break;
                case 1:
                    if(pointerCurrentChannel!=null) {
                        messageJFXListView.setItems(setOfMessageObsList.get(pointerCurrentChannel).getMsgObsList());
	                    userList.setVisible(true);
                        channelNameLabel.setText(pointerCurrentChannel.getName());
                    }
                    break;
                default:
	                if(!setOfPrivateMessageObsLists.isEmpty()) {
		                messageJFXListView.setItems(setOfPrivateMessageObsLists.get(newTab.getText()));
		                channelNameLabel.setText("");
		                usersTableLabel.setText("");
		                userList.setVisible(false);
		                messageJFXListView.refresh();
	                }
                    break;
                    }
	  

        });
    }

   /* @FXML
    private void onMouseClicked(){
        Optional <Channel> channel  = client.getChannel(channelsList.getSelectionModel().getSelectedItem());
        channel.ifPresent(x -> pointerCurrentChannel = x);
    } */

    // After Complete join to the channel
    public void updateChannelListAndMessageSet(Channel channel){
        ObservableList<Message> newObsList = FXCollections.observableArrayList();
        CurrentChannel cc = new CurrentChannel(newObsList,channel);
        setOfMessageObsList.put(channel,cc);
        Platform.runLater(() -> {
            channelObsList.add(channel.getName());
            setChannels(channelObsList);
        });
    }
    // Load obs list into ListView for channels
    public void setChannels(ObservableList<String> list){
        channelsList.setItems(list);
        channelsList.refresh();
    }
 

    private void setCustomMessageListCellFactory(){
        messageJFXListView.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> param) {
                return new JFXListCell<Message>(){
	                
	                Text nickname = new Text();
	                Text message = new Text();
	                Text time = new Text();
	                TextFlow messageCell = new TextFlow(time,nickname,message);
	              
	                
	                
                    @SneakyThrows
                    @Override
                    public void updateItem(Message item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null || item.getMessage() == null) {
                            setText(null);
                        } else if(item.getNickname() == null) {
	                       
                            setText(item.getTime() + " " + item.getMessage()); // Server messages
                        }
                        else{
	                        VBox vBox = new VBox();
	                        setText(null);
	                        setMessageFormatting(item);
	                        if(item.isHasImage()){
		                        message.setText( item.getMessage());
		                        time.setText(item.getTime() + " ");
	                        	if(!vBox.getChildren().contains(item.getImage()) && item.getImage()!=null) {
			                        vBox.getChildren().addAll(messageCell,item.getImage());
		                        }
	                        	setGraphic(vBox);
	                        }
	                        else {
		                        time.setText(item.getTime() + " ");
		                        message.setText( item.getMessage());
		                        setGraphic(messageCell);
	                        }
                        }
                    }
              
                    
	
	                private void setMessageFormatting( Message item ){
                    	if(item.getPrefixUser() != null) {
		                    nickname.setStyle("-fx-fill:" + Config.userColorMap.get(item.getPrefixUser().getPrefix()) + ";-fx-font-weight:bold;");
		                    if( item.getPrefixUser().getPrefix() == ' '){
			                    nickname.setStyle("-fx-fill:"+ Config.userColorMap.get(item.getPrefixUser().getPrefix()));
			                    nickname.setText("<" + item.getPrefixUser().getUser().getNick() + "> ");
		                    }
		                    else {
			                    nickname.setText("<" + item.getPrefixUser().getPrefix() + item.getPrefixUser().getUser().getNick() + "> ");
		                    }
	                    }
                    	else{
		                    nickname.setStyle("-fx-fill:#2196f3;-fx-font-weight:bold;");
		                    nickname.setText("<" + item.getNickname() + "> ");
	                    }
		            
	                }
                };
            }
            
        });
       

    }

    private void setCustomUserListCellFactory(){
	    userList.setCellFactory(new Callback<ListView<PrefixUser>, ListCell<PrefixUser>>() {
		    @Override
		    public ListCell<PrefixUser> call(ListView<PrefixUser> param) {
			    TextFlow messageCell = new TextFlow();
			    Text nickname = new Text();
			    messageCell.getChildren().addAll(nickname);
			    final JFXListCell<PrefixUser> cell = new JFXListCell<PrefixUser>(){
				    @Override
				    public void updateItem(PrefixUser item, boolean empty) {
					    super.updateItem(item, empty);
					    if (empty || item == null) {
						    setText(null);
					    }
					    else{
						    setText(null);
						    pointerCurrentChannel.getUserModes(item.getUser()).ifPresent(thisUser -> {
							    nickname.setStyle("-fx-fill:"+ Config.userColorMap.get(item.getPrefix()) +";-fx-font-weight:bold;");
							    nickname.setText(item.getPrefix() + item.getUser().getNick());
						    });
						    setGraphic(messageCell);
					    }
				    }
				
			    };
			    cell.setOnContextMenuRequested(e -> {
						    userListContextMenu.show(cell, e.getScreenX(), e.getScreenY());
				    privateMessage.setOnAction(event -> {
					   newPrivateConversationTab(cell.getItem().getUser());
				    });
				    whoIs.setOnAction(event -> {
				    	messageJFXListView.getItems().add(new Message(client.commands().whois().target(cell.getItem().getUser().getNick()).toString()));
				    });
				    kick.setOnAction(event -> {
				    	client.commands().kick(pointerCurrentChannel).target(cell.getItem().getUser()).execute();
				    });
				    ban.setOnAction(event -> {
				    });
				    op.setOnAction(event -> {
					    DefaultChannelMode o = new DefaultChannelMode(cell.getItem().getUser().getClient(),'o', ChannelMode.Type.D_PARAMETER_NEVER);
				    	pointerCurrentChannel.commands().mode().add(ModeStatus.Action.ADD,o);
				    });
					    }
			    );
			    return cell;
			    
		    }
		    
	    });

    }





    private void sortUserList(){
	    userList.getItems().sort((o1,o2)->{
		    if(o2.getPrefix()==o1.getPrefix()){
			   if( String.CASE_INSENSITIVE_ORDER.compare(o1.getUser().getNick(),o2.getUser().getNick()) > -1){
			   	return 1;
			   }
			        return 0;
		    }
		    if( Config.metaMap.get(o2.getPrefix()) > Config.metaMap.get(o1.getPrefix())) {
			    return 1;
		    }
		            return 0;
	    });
	   
    }
    private void setCustomChannelListFactory(){
	
	    channelsList.setCellFactory(lv -> {
		    {
			    ListCell<String> cell = new ListCell<>();
			    ContextMenu contextMenu = new ContextMenu();
			    MenuItem deleteItem = new MenuItem();
			    deleteItem.textProperty().bind(Bindings.format("Odłącz \"%s\"", cell.itemProperty()));
			    deleteItem.setOnAction(event ->
			    {
				    client.removeChannel(channelsList.getItems().get(cell.getIndex()));
				    channelsList.getItems().remove(cell.getItem());
				
			    });
			    contextMenu.getItems().addAll(deleteItem);
			    cell.textProperty().bind(cell.itemProperty());
			    cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) ->
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
                upperTabPane.getSelectionModel().select(channelTab);
                motdLabel.setText("");
                pointerCurrentChannel = client.getChannel(channelsList.getSelectionModel().getSelectedItem()).get();
                messageJFXListView.setItems(setOfMessageObsList.get(pointerCurrentChannel).getMsgObsList());
                userObsList.clear();
                userObsList.addAll(pointerCurrentChannel.getUsers().stream()
		                                   .map(user -> new PrefixUser(user,getUserNickPrefix(user)))
		                .collect(Collectors.toList()));
           
                userList.setItems(FXCollections.observableArrayList(userObsList));
                usersTableLabel.setText(userObsList.size() + " Użytkowników"); // Get the number of users in channel user switched to
	            channelNameLabel.setText(pointerCurrentChannel.getName());
                if(pointerCurrentChannel.getTopic().getValue().isPresent())
                motdLabel.setText(pointerCurrentChannel.getTopic().getValue().get()); // Get the topic for channel user switched to
                sortUserList();
	            userList.setVisible(true);
            }
        });
    }
    // Sending input from input field to the server
    @FXML
    private void handleNewChannelButton(ActionEvent event){
        JFXButton button = new JFXButton("Dodaj");
        button.setStyle("-fx-background-color: #29b6f6");
	    JFXDialogFactory.createJFXDialog(stackRootPane, borderPane, Collections.singletonList(button), "Dodaj nowy kanał"
			    , "Wprowadź nazwę #kanału", 1);
    }


    @FXML
    private void onEnterKeyPress(){
        typeField.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER && typeField.isFocused() && !typeField.getText().isEmpty()) {
	            if ( upperTabPane.getSelectionModel().getSelectedIndex() == 1 ) {
		            String message = typeField.getText();
		            Message fullMessage = new Message();
		            client.sendMessage(pointerCurrentChannel, message);
		            fullMessage.setTime(LocalDateTime.now().format(formatter));
		            fullMessage.setMessage(message);
		            fullMessage.setNickname(client.getNick());
		            fullMessage.setPrefixUser(new PrefixUser(client.getUser().get(), getUserNickPrefix(client.getUser().get())));
		            if ( Validator.isUrlValid(message) ) {
			            try {
				            fullMessage.setImage(new MessageCompomentFactory().getImageFromWeb(message));
				            fullMessage.setHasImage(fullMessage.getImage() != null);
			            } catch ( IOException e ) {
				            e.printStackTrace();
			            }
		            }
		            setOfMessageObsList.get(pointerCurrentChannel).getMsgObsList().add(fullMessage);
		            // clear text from input field and refresh the message view
		            typeField.clear();
	            }
	            else{
		            String message = typeField.getText();
		            Message fullMessage = new Message();
		            client.sendMessage(upperTabPane.getSelectionModel().getSelectedItem().getText(),message);
		            fullMessage.setTime(LocalDateTime.now().format(formatter));
		            fullMessage.setMessage(message);
		            fullMessage.setNickname(client.getNick());
		            fullMessage.setPrefixUser(new PrefixUser(client.getUser().get(), getUserNickPrefix(client.getUser().get())));
		            if ( Validator.isUrlValid(message) ) {
			            try {
				            fullMessage.setImage(new MessageCompomentFactory().getImageFromWeb(message));
				            fullMessage.setHasImage(fullMessage.getImage() != null);
			            } catch ( IOException e ) {
				            e.printStackTrace();
			            }
		            }
		            setOfPrivateMessageObsLists.get(upperTabPane.getSelectionModel().getSelectedItem().getText()).add(fullMessage);
		            // clear text from input field and refresh the message view
		            typeField.clear();
	            }
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
   

    // Updating user's nickname when changed in the current, no point updating in every channel

    private char getUserNickPrefix(User user){
    	if(!pointerCurrentChannel.getUserModes(user).isPresent() || pointerCurrentChannel.getUserModes(user).get().isEmpty()){
		    return ' ';
	    }
    	return pointerCurrentChannel.getUserModes(user).get().first().getNickPrefix();
    }
    
    /* Update userList after nickname change event, user leave current channel event
       and user join current channel event
   
     */
    public void updateUserList(org.kitteh.irc.client.library.element.User oldUser, User newUser){
        Platform.runLater(() -> {
	       userObsList.stream().filter(o -> o.getUser().equals(oldUser)).forEach(o -> {
	        	o.setUser(newUser);
	        	o.setPrefix(getUserNickPrefix(newUser));
	        });
        });
	    userList.setItems(FXCollections.observableArrayList(userObsList));
                userList.refresh();
    }
    
    public void updateUserKick(org.kitteh.irc.client.library.element.User user){
	    Platform.runLater(() -> {
			    userObsList.removeIf(o -> o.getUser().equals(user));
			    userList.getItems().clear();
			    userList.setItems(FXCollections.observableArrayList(userObsList));
			    sortUserList();
		        userList.refresh();
			    usersTableLabel.setText(userObsList.size() + " Użytkowników");
	    });
	 
    }
    public void updateUserJoin(org.kitteh.irc.client.library.element.User user){
	    Platform.runLater(() -> {
		userObsList.add(new PrefixUser(user,getUserNickPrefix(user)));
		    userList.getItems().clear();
		    userList.setItems(FXCollections.observableArrayList(userObsList));
		    sortUserList();
		    userList.refresh();
		    usersTableLabel.setText(userObsList.size() + " Użytkowników");
	    });
	    
	   
    }
    // Update ChannelTopic label upon change
    public void updateChannelTopic(Channel channel,String topic){
        Platform.runLater(() -> {
            if(pointerCurrentChannel!=null)
                if(pointerCurrentChannel.equals(channel)) {
                    motdLabel.setText(topic);
                }
        });
    }
    public void addMessageToList( Message message, Channel channel ){
        Platform.runLater(() -> {
            setOfMessageObsList.get(channel).getMsgObsList().add(message);
            messageJFXListView.setItems(setOfMessageObsList.get(pointerCurrentChannel).getMsgObsList());
        });
        {
            messageJFXListView.refresh();
        }
    }
    public void newServerMessage(Message serverMessage){
        Platform.runLater(() -> serverMessages.add(serverMessage));
    }
    // Load message observable list into View
    public void setMessageListAndServerLabel(String serverName){
        Platform.runLater(() -> {
        messageJFXListView.setItems(serverMessages);
        serverNameLabel.setText(serverName);
        messageJFXListView.refresh();
        });
    }
    // Create a new Tab for  server, channel and private conversations
    private void newPrivateConversationTab(User user){
    	if(!hashTableOfTabs.containsKey(user.getNick())) {
		    Tab tab = new Tab();
		    tab.setText(user.getNick());
		    hashTableOfTabs.put(user.getNick(), tab);
		    upperTabPane.getTabs().add(tab);
		    ObservableList<Message> newObsList = FXCollections.observableArrayList();
		    setOfPrivateMessageObsLists.put(user.getNick(), newObsList);
	    }
		         else {
			    if(!upperTabPane.getTabs().contains(hashTableOfTabs.get(user.getNick()))){
				    upperTabPane.getTabs().add(hashTableOfTabs.get(user.getNick()));
			    }
			    
		    }
	    
    }
    public void createNewTab(Message message){
        Platform.runLater(() -> {
            if(!hashTableOfTabs.containsKey(message.getNickname())){
                Tab tab = new Tab();
                tab.setText(message.getNickname());
                hashTableOfTabs.put(message.getNickname(),tab);
                upperTabPane.getTabs().add(tab);
                ObservableList<Message> newObsList = FXCollections.observableArrayList();
                newObsList.add(message);
                setOfPrivateMessageObsLists.put(message.getNickname(),newObsList);
            }
            else {
            	if(!upperTabPane.getTabs().contains(hashTableOfTabs.get(message.getNickname()))){
		            upperTabPane.getTabs().add(hashTableOfTabs.get(message.getNickname()));
	            }
                setOfPrivateMessageObsLists.get(message.getNickname()).add(message);
            }

        });
    }

    // Initialize method
    @FXML
    public  void initialize() {
        config.init();
	    initializeContextMenu();
	    setupCommandComboBox();
        registerListeners();
	    setCustomUserListCellFactory();
        setCustomMessageListCellFactory();
        setCustomChannelListFactory();
        beginClock();
        setupDefaultTabs();
        setupUserListContextMenu();
	    setupMessageListContextMenu();
        typeField.requestFocus();
	    setupJMetroStyle();
    }

private void setupUserListContextMenu(){
        privateMessage.setText("Prywatna wiadomość");
        whoIs.setText("Informacje o użytkowniku");
        kick.setText("Wyrzuć");
        ban.setText("Zbanuj");
        op.setText("Nadaj uprawnienia operatora");
		userListContextMenu.getItems().addAll(privateMessage,whoIs,kick,ban,op);
}
private void setupMessageListContextMenu(){
    	copy.setText("Kopiuj");
}

private void initializeContextMenu(){

}

// Open colour preference window
    @FXML
private void onPreferencesMenuItemClicked(ActionEvent event) throws IOException{
	new SceneFactory().openNewWindow("settings.fxml",false, false,"Preferencje");
}
// Exit the application
@FXML
private void onExitMenuItemClicked(ActionEvent event){
    client.shutdown();
	Platform.exit();
	System.exit(0);
	
}
// Change the topic
@FXML
private void onTopicMenuItemClicked( ActionEvent event)  {
    	if(pointerCurrentChannel!=null) {
		    JFXButton button = new JFXButton("Zmień");
		    button.setStyle("-fx-background-color: #29b6f6");
		    JFXDialogFactory.createJFXDialog(stackRootPane, borderPane, Collections.singletonList(button), "Ustawienia tematu", "Wprowadź temat", 2);
	    }
}
@FXML
private void onBannedMenuItemClicked( ActionEvent event)  {
	if(pointerCurrentChannel!=null) {
		JFXButton button = new JFXButton("Zmień");
		button.setStyle("-fx-background-color: #29b6f6");
		JFXDialogFactory.createJFXDialog(stackRootPane, borderPane, Collections.singletonList(button), "Ustawienia tematu", "Wprowadź temat", 2);
	}
}

@FXML
private void onRegisterMenuItemClicked(ActionEvent event) {
	JFXButton button = new JFXButton("Zarejestruj");
	JFXButton button2 = new JFXButton("Potwierdź");
	button.setStyle("-fx-background-color: #29b6f6; -fx-text-fill: white;");
	button2.setStyle("-fx-background-color: #29b6f6; -fx-text-fill: white;");
	JFXDialogFactory.createJFXRegisterDialog(stackRootPane, borderPane, Collections.singletonList(button), Collections.singletonList(button2), "Rejestracja konta", "Wypełnij wszystkie pola", 2);
	
}

    
    
    private void setupCommandComboBox(){
    
    }
    // Apply JMetro Style to the elements on the stage
    private void setupJMetroStyle(){
	    JMetro jMetro = new JMetro(Style.LIGHT);
	    jMetro.setParent(messageJFXListView);
	    jMetro.setParent(channelsList);
	    jMetro.setParent(upperTabPane);
	    jMetro.setParent(commandComboBox);
    }
    // Add basic tabs to the tabpane
    private void setupDefaultTabs() {
	    upperTabPane.setTabMinHeight(45);
	    upperTabPane.setTabMaxHeight(45);
        serverTab.setText("Serwer");
        channelTab.setText("Kanał");
        serverTab.setClosable(false);
        channelTab.setClosable(false);
        upperTabPane.getTabs().addAll(serverTab,channelTab);
        if(!upperTabPane.getSelectionModel().isEmpty()) {
	        upperTabPane.getSelectionModel().clearSelection();
        }
        
    }

public Client getClient(){
	return client;
}
}

