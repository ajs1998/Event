package me.alexjs.event;

import java.lang.reflect.Method;
import java.util.EventListener;

public class Subscriber {

    private final EventListener listener;
    private final Method method;

    public Subscriber(EventListener listener, Method method) {
        this.listener = listener;
        this.method = method;
    }

    public EventListener getListener() {
        return listener;
    }

    public Method getMethod() {
        return method;
    }

}
