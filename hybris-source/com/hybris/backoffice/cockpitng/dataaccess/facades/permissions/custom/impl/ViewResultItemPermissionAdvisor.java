/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.permissions.custom.impl;

import com.hybris.backoffice.cockpitng.dataaccess.facades.permissions.custom.InstancePermissionAdvisor;
import de.hybris.platform.util.ViewResultItem;

public class ViewResultItemPermissionAdvisor implements InstancePermissionAdvisor<ViewResultItem>
{
    @Override
    public boolean canModify(final ViewResultItem instance)
    {
        return false;
    }


    @Override
    public boolean canDelete(final ViewResultItem instance)
    {
        return false;
    }


    @Override
    public boolean isApplicableTo(final Object instance)
    {
        return instance instanceof ViewResultItem;
    }
}
