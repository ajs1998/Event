package me.alexjs.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Register your subscriber methods with an {@link EventPublisher}
 * <p>
 * An {@link EventListener} will only be registered if all methods annotated with {@link Subscribe} are {@code public}
 * and take exactly one event parameter.
 */
public class EventPublisher {

    private static final Logger log = LogManager.getLogger(EventPublisher.class);

    private final Map<Class<?>, List<Subscriber>> listeners = new HashMap<>();
    private final Executor executor;

    /**
     * Create a new {@link EventPublisher} that uses a single thread to publish events
     *
     * @see #EventPublisher(Executor)
     */
    public EventPublisher() {
        this(Executors.newSingleThreadExecutor());
    }

    /**
     * Create a new {@link EventPublisher} with a custom {@link Executor} to publish events
     *
     * @param executor the executor that events will be published with
     */
    public EventPublisher(Executor executor) {
        this.executor = executor;
    }

    /**
     * Register an {@link EventListener} with this publisher
     * <p>
     * Each {@code public} method annotated with {@link Subscribe} in the listener class will be added to a list.
     * When events are published, methods which have subscribed to that event, or a supertype, will be invoked.
     *
     * @param listener a listener for this publisher to notify when events are published
     */
    public void register(EventListener listener) {

        Class<? extends EventListener> listenerClass = listener.getClass();
        for (Method method : listenerClass.getDeclaredMethods()) {

            // Must be annotated with @Subscribe
            if (method.isAnnotationPresent(Subscribe.class)) {

                // Must be public
                if (!Modifier.isPublic(method.getModifiers())) {
                    throw new IllegalArgumentException("@Subscribe-annotated method must be public: " + method);
                }

                // Must take exactly one parameter
                if (method.getParameterCount() != 1) {
                    throw new IllegalArgumentException("@Subscribe-annotated method must take exactly one parameter: " + method);
                }

                Class<?> type = method.getParameterTypes()[0];
                Subscriber subscriber = new Subscriber(listener, method);

                log.info("Registering listener for event: {}", type.getName());

                // Register the listener
                if (!listeners.containsKey(type)) {
                    List<Subscriber> list = new ArrayList<>();
                    list.add(subscriber);
                    listeners.put(type, list);
                } else {
                    listeners.get(type).add(subscriber);
                }

            }

        }

    }

    /**
     * Publish an event to all registered listeners
     * <p>
     * Only listeners that have subscribed to this kind of event will be notified.
     * If an exception occurs in one listener, it will be logged and dropped.
     * The publisher should have no opinion on the result of one subscriber's notification.
     *
     * @param event the event to publish to registered listeners
     * @return a {@link CompletableFuture} that blocks until the event has been published to all listeners
     */
    public CompletableFuture<Void> publish(Object event) {

        // Iterate through the listeners for this type of event and invoke the subscriber method
        List<Subscriber> subscribers = listeners.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(event.getClass()))
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toList());

        return CompletableFuture.runAsync(() -> {
            for (Subscriber subscriber : subscribers) {
                try {
                    subscriber.method.invoke(subscriber.listener, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    // Drop everything else. The publisher doesn't care at all if one of its listeners fails
                    log.warn("Error in subscriber", e);
                }
            }
        }, executor);

    }

    /**
     * Very simple data object for holding an {@link EventListener} and a {@link Method} within it
     */
    private static final class Subscriber {

        final EventListener listener;
        final Method method;

        Subscriber(EventListener listener, Method method) {
            this.listener = listener;
            this.method = method;
        }

    }

}
