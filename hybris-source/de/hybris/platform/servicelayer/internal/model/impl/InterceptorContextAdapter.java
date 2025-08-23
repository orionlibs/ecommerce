package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class InterceptorContextAdapter implements InterceptorContext
{
    private final InterceptorContext delegate;


    public InterceptorContextAdapter(InterceptorContext delegate)
    {
        this.delegate = delegate;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public Object getSource(Object model)
    {
        return this.delegate.getSource(model);
    }


    public boolean exists(Object model)
    {
        return this.delegate.exists(model);
    }


    public boolean isModified(Object model)
    {
        return this.delegate.isModified(model);
    }


    public Map<String, Set<Locale>> getDirtyAttributes(Object model)
    {
        return this.delegate.getDirtyAttributes(model);
    }


    public void registerElementFor(Object model, PersistenceOperation operation)
    {
        this.delegate.registerElementFor(model, operation);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public boolean contains(Object model)
    {
        return this.delegate.contains(model);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void registerElement(Object model, Object source)
    {
        this.delegate.registerElement(model, source);
    }


    public boolean isModified(Object model, String attribute)
    {
        return this.delegate.isModified(model, attribute);
    }


    public void setAttribute(String key, Object value)
    {
        this.delegate.setAttribute(key, value);
    }


    public boolean isRemoved(Object model)
    {
        return this.delegate.isRemoved(model);
    }


    public boolean isNew(Object model)
    {
        return this.delegate.isNew(model);
    }


    public void registerElement(Object model)
    {
        this.delegate.registerElement(model);
    }


    public Object getAttribute(String key)
    {
        return this.delegate.getAttribute(key);
    }


    public ModelService getModelService()
    {
        return this.delegate.getModelService();
    }


    public boolean contains(Object model, PersistenceOperation operation)
    {
        return this.delegate.contains(model, operation);
    }


    public Set<Object> getElementsRegisteredFor(PersistenceOperation operation)
    {
        return this.delegate.getElementsRegisteredFor(operation);
    }


    public InterceptorContext.TransientStorage getTransientStorage()
    {
        return this.delegate.getTransientStorage();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public Set<Object> getAllRegisteredElements()
    {
        return this.delegate.getAllRegisteredElements();
    }
}
