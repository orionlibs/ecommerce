package de.hybris.platform.servicelayer.internal.model.impl.wrapper;

import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WrapperRegistry
{
    private final Map<ModelAndOperation, ModelWrapper> wrappers = new HashMap<>();
    private final ModelWrapperContext wrapperContext;


    public WrapperRegistry(ModelWrapperContext wrapperContext)
    {
        this.wrapperContext = wrapperContext;
    }


    public ModelWrapper createWrapper(Object model, PersistenceOperation operation)
    {
        ModelAndOperation key = new ModelAndOperation(model, operation);
        if(this.wrappers.containsKey(key))
        {
            return this.wrappers.get(key);
        }
        ModelWrapper wrapper = new ModelWrapper(model, operation, this.wrapperContext);
        this.wrappers.put(key, wrapper);
        return wrapper;
    }


    public Collection<ModelWrapper> wrap(Collection<? extends Object> models, PersistenceOperation persistenceOperation)
    {
        Collection<ModelWrapper> result = new ArrayList<>(models.size());
        for(Object model : models)
        {
            result.add(createWrapper(model, persistenceOperation));
        }
        return result;
    }


    public boolean containsWrapperFor(Object model, PersistenceOperation operation)
    {
        ModelAndOperation key = new ModelAndOperation(model, operation);
        return this.wrappers.containsKey(key);
    }


    public ModelWrapper getWrapperFor(Object model, PersistenceOperation operation)
    {
        ModelAndOperation key = new ModelAndOperation(model, operation);
        return this.wrappers.get(key);
    }
}
