package me.alexjs.event.mock;

import java.util.EventListener;

public class IgnoreListener2 implements EventListener {

    public void ignoredMethod2(MockEvent2 event) {
        event.getValue().getAndIncrement();
    }

}
