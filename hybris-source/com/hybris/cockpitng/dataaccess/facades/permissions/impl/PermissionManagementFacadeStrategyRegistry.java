/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.permissions.impl;

import com.hybris.cockpitng.dataaccess.facades.common.impl.AbstractStrategyRegistry;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionManagementFacadeStrategy;

public class PermissionManagementFacadeStrategyRegistry extends AbstractStrategyRegistry<PermissionManagementFacadeStrategy, String>
{
    @Override
    public boolean canHandle(final PermissionManagementFacadeStrategy strategy, final String context)
    {
        return strategy.canHandle(context);
    }
}
