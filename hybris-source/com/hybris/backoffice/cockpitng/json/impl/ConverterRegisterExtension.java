/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.json.impl;

import com.hybris.backoffice.cockpitng.json.MutableConverterRegistry;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Required;

/**
 * Class defining implicitly which converter should be used for specific conversion
 */
public class ConverterRegisterExtension<S, D>
{
    private MutableConverterRegistry converterRegistry;
    private Class<? extends S> source;
    private Class<? extends D> destination;
    private Converter<S, D> converter;


    @PostConstruct
    public void extendRegistry()
    {
        getConverterRegistry().addConverter(getConverter(), getSource(), getDestination());
    }


    protected MutableConverterRegistry getConverterRegistry()
    {
        return converterRegistry;
    }


    @Required
    public void setConverterRegistry(final MutableConverterRegistry converterRegistry)
    {
        this.converterRegistry = converterRegistry;
    }


    protected Class<? extends S> getSource()
    {
        return source;
    }


    @Required
    public void setSource(final Class<? extends S> source)
    {
        this.source = source;
    }


    protected Class<? extends D> getDestination()
    {
        return destination;
    }


    @Required
    public void setDestination(final Class<? extends D> destination)
    {
        this.destination = destination;
    }


    protected Converter<S, D> getConverter()
    {
        return converter;
    }


    @Required
    public void setConverter(final Converter<S, D> converter)
    {
        this.converter = converter;
    }
}
