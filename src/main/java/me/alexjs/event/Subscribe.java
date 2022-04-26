package me.alexjs.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a method in a listener as a subscriber for events published to an {@link EventPublisher}.
 * <p>
 * A subscribing method must be {@code public}, it must return {@code void}, and it must take exactly one parameter.
 * Its only parameter should be a subclass of {@code event}.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Subscribe {

}
