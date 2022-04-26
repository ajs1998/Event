package me.alexjs.event.mock;

import me.alexjs.event.Subscribe;

import java.util.EventListener;

public class BadListener2 implements EventListener {

    @Subscribe
    public int listen2(MockEvent2 event) {
        return event.getValue().getAndIncrement();
    }

}
