package me.alexjs.event.mock;

import me.alexjs.event.Event;

import java.util.concurrent.atomic.AtomicBoolean;

public class NonFinalEvent implements Event {

    private final AtomicBoolean value;

    public NonFinalEvent(AtomicBoolean value) {
        this.value = value;
    }

    public AtomicBoolean getValue() {
        return value;
    }

}
