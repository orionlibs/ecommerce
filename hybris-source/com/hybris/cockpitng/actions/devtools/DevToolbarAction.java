/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.devtools;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.core.CNG;
import com.hybris.cockpitng.core.util.CockpitProperties;
import javax.annotation.Resource;

public abstract class DevToolbarAction<I, O> implements CockpitAction<I, O>
{
    /**
     * @deprecated since 1811, use {@link CNG#PROPERTY_DEVELOPMENT_MODE} instead
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected static final String COCKPITNG_DEVELOPMENT_MODE = CNG.PROPERTY_DEVELOPMENT_MODE;
    @Resource
    private CockpitProperties cockpitProperties;


    @Override
    public boolean canPerform(final ActionContext<I> ctx)
    {
        return getCockpitProperties().getBoolean(CNG.PROPERTY_DEVELOPMENT_MODE);
    }


    public CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }
}
