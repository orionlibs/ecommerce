/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.core.user.impl;

import com.hybris.cockpitng.core.user.CockpitUserPropertiesService;
import com.hybris.cockpitng.type.ObjectValueService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Provides cockpit user properties of an on-premise user.
 */
public class OnPremiseCockpitUserPropertiesService implements CockpitUserPropertiesService
{
    private static final Logger LOG = LoggerFactory.getLogger(OnPremiseCockpitUserPropertiesService.class);
    private UserService userService;
    private ObjectValueService objectValueService;
    private Map<String, String> propertyMap;


    @Override
    public Map<String, String> getUserProperties(final String userId)
    {
        if(MapUtils.isNotEmpty(this.propertyMap))
        {
            try
            {
                final UserModel user = this.userService.getUserForUID(userId);
                final Map<String, String> result = new HashMap<>(this.propertyMap.size());
                for(final Map.Entry<String, String> entry : this.propertyMap.entrySet())
                {
                    final String propertyName = entry.getKey();
                    final String propertyExpression = entry.getValue();
                    final Object value = this.objectValueService.getValue(propertyExpression, user);
                    result.put(propertyName, valueToString(value));
                }
                return result;
            }
            catch(final de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException e)
            {
                LOG.error(String.format("can not retrieve user properties for user '%s'", userId), e);
            }
        }
        return Collections.emptyMap();
    }


    /**
     * Converts given value to its string representation.
     *
     * @param value
     *           value to convert to string
     * @return string representation of the given value
     */
    protected String valueToString(final Object value)
    {
        if(value == null)
        {
            return null;
        }
        if(value instanceof String)
        {
            return (String)value;
        }
        if(value instanceof Collection)
        {
            return arrayValueToString(((Collection)value).toArray());
        }
        if(value.getClass().isArray())
        {
            return arrayValueToString((Object[])value);
        }
        return ObjectUtils.toString(value);
    }


    private String arrayValueToString(final Object[] arrayValue)
    {
        if(arrayValue == null || arrayValue.length == 0)
        {
            return null;
        }
        final StringBuilder result = new StringBuilder();
        for(int i = 0; i < arrayValue.length; i++)
        {
            if(i > 0)
            {
                result.append(',');
            }
            result.append(valueToString(arrayValue[i]));
        }
        return result.toString();
    }


    public void setPropertyMap(final Map<String, String> propertyMap)
    {
        this.propertyMap = propertyMap;
    }


    @Required
    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setObjectValueService(final ObjectValueService objectValueService)
    {
        this.objectValueService = objectValueService;
    }
}
