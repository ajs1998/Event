package me.alexjs.event.mock;

import java.util.EventListener;

public class IgnoreListener3 implements EventListener {

    private void ignoredMethod3(MockEvent2 event) {
        event.getValue().getAndIncrement();
    }

}
