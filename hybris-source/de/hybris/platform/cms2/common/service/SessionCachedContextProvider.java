package de.hybris.platform.cms2.common.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SessionCachedContextProvider
{
    <T> void addItemToSetCache(String paramString, T paramT);


    <T> Set<T> getAllItemsFromSetCache(String paramString);


    <T> void removeItemFromSetCache(String paramString, T paramT);


    void clearSetCache(String paramString);


    <T> void addItemToListCache(String paramString, T paramT);


    void createEmptyListCache(String paramString);


    <T> List<T> getAllItemsFromListCache(String paramString);


    <T> void removeItemFromListCache(String paramString, T paramT);


    void clearListCache(String paramString);


    <K, V> void addItemToMapCache(String paramString, K paramK, V paramV);


    <K, V> Map<K, V> getAllItemsFromMapCache(String paramString);


    <K> void removeItemFromMapCache(String paramString, K paramK);


    void clearMapCache(String paramString);


    boolean hasCacheKey(String paramString);
}
