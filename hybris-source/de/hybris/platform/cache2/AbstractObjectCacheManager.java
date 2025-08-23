package de.hybris.platform.cache2;

import org.apache.log4j.Logger;

public abstract class AbstractObjectCacheManager<T> implements ObjectCacheManager<T>
{
    private static final Logger log = Logger.getLogger(AbstractObjectCacheManager.class);
    private ObjectCache<T> objectCache;
    private boolean enableExpirationCheck = true;
    private boolean enableObjectAutoCreation = true;


    public void setObjectAutoCreation(boolean autocreate)
    {
        this.enableObjectAutoCreation = autocreate;
    }


    public void setExpirationCheck(boolean expirationCheck)
    {
        this.enableExpirationCheck = expirationCheck;
    }


    public ObjectCache<T> getCache()
    {
        return this.objectCache;
    }


    public void setCache(ObjectCache<T> objectCache)
    {
        if(this.objectCache != null)
        {
            this.objectCache.clear();
        }
        this.objectCache = objectCache;
    }


    public T fetch(ObjectKey<T> objectKey)
    {
        if(this.enableExpirationCheck && objectKey.getExpired())
        {
            handleExpiration(objectKey);
        }
        T result = handleRequest(objectKey);
        if(result == null && this.enableObjectAutoCreation)
        {
            result = handleCreation(objectKey);
        }
        return result;
    }


    public void invalidate(ObjectKey<T> objectKey)
    {
        handleInvalidation(objectKey);
    }


    protected void handleExpiration(ObjectKey<T> objectKey)
    {
        this.objectCache.removeObject(objectKey);
    }


    protected void handleInvalidation(ObjectKey<T> objectKey)
    {
        this.objectCache.removeObject(objectKey);
    }


    protected T handleRequest(ObjectKey<T> objectKey)
    {
        return (T)this.objectCache.getObject(objectKey);
    }


    protected T handleCreation(ObjectKey<T> objectKey)
    {
        T result = (T)objectKey.getObjectCreator().createObject();
        this.objectCache.addObject(objectKey, result);
        return result;
    }


    protected String getDebugKey(ObjectKey<T> objectKey)
    {
        return "(" + this.objectCache.getClass().getSimpleName() + "->" + objectKey.getSignature() + "):";
    }
}
