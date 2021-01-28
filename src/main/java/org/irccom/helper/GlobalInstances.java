package org.irccom.helper;

import org.irccom.sqlite.model.Server;

public class GlobalInstances {
    private static Server selectedServer;
    private static Boolean isPopulate;

    public Boolean getPopulate() {
        return isPopulate;
    }

    public void setPopulate(Boolean populate) {
        isPopulate = populate;
    }

    public Server getSelectedServerServer() {
        return selectedServer;
    }

    public void setServer(Server server) {
        GlobalInstances.selectedServer = server;
    }

}
