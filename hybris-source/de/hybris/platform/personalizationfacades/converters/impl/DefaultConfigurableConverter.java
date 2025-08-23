package de.hybris.platform.personalizationfacades.converters.impl;

import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.converters.ConfigurableConverter;
import de.hybris.platform.servicelayer.exceptions.ConfigurationException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

public class DefaultConfigurableConverter<SOURCE, TARGET, OPTION> implements ConfigurableConverter<SOURCE, TARGET, OPTION>, ConfigurablePopulator<SOURCE, TARGET, OPTION>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultConfigurableConverter.class);
    private Collection<OPTION> defaultOptions;
    private Class<TARGET> targetClass;
    private Map<OPTION, Populator<SOURCE, TARGET>> populators = new ConcurrentHashMap<>();


    public TARGET convert(SOURCE source, TARGET target, Collection<OPTION> options)
    {
        populate(source, target, options);
        return target;
    }


    public void populate(SOURCE source, TARGET target, Collection<OPTION> options)
    {
        Assert.notNull(source, "Parameter [source] must not be null");
        Assert.notNull(target, "Parameter [target] must not be null");
        Assert.notEmpty(options, "Parameter [options] must not be empty");
        if(!CollectionUtils.isEmpty(getPopulators()))
        {
            for(OPTION option : options)
            {
                Populator<SOURCE, TARGET> populator = getPopulators().get(option);
                if(populator == null)
                {
                    LOG.warn("No populator configured for option [{}]", option);
                    continue;
                }
                populator.populate(source, target);
            }
        }
    }


    public TARGET getTargetInstance(SOURCE source)
    {
        return getTargetInstance();
    }


    public TARGET getTargetInstance()
    {
        try
        {
            return this.targetClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch(IllegalAccessException | InstantiationException | NoSuchMethodException | java.lang.reflect.InvocationTargetException e)
        {
            throw new ConfigurationException("Instance of target class can't be created using default constructor", e);
        }
    }


    @Required
    public void setTargetClass(Class<TARGET> targetClass)
    {
        ServicesUtil.validateParameterNotNull(targetClass, "targetClass can't be null");
        this.targetClass = targetClass;
        getTargetInstance();
    }


    public Class<TARGET> getTargetClass()
    {
        return this.targetClass;
    }


    @Required
    public void setDefaultOptions(Collection<OPTION> defaultOptions)
    {
        Assert.notEmpty(defaultOptions, "Parameter [defaultOptions] must not be empty");
        this.defaultOptions = defaultOptions;
    }


    public Collection<OPTION> getDefaultOptions()
    {
        return this.defaultOptions;
    }


    protected Map<OPTION, Populator<SOURCE, TARGET>> getPopulators()
    {
        return this.populators;
    }


    public void setPopulators(Map<OPTION, Populator<SOURCE, TARGET>> populators)
    {
        this.populators = populators;
    }
}
