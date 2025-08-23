package de.hybris.platform.adaptivesearch.util.impl;

import de.hybris.platform.adaptivesearch.AsRuntimeException;
import de.hybris.platform.adaptivesearch.util.ContextAwareConverter;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsContextAwareConverter<S, T, C> implements ContextAwareConverter<S, T, C>
{
    private Class<T> targetClass;
    private List<ContextAwarePopulator<S, T, C>> populators;


    public T convert(S source, C context)
    {
        T target = createTarget();
        if(CollectionUtils.isNotEmpty(this.populators))
        {
            for(ContextAwarePopulator<S, T, C> populator : this.populators)
            {
                populator.populate(source, target, context);
            }
        }
        return target;
    }


    protected T createTarget()
    {
        try
        {
            return this.targetClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch(InstantiationException | IllegalAccessException | NoSuchMethodException | java.lang.reflect.InvocationTargetException e)
        {
            throw new AsRuntimeException(e);
        }
    }


    public Class<T> getTargetClass()
    {
        return this.targetClass;
    }


    public void setTargetClass(Class<T> targetClass)
    {
        this.targetClass = targetClass;
    }


    public List<ContextAwarePopulator<S, T, C>> getPopulators()
    {
        return this.populators;
    }


    @Required
    public void setPopulators(List<ContextAwarePopulator<S, T, C>> populators)
    {
        this.populators = populators;
    }
}
