package org.irccom.controller.custom;

import org.irccom.sqlite.model.Server;

import java.util.Comparator;

public class ListViewComparator implements Comparator<Server> {
    @Override
    public int compare(Server o1, Server o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
