/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.adapters;

import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.dataaccess.services.PositionedSort;
import org.springframework.beans.factory.annotation.Required;

public class AdvancedSearchConfigAdapter implements CockpitConfigurationAdapter<AdvancedSearch>
{
    private PositionedSort positionedSort;


    @Required
    public void setPositionedSort(final PositionedSort positionedSort)
    {
        this.positionedSort = positionedSort;
    }


    @Override
    public Class<AdvancedSearch> getSupportedType()
    {
        return AdvancedSearch.class;
    }


    @Override
    public AdvancedSearch adaptAfterLoad(final ConfigContext context, final AdvancedSearch advancedSearch) throws CockpitConfigurationException
    {
        if(advancedSearch.getFieldList() != null && advancedSearch.getFieldList().getField() != null)
        {
            positionedSort.sort(advancedSearch.getFieldList().getField());
        }
        return advancedSearch;
    }


    @Override
    public AdvancedSearch adaptBeforeStore(final ConfigContext context, final AdvancedSearch advancedSearch) throws CockpitConfigurationException
    {
        return advancedSearch;
    }
}
