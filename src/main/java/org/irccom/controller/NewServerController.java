package org.irccom.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.WindowEvent;
import org.irccom.guava.event.BooleanEvent;
import org.irccom.guava.listener.BooleanEventListener;
import org.irccom.helper.GlobalInstances;
import org.irccom.model.Server;
import org.irccom.model.User;
import org.irccom.sqlite.GenericDao;
import org.irccom.sqlite.ServerGenericDaoImpl;
import org.irccom.sqlite.UserGenericDaoImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class NewServerController   {



    EventBus eb = new EventBus();

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


    private ObservableSet<String> channelsSet = FXCollections.observableSet();

    @FXML
    private void handleNewServerSaveButtonAction(ActionEvent event) {
       saveNewSever();
    }
    @FXML
    private void handleNewUserSaveButtonAction(ActionEvent event) {
        saveNewUser();
    }

    private void saveNewSever(){
        Server server = new Server(
                newServerName.getText(),
                newServerIpAddress.getText(),
                newServerPort.getText()
        );
        SERVER_DAO.save(server);
    }
    private void saveNewUser(){
        User user = new User(
                SERVER_DAO.getId().get(),
                newServerNickname.getText(),
                newServerAlternativeNickname.getText(),
                newServerRealName.getText(),
                newServerUsername.getText(),
                newServerPassword.getText()
        );
        USER_DAO.save(user);
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
             channelsSet.addAll(stringToArray(value.getChannels()));
        });
    }

    private ArrayList<String> stringToArray (String string){
        return new ArrayList<>(Arrays.asList(string.split(",")));
        }



    @FXML
    public void initialize() throws SQLException {
    afterInitialize();

    }

    @Subscribe
    public void afterInitialize() {
            if(helper.getPopulate())
                populateFields();
    }
}
