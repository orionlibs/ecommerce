package de.hybris.platform.servicelayer.interceptor;

import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public interface InterceptorContext
{
    @Deprecated(since = "ages", forRemoval = true)
    Object getSource(Object paramObject);


    @Deprecated(since = "ages", forRemoval = true)
    Set<Object> getAllRegisteredElements();


    Set<Object> getElementsRegisteredFor(PersistenceOperation paramPersistenceOperation);


    @Deprecated(since = "ages", forRemoval = true)
    void registerElement(Object paramObject1, Object paramObject2);


    void registerElement(Object paramObject);


    void registerElementFor(Object paramObject, PersistenceOperation paramPersistenceOperation);


    @Deprecated(since = "ages", forRemoval = true)
    boolean contains(Object paramObject);


    boolean contains(Object paramObject, PersistenceOperation paramPersistenceOperation);


    boolean isNew(Object paramObject);


    boolean exists(Object paramObject);


    boolean isModified(Object paramObject);


    boolean isModified(Object paramObject, String paramString);


    boolean isRemoved(Object paramObject);


    ModelService getModelService();


    Object getAttribute(String paramString);


    void setAttribute(String paramString, Object paramObject);


    Map<String, Set<Locale>> getDirtyAttributes(Object paramObject);


    TransientStorage getTransientStorage();
}
