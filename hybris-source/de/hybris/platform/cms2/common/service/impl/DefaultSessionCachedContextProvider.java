package de.hybris.platform.cms2.common.service.impl;

import de.hybris.platform.cms2.common.service.SessionCachedContextProvider;
import de.hybris.platform.cms2.exceptions.InvalidTypeException;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSessionCachedContextProvider implements SessionCachedContextProvider
{
    private SessionService sessionService;


    public <T> void addItemToSetCache(String cacheKey, T value)
    {
        Set<T> cachedSet = getOrCreateSetCollectionInCache(cacheKey);
        cachedSet.add(value);
    }


    public <T> Set<T> getAllItemsFromSetCache(String cacheKey)
    {
        return getOrCreateSetCollectionInCache(cacheKey);
    }


    public <T> void removeItemFromSetCache(String cacheKey, T value)
    {
        Set cachedSet = getOrCreateSetCollectionInCache(cacheKey);
        cachedSet.remove(value);
    }


    public void clearSetCache(String cacheKey)
    {
        Set cachedSet = getOrCreateSetCollectionInCache(cacheKey);
        cachedSet.clear();
    }


    public <T> void addItemToListCache(String cacheKey, T value)
    {
        List<T> cachedList = getOrCreateListCollectionInCache(cacheKey);
        cachedList.add(value);
    }


    public void createEmptyListCache(String cacheKey)
    {
        getOrCreateListCollectionInCache(cacheKey);
    }


    public <T> List<T> getAllItemsFromListCache(String cacheKey)
    {
        return getOrCreateListCollectionInCache(cacheKey);
    }


    public <T> void removeItemFromListCache(String cacheKey, T value)
    {
        List<T> cachedList = getOrCreateListCollectionInCache(cacheKey);
        cachedList.remove(value);
    }


    public void clearListCache(String cacheKey)
    {
        List cachedList = getOrCreateListCollectionInCache(cacheKey);
        cachedList.clear();
    }


    public <K, V> void addItemToMapCache(String cacheKey, K key, V value)
    {
        Map<K, V> cachedMap = getOrCreateCollectionInCache(cacheKey, HashMap.class);
        cachedMap.put(key, value);
    }


    public <K, V> Map<K, V> getAllItemsFromMapCache(String cacheKey)
    {
        return getOrCreateMapCollectionInCache(cacheKey);
    }


    public <K> void removeItemFromMapCache(String cacheKey, K key)
    {
        Map<K, Object> cachedMap = getOrCreateMapCollectionInCache(cacheKey);
        cachedMap.remove(key);
    }


    public void clearMapCache(String cacheKey)
    {
        Map cachedMap = getOrCreateMapCollectionInCache(cacheKey);
        cachedMap.clear();
    }


    public boolean hasCacheKey(String cacheKey)
    {
        return (getSessionService().getAttribute(cacheKey) != null);
    }


    protected <T> T getOrCreateSetCollectionInCache(String cacheKey)
    {
        return getOrCreateCollectionInCache(cacheKey, HashSet.class);
    }


    protected <T> T getOrCreateListCollectionInCache(String cacheKey)
    {
        return getOrCreateCollectionInCache(cacheKey, ArrayList.class);
    }


    protected <T> T getOrCreateMapCollectionInCache(String cacheKey)
    {
        return getOrCreateCollectionInCache(cacheKey, HashMap.class);
    }


    protected <T> T getOrCreateCollectionInCache(String cacheKey, Class classType)
    {
        AtomicReference cachedCollection = (AtomicReference)getSessionService().getAttribute(cacheKey);
        if(cachedCollection == null)
        {
            try
            {
                cachedCollection = new AtomicReference(classType.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
            }
            catch(ReflectiveOperationException e)
            {
                throw new InvalidTypeException("Failed to instantiate object of type " + classType, e);
            }
            getSessionService().setAttribute(cacheKey, cachedCollection);
        }
        return getWrappedObject(cachedCollection);
    }


    protected <T> T getWrappedObject(Object rawValue)
    {
        if(rawValue instanceof AtomicReference)
        {
            AtomicReference<T> originalValue = (AtomicReference<T>)rawValue;
            return originalValue.get();
        }
        throw new IllegalStateException("Session object should hold a reference of AtomicReference object.");
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
