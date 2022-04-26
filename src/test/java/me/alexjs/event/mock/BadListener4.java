package me.alexjs.event.mock;

import me.alexjs.event.Subscribe;

import java.util.EventListener;

public class BadListener4 implements EventListener {

    @Subscribe
    public void listen4() {
        throw new RuntimeException("uh oh! oh well, this won't happen anyway if tests pass");
    }

}
