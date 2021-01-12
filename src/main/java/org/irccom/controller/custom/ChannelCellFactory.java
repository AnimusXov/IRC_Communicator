package org.irccom.controller.custom;

import javafx.collections.ObservableSet;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ChannelCellFactory implements Callback<ListView<String>,
        ListCell<String>>{

    @Override
    public ListCell<String> call(ListView<String> listView)
    {
        return new ChannelListCell(listView);
    }

}
