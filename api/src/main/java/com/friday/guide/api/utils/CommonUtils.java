package com.friday.guide.api.utils;


import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CommonUtils {

    public static boolean notEmpty(Object... objects) {
        for (Object o : objects) {
            if (o == null)
                return false;
            else if (o instanceof String) {
                String s = (String) o;
                if (org.apache.commons.lang3.StringUtils.isEmpty(s))
                    return false;
            }
        }
        return true;
    }

    public static <T, N> Stream<N> mapToStream(List<T> items, Function<? super T, N> mapper) {
        if (CollectionUtils.isEmpty(items)) return Stream.empty();
        return items.stream().map(mapper);
    }

    public static <T, N> List<N> map(List<T> items, Function<? super T, N> mapper) {
        if (CollectionUtils.isEmpty(items)) return Collections.emptyList();
        return mapToStream(items, mapper).collect(Collectors.toList());
    }

    public static <T> Stream<T> toStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T> List<T> filter(List<T> items, Predicate<T> filter) {
        if (CollectionUtils.isEmpty(items)) return Collections.emptyList();
        return items.stream().filter(filter).collect(Collectors.toList());
    }

    public static <K, V> Map<K, List<V>> grouping(List<V> items, Function<V, K> classifier) {
        if (CollectionUtils.isEmpty(items)) return Collections.emptyMap();
        return items.stream().collect(Collectors.groupingBy(classifier));
    }

    @SafeVarargs
    public static <C extends Comparable<? super C>> C max(C... values) {
        return Stream.of(values).max(Comparator.naturalOrder()).orElse(null);
    }

    @SafeVarargs
    public static <C extends Comparable<? super C>> C min(C... values) {
        return Stream.of(values).min(Comparator.naturalOrder()).orElse(null);
    }

    public static <T, R> R doOrNull(T target, Function<T, R> action) {
        return target == null ? null : action.apply(target);
    }

    public static <T> T findOne(T[] values, Predicate<T> filter) {
        return findOne(Arrays.asList(values), filter);
    }

    public static <T> T findOne(Collection<T> values, Predicate<T> filter) {
        if (!notEmpty(values, filter)) return null;
        return values.stream().filter(filter).findFirst().orElse(null);
    }

    @SafeVarargs
    public static <T> Set<T> set(T... args) {
        return fill(new HashSet<T>(), args);
    }

    @SafeVarargs
    public static <T> List<T> list(T... args) {
        return fill(new ArrayList<>(args.length), args);
    }

    @SafeVarargs
    public static <C extends Collection<T>, T> C fill(C collection, T... args) {
        for (T t : args) collection.add(t);
        return collection;
    }
}
