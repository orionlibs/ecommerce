/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl;

import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.collectionbrowser.CollectionBrowserController;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.TypeCodeResolver;
import java.util.Collection;
import java.util.function.Predicate;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default resolver of the type code for Collection Browser widget.
 */
public class DefaultTypeCodeResolver implements TypeCodeResolver
{
    private TypeFacade typeFacade;


    @Override
    public String resolveTypeCodeFromObject(final Object object, final WidgetInstanceManager wim)
    {
        String typeCode = getTypeFacade().getType(object);
        if(typeCode == null)
        {
            typeCode = object.getClass().getName();
        }
        return typeCode;
    }


    @Override
    public String resolveTypeCodeFromCollection(final Collection<?> objects, final WidgetInstanceManager wim)
    {
        if(CollectionUtils.isEmpty(objects))
        {
            return wim.getWidgetSettings().getString(CollectionBrowserController.SETTING_FALLBACK_TYPE_CODE);
        }
        final Object firstItem = objects.iterator().next();
        if(firstItem == null)
        {
            return null;
        }
        String typeCode = getTypeFacade().getType(firstItem);
        if(typeCode != null)
        {
            return typeCode;
        }
        final Predicate<Object> isCompatibleWithFirstItem = item -> firstItem.getClass().isAssignableFrom(item.getClass());
        if(objects.stream().noneMatch(isCompatibleWithFirstItem.negate()))
        {
            typeCode = firstItem.getClass().getName();
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
}
