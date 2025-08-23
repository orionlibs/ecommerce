package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.search.data.pageable.FullTextSearchPageable;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

@RunWith(MockitoJUnitRunner.class)
public class ProductLinksFacetChartRendererTest
{
    @InjectMocks
    private ProductLinksFacetChartRenderer productLinksSolrChartRenderer;
    private WidgetInstanceManager widgetInstanceManager;
    @Mock
    private FullTextSearchPageable fullTextSearchPageable;


    @Before
    public void setUp()
    {
        CockpitTestUtil.mockZkEnvironment();
        this.widgetInstanceManager = CockpitTestUtil.mockWidgetInstanceManager();
    }


    @Test
    public void shouldRenderPanel()
    {
        Div parent = new Div();
        Mockito.when(Integer.valueOf(this.fullTextSearchPageable.getTotalCount())).thenReturn(Integer.valueOf(123));
        this.productLinksSolrChartRenderer.render(parent, this.widgetInstanceManager, this.fullTextSearchPageable);
        Optional<Component> labelWithTotalCount = CockpitTestUtil.find((Component)parent, c -> isLabelWithText(c, "123"));
        Assertions.assertThat(labelWithTotalCount).isPresent();
        Optional<Component> goToAllButton = CockpitTestUtil.find((Component)parent, c -> isButtonWithCssClass(c, "yw-solrfacetchart-rightpanel-go-to-all-products-button"));
        Assertions.assertThat(goToAllButton).isPresent();
        Optional<Component> addNewButton = CockpitTestUtil.find((Component)parent, c -> isButtonWithCssClass(c, "yw-solrfacetchart-rightpanel-add-new-product-button"));
        Assertions.assertThat(addNewButton).isPresent();
    }


    @Test
    public void shouldGoToAllProducts()
    {
        Div parent = new Div();
        Mockito.when(Integer.valueOf(this.fullTextSearchPageable.getTotalCount())).thenReturn(Integer.valueOf(123));
        this.productLinksSolrChartRenderer.render(parent, this.widgetInstanceManager, this.fullTextSearchPageable);
        Optional<Component> goToAllButton = CockpitTestUtil.find((Component)parent, c -> isButtonWithCssClass(c, "yw-solrfacetchart-rightpanel-go-to-all-products-button"));
        Assertions.assertThat(goToAllButton).isPresent();
        executeEvent(goToAllButton.get(), "onClick");
        ((WidgetInstanceManager)Mockito.verify(this.widgetInstanceManager)).sendOutput((String)Matchers.eq("goToAllProducts"), Matchers.argThat((ArgumentMatcher)new Object(this)));
    }


    @Test
    public void shouldOpenWizardToAddNewProductProducts()
    {
        Div parent = new Div();
        Mockito.when(Integer.valueOf(this.fullTextSearchPageable.getTotalCount())).thenReturn(Integer.valueOf(123));
        this.productLinksSolrChartRenderer.render(parent, this.widgetInstanceManager, this.fullTextSearchPageable);
        Optional<Component> goToAllButton = CockpitTestUtil.find((Component)parent, c -> isButtonWithCssClass(c, "yw-solrfacetchart-rightpanel-add-new-product-button"));
        Assertions.assertThat(goToAllButton).isPresent();
        executeEvent(goToAllButton.get(), "onClick");
        ((WidgetInstanceManager)Mockito.verify(this.widgetInstanceManager)).sendOutput((String)Matchers.eq("addNewProduct"), Matchers.any());
    }


    @Test
    public void shouldRenderLabelTotalProductsFiltered()
    {
        Div parent = new Div();
        Mockito.when(this.widgetInstanceManager.getModel().getValue("filtersCounter", Integer.class)).thenReturn(Integer.valueOf(1));
        this.productLinksSolrChartRenderer.render(parent, this.widgetInstanceManager, this.fullTextSearchPageable);
        Optional<Component> labelWithTotalProductFiltered = CockpitTestUtil.find((Component)parent, c -> isLabelWithText(c, "null*"));
        Assertions.assertThat(labelWithTotalProductFiltered).isPresent();
    }


    private boolean isButtonWithCssClass(Component c, String s)
    {
        return (c instanceof A && s.equals(((A)c).getSclass()));
    }


    private boolean isLabelWithText(Component component, String label)
    {
        return (component instanceof Label && label.equals(((Label)component).getValue()));
    }


    private void executeEvent(Component component, String eventName)
    {
        executeEvent(component, new Event(eventName));
    }


    private void executeEvent(Component component, Event event)
    {
        Iterable<EventListener<? extends Event>> events = component.getEventListeners(event.getName());
        events.forEach(listener -> {
            try
            {
                listener.onEvent(event);
            }
            catch(Exception e)
            {
                Assertions.fail(e.getMessage());
            }
        });
    }
}
