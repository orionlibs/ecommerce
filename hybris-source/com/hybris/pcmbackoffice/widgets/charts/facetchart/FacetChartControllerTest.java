package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.core.impl.DefaultWidgetModel;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.search.data.FullTextSearchData;
import com.hybris.cockpitng.search.data.facet.FacetData;
import com.hybris.cockpitng.search.data.pageable.FullTextSearchPageable;
import com.hybris.cockpitng.testing.AbstractWidgetUnitTest;
import com.hybris.cockpitng.testing.annotation.DeclaredGlobalCockpitEvent;
import com.hybris.cockpitng.testing.annotation.DeclaredGlobalCockpitEvents;
import com.hybris.cockpitng.testing.annotation.DeclaredInput;
import com.hybris.cockpitng.testing.annotation.DeclaredInputs;
import com.hybris.cockpitng.testing.annotation.DeclaredViewEvent;
import com.hybris.cockpitng.testing.annotation.ExtensibleWidget;
import com.hybris.cockpitng.testing.annotation.NullSafeWidget;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.zkoss.chart.Charts;
import org.zkoss.chart.ChartsEvent;
import org.zkoss.chart.Color;
import org.zkoss.chart.Exporting;
import org.zkoss.chart.Point;
import org.zkoss.chart.Series;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;

@DeclaredInputs({@DeclaredInput(value = "fullTextSearchPageable", socketType = FullTextSearchPageable.class), @DeclaredInput(value = "initSearch", socketType = Object.class)})
@DeclaredViewEvent(componentID = "showFilters", eventName = "onClick")
@DeclaredGlobalCockpitEvents({@DeclaredGlobalCockpitEvent(eventName = "objectsUpdated", scope = "session"), @DeclaredGlobalCockpitEvent(eventName = "objectsDeleted", scope = "session"), @DeclaredGlobalCockpitEvent(eventName = "onClientInfo", scope = "session")})
@NullSafeWidget
@ExtensibleWidget(level = 14)
public class FacetChartControllerTest extends AbstractWidgetUnitTest<FacetChartController>
{
    public static final String FACET = "facet";
    @Spy
    @InjectMocks
    private FacetChartController facetChartController;
    @Mock
    private Charts charts;
    @Mock
    private FacetChartDataExtractor facetChartDataExtractor;
    @Mock
    private FacetChartBottomPanelRenderer facetChartBottomPanelRenderer;
    @Mock
    private DefaultFacetChartFiltersRenderer solrChartFiltersRenderer;
    @Mock
    private FacetChartFacetChooserRenderer facetChartFacetChooserRenderer;
    @Mock
    private FullTextSearchPageable fullTextSearchPageableMock;
    @Mock
    private FullTextSearchData fullTextSearchDataMock;
    @Mock
    private Context contextMock;
    @Mock
    private FacetChartRightPanelRenderer facetChartRightPanelRenderer;
    @Mock
    private FacetChartComposer facetChartComposer;
    @Mock
    private Label filtersCounterLabel;
    @Mock
    private Popup filtersPopup;
    @Mock
    private Div filtersContainer;
    @Mock
    private Button showFilters;
    @Mock
    private AdvancedSearchData originalQuery;


