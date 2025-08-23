/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.json.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.json.ObjectMapperConfiguration;
import com.hybris.cockpitng.json.WidgetJSONMapper;
import java.io.IOException;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

/**
 * Default JSON &lt;&gt; Java object mapper, which iterates through all mapper configurations till it finds proper
 * {@link ObjectMapper} and uses it.
 */
public class DefaultJSONMapper implements WidgetJSONMapper
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultJSONMapper.class);
    private List<ObjectMapperConfiguration> configurations;


    protected String writeValue(final ObjectMapper objectMapper, final Object object) throws IOException
    {
        if(object != null)
        {
            return objectMapper.writeValueAsString(object);
        }
        else
        {
            return null;
        }
    }


    protected <T> T readValue(final ObjectMapper objectMapper, final String json, final Class<T> resultType) throws IOException
    {
        if(json != null)
        {
            return objectMapper.readValue(json, resultType);
        }
        else
        {
            return null;
        }
    }


    @Override
    public <T> T fromJSONString(final String json, final Class<T> resultType)
    {
        try
        {
            return readValue(getMapper(resultType), json, resultType);
        }
        catch(final IOException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e.getLocalizedMessage(), e);
            }
            return null;
        }
    }


    @Override
    public <T> T fromJSONString(final WidgetInstanceManager widgetInstanceManager, final String json, final Class<T> resultType)
    {
        try
        {
            final ObjectMapper mapper = getMapper(widgetInstanceManager, resultType);
            return readValue(mapper, json, resultType);
        }
        catch(final IOException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }


    @Override
    public String toJSONString(final Object object)
    {
        try
        {
            return writeValue(getMapper(object.getClass()), object);
        }
        catch(final IOException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }


    @Override
    public String toJSONString(final WidgetInstanceManager widgetInstanceManager, final Object object)
    {
        try
        {
            final ObjectMapper mapper = getMapper(widgetInstanceManager, object.getClass());
            return writeValue(mapper, object);
        }
        catch(final IOException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }


    protected ObjectMapper createMapper(final Class<?> objectType)
    {
        return new WidgetObjectMapper(this);
    }


    protected ObjectMapper configureMapper(final Class<?> objectType, final ObjectMapper mapper)
    {
        ObjectMapper result = mapper;
        for(final ObjectMapperConfiguration configuration : getConfigurations())
        {
            result = configuration.configureObjectMapper(objectType, result);
            Assert.notNull(result,
                            "Incorrect configuration implementation (result may not be null): " + ObjectUtils.toString(configuration));
        }
        return result;
    }


    protected ObjectMapper getMapper(final WidgetInstanceManager widgetInstanceManager, final Class<?> objectType)
    {
        final ObjectMapper mapper = getMapper(objectType);
        if(mapper instanceof WidgetObjectMapper)
        {
            ((WidgetObjectMapper)mapper).setWidgetInstanceManager(widgetInstanceManager);
        }
        return mapper;
    }


    protected ObjectMapper getMapper(final Class<?> objectType)
    {
        final ObjectMapper result = createMapper(objectType);
        return configureMapper(objectType, result);
    }


    public List<ObjectMapperConfiguration> getConfigurations()
    {
        return configurations;
    }


    @Required
    public void setConfigurations(final List<ObjectMapperConfiguration> configurations)
    {
        this.configurations = configurations;
    }
}
