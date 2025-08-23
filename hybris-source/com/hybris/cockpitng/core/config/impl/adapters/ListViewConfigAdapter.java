/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.adapters;

import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListView;
import com.hybris.cockpitng.dataaccess.services.PositionedSort;
import org.springframework.beans.factory.annotation.Required;

/**
 * List view configuration adapter which is responsible for arranging list view columns in proper order
 */
public class ListViewConfigAdapter implements CockpitConfigurationAdapter<ListView>
{
    @Override
    public Class<ListView> getSupportedType()
    {
        return ListView.class;
    }


    @Override
    public ListView adaptAfterLoad(final ConfigContext context, final ListView listView) throws CockpitConfigurationException
    {
        positionedSort.sort(listView.getColumn());
        return listView;
    }


    @Override
    public ListView adaptBeforeStore(final ConfigContext context, final ListView listView) throws CockpitConfigurationException
    {
        return listView;
    }


    private PositionedSort positionedSort;


    @Required
    public void setPositionedSort(final PositionedSort positionedSort)
    {
        this.positionedSort = positionedSort;
    }
}
