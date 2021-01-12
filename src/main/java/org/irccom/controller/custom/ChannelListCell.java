package org.irccom.controller.custom;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

public class ChannelListCell extends ListCell<String> {
    private TextField textField;
    ListCell<String> cell = this;
    int i = 0;



    public ChannelListCell(ListView<String> listView)
    {

        ContextMenu contextMenu = new ContextMenu();


        cell.setEditable(true);



        MenuItem editItem = new MenuItem();
        editItem.textProperty().bind(Bindings.format("Edytuj \"%s\"", cell.itemProperty()));

        editItem.setOnAction(event -> {

            // The LanguageListCell class i want to put here...
            cell.startEdit();
        });




        MenuItem deleteItem = new MenuItem();
        deleteItem.textProperty().bind(Bindings.format("Usuń \"%s\"", cell.itemProperty()));
        deleteItem.setOnAction(ev -> {
            if(listView.getSelectionModel().getSelectedItems().size() - 1 > 0)
            {

                if(i > 0)
                {
                    i = (listView.getItems().size() - listView.getSelectionModel().getSelectedItems().size()) - 1;
                }
                for(String item: listView.getSelectionModel().getSelectedItems())
                {

                    listView.getItems().remove(item);

                }



            }
            listView.getItems().remove(cell.getItem());
            if(i > 0) i = 0;
        });

        contextMenu.setOnShowing(e -> {
            if(listView.getSelectionModel().getSelectedItems().size() - 1 > 0)
            {
                editItem.setDisable(true);
            }
        });

        contextMenu.getItems().addAll(editItem, deleteItem);

        cell.textProperty().bind(cell.itemProperty());

        cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
            if (isNowEmpty) {
                cell.setContextMenu(null);
            } else {

                cell.setContextMenu(contextMenu);

            }
        });



    }



    public String getString() {
        return getItem() == null ? "" : getItem();
    }

    @Override
    public void startEdit() {
        super.startEdit();

        if (textField == null) {
            createTextField();
        }
        cell.textProperty().unbind();
        setText(null);
        setGraphic(textField);
        textField.selectAll();
        textField.requestFocus();
    }



    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem());
        setGraphic(null);
        setGraphic(getGraphic());
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        cell.textProperty().unbind();
        if (empty) {

            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }

                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(getGraphic());
            }
        }
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.setOnKeyReleased(t -> {
            if (t.getCode() == KeyCode.ENTER) {
                if(!textField.getText().trim().equals("") && textField.getText().trim().length() > 3)
                {
                    commitEdit(textField.getText());
                    setGraphic(null);
                    setGraphic(getGraphic());
                }



            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }

}
