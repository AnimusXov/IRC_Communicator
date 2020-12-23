package org.irccom.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import org.irccom.App;


public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");

    }
}