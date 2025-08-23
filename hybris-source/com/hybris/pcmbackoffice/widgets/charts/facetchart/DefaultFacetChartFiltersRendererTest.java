package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.zkoss.zul.Div;

@RunWith(MockitoJUnitRunner.class)
public class DefaultFacetChartFiltersRendererTest
{
    @InjectMocks
    private DefaultFacetChartFiltersRenderer defaultFacetChartFiltersRenderer;
    private WidgetInstanceManager widgetInstanceManager;
    @Mock
    private Div filterContainer;
    @Mock
    private BiConsumer<String, Set<String>> facetSelectionListener;
    @Mock
    private FacetChartFiltersRenderer renderer;


    @Before
    public void setUp()
    {
        CockpitTestUtil.mockZkEnvironment();
        this.widgetInstanceManager = CockpitTestUtil.mockWidgetInstanceManager();
        List<FacetChartFiltersRenderer> renderers = new ArrayList<>();
        renderers.add(this.renderer);
        this.defaultFacetChartFiltersRenderer.setRenderers(renderers);
    }


    @Test
    public void shouldCallAllRenderers()
    {
        this.defaultFacetChartFiltersRenderer.renderFilters(this.widgetInstanceManager, this.filterContainer, this.facetSelectionListener);
        ((FacetChartFiltersRenderer)Mockito.verify(this.renderer)).renderFilters((WidgetInstanceManager)Matchers.any(), (Div)Matchers.any(), (BiConsumer)Matchers.any());
    }
}
