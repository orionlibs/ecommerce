package de.hybris.platform.webservicescommons.conversion;

import com.google.common.collect.Sets;
import de.hybris.platform.converters.ConfigurablePopulator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class ConversionUtils
{
    public static <T, S, I> List<T> convert(S source, Collection<I> collection, BiFunction<I, S, T> function)
    {
        if(collection == null)
        {
            return new ArrayList<>();
        }
        return (List<T>)collection.stream().map(n -> function.apply(n, source)).filter(r -> (r != null)).collect(Collectors.toList());
    }


    public static <R, I> List<R> convert(Collection<I> collection, Function<I, R> function)
    {
        if(collection == null)
        {
            return new ArrayList<>();
        }
        return (List<R>)collection.stream().<R>map(function).filter(r -> (r != null)).collect(Collectors.toList());
    }


    @SafeVarargs
    public static <I, R, O> R convert(ConfigurablePopulator<I, R, O> populator, Supplier<R> supplier, I source, O... options)
    {
        return mapper(populator, supplier, options).apply(source);
    }


    @SafeVarargs
    public static <I, R, O> Function<I, R> mapper(ConfigurablePopulator<I, R, O> populator, Supplier<R> supplier, O... options)
    {
        return input -> {
            R result = supplier.get();
            populator.populate(input, result, Sets.newHashSet(options));
            return (Function)result;
        };
    }
}
