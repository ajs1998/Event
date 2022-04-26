package me.alexjs.event.mock;

import me.alexjs.event.Subscribe;

import java.util.EventListener;

public class MockListener1 implements EventListener {

    @Subscribe
    public void listen1(MockEvent1 event) {
        event.getValue().set(true);
    }

    @Subscribe
    public void listen2(MockEvent2 event) {
        event.getValue().getAndIncrement();
    }

}
