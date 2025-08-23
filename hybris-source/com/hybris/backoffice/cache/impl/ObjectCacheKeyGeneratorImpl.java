/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cache.impl;

import com.google.common.collect.Lists;
import com.hybris.backoffice.cache.ObjectCacheKeyGenerator;
import com.hybris.cockpitng.core.spring.RequestOperationContextHolder;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.labels.LabelStringObjectHandler;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import org.springframework.beans.factory.annotation.Required;

public class ObjectCacheKeyGeneratorImpl implements ObjectCacheKeyGenerator
{
    protected static final String NO_ACTIVE_TENANT = "NO_ACTIVE_TENANT";
    private ObjectFacade objectFacade;
    private LabelStringObjectHandler labelStringObjectHandler;


    @Override
    public ObjectCacheKey createCacheKey(final String typeCode, final Object objectKey)
    {
        return new ObjectCacheKey(typeCode, objectKey, getTenantId());
    }


    @Override
    public Object computeKey(final Object object)
    {
        Object objectKey = object;
        if(object instanceof ItemModel)
        {
            final Object objectId = getObjectFacade().getObjectId(object);
            if(objectId != "")
            {
                objectKey = objectId;
            }
        }
        return Lists.newArrayList(objectKey, getLabelStringObjectHandler().getCurrentLocale(),
                        RequestOperationContextHolder.instance().getContext());
    }


    protected String getTenantId()
    {
        if(Registry.hasCurrentTenant())
        {
            return Registry.getCurrentTenant().getTenantID();
        }
        return NO_ACTIVE_TENANT;
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


    protected LabelStringObjectHandler getLabelStringObjectHandler()
    {
        return labelStringObjectHandler;
    }


    @Required
    public void setLabelStringObjectHandler(final LabelStringObjectHandler labelStringObjectHandler)
    {
        this.labelStringObjectHandler = labelStringObjectHandler;
    }
}
