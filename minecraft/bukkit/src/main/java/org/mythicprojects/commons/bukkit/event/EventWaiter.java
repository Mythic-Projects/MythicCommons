package org.mythicprojects.commons.bukkit.event;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mythicprojects.commons.util.Pair;
import org.mythicprojects.commons.util.Validate;

public class EventWaiter {

    private static final Listener EMPTY_LISTENER = new Listener() {
    };

    private final Plugin plugin;
    private final Map<EventIdentifier<?>, Set<WaitingEvent>> waitingEvents = new ConcurrentHashMap<>();

    public EventWaiter(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Register event in bukkit's plugin manager, to allow the EventWaiter to handle them
     *
     * @param events The events to register
     */
    @SafeVarargs
    public final void register(@NotNull Class<? extends Event>... events) {
        Validate.notNull(events, "Events cannot be null");
        if (events.length == 0) {
            return;
        }

        PluginManager pluginManager = Bukkit.getPluginManager();
        for (Class<? extends Event> eventClass : events) {
            for (EventPriority priority : EventPriority.values()) {
                pluginManager.registerEvent(
                        eventClass,
                        EMPTY_LISTENER,
                        priority,
                        (listener, event) -> this.handleEvent(event, priority),
                        this.plugin
                );
            }
        }
    }

    /**
     * Register event to wait for
     *
     * @param eventClass    The event class
     * @param priority      The event priority (null for normal)
     * @param condition     The condition that must be met to execute the action
     * @param action        The action to execute
     * @param timeout       The timeout in ticks (20 ticks = 1 second)
     * @param timeoutAction The action to execute when the timeout is reached
     * @param <T>           The event type
     */
    public <T extends Event> void waitForEvent(
            @NotNull Class<T> eventClass,
            @Nullable EventPriority priority,
            @NotNull Predicate<T> condition,
            @NotNull Consumer<T> action,
            long timeout,
            @Nullable Runnable timeoutAction
    ) {
        Validate.notNull(eventClass, "Event class cannot be null");
        Validate.notNull(condition, "Condition cannot be null");
        Validate.notNull(action, "Action cannot be null");

        if (priority == null) {
            priority = EventPriority.NORMAL;
        }

        EventIdentifier<T> key = new EventIdentifier<>(eventClass, priority);
        Set<WaitingEvent> waitingEvents = this.waitingEvents.computeIfAbsent(key, k -> ConcurrentHashMap.newKeySet());
        WaitingEvent<T> waitingEvent = new WaitingEvent<>(condition, action);
        waitingEvents.add(waitingEvent);

        if (timeout <= 0 && timeoutAction == null) {
            return;
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, () -> {
            if (!waitingEvents.remove(waitingEvent)) {
                return;
            }
            timeoutAction.run();
        }, timeout);
    }

    /**
     * Register event to wait for
     *
     * @param eventClass    The event class
     * @param condition     The condition that must be met to execute the action
     * @param action        The action to execute
     * @param timeout       The timeout in ticks (20 ticks = 1 second)
     * @param timeoutAction The action to execute when the timeout is reached
     * @param <T>           The event type
     */
    public <T extends Event> void waitForEvent(
            @NotNull Class<T> eventClass,
            @NotNull Predicate<T> condition,
            @NotNull Consumer<T> action,
            long timeout,
            @Nullable Runnable timeoutAction
    ) {
        this.waitForEvent(eventClass, null, condition, action, timeout, timeoutAction);
    }

    public <T extends Event> void waitForEvent(
            @NotNull Class<T> eventClass,
            @NotNull Predicate<T> condition,
            @NotNull Consumer<T> action,
            long timeout
    ) {
        this.waitForEvent(eventClass, null, condition, action, timeout, null);
    }

    /**
     * Register event to wait for
     *
     * @param eventClass The event class
     * @param condition  The condition that must be met to execute the action
     * @param action     The action to execute
     * @param <T>        The event type
     */
    public <T extends Event> void waitForEvent(
            @NotNull Class<T> eventClass,
            @NotNull Predicate<T> condition,
            @NotNull Consumer<T> action
    ) {
        this.waitForEvent(eventClass, null, condition, action, -1, null);
    }

    /**
     * Checks if the event is waiting for and executes the action if the condition is met
     *
     * @param event    The event
     * @param priority The event priority
     * @param <T>      The event type
     */
    private <T extends Event> void handleEvent(@NotNull T event, @NotNull EventPriority priority) {
        EventIdentifier<?> key = new EventIdentifier<>(event.getClass(), priority);
        Set<WaitingEvent> waitingEvents = this.waitingEvents.get(key);
        if (waitingEvents == null) {
            return;
        }
        waitingEvents.removeIf(waitingEvent -> waitingEvent.handle(event));
    }

    private static class WaitingEvent<T extends Event> {

        private final Predicate<T> condition;
        private final Consumer<T> action;

        private WaitingEvent(@NotNull Predicate<T> condition, @NotNull Consumer<T> action) {
            this.condition = condition;
            this.action = action;
        }

        public boolean handle(@NotNull T event) {
            if (!this.condition.test(event)) {
                return false;
            }
            this.action.accept(event);
            return true;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || this.getClass() != obj.getClass()) {
                return false;
            }
            WaitingEvent<?> that = (WaitingEvent<?>) obj;
            return Objects.equals(this.condition, that.condition) && Objects.equals(this.action, that.action);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.condition, this.action);
        }

    }

    private static class EventIdentifier<T extends Event> extends Pair<Class<T>, EventPriority> {

        public EventIdentifier(@NotNull Class<T> first, @NotNull EventPriority second) {
            super(first, second);
        }

    }


}
