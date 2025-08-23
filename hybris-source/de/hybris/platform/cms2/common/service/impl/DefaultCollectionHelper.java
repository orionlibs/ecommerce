package de.hybris.platform.cms2.common.service.impl;

import de.hybris.platform.cms2.common.service.CollectionHelper;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultCollectionHelper implements CollectionHelper
{
    public Map<String, Object> mergeMaps(Map<String, Object> firstMap, Map<String, Object> secondMap, BinaryOperator<Object> mergeFunction)
    {
        return mergeMaps(Arrays.asList((Map<String, Object>[])new Map[] {firstMap, secondMap}, ), mergeFunction);
    }


    public Map<String, Object> mergeMaps(List<Map<String, Object>> maps)
    {
        return mergeMaps(maps, defaultDeepMergeFunction());
    }


    public Map<String, Object> mergeMaps(List<Map<String, Object>> maps, BinaryOperator<Object> mergeFunction)
    {
        return (Map<String, Object>)maps.stream()
                        .map(Map::entrySet)
                        .flatMap(Collection::stream)
                        .collect(
                                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, mergeFunction));
    }


    public BinaryOperator<Object> defaultDeepMergeFunction()
    {
        return (first, second) ->
                        (isCollection(first) && isCollection(second)) ? concatCollections((Collection<Object>)first, (Collection<Object>)second) : (
                                        (isMap(first) && isMap(second)) ? mergeMaps(Arrays.asList((Map<String, Object>[])new Map[] {(Map)first, (Map)second}, ), defaultDeepMergeFunction()) : second);
    }


    protected boolean isCollection(Object obj)
    {
        return obj instanceof Collection;
    }


    protected boolean isMap(Object obj)
    {
        return obj instanceof Map;
    }


    protected List<Object> concatCollections(Collection<Object> first, Collection<Object> second)
    {
        return (List<Object>)Stream.concat(first.stream(), second.stream()).collect(Collectors.toList());
    }
}
