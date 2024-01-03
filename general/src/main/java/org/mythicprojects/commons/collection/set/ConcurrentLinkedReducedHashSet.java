package org.mythicprojects.commons.collection.set;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A concurrent set that maintains insertion order.
 * <p>
 * Based on <a href="https://www.javaspecialists.eu/archive/Issue296-Concurrent-LinkedHashSet.html">this article</a>
 */
@ApiStatus.Experimental
public class ConcurrentLinkedReducedHashSet<E> extends AbstractSet<E> implements Iterable<E> {

    /**
     * Contains a mapping from our element to its insertionOrder.
     */
    private final Map<E, InsertionOrder<E>> elements = new ConcurrentHashMap<>();
    /**
     * The insertion order maintained in a ConcurrentSkipListSet,
     * so that we iterate in the correct order.
     */
    private final Set<InsertionOrder<E>> elementsOrderedByInsertion = new ConcurrentSkipListSet<>(Comparator.comparingLong(InsertionOrder::order));
    /**
     * AtomicLong for generating the next insertion order.
     */
    private final AtomicLong nextOrder = new AtomicLong();

    @Override
    public int size() {
        return this.elements.size();
    }

    @Override
    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.elements.containsKey(o);
    }

    @Override
    public boolean add(E e) {
        var added = new AtomicBoolean(false);
        this.elements.computeIfAbsent(e, key -> {
            var holder = new InsertionOrder<>(e, this.nextOrder.getAndIncrement());
            this.elementsOrderedByInsertion.add(holder);
            added.set(true);
            return holder;
        });
        return added.get();
    }

    @Override
    public boolean remove(Object o) throws ClassCastException {
        var removed = new AtomicBoolean(false);
        this.elements.computeIfPresent((E) o, (key, holder) -> {
            this.elementsOrderedByInsertion.remove(holder);
            removed.set(true);
            return null; // will remove the entry
        });
        return removed.get();
    }

    @Override
    public void clear() {
        // slow, but ensures we remove all entries in both collections
        this.stream().forEach(this::remove);
    }

    @Override
    public Object @NotNull [] toArray() {
        return this.stream().toList().toArray();
    }

    @Override
    public <T> T @NotNull [] toArray(T[] a) {
        return this.stream().toList().toArray(a);
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return this.stream().iterator();
    }

    @Override
    public @NotNull Spliterator<E> spliterator() {
        return this.stream().spliterator();
    }

    @Override
    public @NotNull Stream<E> stream() {
        return this.elementsOrderedByInsertion.stream().map(InsertionOrder::value);
    }

    @Override
    public String toString() {
        return this.stream().map(String::valueOf).collect(Collectors.joining(", ", "[", "]"));
    }

    /**
     * A tuple holding our value and its insertion order.
     */
    private record InsertionOrder<T>(T value, long order) {}

}
