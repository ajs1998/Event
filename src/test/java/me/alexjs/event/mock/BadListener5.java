package me.alexjs.event.mock;

import me.alexjs.event.Subscribe;

import java.util.EventListener;

public class BadListener5 implements EventListener {

    @Subscribe
    public void listen4(NonFinalEvent event) {
        throw new RuntimeException("uh oh! oh well, this won't happen anyway if tests pass");
    }

}
