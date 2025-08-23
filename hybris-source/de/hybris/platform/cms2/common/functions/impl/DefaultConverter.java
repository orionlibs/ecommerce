package de.hybris.platform.cms2.common.functions.impl;

import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.converters.Populator;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultConverter<S, T> implements Converter<S, T>
{
    private List<Populator<S, T>> populators;
    private ObjectFactory<T> objectFactory;


    public T convert(S s)
    {
        if(s == null)
        {
            return null;
        }
        if(getPopulators() == null)
        {
            return null;
        }
        T t = (T)getObjectFactory().getObject();
        getPopulators()
                        .stream()
                        .filter(Objects::nonNull)
                        .forEach(populator -> populator.populate(s, t));
        return t;
    }


    protected List<Populator<S, T>> getPopulators()
    {
        return this.populators;
    }


    @Required
    public void setPopulators(List<Populator<S, T>> populators)
    {
        this.populators = populators;
    }


    public ObjectFactory<T> getObjectFactory()
    {
        return this.objectFactory;
    }


    @Required
    public void setObjectFactory(ObjectFactory<T> objectFactory)
    {
        this.objectFactory = objectFactory;
    }
}
