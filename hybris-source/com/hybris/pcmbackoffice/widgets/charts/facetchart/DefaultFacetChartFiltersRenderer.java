package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import org.springframework.core.OrderComparator;
import org.zkoss.zul.Div;

public class DefaultFacetChartFiltersRenderer
{
    private List<FacetChartFiltersRenderer> renderers = new ArrayList<>();
    private final OrderComparator orderComparator = new OrderComparator();


    public void renderFilters(WidgetInstanceManager widgetInstanceManager, Div filterContainer, BiConsumer<String, Set<String>> facetSelectionChange)
    {
        filterContainer.getChildren().clear();
        this.renderers.forEach(renderer -> renderer.renderFilters(widgetInstanceManager, filterContainer, facetSelectionChange));
    }


    public void handleFilterDeletion(CockpitEvent event, Executable onDeletedCallback)
    {
        this.renderers.forEach(renderer -> renderer.onFilterDeleted(event, onDeletedCallback));
    }


    public List<FacetChartFiltersRenderer> getRenderers()
    {
        return this.renderers;
    }


    public void setRenderers(List<FacetChartFiltersRenderer> renderers)
    {
        this.renderers = renderers;
    }


    public void loadRenderers(List<String> renderers)
    {
        loadRenderersInternal(renderers);
        this.renderers.sort((Comparator<? super FacetChartFiltersRenderer>)this.orderComparator);
    }


    private void loadRenderersInternal(List<String> renderersToLoad)
    {
        for(String name : renderersToLoad)
        {
            FacetChartFiltersRenderer renderer = (FacetChartFiltersRenderer)BackofficeSpringUtil.getBean(name, FacetChartFiltersRenderer.class);
            this.renderers.add(renderer);
        }
    }
}
