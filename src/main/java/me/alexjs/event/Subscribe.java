package me.alexjs.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a method in a listener as a subscriber for events published to an {@link EventPublisher}
 * <p>
 * A subscribing method:
 * <ul>
 *     <li>Must be {@code public}</li>
 *     <li>Must return {@code void}</li>
 *     <li>Must take exactly one parameter, and it must be a subclass of {@link Event}</li>
 * </ul>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Subscribe {

}
