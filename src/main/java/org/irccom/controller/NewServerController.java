package org.irccom.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import org.kitteh.irc.client.library.element.Channel;

public class NewServerController {
    public JFXTextField newServerName;
    public JFXTextField newServerIpAddress;
    public JFXTextField newServerPort;
    public JFXTextField newServerNickname;
    public JFXTextField newServerAlternativeNickname;
    public JFXTextField newServerRealName;
    public JFXTextField newServerUsername;
    public JFXPasswordField newServerPassword;
    public JFXListView<Channel> autoJoinChannelsListView;
    public JFXButton saveAutoJoinChannelsButton;

    private ObservableSet<Channel> channelsSet = FXCollections.observableSet();
}
