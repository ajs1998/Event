package me.alexjs.event;

import java.lang.reflect.Method;
import java.util.EventListener;

/**
 * Very simple data object for holding an {@link EventListener} and a {@link Method} within it.
 *
 * This class is package-private because it really doesn't need to be externally visible.
 */
class Subscriber {

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
