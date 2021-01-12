package org.irccom.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.irccom.guava.event.BooleanEvent;
import org.irccom.guava.listener.BooleanEventListener;
import org.irccom.helper.GlobalInstances;
import org.irccom.irc.Connect;
import org.irccom.model.Server;
import org.irccom.model.User;
import org.irccom.sqlite.GenericDao;
import org.irccom.sqlite.ServerGenericDaoImpl;
import org.irccom.sqlite.UserGenericDaoImpl;

import java.io.IOException;
import java.sql.SQLException;


public class MenuController {

    public JFXButton editServer;
    public JFXTextField username;
    public JFXTextField alt_nickname;
    Connect conn = new Connect();
    EventBus eb = new EventBus();
    public JFXButton addNewServerButton;
    public JFXButton connect;
    public JFXTextField nickname;

    GlobalInstances helper = new GlobalInstances();





    private final GenericDao<Server,Integer> SERVER_DAO = new ServerGenericDaoImpl();
    private final GenericDao<User,Integer> USER_DAO = new UserGenericDaoImpl();

    @FXML
    ObservableList<Server> obsServerList = FXCollections.observableArrayList(SERVER_DAO.getAll());
    @FXML
    JFXListView<Server> serverList = new JFXListView<>();


    @FXML
    public void openNewWindow(String fxml, boolean isClose_stage, String title) throws IOException {
        Stage old_stage = (Stage) connect.getScene().getWindow();
        if(isClose_stage)
        old_stage.close();
        Scene scene;
        FXMLLoader fxmlLoader = new
        FXMLLoader(getClass().getResource("/fxml/"+fxml));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        scene = (new Scene(root1));
        stage.setScene(scene);
        stage.show();


    }

    // Check if server has an user binded to it
    private boolean isUserInfo(){
        return USER_DAO.get(serverList.getSelectionModel().getSelectedItem().getId()).isPresent();
    }

    private void setEmpty(JFXTextField txtField){
        if(txtField.getText().isEmpty());
    }

    // Action after connect button has been pressed
    @FXML
    private void handleConnectButtonAction(ActionEvent event) throws IOException {
        Server server = serverList.getSelectionModel().getSelectedItem();

        User user = new User(nickname.getText(),alt_nickname.getText(),username.getText());

        if(isUserInfo()) {
            user = USER_DAO.get(serverList.getSelectionModel().getSelectedItem().getId()).get();
            conn.connect(server,user);
        }
        conn.defaultConnect(server,user);
        openNewWindow("main_window.fxml",true, "FX_IRC");
    }

    @FXML
    private void handleNewServerButtonAction(ActionEvent event) throws IOException {
        helper.setPopulate(false);
        openNewWindow("new_server.fxml",false,"Dodawanie Nowego Servwera");
    }
    @FXML
    private void handleEditServerButtonAction(ActionEvent event) throws IOException {
        helper.setPopulate(true);
        helper.setServer(serverList.getSelectionModel().getSelectedItem());
        openNewWindow("new_server.fxml",false, "Edycja Informacji Użytkownika");
    }

   public void initEventBus(){
       eb.register(new BooleanEventListener());
   }


   @FXML
    public void initialize() throws SQLException {
        serverList.setItems(obsServerList);
        serverList.refresh();
   }
}
