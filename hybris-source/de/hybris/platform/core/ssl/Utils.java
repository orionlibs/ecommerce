package de.hybris.platform.core.ssl;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

final class Utils
{
    static <E, T> E[] mergeArrays(Class<E> clazz, Function<T, E[]> arrayExtractor, T... elements)
    {
        LinkedHashSet<E> result = new LinkedHashSet<>();
        for(T e : elements)
        {
            if(e != null)
            {
                result.addAll(Arrays.asList(arrayExtractor.apply(e)));
            }
        }
        return (E[])result.toArray((Object[])Array.newInstance(clazz, result.size()));
    }


    static <E, T> E getFirstOrNull(Function<T, E> extractor, T... elements)
    {
        return Stream.<T>of(elements)
                        .filter(Objects::nonNull)
                        .<E>map(extractor)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null);
    }


    static <T, E> T getFirstInstanceOfOrNull(Class<T> expected, E... elements)
    {
        Objects.requireNonNull(expected);
        Objects.requireNonNull(expected);
        return Stream.<E>of(elements).filter(expected::isInstance).map(expected::cast).findFirst().orElse(null);
    }
}
