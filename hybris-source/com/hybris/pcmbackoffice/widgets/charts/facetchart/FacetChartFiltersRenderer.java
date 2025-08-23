package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Set;
import java.util.function.BiConsumer;
import org.springframework.core.Ordered;
import org.zkoss.zul.Div;

public interface FacetChartFiltersRenderer extends Ordered
{
    void renderFilters(WidgetInstanceManager paramWidgetInstanceManager, Div paramDiv, BiConsumer<String, Set<String>> paramBiConsumer);


    default void onFilterDeleted(CockpitEvent event, Executable onDeletedCallback)
    {
    }
}
