package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchMode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.async.Operation;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.search.data.FullTextSearchData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.search.data.facet.FacetData;
import com.hybris.cockpitng.search.data.pageable.FullTextSearchPageable;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.chart.Charts;
import org.zkoss.chart.ChartsEvent;
import org.zkoss.chart.Color;
import org.zkoss.chart.Point;
import org.zkoss.chart.Series;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;

public class FacetChartController extends DefaultWidgetController
{
    protected static final String EVENT_ON_POINT_CLICK = "onPointClick";
    protected static final String COMPONENT_SHOW_FILTERS_BUTTON_ID = "showFilters";
    protected static final String SELECTED_FACETS = "selectedFacets";
    protected static final String SOCKET_IN_PAGEABLE = "fullTextSearchPageable";
    protected static final String SOCKET_IN_INIT_SEARCH = "initSearch";
    protected static final String SOCKET_OUT_FACETS = "selectedFacets";
    protected static final String SOCKET_OUT_INITIAL_SEARCH_DATA = "initialSearchData";
    protected static final String SOCKET_OUT_INITIAL_SEARCH_CONTEXT = "initialSearchDataContext";
    protected static final String MODEL_FILTERS_COUNTER = "filtersCounter";
    private static final Logger LOG = LoggerFactory.getLogger(FacetChartController.class);
    private static final String TYPE_CODE_PRODUCT = "Product";
    private static final String MODEL_CURRENT_FACET = "currentFacet";
    private static final String MODEL_PAGEABLE = "pageable";
    private static final String MODEL_INIT_SEARCH_PERFORMED = "initSearchPerformed";
    private static final String SETTING_BOTTOM_PANEL_RENDERER = "bottomPanelRenderer";
    private static final String SETTING_FILTER_PANEL_RENDERER = "filterPanelRenderer";
    private static final Object SETTING_FACET_CHOOSER_RENDERER = "facetChooserRenderer";
    private static final String SETTING_RIGHT_PANEL_RENDERER = "rightPanelRenderer";
    private static final String SETTING_EXPORT_ENABLED = "exportEnabled";
    private static final String SETTING_FILTER_PANEL_RENDERERS = "filterPanelRendererNames";
    private static final String SETTING_CHART_DECORATOR_NAME = "chartDecorator";
    private static final String SETTING_FACETS_NAMES = "facetsNames";
    private static final String SETTING_CHART_TITLE_KEY = "chartTitleKey";
    private static final String SCLASS_FILTERS_COUNTER = "yw-solrfacetchart-toppanel-filters-counter";
    private static final String PROPERTY_SEARCH_STRATEGY = "backoffice.fulltext.search.strategy";
    private static final String COLORS_DELIMITER = ";";
    private static final String CHART_COLORS_LIST = "chartColors";
    @Wire
    private Charts charts;
    @Wire
    private Div bottomPanel;
    @Wire
    private Button showFilters;
    @Wire
    private Popup filtersPopup;
    @Wire
    private Div filtersContainer;
    @Wire
    private Div facetChooser;
    @Wire
    private Div rightPanel;
    @Wire
    private Label chartTitle;
    @Wire
    private Label filtersCounterLabel;
    @WireVariable
    private transient NotificationService notificationService;
    @WireVariable
    private transient FacetChartDataExtractor facetChartDataExtractor;
    private transient FacetChartBottomPanelRenderer facetChartBottomPanelRenderer;
    private transient DefaultFacetChartFiltersRenderer facetChartFiltersRenderer;
    private transient FacetChartFacetChooserRenderer facetChartFacetChooserRenderer;
    private transient FacetChartRightPanelRenderer rightPanelRenderer;
    private transient FacetChartComposer facetChartComposer;


    public void initialize(Component comp)
    {
        super.initialize(comp);
        initBeans();
        setChartTitle();
        applyChartColors(getWidgetInstanceManager(), this.charts);
        composeChart(this.charts);
        initCurrentFacet();
        renderFilters();
        initializeFiltersCounterLabel();
        processSearchResultAfterSearchExecution();
    }


