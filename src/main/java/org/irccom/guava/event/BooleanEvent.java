package org.irccom.guava.event;

public class BooleanEvent {
    private final boolean bool;

    public BooleanEvent(boolean bool) {
        this.bool = bool;
    }

    public Boolean getBool() {
        return bool;
    }
}

