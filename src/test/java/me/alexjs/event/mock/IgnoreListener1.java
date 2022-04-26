package me.alexjs.event.mock;

import java.util.EventListener;

public class IgnoreListener1 implements EventListener {

    public void ignoredMethod1() {
        throw new RuntimeException("uh oh! oh well, this won't happen anyway if tests pass");
    }

}
