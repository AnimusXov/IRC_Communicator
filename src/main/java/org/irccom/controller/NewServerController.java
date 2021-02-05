package org.irccom.controller;

import com.google.common.eventbus.EventBus;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.StackPane;
import org.irccom.controller.custom.ChannelCellFactory;
import org.irccom.helper.Converter;
import org.irccom.helper.GlobalInstances;
import org.irccom.sqlite.model.Server;
import org.irccom.sqlite.model.User;
import org.irccom.sqlite.GenericDao;
import org.irccom.sqlite.ServerGenericDaoImpl;
import org.irccom.sqlite.UserGenericDaoImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class NewServerController   {

    public JFXButton addNewChannelButton;
    EventBus eb = new EventBus();
    Converter converter = new org.irccom.helper.Converter();

    public JFXTextField newServerName;
    public JFXTextField newServerIpAddress;
    public JFXTextField newServerPort;
    public JFXTextField newServerNickname;
    public JFXTextField newServerAlternativeNickname;
    public JFXTextField newServerRealName;
    public JFXTextField newServerUsername;
    public JFXPasswordField newServerPassword;
    public JFXListView<String> autoJoinChannelsListView;
    public JFXButton saveAutoJoinChannelsButton;

    private final GenericDao<User,Integer> USER_DAO = new UserGenericDaoImpl();
    private final GenericDao<Server,Integer> SERVER_DAO = new ServerGenericDaoImpl();
    public JFXButton newUserAddButton;
    public StackPane stackPane;

    GlobalInstances helper = new GlobalInstances();

    private final ObservableSet<String> channelsSet = FXCollections.observableSet();

    @FXML
    private void handleNewServerSaveButtonAction(ActionEvent event) {
       saveNewSever(helper.getPopulate());
    }
    @FXML
    private void handleNewUserSaveButtonAction(ActionEvent event) {
        saveNewUser(helper.getPopulate());
    }
    @FXML
    private void handleNewChannelListButtonAction(ActionEvent event) {
        saveNewChannelList(helper.getPopulate());
    }

    private void saveNewChannelList(boolean isUpdate){
    }
    private void saveNewSever(boolean isUpdate){
        Server server = new Server(
                newServerName.getText(),
                newServerIpAddress.getText(),
                newServerPort.getText()
        );
        if(isUpdate) {
            SERVER_DAO.update(server);
        }
        else
        SERVER_DAO.save(server);

    }
    private void saveNewUser(boolean isUpdate){
        User user = new User();

        user.setNickname(newServerNickname.getText());
        user.setAlt_nickname(newServerAlternativeNickname.getText());
        user.setRealname(newServerRealName.getText());
        user.setUsername(newServerUsername.getText());
        user.setPassword(newServerPassword.getText());

        if(!channelsSet.isEmpty()) {
            String channelString = String.join(",", autoJoinChannelsListView.getItems());
            user.setChannels(channelString);
            channelsSet.clear();
            channelsSet.addAll(autoJoinChannelsListView.getItems());
        }
        if(isUpdate) {
            user.setId(helper.getSelectedServerServer().getId());
            if(USER_DAO.get(helper.getSelectedServerServer().getId()).isPresent()) {
                USER_DAO.update(user);
            }
            else
            USER_DAO.save(user);
        }
        if(!isUpdate)
        USER_DAO.save(user);
    }

    @FXML
    private void handleNewChannelButton(ActionEvent event)  {
        channelsSet.clear();
        channelsSet.addAll(autoJoinChannelsListView.getItems());
        channelsSet.add("#Nowy" + channelsSet.size());
        autoJoinChannelsListView.setItems(FXCollections.observableArrayList(channelsSet));
        autoJoinChannelsListView.refresh();
    }

    private void populateFields(){
        newServerName.setText(helper.getSelectedServerServer().getName());
        newServerIpAddress.setText(helper.getSelectedServerServer().getIp());
        newServerPort.setText(helper.getSelectedServerServer().getPort());
        Optional<User> user  = USER_DAO.get(helper.getSelectedServerServer().getId());
        user.ifPresent(value -> {
             newServerNickname.setText(value.getNickname());
             newServerAlternativeNickname.setText(value.getAlt_nickname());
             newServerRealName.setText(value.getRealname());
             newServerUsername.setText(value.getUsername());
             newServerPassword.setText(value.getPassword());
             if(value.getChannels()!=null) {
                 channelsSet.addAll(converter.stringToArray(value.getChannels()));
                 autoJoinChannelsListView.setItems(FXCollections.observableArrayList(channelsSet));
                 autoJoinChannelsListView.refresh();
             }

        });

    }


    @FXML
    public void initialize() {
    autoJoinChannelsListView.setEditable(true);
    autoJoinChannelsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    autoJoinChannelsListView.setCellFactory(new ChannelCellFactory());
    autoJoinChannelsListView.setItems(FXCollections.observableArrayList(channelsSet));

    afterInitialize();
    }

    public void afterInitialize() {
            if(helper.getPopulate()) {
                populateFields();
            }
    }
}
