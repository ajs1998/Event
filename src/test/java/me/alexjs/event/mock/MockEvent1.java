package me.alexjs.event.mock;

import java.util.concurrent.atomic.AtomicBoolean;

public class MockEvent1 {

    private final AtomicBoolean value;

    public MockEvent1(AtomicBoolean value) {
        this.value = value;
    }

    public AtomicBoolean getValue() {
        return value;
    }

}
