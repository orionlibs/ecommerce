package de.hybris.platform.ruleengineservices.rao.providers.impl;

import de.hybris.platform.ruleengineservices.enums.FactContextType;
import de.hybris.platform.ruleengineservices.rao.providers.RAOProvider;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FactContext
{
    private final FactContextType type;
    private final Collection facts;
    private final Map<Class, List<RAOProvider>> raoProviders;


    FactContext(FactContextType type, Map<Class<?>, List<RAOProvider>> raoProviders, Collection<?> facts)
    {
        this.type = type;
        this.facts = facts;
        this.raoProviders = raoProviders;
    }


    public FactContextType getType()
    {
        return this.type;
    }


    public Collection getFacts()
    {
        return this.facts;
    }


    public Set<RAOProvider> getProviders(Object obj)
    {
        Set<RAOProvider> result = new HashSet<>();
        for(Class clazz : getFactClasses(obj))
        {
            if(this.raoProviders.containsKey(clazz))
            {
                result.addAll(this.raoProviders.get(clazz));
            }
        }
        return result;
    }


    protected Set<Class> getFactClasses(Object obj)
    {
        Set<Class<?>> result = new HashSet<>();
        Class<?> clazz = obj.getClass();
        while(clazz != null)
        {
            result.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return result;
    }
}
