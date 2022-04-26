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

public class EventPublisher {

    private static final Logger log = LogManager.getLogger(EventPublisher.class);

    private final Map<Class<? extends Event>, List<Subscriber>> listeners = new HashMap<>();

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
                Class<? extends Event> type = (Class<? extends Event>) method.getParameters()[0].getType();

                log.info("Registering listener for type: {}", type.getName());

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

    public void publish(Event event) {

        Class<? extends Event> type = event.getClass();

        // Iterate through the listeners for this type of event and invoke the subscriber method
        if (!listeners.isEmpty()) {
            for (Subscriber subscriber : listeners.get(type)) {
                try {
                    subscriber.getMethod().invoke(subscriber.getListener(), event);
                } catch (Throwable ignore) {
                    log.error("Error while publishing event", ignore);
                }
            }
        }

    }

}
