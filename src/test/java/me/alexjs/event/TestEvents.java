package me.alexjs.event;

import me.alexjs.event.mock.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TestEvents {

    @Test
    public void testMultipleEvents() throws ExecutionException, InterruptedException {

        EventPublisher publisher = new EventPublisher();
        Assertions.assertDoesNotThrow(() -> publisher.register(new MockListener1()));

        MockEvent1 event1 = new MockEvent1(new AtomicBoolean(false));
        publisher.publish(event1).get();

        MockEvent2 event2 = new MockEvent2(new AtomicInteger(0));
        publisher.publish(event2).get();

        Assertions.assertTrue(event1.getValue().get());
        Assertions.assertEquals(1, event2.getValue().get());

    }

    @Test
    public void testMultipleListeners() throws ExecutionException, InterruptedException {

        EventPublisher publisher = new EventPublisher();
        Assertions.assertDoesNotThrow(() -> publisher.register(new MockListener1()));
        Assertions.assertDoesNotThrow(() -> publisher.register(new MockListener1()));

        MockEvent2 event = new MockEvent2(new AtomicInteger(0));
        publisher.publish(event).get();

        Assertions.assertEquals(2, event.getValue().get());

    }

    @Test
    public void testPublishNull() {

        EventPublisher publisher = new EventPublisher();
        Assertions.assertDoesNotThrow(() -> publisher.register(new MockListener1()));

        Assertions.assertThrowsExactly(NullPointerException.class, () -> publisher.publish(null));

    }

    @Test
    public void testNoListeners() throws ExecutionException, InterruptedException {

        EventPublisher publisher = new EventPublisher();

        MockEvent2 event = new MockEvent2(new AtomicInteger(0));
        publisher.publish(event).get();

        Assertions.assertEquals(0, event.getValue().get());

    }

    @Test
    public void testNoSubscribers() throws ExecutionException, InterruptedException {

        EventPublisher publisher = new EventPublisher();

        Assertions.assertDoesNotThrow(() -> publisher.register(new IgnoreListener1()));
        Assertions.assertDoesNotThrow(() -> publisher.register(new IgnoreListener2()));
        Assertions.assertDoesNotThrow(() -> publisher.register(new IgnoreListener3()));

        MockEvent2 event = new MockEvent2(new AtomicInteger(0));
        publisher.publish(event).get();

        Assertions.assertEquals(0, event.getValue().get());

    }

    @Test
    public void testException() throws ExecutionException, InterruptedException {

        EventPublisher publisher = new EventPublisher();

        MockListener3 listener1 = new MockListener3();
        MockListener2 badListener = new MockListener2();
        MockListener3 listener2 = new MockListener3();

        Assertions.assertDoesNotThrow(() -> publisher.register(listener1));
        Assertions.assertDoesNotThrow(() -> publisher.register(badListener));
        Assertions.assertDoesNotThrow(() -> publisher.register(listener2));

        MockEvent2 event = new MockEvent2(new AtomicInteger(0));
        Assertions.assertDoesNotThrow(() -> publisher.publish(event)).get();

        Assertions.assertEquals(1, listener1.getValue());
        Assertions.assertEquals(1, listener2.getValue());

    }

    @Test
    public void testMultipleSubscribers() throws ExecutionException, InterruptedException {

        EventPublisher publisher = new EventPublisher();

        MockListener4 listener = new MockListener4();

        Assertions.assertDoesNotThrow(() -> publisher.register(listener));

        MockEvent2 event = new MockEvent2(new AtomicInteger(0));
        publisher.publish(event).get();

        Assertions.assertEquals(2, event.getValue().get());

    }

    @Test
    public void testBadSubscribers() throws ExecutionException, InterruptedException {

        EventPublisher publisher = new EventPublisher();

        BadListener1 listener1 = new BadListener1();
        BadListener2 listener2 = new BadListener2();
        BadListener3 listener3 = new BadListener3();
        BadListener4 listener4 = new BadListener4();
        BadListener5 listener5 = new BadListener5();

        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> publisher.register(listener1));
        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> publisher.register(listener2));
        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> publisher.register(listener3));
        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> publisher.register(listener4));
        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> publisher.register(listener5));

        MockEvent2 event = new MockEvent2(new AtomicInteger(0));
        publisher.publish(event).get();

        // None of the methods in the listeners should have touched this value.
        Assertions.assertEquals(0, event.getValue().get());

    }

    @Test
    public void testManyEvents() throws ExecutionException, InterruptedException {

        EventPublisher publisher = new EventPublisher();

        MockListener1 listener1 = new MockListener1();
        MockListener1 listener2 = new MockListener1();
        MockListener1 listener3 = new MockListener1();
        publisher.register(listener1);
        publisher.register(listener2);
        publisher.register(listener3);

        MockEvent2 event = new MockEvent2(new AtomicInteger(0));

        for (int i = 0; i < 1000; i++) {
            publisher.publish(event).get();
        }

        Assertions.assertEquals(3000, event.getValue().get());

    }

}
