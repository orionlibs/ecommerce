/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.permissions.impl;

import com.hybris.cockpitng.core.util.CockpitTypeUtils;
import com.hybris.cockpitng.dataaccess.facades.common.impl.AbstractStrategyRegistry;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacadeStrategy;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

/**
 * Extension of {@link AbstractStrategyRegistry} that can handle {@link PermissionFacadeStrategy}s.
 */
public class PermissionFacadeStrategyRegistry extends AbstractStrategyRegistry<PermissionFacadeStrategy, String>
{
    private TypeFacade typeFacade;
    private CockpitTypeUtils cockpitTypeUtils;


    @Override
    public boolean canHandle(final PermissionFacadeStrategy strategy, final String context)
    {
        return strategy.canHandle(context);
    }


    public PermissionFacadeStrategy getStrategyByInstance(final Object instance)
    {
        if(instance == null)
        {
            return null;
        }
        final String type = typeFacade.getType(instance);
        return type == null ? null : getStrategy(type);
    }


    public PermissionFacadeStrategy getStrategyByInstances(final Collection<Object> instances)
    {
        if(instances == null || instances.stream().anyMatch(Objects::isNull))
        {
            return null;
        }
        final String type = cockpitTypeUtils.findClosestSuperType(new ArrayList<>(instances));
        return type == null ? null : getStrategy(type);
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    @Required
    public void setCockpitTypeUtils(final CockpitTypeUtils cockpitTypeUtils)
    {
        this.cockpitTypeUtils = cockpitTypeUtils;
    }
}
