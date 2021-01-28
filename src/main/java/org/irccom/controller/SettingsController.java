package org.irccom.controller;

import com.jfoenix.controls.JFXColorPicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.irccom.controller.preset.DefaultSettings;
import org.irccom.helper.Converter;

import java.awt.*;

public class SettingsController{

Converter converter = new Converter();
public JFXColorPicker ownerColour;
public JFXColorPicker operatorColour;
public JFXColorPicker halfOpColour;
public JFXColorPicker ircAdminColour;
public JFXColorPicker myColour;
public JFXColorPicker otherUsers;

@FXML
private void onColorChanged(ActionEvent event ){
	JFXColorPicker colorPicker = (JFXColorPicker) event.getSource();
	
}


public void initialize(){
getDefaultSettings();

}

private void getDefaultSettings(){
	ownerColour.setValue(converter.convertToFXColor('~'));
	operatorColour.setValue(converter.convertToFXColor('@'));
	halfOpColour.setValue(converter.convertToFXColor('%'));
	ircAdminColour.setValue(converter.convertToFXColor('&'));
	myColour.setValue(converter.convertToFXColor('U'));
	otherUsers.setValue(converter.convertToFXColor(' '));
}



}
