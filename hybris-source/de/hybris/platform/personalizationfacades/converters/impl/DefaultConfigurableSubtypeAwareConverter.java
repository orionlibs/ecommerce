package de.hybris.platform.personalizationfacades.converters.impl;

import de.hybris.platform.personalizationfacades.converters.ConfigurableConverter;
import de.hybris.platform.personalizationfacades.converters.ConfigurableSubtypeConverter;
import de.hybris.platform.personalizationfacades.exceptions.TypeConflictException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class DefaultConfigurableSubtypeAwareConverter<SOURCE, TARGET, OPTION> implements ConfigurableConverter<SOURCE, TARGET, OPTION>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultConfigurableSubtypeAwareConverter.class);
    private Map<Class<?>, ConfigurableSubtypeConverter<SOURCE, TARGET, OPTION>> populatorMap = new ConcurrentHashMap<>();
    private Collection<ConfigurableSubtypeConverter<SOURCE, TARGET, OPTION>> allSubtypeConverters;
    private Collection<OPTION> defaultOptions;
    private Class<?> markerClass;


    @PostConstruct
    public void init()
    {
        if(this.allSubtypeConverters == null)
        {
            LOG.info("No ConfigurableSubtypeConverters found in spring context.");
            return;
        }
        LOG.debug("{} ConfigurableSubtypeConverters found in spring context.", Integer.valueOf(this.allSubtypeConverters.size()));
        this
                        .populatorMap = (Map<Class<?>, ConfigurableSubtypeConverter<SOURCE, TARGET, OPTION>>)this.allSubtypeConverters.stream().filter(this::hasValidMarkerClass).collect(Collectors.toConcurrentMap(ConfigurableSubtypeConverter::getSourceClass, Function.identity()));
        LOG.info("{} ConfigurableSubtypeConverters found for type {}", Integer.valueOf(this.populatorMap.size()), this.markerClass.getName());
        this.allSubtypeConverters = null;
    }


    protected boolean hasValidMarkerClass(ConfigurableSubtypeConverter csc)
    {
        return (this.markerClass == csc.getMarkerClass());
    }


    public TARGET convert(SOURCE source, TARGET target, Collection<OPTION> options)
    {
        Assert.notNull(source, "Parameter [source] must not be null");
        Assert.notNull(target, "Parameter [target] must not be null");
        Assert.notEmpty(options, "Parameter [options] must not be empty");
        ConfigurableSubtypeConverter<SOURCE, TARGET, OPTION> converter = findConverter(source);
        if(!converter.getTargetClass().isAssignableFrom(target.getClass()))
        {
            throw new TypeConflictException("Given action data type and existing action type do not match");
        }
        converter.convert(source, target, options);
        return target;
    }


    public TARGET getTargetInstance(SOURCE source)
    {
        return (TARGET)findConverter(source).getTargetInstance(source);
    }


    protected ConfigurableSubtypeConverter<SOURCE, TARGET, OPTION> findConverter(SOURCE source)
    {
        Assert.notNull(source, "Parameter [source] must not be null");
        ConfigurableSubtypeConverter<SOURCE, TARGET, OPTION> converter = this.populatorMap.get(source.getClass());
        if(converter == null)
        {
            throw new IllegalArgumentException("Illegal source class " + source.getClass());
        }
        return converter;
    }


    public Collection<OPTION> getDefaultOptions()
    {
        return this.defaultOptions;
    }


    @Required
    public void setDefaultOptions(Collection<OPTION> defaultOptions)
    {
        Assert.notEmpty(defaultOptions, "Parameter [defaultOptions] must not be empty");
        this.defaultOptions = defaultOptions;
    }


    @Required
    public void setMarkerClass(Class<?> markerClass)
    {
        this.markerClass = markerClass;
    }


    protected Class<?> getMarkerClass()
    {
        return this.markerClass;
    }


    @Autowired(required = false)
    public void setAllSubtypeConverters(Collection<ConfigurableSubtypeConverter<SOURCE, TARGET, OPTION>> allSubtypeConverters)
    {
        this.allSubtypeConverters = allSubtypeConverters;
    }
}
