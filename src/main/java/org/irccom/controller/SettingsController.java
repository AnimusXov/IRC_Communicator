package org.irccom.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXMasonryPane;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.irccom.controller.preset.Config;
import org.irccom.helper.Converter;
import org.irccom.sqlite.GenericDao;
import org.irccom.sqlite.PreferenceGenericDaoImpl;
import org.irccom.sqlite.model.Preference;

public class SettingsController{

public JFXMasonryPane masonryPane;
public JFXButton saveButton;
public JFXButton resetButton;
public JFXButton cancelButton;
Converter converter = new Converter();
public JFXColorPicker ownerColour;
public JFXColorPicker operatorColour;
public JFXColorPicker halfOpColour;
public JFXColorPicker ircAdminColour;
public JFXColorPicker myColour;
public JFXColorPicker otherUsers;
private final GenericDao<Preference,Integer> PREFERENCE_DAO = new PreferenceGenericDaoImpl();

public void initialize(){
	getDefaultSettings();
}

private void getDefaultSettings(){
	if(Config.userColorMap.isEmpty()) {
		setDefaultsColours();
	}
	else{
		setUserColours();
	}
}
private void setUserColours(){
	ownerColour.setValue(converter.hexToColor(Config.userColorMap,'~'));
	operatorColour.setValue(converter.hexToColor(Config.userColorMap,'@'));
	halfOpColour.setValue(converter.hexToColor(Config.userColorMap,'%'));
	ircAdminColour.setValue(converter.hexToColor(Config.userColorMap,'&'));
	myColour.setValue(converter.hexToColor(Config.userColorMap,'U'));
	otherUsers.setValue(converter.hexToColor(Config.userColorMap,' '));
}
private void setDefaultsColours(){
	ownerColour.setValue(converter.convertToFXColor(Config.defaultColorMap,'~'));
	operatorColour.setValue(converter.convertToFXColor(Config.defaultColorMap,'@'));
	halfOpColour.setValue(converter.convertToFXColor(Config.defaultColorMap,'%'));
	ircAdminColour.setValue(converter.convertToFXColor(Config.defaultColorMap,'&'));
	myColour.setValue(converter.convertToFXColor(Config.defaultColorMap,'U'));
	otherUsers.setValue(converter.convertToFXColor(Config.defaultColorMap,' '));
}
private void putColoursValuesToUserHashMap(){
	Config.userColorMap.put('~',converter.toHexString(ownerColour.getValue()));
	Config.userColorMap.put('@',converter.toHexString(operatorColour.getValue()));
	Config.userColorMap.put('%',converter.toHexString(halfOpColour.getValue()));
	Config.userColorMap.put('&',converter.toHexString(ircAdminColour.getValue()));
	Config.userColorMap.put('U',converter.toHexString(myColour.getValue()));
	Config.userColorMap.put(' ',converter.toHexString(otherUsers.getValue()));
}

@FXML
private void handleSaveSettingsButtonAction(){
	putColoursValuesToUserHashMap();
	if(!PREFERENCE_DAO.get(1).isPresent()) {
	PREFERENCE_DAO.delete(new Preference(1,""));
	PREFERENCE_DAO.save(new Preference(1,converter.mapToString(Config.userColorMap)));
	}
	PREFERENCE_DAO.update(new Preference(1,converter.mapToString(Config.userColorMap)));
}

@FXML
private void handleResetSettingsButtonAction(){
	setDefaultsColours();
}

@FXML
private void handleCancelSettingsButtonAction(){
	Stage old_stage = (Stage) ownerColour.getScene().getWindow();
	old_stage.close();
}




}
