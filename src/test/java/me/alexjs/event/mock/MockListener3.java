package me.alexjs.event.mock;

import me.alexjs.event.Subscribe;

import java.util.EventListener;
import java.util.concurrent.atomic.AtomicInteger;

public class MockListener3 implements EventListener {

    private final AtomicInteger value = new AtomicInteger(0);

    @Subscribe
    public void listen(MockEvent2 event2) {
        value.getAndIncrement();
    }

    public int getValue() {
        return value.get();
    }

}
