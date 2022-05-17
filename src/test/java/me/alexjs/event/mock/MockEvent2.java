package me.alexjs.event.mock;

import java.util.concurrent.atomic.AtomicInteger;

public class MockEvent2 {

    private final AtomicInteger value;

    public MockEvent2(AtomicInteger value) {
        this.value = value;
    }

    public AtomicInteger getValue() {
        return value;
    }

}