    protected void initBeans()
    {
        this.facetChartBottomPanelRenderer = (FacetChartBottomPanelRenderer)BackofficeSpringUtil.getBean(getBottomPanelRendererName(), FacetChartBottomPanelRenderer.class);
        this.facetChartFiltersRenderer = (DefaultFacetChartFiltersRenderer)BackofficeSpringUtil.getBean(getFilterPanelRendererName(), DefaultFacetChartFiltersRenderer.class);
        this.facetChartFiltersRenderer.loadRenderers(getFilterRendererNames());
        this.facetChartFacetChooserRenderer = (FacetChartFacetChooserRenderer)BackofficeSpringUtil.getBean(getFacetChooserRendererName(), FacetChartFacetChooserRenderer.class);
        this.facetChartComposer = (FacetChartComposer)BackofficeSpringUtil.getBean(getChartComposerName(), FacetChartComposer.class);
        this.rightPanelRenderer = (FacetChartRightPanelRenderer)BackofficeSpringUtil.getBean(getRightPanelRendererName(), FacetChartRightPanelRenderer.class);
    }


    protected void setChartTitle()
    {
        String chartTitleKey = getWidgetSettings().getString("chartTitleKey");
        if(chartTitleKey != null)
        {
            getChartTitle().setValue(Labels.getLabel(chartTitleKey));
        }
    }


    protected void initCurrentFacet()
    {
        if(getCurrentFacet() == null)
        {
            if(getFacetNames() == null || getFacetNames().isEmpty())
            {
                LOG.warn("No facet is set, add at least one facet to configuration");
                storeCurrentFacet("");
                return;
            }
            storeCurrentFacet(getFacetNames().get(0));
        }
    }


    @Deprecated(since = "2005")
    @GlobalCockpitEvent(eventName = "onClientInfo", scope = "session")
    public void onViewInfo(DefaultCockpitEvent event)
    {
    }


    @GlobalCockpitEvent(eventName = "objectsDeleted", scope = "session")
    public void onFilterDeleted(CockpitEvent event)
    {
        this.facetChartFiltersRenderer.handleFilterDeletion(event, this::decrementFiltersCounterLabel);
    }


    protected void decrementFiltersCounterLabel()
    {
        Integer numberOfSelectedFilters = Integer.valueOf(getFiltersCounterLabel().getValue());
        setFiltersCounterLabelValue((numberOfSelectedFilters.intValue() > 0) ? (numberOfSelectedFilters.intValue() - 1) : 0);
    }


    @SocketEvent(socketId = "initSearch")
    public void initSearch()
    {
        DefaultContext defaultContext = new DefaultContext();
        defaultContext.addAttribute("preferredStrategyName",
                        Config.getString("backoffice.fulltext.search.strategy", "solr"));
        sendOutput("initialSearchDataContext", defaultContext);
        AdvancedSearchData advancedSearchData = createAdvancedSearchData();
        applySelectedFacets(advancedSearchData);
        sendOutput("initialSearchData", advancedSearchData);
        setValue("initSearchPerformed", Boolean.TRUE);
    }


    protected AdvancedSearchData createAdvancedSearchData()
    {
        AdvancedSearchData advancedSearchData = new AdvancedSearchData();
        advancedSearchData.setTypeCode("Product");
        advancedSearchData.setIncludeSubtypes(Boolean.valueOf(true));
        advancedSearchData.setGlobalOperator(ValueComparisonOperator.OR);
        advancedSearchData.setAdvancedSearchMode(AdvancedSearchMode.SIMPLE);
        advancedSearchData.setSearchQueryText("");
        advancedSearchData.setSelectedFacets(new HashMap<>());
        return advancedSearchData;
    }


    @InextensibleMethod
    private void applySelectedFacets(AdvancedSearchData advancedSearchData)
    {
        Map<String, Set<String>> facets = (Map<String, Set<String>>)getValue("selectedFacets", Map.class);
        if(facets != null && !facets.isEmpty())
        {
            advancedSearchData.setSelectedFacets(facets);
        }
    }


