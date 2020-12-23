package org.irccom.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.irccom.irc.Connect;
import org.irccom.model.Server;
import org.irccom.sqlite.Crud;

import java.io.IOException;
import java.sql.SQLException;


public class MenuController {
    Connect conn = new Connect();
    public JFXButton connect;
    public JFXTextField nickname;


    @FXML
    public void openNewWindow(String fxml) throws IOException {
        Stage old_stage = (Stage) connect.getScene().getWindow();
        old_stage.close();
        Scene scene;
        FXMLLoader fxmlLoader = new
        FXMLLoader(getClass().getResource("/fxml/"+fxml));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.NONE);
        scene = (new Scene(root1));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    JFXListView<Server> serverList = new JFXListView<>();

    // Action after connect button has been pressed
    @FXML
    private void handleConnectButtonAction(ActionEvent event) throws IOException {
        conn.connect(nickname.getText(),serverList.getSelectionModel().getSelectedItem().getIp());
        openNewWindow("main_window.fxml");



    }

    private void getServers(){
    }

   @FXML
    public void initialize() throws SQLException {
        serverList.setItems(Crud.selectServers());
        serverList.refresh();
   }
}
