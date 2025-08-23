package com.hybris.pcmbackoffice.widgets.charts.facetchart.filter;

import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.engine.WidgetInstanceManager;

public interface ReferenceEditorFacetChartFilterAdapter
{
    String convertToFacetValue(Object paramObject);


    void deleteFilter(CockpitEvent paramCockpitEvent, String paramString, WidgetInstanceManager paramWidgetInstanceManager, Executable paramExecutable);
}
