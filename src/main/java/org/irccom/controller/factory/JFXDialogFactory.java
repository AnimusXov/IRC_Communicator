package org.irccom.controller.factory;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.irccom.controller.MainWindowController;

import javax.xml.soap.Text;
import java.util.List;

public class JFXDialogFactory{
private final MainWindowController mainWindowController;

public JFXDialogFactory( MainWindowController mainWindowController ){ this.mainWindowController = mainWindowController; }

public void createJFXDialog( StackPane root, Node blurredNode, List<JFXButton> controls, String header, String body, int type ){
	
	TextField textField = new TextField();
	if ( type == 2 ) {
		mainWindowController.pointerCurrentChannel.getTopic().getValue().ifPresent(textField::setText);
	}
	BoxBlur blur = new BoxBlur(3, 3, 3);
	if ( controls.isEmpty() ) {
		controls.add(new JFXButton("Dodaj"));
	}
	JFXDialogLayout dialogLayout = new JFXDialogLayout();
	JFXDialog dialog = new JFXDialog(root, dialogLayout, JFXDialog.DialogTransition.TOP);
	
	controls.forEach(controlButton -> {
		controlButton.getStyleClass().add("confirm-button");
		controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, ( MouseEvent mouseEvent ) -> dialog.close());
	});
	dialogLayout.setHeading(new Label(header));
	dialogLayout.setBody(new Label(body));
	dialogLayout.setBody(textField);
	dialogLayout.setActions(controls);
	dialog.show();
	dialog.setOnDialogClosed(( JFXDialogEvent event1 ) -> {
		blurredNode.setEffect(null);
		
		Platform.runLater(() -> {
			switch ( type ) {
				case 1:
					if ( !textField.getText().isEmpty() ) {
						mainWindowController.getClient().addChannel(textField.getText());
						break;
					}
				case 2:
					mainWindowController.pointerCurrentChannel.setTopic(textField.getText());
			}
			
		});
	});
	blurredNode.setEffect(blur);
	
}



public void createJFXRegisterDialog( StackPane root, Node blurredNode, List<JFXButton> controls,List<JFXButton> controls2, String header, String body, int type ){
	
	TextField email = new TextField();
	TextField password = new TextField();
	TextField passwordConfirmation = new TextField();
	TextField accountCodeConfirmation = new TextField();
	VBox vBox = new VBox();
	VBox vBox2 = new VBox();
	vBox.setSpacing(10);
	vBox2.setSpacing(10);
	
	email.setPromptText("Adres E-Mail");
	password.setPromptText("Hasło");
	passwordConfirmation.setPromptText("Potwierdź hasło");
	BoxBlur blur = new BoxBlur(3, 3, 3);
	if ( controls.isEmpty() ) {
		controls.add(new JFXButton("Zarejestruj"));
	}
	if ( controls2.isEmpty() ) {
		controls2.add(new JFXButton("Potwierdź"));
	}
	
	JFXDialogLayout dialogLayout = new JFXDialogLayout();
	JFXDialogLayout dialogLayout2 = new JFXDialogLayout();
	JFXDialog dialog = new JFXDialog(root, dialogLayout, JFXDialog.DialogTransition.TOP);
	JFXDialog dialog2 = new JFXDialog(root, dialogLayout2, JFXDialog.DialogTransition.TOP);
	controls.forEach(controlButton -> {
		controlButton.getStyleClass().add("confirm-button");
		controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, ( MouseEvent mouseEvent ) -> { dialog2.show(); dialog.close();
		Platform.runLater(() -> {
			mainWindowController.getClient().sendRawLineImmediately("privmsg NickServ " + password.getText() + " " + email.getText());
		});
		});
	});
	controls2.forEach(controlButton -> {
		controlButton.getStyleClass().add("confirm-button");
		controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> dialog2.close());
	});
	
	dialogLayout2.setHeading(new Label("Potwierdzenie adresu E-Mail"));
	dialogLayout.setHeading(new Label(header));
	vBox.getChildren().addAll(email,password,passwordConfirmation,new Label(body));
	vBox2.getChildren().addAll(accountCodeConfirmation, new Label("Wprowadź kod przesłany na adres E-Mail"));
	dialogLayout2.setBody(vBox2);
	dialogLayout2.setActions(controls2);
	dialogLayout.setBody(vBox);
	dialogLayout.setActions(controls);
	dialog.show();
	blurredNode.setEffect(blur);
	
	dialog2.setOnDialogClosed(( JFXDialogEvent event1 ) -> {
		blurredNode.setEffect(null);
		Platform.runLater(() -> {
			mainWindowController.getClient().sendRawLineImmediately("privmsg NickServ confirm " + accountCodeConfirmation.getText());
		});
	});
	
	dialog.setOnDialogClosed(( JFXDialogEvent event1 ) -> {
		if(!dialog2.isVisible()) {
			blurredNode.setEffect(null);
		}
	});
}
}