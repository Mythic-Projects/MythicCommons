package org.mythicprojects.commons.collection;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public final class MapHelper {

    private MapHelper() {
    }

    public static <K extends Comparable<? super K>, V> @NotNull Map<K, V> sortByKey(@NotNull Map<K, V> map, boolean descending) {
        Stream<Entry<K, V>> stream = map.entrySet().stream();

        if (descending) {
            stream = stream.sorted(Entry.comparingByKey(Collections.reverseOrder()));
        } else {
            stream = stream.sorted(Entry.comparingByKey());
        }

        return stream.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (firstEntry, secondEntry) -> secondEntry, LinkedHashMap::new));
    }

    public static <K extends Comparable<? super K>, V> @NotNull Map<K, V> sortByKey(@NotNull Map<K, V> map) {
        return sortByKey(map, true);
    }

    public static <K, V extends Comparable<? super V>> @NotNull Map<K, V> sortByValue(@NotNull Map<K, V> map, boolean descending) {
        Stream<Entry<K, V>> stream = map.entrySet().stream();

        if (descending) {
            stream = stream.sorted(Entry.comparingByValue(Collections.reverseOrder()));
        } else {
            stream = stream.sorted(Entry.comparingByValue());
        }

        return stream.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (firstEntry, secondEntry) -> firstEntry, LinkedHashMap::new));
    }

    public static <K, V extends Comparable<? super V>> @NotNull Map<K, V> sortByValue(@NotNull Map<K, V> map) {
        return sortByValue(map, true);
    }

}
