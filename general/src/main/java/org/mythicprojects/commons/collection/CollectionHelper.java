package org.mythicprojects.commons.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CollectionHelper {

    private CollectionHelper() {
    }

    public static <K, V> @Unmodifiable Map<K, V> modify(@NotNull Map<K, V> sourceMap, @NotNull Function<Map<K, V>, Map<K, V>> mutableMapFactory, @NotNull Consumer<Map<K, V>> consumer) {
        Map<K, V> newMap = mutableMapFactory.apply(sourceMap);
        consumer.accept(newMap);
        return Collections.unmodifiableMap(newMap);
    }

    public static <K, V> @Unmodifiable Map<K, V> modify(@NotNull Map<K, V> sourceMap, @NotNull Consumer<Map<K, V>> consumer) {
        return modify(sourceMap, LinkedHashMap::new, consumer);
    }

    public static <K, V> @Unmodifiable Map<K, V> put(@NotNull Map<K, V> sourceMap, K key, V value) {
        return modify(sourceMap, m -> m.put(key, value));
    }

    public static <K, V> @Unmodifiable Map<K, V> putAll(@NotNull Map<K, V> sourceMap, @NotNull Map<K, V> toPut) {
        return modify(sourceMap, m -> m.putAll(toPut));
    }

    public static <K, V> @Unmodifiable Map<K, V> putAll(@NotNull Map<K, V> sourceMap, @NotNull Collection<V> toPut, @NotNull Function<V, K> keyMapper) {
        return putAll(sourceMap, toPut.stream().collect(Collectors.toMap(keyMapper, Function.identity())));
    }

    public static <K, V> @Unmodifiable Map<K, V> remove(@NotNull Map<K, V> sourceMap, K key) {
        return modify(sourceMap, m -> m.remove(key));
    }

    public static <V> @Unmodifiable List<V> modify(@NotNull List<V> sourceList, @NotNull Function<List<V>, List<V>> mutableListFactory, @NotNull Consumer<List<V>> consumer) {
        List<V> newList = mutableListFactory.apply(sourceList);
        consumer.accept(newList);
        return Collections.unmodifiableList(newList);
    }

    public static <V> @Unmodifiable List<V> modify(@NotNull List<V> sourceList, @NotNull Consumer<List<V>> consumer) {
        return modify(sourceList, ArrayList::new, consumer);
    }

    public static <V> @Unmodifiable List<V> add(@NotNull List<V> sourceList, V value) {
        return modify(sourceList, s -> s.add(value));
    }

    public static <V> @Unmodifiable List<V> addAll(@NotNull List<V> sourceList, @NotNull Collection<V> toAdd) {
        return modify(sourceList, s -> s.addAll(toAdd));
    }

    public static <V> @Unmodifiable List<V> remove(@NotNull List<V> sourceList, V value) {
        return modify(sourceList, s -> s.remove(value));
    }

    public static <V> @Unmodifiable List<V> remove(@NotNull List<V> sourceList, int index) {
        return modify(sourceList, s -> s.remove(index));
    }

    public static <V> @Unmodifiable List<V> removeAll(@NotNull List<V> sourceList, @NotNull Collection<V> toRemove) {
        return modify(sourceList, s -> s.removeAll(toRemove));
    }

    public static <V> @Unmodifiable Set<V> modify(@NotNull Set<V> sourceSet, @NotNull Function<Set<V>, Set<V>> mutableSetFactory, @NotNull Consumer<Set<V>> consumer) {
        Set<V> newSet = mutableSetFactory.apply(sourceSet);
        consumer.accept(newSet);
        return Collections.unmodifiableSet(newSet);
    }

    public static <V> @Unmodifiable Set<V> modify(@NotNull Set<V> sourceSet, @NotNull Consumer<Set<V>> consumer) {
        return modify(sourceSet, LinkedHashSet::new, consumer);
    }

    public static <V> @Unmodifiable Set<V> add(@NotNull Set<V> sourceSet, V value) {
        return modify(sourceSet, s -> s.add(value));
    }

    public static <V> @Unmodifiable Set<V> addAll(@NotNull Set<V> sourceSet, @NotNull Collection<V> toAdd) {
        return modify(sourceSet, s -> s.addAll(toAdd));
    }

    public static <V> @Unmodifiable Set<V> remove(@NotNull Set<V> sourceSet, V value) {
        return modify(sourceSet, s -> s.remove(value));
    }

    public static <V> @Unmodifiable Set<V> removeAll(@NotNull Set<V> sourceSet, @NotNull Collection<V> toRemove) {
        return modify(sourceSet, s -> s.removeAll(toRemove));
    }

}