    @Deprecated(since = "2005", forRemoval = true)
    public void onInput(FullTextSearchPageable fullTextSearchPagable)
    {
        onInput((Pageable)fullTextSearchPagable);
    }


    @SocketEvent(socketId = "fullTextSearchPageable")
    public void onInput(Pageable pageable)
    {
        if(pageable instanceof FullTextSearchPageable)
        {
            executeSearchOperation((FullTextSearchPageable)pageable);
        }
        else
        {
            getModel().setValue("pageable", null);
            processSearchResultAfterSearchExecution();
        }
    }


    protected void executeSearchOperation(FullTextSearchPageable fullTextSearchPagable)
    {
        executeOperation((Operation)new Object(this, fullTextSearchPagable), ev -> processSearchResultAfterSearchExecution(), null);
    }


    protected void executeSearch(FullTextSearchPageable fullTextSearchPagable)
    {
        fullTextSearchPagable.getCurrentPage();
        fullTextSearchPagable.onPageLoaded();
    }


    protected void processSearchResultAfterSearchExecution()
    {
        notifyIfFullTextSearchFailed();
        FullTextSearchData fullTextSearchData = getFullTextSearchData();
        if(fullTextSearchData == null)
        {
            return;
        }
        render();
        renderFacetChooser();
    }


    protected void notifyIfFullTextSearchFailed()
    {
        FullTextSearchPageable ftp = (FullTextSearchPageable)getModel().getValue("pageable", FullTextSearchPageable.class);
        if(Objects.nonNull(ftp) && Objects.isNull(ftp.getFullTextSearchData()))
        {
            getNotificationService().notifyUser(
                            getNotificationService().getWidgetNotificationSource(getWidgetInstanceManager()), "General", NotificationEvent.Level.FAILURE, new Object[0]);
        }
    }


    @GlobalCockpitEvent(eventName = "objectsUpdated", scope = "session")
    public void onProductUpdate(CockpitEvent cockpitEvent)
    {
        Objects.requireNonNull(ProductModel.class);
        if(cockpitEvent.getDataAsCollection().stream().anyMatch(ProductModel.class::isInstance) &&
                        isNewTheUpdatedObject(cockpitEvent))
        {
            AdvancedSearchData advancedSearchData = createAdvancedSearchData();
            applySelectedFacets(advancedSearchData);
            sendOutput("initialSearchData", advancedSearchData);
        }
    }


    protected boolean isNewTheUpdatedObject(CockpitEvent event)
    {
        if(!(event instanceof DefaultCockpitEvent) || ((DefaultCockpitEvent)event).getContext() == null)
        {
            return false;
        }
        Map<String, Object> context = ((DefaultCockpitEvent)event).getContext();
        if(context.get("updatedObjectIsNew") instanceof Map)
        {
            Map isNewObjectMap = (Map)context.getOrDefault("updatedObjectIsNew", new HashMap<>());
            return event.getDataAsCollection().stream().anyMatch(object -> Boolean.TRUE.equals(isNewObjectMap.get(object)));
        }
        return Boolean.TRUE.equals(context.get("updatedObjectIsNew"));
    }


    protected void render()
    {
        if(getFullTextSearchData() == null || !isInitSearchPerformed())
        {
            LOG.warn("No data to show");
            return;
        }
        getCharts().getChildren().clear();
        Series series = getCharts().getSeries();
        List<Point> points = this.facetChartDataExtractor.getPoints(getFullTextSearchData(), getCurrentFacet());
        assignColorsToPoint(points);
        Point[] pointArray = new Point[points.size()];
        points.toArray(pointArray);
        series.setData(pointArray);
        composeSeries(series);
        renderFilters();
        renderBottomPanel(points);
        renderRightPanel(null);
    }


    protected void composeSeries(Series series)
    {
        if(this.facetChartComposer != null)
        {
            series.setName((String)getValue("currentFacet", String.class));
            this.facetChartComposer.composeSeries(series);
        }
    }


