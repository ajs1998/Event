package me.alexjs.event.mock;

import java.util.concurrent.atomic.AtomicBoolean;

public class NonFinalEvent {

    private final AtomicBoolean value;

    public NonFinalEvent(AtomicBoolean value) {
        this.value = value;
    }

    public AtomicBoolean getValue() {
        return value;
    }

}
