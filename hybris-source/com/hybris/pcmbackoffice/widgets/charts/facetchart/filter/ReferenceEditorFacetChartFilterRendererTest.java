package com.hybris.pcmbackoffice.widgets.charts.facetchart.filter;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import de.hybris.bootstrap.annotations.UnitTest;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Div;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ReferenceEditorFacetChartFilterRendererTest
{
    private static final String FACET_NAME = "myFacet";
    @InjectMocks
    @Spy
    private ReferenceEditorFacetChartFilterRenderer renderer;
    private WidgetInstanceManager widgetInstanceManager;
    @Spy
    private Div filterContainer = new Div();
    @Mock
    private BiConsumer<String, Set<String>> facetSelectionListener;
    @Mock
    private ReferenceEditorFacetChartFilterAdapter adapter;


    @Before
    public void setUp()
    {
        CockpitTestUtil.mockZkEnvironment();
        this.widgetInstanceManager = CockpitTestUtil.mockWidgetInstanceManager();
    }


    @Test
    public void shouldRender3Elements()
    {
        ((ReferenceEditorFacetChartFilterRenderer)Mockito.doReturn(new Editor()).when(this.renderer)).createEditor();
        this.renderer.renderFilters(this.widgetInstanceManager, this.filterContainer, this.facetSelectionListener);
        ((Div)Mockito.verify(this.filterContainer)).appendChild((Component)Matchers.any());
        Assertions.assertThat(((Component)this.filterContainer.getChildren().get(0)).getChildren()).hasSize(3);
    }


    @Test
    public void shouldStoreSelectedValueAndFireEventWhenNewFilterWasSet()
    {
        this.renderer.setFacetName("myFacet");
        ((ReferenceEditorFacetChartFilterRenderer)Mockito.doReturn(new Editor()).when(this.renderer)).createEditor();
        this.renderer.renderFilters(this.widgetInstanceManager, this.filterContainer, this.facetSelectionListener);
        this.renderer.filterChanged((Event)Mockito.mock(Event.class));
        WidgetModel model = this.widgetInstanceManager.getModel();
        ((WidgetModel)Mockito.verify(model)).setValue((String)Matchers.eq("selectedFilter_myFacet"), Matchers.any());
        ((BiConsumer<String, Set>)Mockito.verify(this.facetSelectionListener)).accept((String)Matchers.eq("myFacet"), (Set)Matchers.any());
    }


    @Test
    public void shouldSetEmptyValueEndTriggerEventWhenClearAllFiltersWasClicked()
    {
        this.renderer.setFacetName("myFacet");
        Editor mockedEditor = (Editor)Mockito.mock(Editor.class);
        ((ReferenceEditorFacetChartFilterRenderer)Mockito.doReturn(mockedEditor).when(this.renderer)).createEditor();
        ((ReferenceEditorFacetChartFilterRenderer)Mockito.doReturn(Optional.of(mockedEditor)).when(this.renderer)).findEditor((Event)Matchers.any());
        this.renderer.renderFilters(this.widgetInstanceManager, this.filterContainer, this.facetSelectionListener);
        this.renderer.removeAllFilters((Event)Mockito.mock(Event.class));
        WidgetModel model = this.widgetInstanceManager.getModel();
        ((WidgetModel)Mockito.verify(model)).setValue("selectedFilter_myFacet", Collections.emptySet());
        ((BiConsumer)Mockito.verify(this.facetSelectionListener)).accept("myFacet", Collections.emptySet());
        ((Editor)Mockito.verify(mockedEditor)).setValue(null);
    }


    @Test
    public void shouldDeleteFilterOnFilterDeletedEvent()
    {
        CockpitEvent event = (CockpitEvent)Mockito.mock(CockpitEvent.class);
        Executable onDeletedCallback = (Executable)Mockito.mock(Executable.class);
        this.renderer.setFacetName("myFacet");
        ((ReferenceEditorFacetChartFilterRenderer)Mockito.doReturn(Mockito.mock(Editor.class)).when(this.renderer)).createEditor();
        this.renderer.renderFilters(this.widgetInstanceManager, this.filterContainer, this.facetSelectionListener);
        this.renderer.onFilterDeleted(event, onDeletedCallback);
        ((ReferenceEditorFacetChartFilterAdapter)Mockito.verify(this.adapter)).deleteFilter((CockpitEvent)Matchers.any(CockpitEvent.class), Matchers.anyString(), (WidgetInstanceManager)Matchers.any(WidgetInstanceManager.class), (Executable)Matchers.any(Executable.class));
    }
}
