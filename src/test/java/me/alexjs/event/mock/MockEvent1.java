package me.alexjs.event.mock;

import me.alexjs.event.Event;

import java.util.concurrent.atomic.AtomicBoolean;

public class MockEvent1 implements Event {

    private final AtomicBoolean value;

    public MockEvent1(AtomicBoolean value) {
        this.value = value;
    }

    public AtomicBoolean getValue() {
        return value;
    }

}
