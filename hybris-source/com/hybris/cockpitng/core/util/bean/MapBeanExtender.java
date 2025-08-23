/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.bean;

import java.util.Map;
import java.util.Set;

/**
 * Modification bean to change contents of a map property of specified bean from any spring contexts in hierarchy
 */
public class MapBeanExtender extends AbstractBeanExtender
{
    public static final String BEAN_NAME = "mapExtender";
    private Map<Object, Object> put;
    private Set<Object> remove;


    public MapBeanExtender(final String extendedBeanName)
    {
        super(extendedBeanName);
    }


    @Override
    protected void checkProperty(final Object target)
    {
        checkPropertyType(target, Map.class);
    }


    @Override
    protected Object modifyProperty(final Object target)
    {
        final Map<Object, Object> map = (Map<Object, Object>)target;
        if(getPut() != null)
        {
            map.putAll(getPut());
        }
        if(getRemove() != null)
        {
            getRemove().forEach(map::remove);
        }
        return map;
    }


    protected Map<Object, Object> getPut()
    {
        return put;
    }


    public void setPut(final Map<Object, Object> put)
    {
        this.put = put;
    }


    protected Set<Object> getRemove()
    {
        return remove;
    }


    public void setRemove(final Set<Object> remove)
    {
        this.remove = remove;
    }
}
