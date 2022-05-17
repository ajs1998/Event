package me.alexjs.event.mock;

import java.util.concurrent.atomic.AtomicInteger;

public class MockEvent2Subclass extends MockEvent2 {

    public MockEvent2Subclass(AtomicInteger value) {
        super(value);
    }

}
