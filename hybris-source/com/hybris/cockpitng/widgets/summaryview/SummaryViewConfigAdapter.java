/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.summaryview;

import com.hybris.cockpitng.config.summaryview.jaxb.SummaryView;
import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.dataaccess.services.PositionedSort;
import org.springframework.beans.factory.annotation.Required;

/**
 * EditorArea configuration adapter which is responsible for arranging tabs, sections, panels, attributes in proper
 * order
 */
public class SummaryViewConfigAdapter implements CockpitConfigurationAdapter<SummaryView>
{
    private PositionedSort positionedSort;


    @Required
    public void setPositionedSort(final PositionedSort positionedSort)
    {
        this.positionedSort = positionedSort;
    }


    @Override
    public Class<SummaryView> getSupportedType()
    {
        return SummaryView.class;
    }


    @Override
    public SummaryView adaptAfterLoad(final ConfigContext context, final SummaryView summaryView)
                    throws CockpitConfigurationException
    {
        positionedSort.sort(summaryView.getCustomSectionOrSection());
        summaryView.getCustomSectionOrSection()
                        .forEach((section) -> positionedSort.sort(section.getCustomAttributeOrAttributeOrActions()));
        return summaryView;
    }


    @Override
    public SummaryView adaptBeforeStore(final ConfigContext context, final SummaryView summaryView)
                    throws CockpitConfigurationException
    {
        return summaryView;
    }
}
