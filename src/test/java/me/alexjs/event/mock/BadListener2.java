package me.alexjs.event.mock;

import me.alexjs.event.Subscribe;

import java.util.EventListener;

public class BadListener2 implements EventListener {

    @Subscribe
    public void listen3(MockEvent2 event, boolean b) {
        event.getValue().getAndIncrement();
    }

}
