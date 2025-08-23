/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels.impl;

import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Extension of {@link DefaultLabelService} that is able to resolve types on base of {@link TypeFacade}.
 */
public class TypeAwareLabelService extends DefaultLabelService
{
    private TypeFacade typeFacade;
    private PermissionFacade permissionFacade;


    @Override
    public String getObjectLabel(final Object object)
    {
        if(object == null || getPermissionFacade().canReadInstance(object))
        {
            return super.getObjectLabel(object);
        }
        return getNoReadAccessMessage(object);
    }


    @Override
    public String getObjectDescription(final Object object)
    {
        if(object == null || getPermissionFacade().canReadInstance(object))
        {
            return super.getObjectDescription(object);
        }
        return getNoReadAccessMessage(object);
    }


    @Override
    public String getAccessDeniedLabel(final Object object)
    {
        return getNoReadAccessMessage(object);
    }


    @Override
    public String getLanguageDisabledLabel(final Object object)
    {
        return getDisabledMessage(object);
    }


    protected String getNoReadAccessMessage(final Object object)
    {
        return StringUtils.EMPTY;
    }


    protected String getDisabledMessage(final Object object)
    {
        return StringUtils.EMPTY;
    }


    @Override
    protected String getType(final Object object)
    {
        String typeCode = getTypeFacade().getType(object);
        if(StringUtils.isEmpty(typeCode))
        {
            if(object instanceof Collection)
            {
                typeCode = Collection.class.getName();
            }
            else
            {
                typeCode = object.getClass().getName();
            }
        }
        return typeCode;
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }
}
