package de.hybris.platform.personalizationservices.action.property;

import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class ActionPropertySetter<T extends CxAbstractActionModel>
{
    Map<Class<T>, List<ActionPropertyProvider<T>>> map = new HashMap<>();


    protected void createMap(Collection<ActionPropertyProvider<T>> providers)
    {
        providers.forEach(this::addValue);
    }


    protected void addValue(ActionPropertyProvider<T> provider)
    {
        List<ActionPropertyProvider<T>> list = this.map.computeIfAbsent(provider.supports(), k -> getEmptyList());
        list.add(provider);
    }


    protected List<ActionPropertyProvider<T>> getEmptyList()
    {
        return new ArrayList<>();
    }


    @Autowired(required = false)
    public void setProviders(Collection<ActionPropertyProvider<T>> providers)
    {
        if(CollectionUtils.isNotEmpty(providers))
        {
            createMap(providers);
        }
    }


    public void setValues(CxAbstractActionModel action)
    {
        List<ActionPropertyProvider<T>> list = this.map.getOrDefault(action.getClass(), getEmptyList());
        list.forEach(p -> p.provideValues(action));
    }
}
