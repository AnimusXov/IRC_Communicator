package org.irccom.controller;

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
import org.irccom.irc.Connect;
import org.irccom.model.Server;
import org.irccom.sqlite.GenericDao;
import org.irccom.sqlite.GenericDaoImpl;

import java.io.IOException;
import java.sql.SQLException;


public class MenuController {
    public JFXButton addNewServerButton;
    Connect conn = new Connect();
    public JFXButton connect;
    public JFXTextField nickname;

    private final GenericDao<Server,Integer> SERVER_DAO = new GenericDaoImpl();

    @FXML
    JFXListView<Server> serverList = new JFXListView<>();
    ObservableList<Server> obsServerList = FXCollections.observableArrayList(SERVER_DAO.getAll());





    @FXML
    public void openNewWindow(String fxml,boolean close_stage) throws IOException {
        Stage old_stage = (Stage) connect.getScene().getWindow();
        if(close_stage)
        old_stage.close();
        Scene scene;
        FXMLLoader fxmlLoader = new
        FXMLLoader(getClass().getResource("/fxml/"+fxml));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        scene = (new Scene(root1));
        stage.setScene(scene);
        stage.show();
    }

    // Action after connect button has been pressed
    @FXML
    private void handleConnectButtonAction(ActionEvent event) throws IOException {
        conn.connect(nickname.getText(),serverList.getSelectionModel().getSelectedItem().getIp());
        openNewWindow("main_window.fxml",true);
    }

    @FXML
    private void handleNewServerButtonAction(ActionEvent event) throws IOException {
        openNewWindow("new_server.fxml",false);
    }



   @FXML
    public void initialize() throws SQLException {
        serverList.setItems(obsServerList);
        serverList.refresh();
   }
}
