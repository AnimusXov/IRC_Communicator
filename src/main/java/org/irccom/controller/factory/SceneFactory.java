package org.irccom.controller.factory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;

public class SceneFactory{
@FXML
public void openNewWindow(String fxml, boolean isClose_stage, boolean isJMETRO, String title) throws IOException{
	
	Scene scene;
	FXMLLoader fxmlLoader = new
	FXMLLoader(getClass().getResource("/fxml/"+fxml));
	Parent root1 = fxmlLoader.load();
	Stage stage = new Stage();
	stage.setTitle(title);
	stage.initModality(Modality.NONE);
	scene = (new Scene(root1));
	stage.setScene(scene);
	stage.show();
	stage.setMaxWidth(720);
	stage.setMaxHeight(480);
	stage.setMinWidth(380);
	stage.setMinHeight(360);
	if(isJMETRO) {
		JMetro jMetro = new JMetro(Style.LIGHT);
		jMetro.setAutomaticallyColorPanes(false);
		jMetro.setScene(scene);
	}
	stage.setScene(scene);
	stage.show();
	}
}