    protected void composeChart(Charts charts)
    {
        charts.addEventListener("onPlotClick", this::handleClickOnPoint);
        charts.getExporting().setEnabled(isExportEnabled());
        if(this.facetChartComposer != null)
        {
            this.facetChartComposer.composeChart(charts);
        }
    }


    protected void assignColorsToPoint(List<Point> points)
    {
        int i = 0;
        List<Color> colors = this.charts.getColors();
        for(Point p : points)
        {
            if(colors.size() <= i)
            {
                i = 0;
            }
            p.setColor(colors.get(i));
            i++;
        }
    }


    protected void renderBottomPanel(List<Point> points)
    {
        if(this.facetChartBottomPanelRenderer != null)
        {
            this.facetChartBottomPanelRenderer.render(this.bottomPanel, points, this::handleClickOnPoint);
        }
    }


    protected void renderFilters()
    {
        if(this.facetChartFiltersRenderer == null)
        {
            return;
        }
        if(this.facetChartFiltersRenderer.getRenderers().isEmpty())
        {
            showFilterComponents(false);
        }
        else
        {
            this.facetChartFiltersRenderer.renderFilters(getWidgetInstanceManager(), this.filtersContainer, this::applyFacetSelection);
            showFilterComponents(true);
        }
    }


    protected void showFilterComponents(boolean visibility)
    {
        this.filtersContainer.setVisible(visibility);
        this.filtersCounterLabel.setVisible(visibility);
        this.showFilters.setVisible(visibility);
    }


    protected void renderFacetChooser()
    {
        if(this.facetChartFacetChooserRenderer != null)
        {
            this.facetChartFacetChooserRenderer.render(this.facetChooser, getWidgetInstanceManager(), this::onFacetChange,
                            convertToFacets(getFacetNames()));
        }
    }


    protected List<FacetData> convertToFacets(List<String> facetNames)
    {
        List<FacetData> result = new LinkedList<>();
        if(getFullTextSearchData() == null)
        {
            return result;
        }
        getFullTextSearchData().getFacets().forEach(facet -> facetNames.stream().filter(()).forEach(()));
        return result;
    }


    @Deprecated(since = "2005")
    protected void renderRightPanel(FullTextSearchPageable fullTextSearchPageable)
    {
        renderRightPanel();
    }


    protected boolean isInitSearchPerformed()
    {
        return BooleanUtils.isTrue((Boolean)getValue("initSearchPerformed", Boolean.class));
    }


    protected void renderRightPanel()
    {
        FullTextSearchPageable ftp = (FullTextSearchPageable)getModel().getValue("pageable", FullTextSearchPageable.class);
        if(this.rightPanelRenderer != null)
        {
            this.rightPanelRenderer.render(this.rightPanel, getWidgetInstanceManager(), ftp);
        }
    }


    protected void onFacetChange(String facetName)
    {
        storeCurrentFacet(facetName);
        render();
    }


    protected void handleClickOnPoint(Event event)
    {
        Optional<Point> point = extractEventData(event);
        if(getOriginalQuery() instanceof AdvancedSearchData && point.isPresent())
        {
            applyFacetSelection((AdvancedSearchData)getOriginalQuery(), point.get());
        }
    }


    protected Optional<Point> extractEventData(Event event)
    {
        Point point = null;
        if(event instanceof ChartsEvent)
        {
            point = ((ChartsEvent)event).getPoint();
        }
        else if("onPointClick".equals(event.getName()))
        {
            point = (Point)event.getData();
        }
        return Optional.ofNullable(point);
    }


    protected void applyFacetSelection(AdvancedSearchData query, Point point)
    {
        AdvancedSearchData queryData = createCopyAdvancedSearchData(query);
        queryData.setTokenizable(true);
        if(queryData.getSelectedFacets() != null)
        {
            Map<String, Set<String>> selectedFacets = new HashMap<>(queryData.getSelectedFacets());
            selectedFacets.put(getCurrentFacet(), Collections.singleton(point.getId()));
            queryData.setSelectedFacets(selectedFacets);
        }
        queryData.setAdvancedSearchMode(AdvancedSearchMode.SIMPLE);
        sendOutput("selectedFacets", queryData);
    }


