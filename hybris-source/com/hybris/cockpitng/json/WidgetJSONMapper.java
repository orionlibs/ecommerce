/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.json;

import com.hybris.cockpitng.engine.WidgetInstanceManager;

/**
 *
 *
 */
public interface WidgetJSONMapper extends JSONMapper
{
    default <T> T fromJSONString(final WidgetInstanceManager widgetInstanceManager, final String json, final Class<T> resultType)
    {
        return fromJSONString(json, resultType);
    }


    default String toJSONString(final WidgetInstanceManager widgetInstanceManager, final Object object)
    {
        return toJSONString(object);
    }
}
