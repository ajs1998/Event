package me.alexjs.event.mock;

import me.alexjs.event.Subscribe;

import java.util.EventListener;

public class MockListener2 implements EventListener {

    @Subscribe
    public void listen(MockEvent2 event) {
        throw new RuntimeException("uh oh!");
    }

}