    @ViewEvent(eventName = "onClick", componentID = "showFilters")
    public void showFilters()
    {
        this.filtersPopup.open((Component)this.showFilters, "after_end");
    }


    protected void applyFacetSelection(String facetName, Set<String> selectedFacets)
    {
        Validate.notNull("Selected facets can not be null", new Object[] {selectedFacets});
        Map<String, Set<String>> alreadySelected = calculateSelectedFacets(facetName, selectedFacets);
        updateFiltersCounter(alreadySelected);
        setValue("selectedFacets", alreadySelected);
        Object originalQuery = getOriginalQuery();
        if(originalQuery == null || !(originalQuery instanceof AdvancedSearchData))
        {
            return;
        }
        AdvancedSearchData queryData = createCopyAdvancedSearchData((AdvancedSearchData)getOriginalQuery());
        queryData.setTokenizable(true);
        queryData.setSelectedFacets(alreadySelected);
        queryData.setAdvancedSearchMode(AdvancedSearchMode.SIMPLE);
        sendOutput("initialSearchData", queryData);
    }


    @InextensibleMethod
    private Map<String, Set<String>> calculateSelectedFacets(String facetName, Set<String> selectedFacets)
    {
        Map<String, Set<String>> alreadySelected = getSelectedFacetsCopy();
        if(selectedFacets.isEmpty())
        {
            alreadySelected.remove(facetName);
        }
        else
        {
            alreadySelected.put(facetName, selectedFacets);
        }
        return alreadySelected;
    }


    @InextensibleMethod
    private Map<String, Set<String>> getSelectedFacetsCopy()
    {
        HashMap<String, Set<String>> newFilters;
        Map<String, Set<String>> alreadySelected = (Map<String, Set<String>>)getValue("selectedFacets", Map.class);
        if(alreadySelected == null)
        {
            newFilters = new HashMap<>();
        }
        else
        {
            newFilters = new HashMap<>(alreadySelected);
        }
        return newFilters;
    }


    protected AdvancedSearchData createCopyAdvancedSearchData(AdvancedSearchData data)
    {
        return new AdvancedSearchData(data);
    }


    protected List<String> getFacetNames()
    {
        String facetsFromSettings = getWidgetInstanceManager().getWidgetSettings().getString("facetsNames");
        if(StringUtils.isBlank(facetsFromSettings))
        {
            return Collections.emptyList();
        }
        return Arrays.asList(facetsFromSettings.split(","));
    }


    protected void initializeFiltersCounterLabel()
    {
        getFiltersCounterLabel().setSclass("yw-solrfacetchart-toppanel-filters-counter");
        Integer numberOfFilters = (Integer)getModel().getValue("filtersCounter", Integer.class);
        if(numberOfFilters == null)
        {
            setFiltersCounterLabelValue(0);
        }
        else
        {
            setFiltersCounterLabelValue(numberOfFilters.intValue());
        }
    }


    protected void setFiltersCounterLabelValue(int numberOfFilters)
    {
        String numberOfFiltersAsString = String.valueOf(numberOfFilters);
        getFiltersCounterLabel().setValue(numberOfFiltersAsString);
    }


    protected void saveFiltersCounterModelValue(int numberOfFilters)
    {
        getModel().setValue("filtersCounter", Integer.valueOf(numberOfFilters));
    }


    protected void updateFiltersCounter(Map<String, Set<String>> alreadySelected)
    {
        int numberOfFilters = alreadySelected.values().stream().mapToInt(Set::size).sum();
        setFiltersCounterLabelValue(numberOfFilters);
        saveFiltersCounterModelValue(numberOfFilters);
    }


    protected String getBottomPanelRendererName()
    {
        return getWidgetInstanceManager().getWidgetSettings().getString("bottomPanelRenderer");
    }


