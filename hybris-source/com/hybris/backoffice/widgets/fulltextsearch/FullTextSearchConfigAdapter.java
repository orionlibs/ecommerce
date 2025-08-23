/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.fulltextsearch;

import com.hybris.cockpitng.config.fulltextsearch.jaxb.FulltextSearch;
import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.dataaccess.services.PositionedSort;
import org.springframework.beans.factory.annotation.Required;

/**
 * Fulltext Search configuration adapter which is responsible for arranging search fields order
 */
public class FullTextSearchConfigAdapter implements CockpitConfigurationAdapter<FulltextSearch>
{
    private PositionedSort positionedSort;


    @Override
    public Class<FulltextSearch> getSupportedType()
    {
        return FulltextSearch.class;
    }


    @Override
    public FulltextSearch adaptAfterLoad(final ConfigContext context, final FulltextSearch fulltextSearch)
                    throws CockpitConfigurationException
    {
        positionedSort.sort(fulltextSearch.getFieldList().getField());
        return fulltextSearch;
    }


    @Override
    public FulltextSearch adaptBeforeStore(final ConfigContext context, final FulltextSearch fulltextSearch)
                    throws CockpitConfigurationException
    {
        return fulltextSearch;
    }


    @Required
    public void setPositionedSort(final PositionedSort positionedSort)
    {
        this.positionedSort = positionedSort;
    }


    protected PositionedSort getPositionedSort()
    {
        return positionedSort;
    }
}
