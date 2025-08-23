package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.service.AbstractService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public abstract class AbstractModelService extends AbstractService implements ModelService
{
    private static final Logger LOG = Logger.getLogger(AbstractModelService.class);


    public void saveAll(Object... models) throws ModelSavingException
    {
        saveAll(Arrays.asList(models));
    }


    public <T> T toModelLayer(Object persistentValue)
    {
        T ret;
        if(persistentValue == null)
        {
            ret = null;
        }
        else if(persistentValue instanceof Collection)
        {
            boolean isSet = persistentValue instanceof Set;
            Collection orig = (Collection)persistentValue;
            if(orig.isEmpty())
            {
                ret = isSet ? (T)Collections.emptySet() : (T)Collections.emptyList();
            }
            else
            {
                int s = orig.size();
                Collection wrapped = isSet ? new LinkedHashSet(s) : new ArrayList(s);
                for(Object o : orig)
                {
                    wrapped.add(toModelLayer(o));
                }
                ret = isSet ? (T)Collections.unmodifiableSet((Set)wrapped) : (T)Collections.unmodifiableList((List)wrapped);
            }
        }
        else if(persistentValue instanceof Map)
        {
            Map<?, ?> orig = (Map<?, ?>)persistentValue;
            if(orig.isEmpty())
            {
                Map<?, ?> map = Collections.emptyMap();
            }
            else
            {
                Map<Object, Object> wrapped = new LinkedHashMap<>(orig.size());
                for(Map.Entry<?, ?> e : orig.entrySet())
                {
                    wrapped.put(toModelLayer(e.getKey()), toModelLayer(e.getValue()));
                }
                Map<Object, Object> map1 = Collections.unmodifiableMap(wrapped);
            }
        }
        else
        {
            Object model = getModelForPersistentValue(persistentValue);
            if(model == null && persistentValue instanceof de.hybris.platform.util.ItemPropertyValue)
            {
                return null;
            }
            ret = (model != null) ? (T)model : (T)persistentValue;
        }
        return ret;
    }


    protected abstract Object getModelForPersistentValue(Object paramObject);


    public <T> T toPersistenceLayer(Object modelValue)
    {
        T ret;
        if(modelValue == null)
        {
            ret = null;
        }
        else if(modelValue instanceof Collection)
        {
            Collection orig = (Collection)modelValue;
            if(orig.isEmpty())
            {
                Collection collection = orig;
            }
            else
            {
                int s = orig.size();
                Collection wrapped = (modelValue instanceof Set) ? new LinkedHashSet(s) : new ArrayList(s);
                for(Object o : orig)
                {
                    wrapped.add(toPersistenceLayer(o));
                }
                Collection collection1 = wrapped;
            }
        }
        else if(modelValue instanceof Map)
        {
            Map<?, ?> orig = (Map<?, ?>)modelValue;
            if(orig.isEmpty())
            {
                Map<?, ?> map = orig;
            }
            else
            {
                Map<Object, Object> wrapped = new LinkedHashMap<>(orig.size());
                for(Map.Entry<?, ?> e : orig.entrySet())
                {
                    wrapped.put(toPersistenceLayer(e.getKey()), toPersistenceLayer(e.getValue()));
                }
                Map<Object, Object> map1 = wrapped;
            }
        }
        else
        {
            Object model = getPersistentValueForModel(modelValue);
            ret = (model != null) ? (T)model : (T)modelValue;
        }
        return ret;
    }


    protected abstract Object getPersistentValueForModel(Object paramObject);


    public <T extends Collection> T getAllSources(Collection<? extends Object> models, T result)
    {
        for(Object model : models)
        {
            result.add(getSource(model));
        }
        return result;
    }


    public <T extends Collection> T getAll(Collection<? extends Object> sources, T result)
    {
        for(Object source : sources)
        {
            result.add(get(source));
        }
        return result;
    }


    public <T extends Collection> T getAll(Collection<? extends Object> sources, T result, String conversionType)
    {
        for(Object source : sources)
        {
            result.add(get(source, conversionType));
        }
        return result;
    }
}
