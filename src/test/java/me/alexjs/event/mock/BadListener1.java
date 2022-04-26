package me.alexjs.event.mock;

import me.alexjs.event.Subscribe;

import java.util.EventListener;

public class BadListener1 implements EventListener {

    @Subscribe
    private void listen1(MockEvent2 event) {
        event.getValue().getAndIncrement();
    }

}
