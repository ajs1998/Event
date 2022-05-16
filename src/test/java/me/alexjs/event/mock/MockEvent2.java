package me.alexjs.event.mock;

import me.alexjs.event.Event;

import java.util.concurrent.atomic.AtomicInteger;

public final class MockEvent2 implements Event {

    private final AtomicInteger value;

    public MockEvent2(AtomicInteger value) {
        this.value = value;
    }

    public AtomicInteger getValue() {
        return value;
    }

}
