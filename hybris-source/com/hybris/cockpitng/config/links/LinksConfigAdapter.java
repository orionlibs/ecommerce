/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.links;

import com.hybris.cockpitng.config.links.jaxb.Links;
import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.dataaccess.services.PositionedSort;
import org.springframework.beans.factory.annotation.Required;

public class LinksConfigAdapter implements CockpitConfigurationAdapter<Links>
{
    private PositionedSort positionedSort;


    @Required
    public void setPositionedSort(final PositionedSort positionedSort)
    {
        this.positionedSort = positionedSort;
    }


    @Override
    public Class<Links> getSupportedType()
    {
        return Links.class;
    }


    @Override
    public Links adaptAfterLoad(final ConfigContext context, final Links links) throws CockpitConfigurationException
    {
        positionedSort.sort(links.getLink());
        return links;
    }


    @Override
    public Links adaptBeforeStore(final ConfigContext context, final Links links) throws CockpitConfigurationException
    {
        return links;
    }
}