    protected String getFilterPanelRendererName()
    {
        return getWidgetInstanceManager().getWidgetSettings().getString("filterPanelRenderer");
    }


    protected String getFacetChooserRendererName()
    {
        return getWidgetInstanceManager().getWidgetSettings().getString(SETTING_FACET_CHOOSER_RENDERER);
    }


    protected String getChartComposerName()
    {
        return getWidgetInstanceManager().getWidgetSettings().getString("chartDecorator");
    }


    protected boolean isExportEnabled()
    {
        return getWidgetInstanceManager().getWidgetSettings().getBoolean("exportEnabled");
    }


    protected List<String> getFilterRendererNames()
    {
        String rendererNames = getWidgetInstanceManager().getWidgetSettings().getString("filterPanelRendererNames");
        if(StringUtils.isBlank(rendererNames))
        {
            return Collections.emptyList();
        }
        return Arrays.asList(rendererNames.split(","));
    }


    protected String getRightPanelRendererName()
    {
        return getWidgetInstanceManager().getWidgetSettings().getString("rightPanelRenderer");
    }


    protected String getCurrentFacet()
    {
        return (String)getValue("currentFacet", String.class);
    }


    protected void storeCurrentFacet(String curentFacet)
    {
        setValue("currentFacet", curentFacet);
    }


    protected Charts getCharts()
    {
        return this.charts;
    }


    protected Div getBottomPanel()
    {
        return this.bottomPanel;
    }


    protected void setFacetChartDataExtractor(FacetChartDataExtractor facetChartDataExtractor)
    {
        this.facetChartDataExtractor = facetChartDataExtractor;
    }


    protected void setFacetChartBottomPanelRenderer(FacetChartBottomPanelRenderer facetChartBottomPanelRenderer)
    {
        this.facetChartBottomPanelRenderer = facetChartBottomPanelRenderer;
    }


    protected Button getShowFilters()
    {
        return this.showFilters;
    }


    protected Popup getFiltersPopup()
    {
        return this.filtersPopup;
    }


    protected Div getFiltersContainer()
    {
        return this.filtersContainer;
    }


    protected Div getFacetChooser()
    {
        return this.facetChooser;
    }


    protected void setFacetChooser(Div facetChooser)
    {
        this.facetChooser = facetChooser;
    }


    protected Div getRightPanel()
    {
        return this.rightPanel;
    }


    protected Label getChartTitle()
    {
        return this.chartTitle;
    }


    protected Label getFiltersCounterLabel()
    {
        return this.filtersCounterLabel;
    }


    protected FullTextSearchData getFullTextSearchData()
    {
        FullTextSearchPageable ftp = (FullTextSearchPageable)getModel().getValue("pageable", FullTextSearchPageable.class);
        if(ftp == null)
        {
            return new FullTextSearchData(CollectionUtils.EMPTY_COLLECTION, "");
        }
        FullTextSearchData fullTextSearchData = ftp.getFullTextSearchData();
        if(Objects.isNull(fullTextSearchData))
        {
            fullTextSearchData = new FullTextSearchData(CollectionUtils.EMPTY_COLLECTION, "");
        }
        return fullTextSearchData;
    }


    protected Object getOriginalQuery()
    {
        FullTextSearchData fullTextSearchData = getFullTextSearchData();
        if(fullTextSearchData != null)
        {
            return fullTextSearchData.getContext().getAttribute("originalQuery");
        }
        return null;
    }


    protected void applyChartColors(WidgetInstanceManager wim, Charts charts)
    {
        List<Color> colors = new ArrayList<>();
        String colorsCollection = wim.getWidgetSettings().getString("chartColors");
        if(StringUtils.isNotBlank(colorsCollection))
        {
            String[] colorsList = colorsCollection.split(";");
            for(String color : colorsList)
            {
                colors.add(new Color(color));
            }
            charts.setColors(colors);
        }
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }


    public void setNotificationService(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }
}
