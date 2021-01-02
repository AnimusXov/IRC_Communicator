package org.irccom.guava.listener;

import com.google.common.eventbus.Subscribe;
import org.irccom.guava.event.BooleanEvent;

public class BooleanEventListener {
    public boolean lastBool = false;

    @Subscribe
    public void listen(BooleanEvent event) {
        lastBool = event.getBool();
    }

    public boolean getLastBool() {
        return lastBool;
    }
}