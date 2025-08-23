package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.search.data.facet.FacetData;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.MockitoJUnitRunner;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;

@RunWith(MockitoJUnitRunner.class)
public class FacetChartFacetChooserRendererTest
{
    private static final String FACET_NAME = "facetName";
    private FacetChartFacetChooserRenderer facetChartFacetChooserRenderer;
    private WidgetInstanceManager widgetInstanceManager;
    @Spy
    private Div parent = new Div();
    @Mock
    private Consumer<String> onFacetSelectionChangeConsumer;
    @Mock
    private FacetData facetData;


    @Before
    public void setUp()
    {
        CockpitTestUtil.mockZkEnvironment();
        this.widgetInstanceManager = CockpitTestUtil.mockWidgetInstanceManager();
        this.facetChartFacetChooserRenderer = new FacetChartFacetChooserRenderer();
    }


    @Test
    public void shouldNotRenderComboBoxWithOneItem()
    {
        this.facetChartFacetChooserRenderer.render(this.parent, this.widgetInstanceManager, this.onFacetSelectionChangeConsumer,
                        Collections.singletonList(this.facetData));
        Optional<Combobox> combobox = CockpitTestUtil.find((Component)this.parent, Combobox.class);
        Assertions.assertThat(combobox.isPresent()).isFalse();
    }


    @Test
    public void shouldRenderComboBoxWithTreeItem()
    {
        this.facetChartFacetChooserRenderer.render(this.parent, this.widgetInstanceManager, this.onFacetSelectionChangeConsumer,
                        Arrays.asList(new FacetData[] {this.facetData, this.facetData, this.facetData}));
        Optional<Combobox> combobox = CockpitTestUtil.find((Component)this.parent, Combobox.class);
        Assertions.assertThat(combobox).isPresent();
        Assertions.assertThat(CockpitTestUtil.findAll((Component)combobox.get(), Comboitem.class).count()).isEqualTo(3L);
    }


    @Test
    public void shouldFireEventAfterSelectionChange()
    {
        this.facetChartFacetChooserRenderer.render(this.parent, this.widgetInstanceManager, this.onFacetSelectionChangeConsumer,
                        Arrays.asList(new FacetData[] {this.facetData, this.facetData, this.facetData}));
        Comboitem comboitem = new Comboitem("facetName");
        comboitem.setValue("facetName");
        Set<Comboitem> selected = Sets.newSet((Object[])new Comboitem[] {comboitem});
        SelectEvent<Comboitem, String> selectEvent = new SelectEvent("", null, selected);
        this.facetChartFacetChooserRenderer.onSelectFacet(selectEvent);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        ((Consumer<String>)Mockito.verify(this.onFacetSelectionChangeConsumer)).accept((String)argumentCaptor.capture());
        Assertions.assertThat((String)argumentCaptor.getValue()).isEqualToIgnoringCase("facetName");
    }


    @Test
    public void shouldDoNothingWhenNoFacetAreSet()
    {
        this.facetChartFacetChooserRenderer.render(this.parent, this.widgetInstanceManager, this.onFacetSelectionChangeConsumer,
                        Collections.emptyList());
        Assertions.assertThat(this.parent.getChildren()).hasSize(0);
    }
}
