package org.mythicprojects.commons.util;

import java.util.Map;
import java.util.stream.Collectors;

public final class PrimitiveHelper {

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER;
    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE;

    static {
        PRIMITIVE_TO_WRAPPER = Map.of(
                boolean.class, Boolean.class,
                byte.class, Byte.class,
                char.class, Character.class,
                short.class, Short.class,
                int.class, Integer.class,
                long.class, Long.class,
                float.class, Float.class,
                double.class, Double.class,
                void.class, Void.class
        );
        WRAPPER_TO_PRIMITIVE = PRIMITIVE_TO_WRAPPER.entrySet()
                .stream()
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    private PrimitiveHelper() {
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> convertPrimitiveToWrapper(Class<T> clazz) {
        return (Class<T>) PRIMITIVE_TO_WRAPPER.getOrDefault(clazz, clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> convertWrapperToPrimitive(Class<T> clazz) {
        return (Class<T>) WRAPPER_TO_PRIMITIVE.getOrDefault(clazz, clazz);
    }

}
