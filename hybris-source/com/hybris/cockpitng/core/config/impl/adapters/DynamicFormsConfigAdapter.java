/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.adapters;

import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.exception.CyclicDynamicFormsException;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicForms;
import com.hybris.cockpitng.core.config.util.DynamicFormsCycleFinder;
import com.hybris.cockpitng.dataaccess.services.PositionedSort;
import org.springframework.beans.factory.annotation.Required;

public class DynamicFormsConfigAdapter implements CockpitConfigurationAdapter<DynamicForms>
{
    private PositionedSort positionedSort;


    @Required
    public void setPositionedSort(final PositionedSort positionedSort)
    {
        this.positionedSort = positionedSort;
    }


    @Override
    public Class<DynamicForms> getSupportedType()
    {
        return DynamicForms.class;
    }


    @Override
    public DynamicForms adaptAfterLoad(final ConfigContext context, final DynamicForms dynamicForms)
                    throws CockpitConfigurationException
    {
        adaptAttributes(dynamicForms);
        adaptTabs(dynamicForms);
        adaptSections(dynamicForms);
        return dynamicForms;
    }


    private void adaptAttributes(final DynamicForms dynamicForms)
    {
        if(dynamicForms.getAttribute() != null)
        {
            final DynamicFormsCycleFinder cycleFinder = DynamicFormsCycleFinder.findCycles(dynamicForms);
            if(cycleFinder.hasCycle())
            {
                throw new CyclicDynamicFormsException("A cycle in dynamicForms configuration has been found", cycleFinder.getCycle());
            }
            positionedSort.sort(dynamicForms.getAttribute());
        }
    }


    private void adaptTabs(final DynamicForms dynamicForms)
    {
        if(dynamicForms.getTab() != null)
        {
            positionedSort.sort(dynamicForms.getTab());
        }
    }


    private void adaptSections(final DynamicForms dynamicForms)
    {
        if(dynamicForms.getSection() != null)
        {
            positionedSort.sort(dynamicForms.getSection());
        }
    }


    @Override
    public DynamicForms adaptBeforeStore(final ConfigContext context, final DynamicForms dynamicForms)
                    throws CockpitConfigurationException
    {
        return dynamicForms;
    }
}
