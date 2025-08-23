package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.search.data.pageable.FullTextSearchPageable;
import org.zkoss.zul.Div;

public interface FacetChartRightPanelRenderer
{
    void render(Div paramDiv, WidgetInstanceManager paramWidgetInstanceManager, FullTextSearchPageable paramFullTextSearchPageable);
}
