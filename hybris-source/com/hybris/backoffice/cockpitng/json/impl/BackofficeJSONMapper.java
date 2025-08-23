/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.json.impl;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.hybris.backoffice.cockpitng.json.ModelDataMapper;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.json.impl.DefaultJSONMapper;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Extension of default JSOM mapper (see {@link DefaultJSONMapper}) that adds functionality of data mapping using
 * converters.
 * <P>
 * Mapper uses {@link ModelDataMapper} to map platform model to DTO before JSON-ing and another way round.
 */
public class BackofficeJSONMapper extends DefaultJSONMapper
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BackofficeJSONMapper.class);
    private static final String ATTRIBUTE_PK = "pk";
    private static final Object PK_VALUE_KEY = "longValue";
    private ObjectFacade objectFacade;
    private ModelDataMapper modelDataMapper;


    @Override
    public String toJSONString(final WidgetInstanceManager widgetInstanceManager, final Object object)
    {
        if(object != null)
        {
            String result = super.toJSONString(widgetInstanceManager, getModelDataMapper().map(widgetInstanceManager, object));
            if(object instanceof AbstractItemModel)
            {
                final Map<String, Object> map = super.fromJSONString(result, Map.class);
                if(!map.containsKey(ATTRIBUTE_PK) && ((AbstractItemModel)object).getPk() != null)
                {
                    map.put(ATTRIBUTE_PK, ((AbstractItemModel)object).getPk().getLongValueAsString());
                    result = super.toJSONString(map);
                }
            }
            return result;
        }
        else
        {
            return null;
        }
    }


    @Override
    public <T> T fromJSONString(final WidgetInstanceManager widgetInstanceManager, final String json, final Class<T> resultType)
    {
        final Class<?> dtoClass = getModelDataMapper().getSourceType(widgetInstanceManager, resultType);
        T result = null;
        if(dtoClass == null && AbstractItemModel.class.isAssignableFrom(resultType))
        {
            final Map<String, Object> map = fromJSONString(json, Map.class);
            Object pkObject = map.remove(ATTRIBUTE_PK);
            if(pkObject != null)
            {
                final String pk;
                if(pkObject instanceof Map)
                {
                    pkObject = ((Map)pkObject).get(PK_VALUE_KEY);
                }
                pk = Objects.toString(pkObject, "");
                try
                {
                    final Object target = getObjectFacade().load(pk);
                    getModelDataMapper().map(widgetInstanceManager, target, map);
                    result = (T)target;
                }
                catch(final ObjectNotFoundException e)
                {
                    LOGGER.error(e.getLocalizedMessage(), e);
                }
            }
            if(result == null)
            {
                result = super.fromJSONString(widgetInstanceManager, json, resultType);
            }
        }
        else if(dtoClass != null)
        {
            Object dto;
            try
            {
                dto = readValue(getMapper(widgetInstanceManager, resultType), json, dtoClass);
            }
            catch(final UnrecognizedPropertyException e)
            {
                if(LOGGER.isDebugEnabled())
                {
                    LOGGER.debug(String.format("Unrecognized property: %s.", e.getPropertyName()), e);
                }
                final Map<String, Object> map = fromJSONString(json, Map.class);
                map.remove(ATTRIBUTE_PK);
                dto = super.fromJSONString(widgetInstanceManager, toJSONString(map), dtoClass);
            }
            catch(final IOException ex)
            {
                LOGGER.error(ex.getLocalizedMessage(), ex);
                dto = null;
            }
            if(!Objects.equals(dtoClass, resultType))
            {
                result = getModelDataMapper().map(widgetInstanceManager, dto);
            }
        }
        else
        {
            result = super.fromJSONString(widgetInstanceManager, json, resultType);
        }
        return result;
    }


    protected ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    @Required
    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    protected ModelDataMapper getModelDataMapper()
    {
        return modelDataMapper;
    }


    @Required
    public void setModelDataMapper(final ModelDataMapper modelDataMapper)
    {
        this.modelDataMapper = modelDataMapper;
    }
}
