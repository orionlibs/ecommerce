/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels.impl;

import com.hybris.cockpitng.labels.LabelProvider;
import com.hybris.cockpitng.labels.LabelService;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Label provider for Maps. It prints map in format {key1=value1, key2=value2,...}, where key and value are fetched from
 * labelService.
 */
public class DefaultMapLabelProvider implements LabelProvider<Map>
{
    private LabelService labelService;


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    @Override
    public String getLabel(final Map map)
    {
        final StringBuilder sb = new StringBuilder("{");
        if(MapUtils.isNotEmpty(map))
        {
            final Iterator iterator = map.entrySet().iterator();
            while(iterator.hasNext())
            {
                final Map.Entry entry = (Map.Entry)iterator.next();
                sb.append(getObjectLabel(entry.getKey()));
                sb.append(" = ");
                sb.append(getObjectLabel(entry.getValue()));
                if(iterator.hasNext())
                {
                    sb.append(", ");
                }
            }
        }
        sb.append("}");
        return sb.toString();
    }


    protected String getObjectLabel(final Object object)
    {
        if(object == null)
        {
            return "null";
        }
        else if(object instanceof String)
        {
            return (String)object;
        }
        else
        {
            return labelService.getObjectLabel(object);
        }
    }


    @Override
    public String getDescription(final Map object)
    {
        return null;
    }


    @Override
    public String getIconPath(final Map object)
    {
        return null;
    }
}