    protected FacetChartController getWidgetController()
    {
        return this.facetChartController;
    }


    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        Mockito.when(this.charts.getSeries()).thenReturn(Mockito.mock(Series.class));
        Mockito.when(this.charts.getExporting()).thenReturn(Mockito.mock(Exporting.class));
        Mockito.when(this.charts.getColors()).thenReturn(Collections.singletonList(new Color("red")));
        Mockito.when(this.facetChartDataExtractor.getPoints((FullTextSearchData)Matchers.any(), (String)Matchers.any())).thenReturn(Collections.singletonList(new Point("name", Integer.valueOf(5))));
        Mockito.when(this.fullTextSearchPageableMock.getFullTextSearchData()).thenReturn(this.fullTextSearchDataMock);
        Mockito.when(this.fullTextSearchDataMock.getContext()).thenReturn(this.contextMock);
        Mockito.when(this.contextMock.getAttribute((String)Matchers.any())).thenReturn(Mockito.mock(AdvancedSearchData.class));
        ((FacetChartController)Mockito.doNothing().when(this.facetChartController)).initBeans();
    }


    @Test
    public void shouldInitializeAndRenderFilters()
    {
        ((FacetChartController)Mockito.doReturn(Collections.singletonList("facetName")).when(this.facetChartController)).getFacetNames();
        Mockito.when(this.solrChartFiltersRenderer.getRenderers()).thenReturn(Collections.singletonList((FacetChartFiltersRenderer)Mockito.mock(FacetChartFiltersRenderer.class)));
        this.facetChartController.initialize((Component)Mockito.mock(Component.class));
        ((DefaultFacetChartFiltersRenderer)Mockito.verify(this.solrChartFiltersRenderer)).renderFilters((WidgetInstanceManager)Matchers.any(), (Div)Matchers.any(), (BiConsumer)Matchers.any());
    }


    @Test
    public void shouldRenderFacetChooser()
    {
        ((FacetChartController)Mockito.doReturn(this.fullTextSearchDataMock).when(this.facetChartController)).getFullTextSearchData();
        this.facetChartController.processSearchResultAfterSearchExecution();
        ((FacetChartFacetChooserRenderer)Mockito.verify(this.facetChartFacetChooserRenderer)).render((Div)Matchers.any(), (WidgetInstanceManager)Matchers.any(), (Consumer)Matchers.any(), (List)Matchers.any());
    }


    @Test
    public void shouldRenderFilters()
    {
        this.widgetModel.setValue("initSearchPerformed", Boolean.valueOf(true));
        Mockito.when(this.solrChartFiltersRenderer.getRenderers()).thenReturn(Collections.singletonList((FacetChartFiltersRenderer)Mockito.mock(FacetChartFiltersRenderer.class)));
        ((FacetChartController)Mockito.doReturn(this.fullTextSearchDataMock).when(this.facetChartController)).getFullTextSearchData();
        this.facetChartController.processSearchResultAfterSearchExecution();
        ((DefaultFacetChartFiltersRenderer)Mockito.verify(this.solrChartFiltersRenderer)).renderFilters((WidgetInstanceManager)Matchers.any(), (Div)Matchers.any(), (BiConsumer)Matchers.any());
    }


    @Test
    public void shouldShowFilterComponents()
    {
        this.widgetModel.setValue("initSearchPerformed", Boolean.valueOf(true));
        Mockito.when(this.solrChartFiltersRenderer.getRenderers()).thenReturn(Collections.singletonList((FacetChartFiltersRenderer)Mockito.mock(FacetChartFiltersRenderer.class)));
        ((FacetChartController)Mockito.doReturn(this.fullTextSearchDataMock).when(this.facetChartController)).getFullTextSearchData();
        this.facetChartController.processSearchResultAfterSearchExecution();
        ((FacetChartController)Mockito.verify(this.facetChartController)).showFilterComponents(true);
    }


    @Test
    public void shouldHideFilterComponents()
    {
        this.widgetModel.setValue("initSearchPerformed", Boolean.valueOf(true));
        Mockito.when(this.solrChartFiltersRenderer.getRenderers()).thenReturn(Collections.emptyList());
        ((FacetChartController)Mockito.doReturn(this.fullTextSearchDataMock).when(this.facetChartController)).getFullTextSearchData();
        this.facetChartController.processSearchResultAfterSearchExecution();
        ((FacetChartController)Mockito.verify(this.facetChartController)).showFilterComponents(false);
    }


    @Test
    public void shouldInitSearch()
    {
        executeInputSocketEvent("initSearch", new Object[0]);
        assertSocketOutput("initialSearchDataContext", 1, data -> data instanceof Context);
        assertSocketOutput("initialSearchData", 1, data -> data instanceof AdvancedSearchData);
    }


    @Test
    public void shouldInitSearchWithSelectedFacetsAndFilters()
    {
        Map<String, Set<String>> selectedFacets = new HashMap<>();
        selectedFacets.put("facet", Collections.singleton("facetValue"));
        Mockito.when(this.widgetInstanceManager.getModel().getValue("selectedFacets", Map.class)).thenReturn(selectedFacets);
        executeInputSocketEvent("initSearch", new Object[0]);
        ArgumentCaptor<AdvancedSearchData> argumentCaptor = ArgumentCaptor.forClass(AdvancedSearchData.class);
        ((WidgetInstanceManager)Mockito.verify(this.widgetInstanceManager)).sendOutput((String)Matchers.eq("initialSearchData"), argumentCaptor.capture());
        AdvancedSearchData sendQuery = (AdvancedSearchData)argumentCaptor.getValue();
        Assertions.assertThat(sendQuery.getSelectedFacets()).containsKeys((Object[])new String[] {"facet"});
        Assertions.assertThat((Iterable)sendQuery.getSelectedFacets().get("facet")).hasSize(1);
        Assertions.assertThat((Iterable)sendQuery.getSelectedFacets().get("facet")).contains((Object[])new String[] {"facetValue"});
    }


    @Test
    public void shouldExecuteSearchOperationOnInputEvent()
    {
        executeInputSocketEvent("fullTextSearchPageable", new Object[] {this.fullTextSearchPageableMock});
        ((FacetChartController)Mockito.verify(this.facetChartController)).executeSearchOperation(this.fullTextSearchPageableMock);
    }


    @Test
    public void shouldRenderChart()
    {
        ((FacetChartController)Mockito.doReturn(this.fullTextSearchDataMock).when(this.facetChartController)).getFullTextSearchData();
        this.widgetModel.setValue("initSearchPerformed", Boolean.valueOf(true));
        this.facetChartController.processSearchResultAfterSearchExecution();
        ((FacetChartController)Mockito.verify(this.facetChartController)).render();
        ((FacetChartDataExtractor)Mockito.verify(this.facetChartDataExtractor)).getPoints((FullTextSearchData)Matchers.any(), (String)Matchers.any());
        ((FacetChartBottomPanelRenderer)Mockito.verify(this.facetChartBottomPanelRenderer)).render((Div)Matchers.any(), (List)Matchers.any(), (EventListener)Matchers.any());
        ((FacetChartController)Mockito.verify(this.facetChartController)).renderFilters();
        ((FacetChartController)Mockito.verify(this.facetChartController)).renderBottomPanel((List)Matchers.any());
        ((FacetChartController)Mockito.verify(this.facetChartController)).renderRightPanel((FullTextSearchPageable)Matchers.any());
    }


    @Test
    public void shouldMoveToCollectionBrowserAfterChartClick()
    {
        AdvancedSearchData advancedSearchDataMock = (AdvancedSearchData)Mockito.mock(AdvancedSearchData.class);
        ((FacetChartController)Mockito.doReturn(advancedSearchDataMock).when(this.facetChartController)).createCopyAdvancedSearchData((AdvancedSearchData)Matchers.any());
        ((FacetChartController)Mockito.doReturn(this.originalQuery).when(this.facetChartController)).getOriginalQuery();
        executeInputSocketEvent("fullTextSearchPageable", new Object[] {this.fullTextSearchPageableMock});
        ChartsEvent event = (ChartsEvent)Mockito.mock(ChartsEvent.class);
        Mockito.when(event.getPoint()).thenReturn(Mockito.mock(Point.class));
        this.facetChartController.handleClickOnPoint((Event)event);
        assertSocketOutput("selectedFacets", advancedSearchDataMock);
    }


    @Test
    public void shouldRunSearchAfterNewFacetSelection()
    {
        AdvancedSearchData advancedSearchDataMock = (AdvancedSearchData)Mockito.mock(AdvancedSearchData.class);
        ((FacetChartController)Mockito.doReturn(advancedSearchDataMock).when(this.facetChartController)).createCopyAdvancedSearchData((AdvancedSearchData)Matchers.any());
        ((FacetChartController)Mockito.doReturn(this.originalQuery).when(this.facetChartController)).getOriginalQuery();
        Set<String> facetSelected = new HashSet<>();
        this.facetChartController.applyFacetSelection("facetName", facetSelected);
        assertSocketOutput("initialSearchData", 1, advancedSearchDataMock);
        ((DefaultWidgetModel)Mockito.verify(this.widgetModel)).setValue((String)Matchers.eq("selectedFacets"), Matchers.any());
    }


    @Test
    public void testConvertFacetNameToFacetData()
    {
        ((FacetChartController)Mockito.doReturn(this.fullTextSearchDataMock).when(this.facetChartController)).getFullTextSearchData();
        List<String> facetNames = new LinkedList<>();
        facetNames.add("A");
        facetNames.add("C");
        List<FacetData> facetData = new LinkedList<>();
        facetData.add(facetData("A", "dA"));
        facetData.add(facetData("B", "dB"));
        facetData.add(facetData("C", "dC"));
        facetData.add(facetData("D", "dD"));
        ((FullTextSearchData)Mockito.doReturn(facetData).when(this.fullTextSearchDataMock)).getFacets();
        List<FacetData> result = this.facetChartController.convertToFacets(facetNames);
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result).contains((Object[])new FacetData[] {facetData.get(0), facetData.get(2)});
        ((FullTextSearchData)Mockito.verify(this.fullTextSearchDataMock)).getFacets();
    }


    @Test
    public void shouldUpdateFiltersCounterLabel()
    {
        Map<String, Set<String>> selectedFacets = new HashMap<>();
        selectedFacets.put("facet", Collections.singleton("facetValue"));
        int filtersNumber = selectedFacets.size();
        this.facetChartController.updateFiltersCounter(selectedFacets);
        ((FacetChartController)Mockito.verify(this.facetChartController)).setFiltersCounterLabelValue(filtersNumber);
    }


    @Test
    public void shouldApplyFacetSelectionEvenIfSearchQueryNotExists()
    {
        Object originalQuery = null;
        ((FacetChartController)Mockito.doReturn(originalQuery).when(this.facetChartController)).getOriginalQuery();
        Set<String> facetSelected = new HashSet<>();
        this.facetChartController.applyFacetSelection("facetName", facetSelected);
        ((DefaultWidgetModel)Mockito.verify(this.widgetModel)).setValue((String)Matchers.eq("selectedFacets"), Matchers.any());
        ((FacetChartController)Mockito.verify(this.facetChartController)).updateFiltersCounter((Map)Matchers.any());
        ((FacetChartController)Mockito.verify(this.facetChartController)).setValue((String)Matchers.any(), Matchers.any());
        ((FacetChartController)Mockito.verify(this.facetChartController, Mockito.never())).createCopyAdvancedSearchData((AdvancedSearchData)Matchers.any());
    }


    @Test
    public void shouldTriggerRefreshDataWhenNewProductModelSaved()
    {
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        ((FacetChartController)Mockito.doReturn(Boolean.valueOf(true)).when(this.facetChartController)).isNewTheUpdatedObject((CockpitEvent)Matchers.any());
        AdvancedSearchData advancedSearchDataMock = (AdvancedSearchData)Mockito.mock(AdvancedSearchData.class);
        ((FacetChartController)Mockito.doReturn(advancedSearchDataMock).when(this.facetChartController)).createAdvancedSearchData();
        executeGlobalEvent("objectsUpdated", "session", new Object[] {new DefaultCockpitEvent("objectsUpdated", product, null)});
        assertSocketOutput("initialSearchData", 1, advancedSearchDataMock);
    }


    @Test
    public void shouldNotTriggerRefreshDataWhenProductModelIsNotNew()
    {
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        ((FacetChartController)Mockito.doReturn(Boolean.valueOf(false)).when(this.facetChartController)).isNewTheUpdatedObject((CockpitEvent)Matchers.any());
        AdvancedSearchData advancedSearchDataMock = (AdvancedSearchData)Mockito.mock(AdvancedSearchData.class);
        ((FacetChartController)Mockito.doReturn(advancedSearchDataMock).when(this.facetChartController)).createAdvancedSearchData();
        executeGlobalEvent("objectsUpdated", "session", new Object[] {new DefaultCockpitEvent("objectsUpdated", product, null)});
        assertSocketOutput("initialSearchData", 0, advancedSearchDataMock);
    }


    @Test
    public void shouldNotRefreshDataOnWrongModel()
    {
        ItemModel model = (ItemModel)Mockito.mock(ItemModel.class);
        ((FacetChartController)Mockito.doReturn(Boolean.valueOf(true)).when(this.facetChartController)).isNewTheUpdatedObject((CockpitEvent)Matchers.any());
        AdvancedSearchData advancedSearchDataMock = (AdvancedSearchData)Mockito.mock(AdvancedSearchData.class);
        ((FacetChartController)Mockito.doReturn(advancedSearchDataMock).when(this.facetChartController)).createAdvancedSearchData();
        executeGlobalEvent("objectsUpdated", "session", new Object[] {new DefaultCockpitEvent("objectsUpdated", model, null)});
        assertSocketOutput("initialSearchData", 0, advancedSearchDataMock);
    }


    @Test
    public void shouldDecrementFiltersCounterLabel()
    {
        Mockito.when(this.facetChartController.getFiltersCounterLabel()).thenReturn(Mockito.mock(Label.class));
        Mockito.when(this.facetChartController.getFiltersCounterLabel().getValue()).thenReturn("3");
        this.facetChartController.decrementFiltersCounterLabel();
        ((FacetChartController)Mockito.verify(this.facetChartController)).setFiltersCounterLabelValue(2);
    }


    @Test
    public void shouldNotDecrementFiltersCounterLabelWhenItsZero()
    {
        Mockito.when(this.facetChartController.getFiltersCounterLabel()).thenReturn(Mockito.mock(Label.class));
        Mockito.when(this.facetChartController.getFiltersCounterLabel().getValue()).thenReturn("0");
        this.facetChartController.decrementFiltersCounterLabel();
        ((FacetChartController)Mockito.verify(this.facetChartController)).setFiltersCounterLabelValue(0);
    }


    @Test
    public void shouldStoreValueInModelWhenSearchInitHasBeenPerformed()
    {
        this.facetChartController.initSearch();
        Assertions.assertThat((Boolean)this.widgetModel.getValue("initSearchPerformed", Boolean.class)).isEqualTo(true);
    }


    @Test
    public void shouldNotRenderWhenInitSearchWasNotInvoked()
    {
        this.widgetModel.setValue("pageable", this.fullTextSearchPageableMock);
        this.widgetModel.setValue("initSearchPerformed", null);
        this.facetChartController.render();
        ((FacetChartController)Mockito.verify(this.facetChartController, Mockito.never())).composeSeries((Series)Matchers.any());
    }


    @Test
    public void shouldRenderWhenInitSearchWasInvoked()
    {
        this.widgetModel.setValue("pageable", this.fullTextSearchPageableMock);
        this.widgetModel.setValue("initSearchPerformed", Boolean.valueOf(true));
        this.facetChartController.render();
        ((FacetChartController)Mockito.verify(this.facetChartController)).composeSeries((Series)Matchers.any());
    }


    private FacetData facetData(String name, String displayName)
    {
        return new FacetData(name, displayName, null, Collections.emptyList());
    }
}
