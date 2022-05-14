package me.alexjs.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

/**
 * Register your subscriber methods with an {@link EventPublisher}
 * <p>
 * Only {@code public void} methods annotated with {@link Subscribe} will be registered
 */
public class EventPublisher {

    private static final Logger log = LogManager.getLogger(EventPublisher.class);

    private final Map<Class<? extends Event>, List<Subscriber>> listeners = new HashMap<>();
    private final Executor executor;

    /**
     * Create a new {@link EventPublisher} that uses a single thread to publish events
     */
    public EventPublisher() {
        this(Executors.newSingleThreadExecutor());
    }

    /**
     * Create a new {@link EventPublisher} with a custom {@link Executor} to publish events
     * @param executor the executor that events will be published with
     */
    public EventPublisher(Executor executor) {
        this.executor = executor;
    }

    /**
     * Register an {@link EventListener} with this publisher
     * <p>
     * Each {@code public void} method annotated with {@link Subscribe} in the listener class will be added to a list.
     * When events are published, only methods that have subscribed to that event will be invoked.
     *
     * @param listener a listener for this publisher to notify when events are published
     */
    public void register(EventListener listener) {

        for (Method method : listener.getClass().getDeclaredMethods()) {

            // Must be annotated with @Subscribe
            if (method.isAnnotationPresent(Subscribe.class)) {

                // Must be public
                if (!Modifier.isPublic(method.getModifiers())) {
                    throw new IllegalArgumentException("@Subscribe-annotated method must be public");
                }

                // Must return void
                if (method.getReturnType() != void.class) {
                    throw new IllegalArgumentException("@Subscribe-annotated method must return void");
                }

                // Must take exactly one MonoEvent parameter
                if (method.getParameterCount() != 1 || !Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                    throw new IllegalArgumentException("@Subscribe-annotated method must take exactly one MonoEvent parameter");
                }

                Subscriber subscriber = new Subscriber(listener, method);
                @SuppressWarnings("unchecked")
                Class<? extends Event> type = (Class<? extends Event>) method.getParameters()[0].getType();

                log.info("Registering listener for event: {}", type.getName());

                // Register the listener
                if (listeners.containsKey(type)) {
                    listeners.get(type).add(subscriber);
                } else {
                    List<Subscriber> list = new ArrayList<>();
                    list.add(subscriber);
                    listeners.put(type, list);
                }

            }

        }

    }

    /**
     * Publish an event to all registered listeners
     * <p>
     * Only listeners that have subscribed to this kind of event will be notified
     * <p>
     * If an exception occurs in one listener, it will be logged and dropped.
     * The publisher should have no opinion on the result of one subscriber's notification.
     *
     * @param event the event to publish to registered listeners
     *
     * @return a {@link CompletableFuture} that blocks until the event has been published to all listeners
     */
    public CompletableFuture<Void> publish(Event event) {

        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);

        // Iterate through the listeners for this type of event and invoke the subscriber method
        if (!listeners.isEmpty()) {
            for (Subscriber subscriber : listeners.get(event.getClass())) {

                future = future.thenRunAsync(() -> {
                    try {
                        subscriber.getMethod().invoke(subscriber.getListener(), event);
                    } catch (Throwable t) {
                        // Drop the exception. The publisher doesn't care at all if one of its listeners fails
                        log.warn("Error in subscriber", t);
                    }
                }, executor);

            }
        }

        return future;

    }

    /**
     * Very simple data object for holding an {@link EventListener} and a {@link Method} within it
     * <p>
     * This class is package-private because it really doesn't need to be externally visible
     */
    private static final class Subscriber {

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

}
