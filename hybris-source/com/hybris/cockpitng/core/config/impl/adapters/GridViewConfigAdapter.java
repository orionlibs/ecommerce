/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.adapters;

import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.gridview.GridView;
import com.hybris.cockpitng.dataaccess.services.PositionedSort;
import org.springframework.beans.factory.annotation.Required;

public class GridViewConfigAdapter implements CockpitConfigurationAdapter<GridView>
{
    private PositionedSort positionedSort;


    @Required
    public void setPositionedSort(final PositionedSort positionedSort)
    {
        this.positionedSort = positionedSort;
    }


    @Override
    public Class<GridView> getSupportedType()
    {
        return GridView.class;
    }


    @Override
    public GridView adaptAfterLoad(final ConfigContext context, final GridView gridView) throws CockpitConfigurationException
    {
        positionedSort.sort(gridView.getAdditionalRenderer());
        return gridView;
    }


    @Override
    public GridView adaptBeforeStore(final ConfigContext context, final GridView gridView) throws CockpitConfigurationException
    {
        return gridView;
    }
}
