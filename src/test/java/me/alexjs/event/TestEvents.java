package me.alexjs.event;

import me.alexjs.event.mock.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TestEvents {

    @Test
    public void testMultipleEvents() {

        EventPublisher publisher = new EventPublisher();
        publisher.register(new MockListener1());

        MockEvent1 event1 = new MockEvent1(new AtomicBoolean(false));
        publisher.publish(event1);

        MockEvent2 event2 = new MockEvent2(new AtomicInteger(0));
        publisher.publish(event2);

        Assertions.assertTrue(event1.getValue().get());
        Assertions.assertEquals(1, event2.getValue().get());

    }

    @Test
    public void testMultipleListeners() {

        EventPublisher publisher = new EventPublisher();
        publisher.register(new MockListener1());
        publisher.register(new MockListener1());

        MockEvent2 event = new MockEvent2(new AtomicInteger(0));
        publisher.publish(event);

        Assertions.assertEquals(2, event.getValue().get());

    }

    @Test
    public void testPublishNull() {

        EventPublisher publisher = new EventPublisher();
        publisher.register(new MockListener1());

        Assertions.assertThrowsExactly(NullPointerException.class, () -> publisher.publish(null));

    }

    @Test
    public void testNoListeners() {

        EventPublisher publisher = new EventPublisher();

        MockEvent2 event = new MockEvent2(new AtomicInteger(0));
        publisher.publish(event);

        Assertions.assertEquals(0, event.getValue().get());

    }

    @Test
    public void testException() {

        EventPublisher publisher = new EventPublisher();

        MockListener3 listener1 = new MockListener3();
        MockListener2 badListener = new MockListener2();
        MockListener3 listener2 = new MockListener3();

        publisher.register(listener1);
        publisher.register(badListener);
        publisher.register(listener2);

        MockEvent2 event = new MockEvent2(new AtomicInteger(0));
        Assertions.assertDoesNotThrow(() -> publisher.publish(event));

        Assertions.assertEquals(1, listener1.getValue());
        Assertions.assertEquals(1, listener2.getValue());

    }

    @Test
    public void testMultipleSubscribers() {

        EventPublisher publisher = new EventPublisher();

        MockListener4 listener = new MockListener4();
        publisher.register(listener);

        MockEvent2 event = new MockEvent2(new AtomicInteger(0));
        publisher.publish(event);

        Assertions.assertEquals(2, event.getValue().get());

    }

}
