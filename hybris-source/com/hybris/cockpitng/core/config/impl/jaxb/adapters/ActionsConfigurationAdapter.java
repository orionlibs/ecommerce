/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.adapters;

import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.ActionGroup;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Actions;
import org.apache.commons.collections.CollectionUtils;

/**
 * Configuration adapter that sorts action groups and actions included by those groups.
 */
public class ActionsConfigurationAdapter extends PositionedConfigurationAdapter<Actions, ActionGroup>
{
    private final CockpitConfigurationAdapter<ActionGroup> actionGroupAdapter;


    public ActionsConfigurationAdapter(final PositionedSort positionedSort,
                    final CockpitConfigurationAdapter<ActionGroup> groupAdapter)
    {
        super(positionedSort, Actions.class, "group");
        this.actionGroupAdapter = groupAdapter;
    }


    protected CockpitConfigurationAdapter<ActionGroup> getActionGroupAdapter()
    {
        return actionGroupAdapter;
    }


    @Override
    public Actions adaptBeforeMerge(final ConfigContext context, final Actions actions) throws CockpitConfigurationException
    {
        final Actions beforeMerge = super.adaptBeforeMerge(context, actions);
        if(beforeMerge != null && CollectionUtils.isNotEmpty(beforeMerge.getGroup()))
        {
            for(final ActionGroup group : beforeMerge.getGroup())
            {
                getActionGroupAdapter().adaptBeforeMerge(context, group);
            }
        }
        return beforeMerge;
    }


    @Override
    public Actions adaptAfterLoad(final ConfigContext context, final Actions actions) throws CockpitConfigurationException
    {
        final Actions afterLoad = super.adaptAfterLoad(context, actions);
        if(afterLoad != null && CollectionUtils.isNotEmpty(afterLoad.getGroup()))
        {
            for(final ActionGroup group : afterLoad.getGroup())
            {
                getActionGroupAdapter().adaptAfterLoad(context, group);
            }
        }
        return afterLoad;
    }


    @Override
    public Actions adaptBeforeStore(final ConfigContext context, final Actions actions) throws CockpitConfigurationException
    {
        final Actions beforeStore = super.adaptBeforeStore(context, actions);
        if(beforeStore != null && CollectionUtils.isNotEmpty(beforeStore.getGroup()))
        {
            for(final ActionGroup group : beforeStore.getGroup())
            {
                getActionGroupAdapter().adaptBeforeStore(context, group);
            }
        }
        return beforeStore;
    }
}
