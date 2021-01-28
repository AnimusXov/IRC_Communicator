package org.irccom.controller.factory;

import javafx.scene.control.*;

import java.util.Optional;

public class DialogWindowFactory {

    public static boolean showConfirmDialog(String title, String content) {
        ButtonType yes = new ButtonType("Potwierdź", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Analuj", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                content,
                yes,
                no);

        alert.setTitle(title);
        Optional<ButtonType> result = alert.showAndWait();

        return result.orElse(no) == yes;
    }
}
