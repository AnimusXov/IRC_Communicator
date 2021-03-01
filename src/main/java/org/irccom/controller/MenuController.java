package org.irccom.controller;

import com.google.common.eventbus.EventBus;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.irccom.controller.custom.ListViewComparator;
import org.irccom.controller.factory.DialogWindowFactory;
import org.irccom.helper.GlobalInstances;
import org.irccom.irc.Connect;
import org.irccom.sqlite.model.Server;
import org.irccom.sqlite.model.User;
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
    private boolean isSortedReversed = false;
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

        Scene scene;
        FXMLLoader fxmlLoader = new
        FXMLLoader(getClass().getResource("/fxml/"+fxml));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.NONE);
        scene = (new Scene(root1));
        if(!isClose_stage) {
            stage.setScene(scene);
            stage.show();
            stage.setMaxWidth(380);
            stage.setMaxHeight(360);
            stage.setMinWidth(380);
            stage.setMinHeight(360);
            stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);

        }
        else {
            old_stage.close();
           // JMetro jMetro = new JMetro(Style.LIGHT);
          //  jMetro.setAutomaticallyColorPanes(false);
          //  jMetro.getOverridingStylesheets().add("/stylesheet/chatListView.css");
            stage.setMinWidth(1200);
            stage.setMinHeight(800);
          //  jMetro.setScene(scene);
            stage.setScene(scene);
            stage.show();
	        stage.setOnCloseRequest(e -> Platform.exit());
        }
    }
    private void closeWindowEvent(WindowEvent event) {
    obsServerList.clear();
    obsServerList.addAll(SERVER_DAO.getAll());
    serverList.refresh();
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
        if(!serverList.getSelectionModel().isEmpty()) {
            Server server = serverList.getSelectionModel().getSelectedItem();
            User user = new User(nickname.getText(), alt_nickname.getText(), username.getText());
            if (isUserInfo()) {
                user = USER_DAO.get(serverList.getSelectionModel().getSelectedItem().getId()).get();
            }
            conn.connect(server, user,isUserInfo());
            openNewWindow("main_window.fxml", true, "JFFX");
        }
    }

    @FXML
    private void handleNewServerButtonAction(ActionEvent event) throws IOException {
        helper.setPopulate(false);
        openNewWindow("new_server.fxml",false,"Dodawanie Nowego Servwera");
    }
    @FXML
    private void handleDeleteServerButtonAction(ActionEvent event) throws IOException {
        // If user chooses a confirm button, server will be deleted.
         if(
         DialogWindowFactory.showConfirmDialog("Potwierdzenie usunięcia","Czy aby napewno usunąć "
                + serverList.getSelectionModel().getSelectedItem().getName() + "?")){
             if(!serverList.getSelectionModel().isEmpty()) {
                 SERVER_DAO.delete(serverList.getSelectionModel().getSelectedItem());
                 serverList.getItems().remove(serverList.getSelectionModel().getSelectedItem());
                 serverList.refresh();
             }
         }

    }

    @FXML
    private void handleSortServerButtonAction(ActionEvent event) {
        if(!isSortedReversed){
        serverList.getItems().sort(new ListViewComparator().reversed());
        isSortedReversed = true;
        }
        else {
            serverList.getItems().sort(new ListViewComparator());
            isSortedReversed = false;
        }
    }

    @FXML
    private void handleEditServerButtonAction(ActionEvent event) throws IOException {
        if(!serverList.getSelectionModel().isEmpty()) {
            helper.setPopulate(true);
            helper.setServer(serverList.getSelectionModel().getSelectedItem());
            openNewWindow("new_server.fxml", false, "Edycja Informacji Użytkownika");
        }
    }


   @FXML
    public void initialize() throws SQLException {
        serverList.setItems(obsServerList);
        serverList.refresh();
   }
}
