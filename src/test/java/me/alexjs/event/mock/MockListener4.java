package me.alexjs.event.mock;

import me.alexjs.event.Subscribe;

import java.util.EventListener;
import java.util.concurrent.atomic.AtomicInteger;

public class MockListener4 implements EventListener {

    @Subscribe
    public void listen1(MockEvent2 event) {
        event.getValue().getAndIncrement();
    }

    @Subscribe
    public void listen2(MockEvent2 event) {
        event.getValue().getAndIncrement();
    }

}
