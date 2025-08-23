/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.fulltextsearch;

import com.google.common.collect.Lists;
import com.hybris.backoffice.widgets.advancedsearch.AbstractSearchController;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchMode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.fulltextsearch.renderer.FullTextSearchFilter;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.config.fulltextsearch.jaxb.FulltextSearch;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.search.impl.FieldSearchFacadeStrategyRegistry;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.search.data.FullTextSearchData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.search.data.facet.FacetData;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;

/**
 * Widget controller of the <code>full text search</code> widget.
 */
public class FullTextSearchController extends AbstractSearchController
{
    public static final String SOCKET_IN_INIT_CONTEXT = "initContext";
    public static final String SOCKET_IN_FULL_TEXT_SEARCH_DATA = "fullTextSearchData";
    public static final String SOCKET_IN_TYPE = "type";
    public static final String SOCKET_IN_AUTO_SUGGESTIONS = "autosuggestions";
    public static final String SOCKET_IN_RESET = "reset";
    public static final String SOCKET_IN_CLEAR = "clear";
    public static final String SOCKET_IN_UPDATE_FILTER_CONDITION = "updateFilterCondition";
    public static final String SOCKET_IN_RESET_SEARCH_DATA = "resetSearchData";
    public static final String SOCKET_OUT_SEARCH_CONTEXT_CHANGED = "searchContextChanged";
    public static final String SOCKET_OUT_UPDATE_SEARCH_DATA = "updateSearchData";
    public static final String MODEL_PREFERRED_STRATEGY = "strategy";
    public static final String CONFIG_CONTEXT_STRATEGY = "strategy";
    protected static final String PROPERTY_SEARCH_STRATEGY = "fulltext.search.strategy";
    public static final String MODEL_FIELD_QUERY_POPUP_FILTERS = "fieldQueryFilters";
    public static final String EVENT_ON_APPLY_FILTERS = "onApplyFilters";
    public static final String NOTIFICATION_EVENT_TYPE_FILTER_INVALID = "filterInvalid";
    protected static final String MODEL_KEY_FULLTEXTSEARCH_IS_EXPANDED = "searchboxExpanded";
    protected static final String MODEL_KEY_MOUSE_OVER_SEARCH_BUTTON = "_mouse_over_searchButton";
    protected static final String MODEL_DATA_TYPE = "dataType";
    protected static final String MODEL_KEY_SELECTED_TYPE = "selectedType";
    protected static final String MODEL_FILTERS_COUNTER = "filtersCounter";
    protected static final String FIELD_QUERY_POPUP = "fieldQueryPopup";
    protected static final String MODEL_FIELD_QUERIES = "fieldQueries";
    protected static final String MODEL_ADDITION_FIELD_QUERIES = "additionFieldQueries";
    private static final String CONDITIONS = "conditions";
    private static final String CONTEXT = "context";
    protected static final String SETTING_FULLTEXT_SEARCH_CONFIG_CTX_CODE = "fulltextSearchConfigCtxCode";
    protected static final String DEFAULT_VALUE_FULLTEXT_SEARCH_CONFIG_CTX_CODE = "fulltext-search";
    protected static final String SETTING_FIELD_QUERY_ENABLED = "fieldQueryEnabled";
    protected static final String SETTING_PREFERRED_SEARCH_STRATEGY = "preferredSearchStrategy";
    protected static final Boolean DEFAULT_VALUE_FIELD_QUERY_ENABLED = Boolean.TRUE;
    protected static final String MODEL_KEY_SELECTED_FACETS = "selectedFacets";
    private static final String SCLASS_CONTAINER_EXPANDED = "yw-fulltextsearch-container-expanded";
    private static final String SCLASS_CONTAINER_COLLAPSED = "yw-fulltextsearch-container-collapsed";
    private static final String SCLASS_FILTERS_COUNTER = "yw-fulltextsearch-filters-counter";
    private static final String LABEL_FIELDQUERY_BUTTON_TOOLTIP = "fieldquerybutton.tooltip";
    private transient WidgetComponentRenderer<Popup, FulltextSearch, AdvancedSearchData> fieldQueryPopupRenderer;
    @WireVariable
    private transient NotificationService notificationService;
    @WireVariable
    private transient CockpitProperties cockpitProperties;
    @WireVariable
    private transient SearchFilterValidationStrategyRegistry searchFilterValidationStrategyRegistry;
    @Wire
    private Div fullTextSearchContainer;
    @Wire
    private Div fullTextSearchBox;
    @Wire
    private Button searchButton;
    @Wire
    private Button fieldQueryButton;
    @Wire
    private Label fieldQueryFiltersCounterLabel;
    @Wire
    private Popup fieldQueryPopup;
    @Wire
    private Button clearButton;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        UITools.modifySClass(fullTextSearchContainer, SCLASS_CONTAINER_COLLAPSED, true);
        UITools.modifySClass(fullTextSearchContainer, SCLASS_CONTAINER_EXPANDED, false);
        searchBox.setText(getValue(SIMPLE_SEARCH_TEXT_QUERY, String.class));
        final boolean isFullTextSearchExpanded = StringUtils.isNotBlank(searchBox.getValue());
        setValue(MODEL_KEY_FULLTEXTSEARCH_IS_EXPANDED, Boolean.valueOf(isFullTextSearchExpanded));
        updateSearchBoxState(isFullTextSearchExpanded);
        clearButton.setVisible(isFullTextSearchExpanded);
        searchButton.addEventListener(Events.ON_CLICK, this::onSearchButtonClick);
        searchButton.addEventListener(Events.ON_MOUSE_OVER, this::onSearchButtonMouseOver);
        searchButton.addEventListener(Events.ON_MOUSE_OUT, this::onSearchButtonMouseOut);
        searchBox.addEventListener(Events.ON_BLUR, this::onSearchBoxBlur);
        searchBox.addEventListener(Events.ON_OK, this::onSearchBoxOk);
        searchBox.addEventListener(Events.ON_CHANGE, this::onSearchBoxValueChange);
        clearButton.addEventListener(Events.ON_CLICK, this::onClearButtonClick);
        if(isFieldQueryEnabled())
        {
            initializeFieldQueryComponents();
            initializeFieldQueryFiltersCounterLabel();
        }
        initAutoSuggestionAndAutoCorrectionComponents();
    }


    @ViewEvent(componentID = FIELD_QUERY_POPUP, eventName = EVENT_ON_APPLY_FILTERS)
    public void onApplyFilters(final Event event)
    {
        if(canProcessFilterChangeEvent(event))
        {
            final Map<String, FullTextSearchFilter> filters = (Map<String, FullTextSearchFilter>)event.getData();
            setValue(MODEL_FIELD_QUERY_POPUP_FILTERS, filters);
            final List<SearchConditionData> conditions = buildSearchConditionData(filters);
            updateFilterCounter(filters);
            setValue(MODEL_FIELD_QUERIES, conditions);
            doSearch();
        }
    }


    protected void clearAppliedFilters()
    {
        setValue(MODEL_FIELD_QUERY_POPUP_FILTERS, Collections.emptyMap());
        setValue(MODEL_FIELD_QUERIES, Collections.emptyList());
        doSearch();
    }


    protected void updateFilterCounter(final Map<String, FullTextSearchFilter> filters)
    {
        final int numberOfFilters = (int)filters.values().stream().filter(FullTextSearchFilter::isEnabled).count();
        setFiltersCounterLabelValue(numberOfFilters);
        saveFiltersCounterModelValue(numberOfFilters);
    }


    protected void setFiltersCounterLabelValue(final int numberOfFilters)
    {
        final String numberOfFiltersAsString = String.valueOf(numberOfFilters);
        getFieldQueryFiltersCounterLabel().setValue(numberOfFiltersAsString);
    }


    protected void saveFiltersCounterModelValue(final int numberOfFilters)
    {
        getModel().setValue(MODEL_FILTERS_COUNTER, Integer.valueOf(numberOfFilters));
    }


    protected List<SearchConditionData> buildSearchConditionData(final Map<String, FullTextSearchFilter> filters)
    {
        final List<SearchConditionData> conditions = Lists.newArrayList();
        filters.values().stream().filter(FullTextSearchFilter::isEnabled).filter(this::isNotEmptyFilterConditions)
                        .forEach(filter -> {
                            final FieldType fieldType = new FieldType();
                            fieldType.setName(filter.getName());
                            final SearchConditionData condition = new SearchConditionData(fieldType, filter.getValue(), filter.getOperator());
                            clearLocalizedValues(filter.getValue(), filter.getLocale());
                            conditions.add(condition);
                        });
        return conditions;
    }


    @InextensibleMethod
    private boolean isNotEmptyFilterConditions(final FullTextSearchFilter filter)
    {
        if(filter.getValue() instanceof Map)
        {
            final Map<Locale, Object> localizedValue = (Map)filter.getValue();
            return !filter.getOperator().isRequireValue() || localizedValue.values().stream().noneMatch(StringUtils.EMPTY::equals);
        }
        return !filter.getOperator().isRequireValue() || filter.getValue() != null;
    }


    protected void clearLocalizedValues(final Object value, final Locale locale)
    {
        if(locale != null && value != null && value instanceof Map)
        {
            final Map<Locale, Object> localizedValues = (Map<Locale, Object>)value;
            if(!localizedValues.isEmpty())
            {
                localizedValues.entrySet().removeIf(entry -> !entry.getKey().equals(locale));
            }
        }
    }


    protected boolean isFieldQueryEnabled()
    {
        return ((Boolean)getWidgetSettings().getOrDefault(SETTING_FIELD_QUERY_ENABLED, DEFAULT_VALUE_FIELD_QUERY_ENABLED))
                        .booleanValue();
    }


    protected void initializeFieldQueryComponents()
    {
        getFieldQueryButton().setVisible(isFieldQueryEnabled());
        getFieldQueryButton().setTooltiptext(getLabel(LABEL_FIELDQUERY_BUTTON_TOOLTIP));
        getFieldQueryButton().addEventListener(Events.ON_CLICK, e -> {
            adjustFieldQuery();
            getFieldQueryPopup().open(e.getTarget(), "after_end");
        });
    }


    public void changeSearchContext(final String strategy)
    {
        final Context context = new DefaultContext();
        context.addAttribute(FieldSearchFacadeStrategyRegistry.CONTEXT_ATTR_PREFERRED_STRATEGY_NAME, strategy);
        sendOutput(SOCKET_OUT_SEARCH_CONTEXT_CHANGED, context);
    }


    protected String getPreferredSearchStrategy()
    {
        return getValue(MODEL_PREFERRED_STRATEGY, String.class);
    }


    protected void setPreferredSearchStrategy(final String strategy)
    {
        setValue(MODEL_PREFERRED_STRATEGY, strategy);
        if(isFieldQueryEnabled())
        {
            final boolean newStrategyIsNotEmpty = StringUtils.isNotEmpty(strategy);
            final boolean strategyInitialized = StringUtils.isNotEmpty(getPreferredSearchStrategy());
            final boolean strategyChanged = !getPreferredSearchStrategy().equals(strategy);
            if(strategyInitialized && newStrategyIsNotEmpty && strategyChanged)
            {
                clearAppliedFilters();
            }
            adjustFieldQuery();
        }
        changeSearchContext(strategy);
    }


    @SocketEvent(socketId = SOCKET_IN_INIT_CONTEXT)
    public void initializeWithContext(final AdvancedSearchInitContext initContext)
    {
        if(initContext == null)
        {
            return;
        }
        setValue(MODEL_INIT_CONTEXT, initContext);
        final AdvancedSearchData searchData = initContext.getAdvancedSearchData();
        final DataType dataType = loadDataTypeForCode(searchData.getTypeCode());
        handlePreferredSearchStrategy(searchData);
        final AdvancedSearchData copyOfSearchData = new AdvancedSearchData(searchData);
        adjustWidgetModel(null, copyOfSearchData, true, dataType);
        adjustFieldQuery();
        doSearch();
    }


    protected void handlePreferredSearchStrategy(final AdvancedSearchData searchData)
    {
        final FulltextSearch configuration = loadFullTextConfiguration(searchData.getTypeCode());
        String preferredSearchStrategy = configuration.getPreferredSearchStrategy();
        if(StringUtils.isBlank(preferredSearchStrategy))
        {
            preferredSearchStrategy = (String)getWidgetSettings().getOrDefault(SETTING_PREFERRED_SEARCH_STRATEGY, cockpitProperties.getProperty(PROPERTY_SEARCH_STRATEGY));
        }
        if(StringUtils.isNotBlank(preferredSearchStrategy))
        {
            setPreferredSearchStrategy(preferredSearchStrategy);
        }
    }


    protected void initializeFieldQueryFiltersCounterLabel()
    {
        getFieldQueryFiltersCounterLabel().setSclass(SCLASS_FILTERS_COUNTER);
        final Integer numberOfFilters = getModel().getValue(MODEL_FILTERS_COUNTER, Integer.class);
        if(numberOfFilters == null)
        {
            getFieldQueryFiltersCounterLabel().setValue("0");
        }
        else
        {
            getFieldQueryFiltersCounterLabel().setValue(String.valueOf(numberOfFilters));
        }
    }


    protected void adjustFieldQuery()
    {
        final AdvancedSearchInitContext initContext = getValue(MODEL_INIT_CONTEXT, AdvancedSearchInitContext.class);
        if(isFieldQueryEnabled())
        {
            getFieldQueryPopup().getChildren().clear();
            if(getCurrentDataType() != null)
            {
                final FulltextSearch configuration = loadFullTextConfiguration(getCurrentDataType().getCode());
                if(StringUtils.isEmpty(configuration.getPreferredSearchStrategy()))
                {
                    configuration.setPreferredSearchStrategy(cockpitProperties.getProperty(PROPERTY_SEARCH_STRATEGY));
                }
                final AdvancedSearchData searchData = initContext != null ? initContext.getAdvancedSearchData() : null;
                getFieldQueryPopupRenderer().render(getFieldQueryPopup(), configuration, searchData, getCurrentDataType(),
                                getWidgetInstanceManager());
            }
        }
    }


    protected FulltextSearch loadFullTextConfiguration(final String type)
    {
        final String configCtxCode = StringUtils.defaultIfBlank(
                        getWidgetSettings().getString(SETTING_FULLTEXT_SEARCH_CONFIG_CTX_CODE),
                        DEFAULT_VALUE_FULLTEXT_SEARCH_CONFIG_CTX_CODE);
        final DefaultConfigContext configContext = new DefaultConfigContext(configCtxCode, StringUtils.trim(type));
        return loadConfiguration(configContext, FulltextSearch.class);
    }


    @SocketEvent(socketId = SOCKET_IN_FULL_TEXT_SEARCH_DATA)
    public void onFullTextSearchData(final FullTextSearchData fullTextSearchData)
    {
        if(fullTextSearchData == null || isSearchBoxOrRelatedViewsNull())
        {
            return;
        }
        extractAndRememberSelectedFacets(fullTextSearchData.getFacets());
        processFullTextSearchData(fullTextSearchData);
    }


    @SocketEvent(socketId = SOCKET_IN_TYPE)
    public void changeType(final String typeCode)
    {
        final String previousTypeCode = getValue(MODEL_KEY_SELECTED_TYPE, String.class);
        if(!StringUtils.equals(typeCode, previousTypeCode))
        {
            setValue(MODEL_KEY_SELECTED_TYPE, typeCode);
            setValue(SIMPLE_SEARCH_TEXT_QUERY, StringUtils.EMPTY);
            searchBox.setText(StringUtils.EMPTY);
            final AdvancedSearchData searchData = new AdvancedSearchData();
            searchData.setTypeCode(typeCode);
            initializeWithContext(new AdvancedSearchInitContext(searchData));
        }
    }


    @SocketEvent(socketId = SOCKET_IN_AUTO_SUGGESTIONS)
    public void onAutoSuggestions(final Map<String, Collection> autoSuggestions)
    {
        processAutoSuggestions(autoSuggestions);
    }


    @SocketEvent(socketId = SOCKET_IN_RESET)
    public void reset()
    {
        setValue(MODEL_KEY_SELECTED_FACETS, new HashMap<>());
    }


    @SocketEvent(socketId = SOCKET_IN_CLEAR)
    public void clear()
    {
        setValue(MODEL_FILTERS_COUNTER, 0);
        setValue(MODEL_FIELD_QUERY_POPUP_FILTERS, Collections.emptyMap());
        setValue(MODEL_FIELD_QUERIES, Collections.emptyList());
        setValue(MODEL_ADDITION_FIELD_QUERIES, Collections.emptyList());
        onClearButtonClick(null);
        initializeFieldQueryFiltersCounterLabel();
    }


    @SocketEvent(socketId = SOCKET_IN_UPDATE_FILTER_CONDITION)
    public void updateFilterCondition(final List<SearchConditionData> conditions)
    {
        setValue(MODEL_ADDITION_FIELD_QUERIES, conditions);
        if(searchBox != null && getValue(SEARCH_MODEL, AdvancedSearchData.class) != null)
        {
            sendOutput(SOCKET_OUT_UPDATE_SEARCH_DATA, buildQueryData());
        }
    }


    @SocketEvent(socketId = SOCKET_IN_RESET_SEARCH_DATA)
    public void resetSearchData(final Map<String, Object> data)
    {
        if(data != null)
        {
            setValue(MODEL_ADDITION_FIELD_QUERIES, data.get(CONDITIONS));
            initializeWithContext((AdvancedSearchInitContext)data.get(CONTEXT));
        }
    }


    protected void updateFiltersCounterLabel(final Event event)
    {
        if(canProcessFilterChangeEvent(event))
        {
            final int numberOfFilters = ((HashMap)event.getData()).size();
            setFiltersCounterLabelValue(numberOfFilters);
        }
    }


    protected void saveFiltersCounterInModel(final Event event)
    {
        if(canProcessFilterChangeEvent(event))
        {
            final Integer numberOfFilters = Integer.valueOf(((HashMap)event.getData()).size());
            saveFiltersCounterModelValue(numberOfFilters.intValue());
        }
    }


    protected boolean canProcessFilterChangeEvent(final Event event)
    {
        return event != null && event.getData() != null && event.getData() instanceof Map;
    }


    protected void onSearchButtonMouseOut(final Event event)
    {
        setValue(MODEL_KEY_MOUSE_OVER_SEARCH_BUTTON, Boolean.FALSE);
    }


    protected void onSearchButtonMouseOver(final Event event)
    {
        setValue(MODEL_KEY_MOUSE_OVER_SEARCH_BUTTON, Boolean.TRUE);
    }


    protected void onClearButtonClick(final Event event)
    {
        searchBox.setValue(StringUtils.EMPTY);
        updateSearchBoxState(false);
        clearButton.setVisible(false);
        doSimpleSearch();
    }


    protected void onSearchBoxValueChange(final Event event)
    {
        if(event instanceof InputEvent)
        {
            clearButton.setVisible(StringUtils.isNotBlank(((InputEvent)event).getValue()));
        }
    }


    protected boolean onSearchBoxOk(final Event event)
    {
        return doSearch();
    }


    protected void onSearchBoxBlur(final Event event)
    {
        if(StringUtils.isBlank(searchBox.getValue())
                        && BooleanUtils.isNotTrue(getValue(MODEL_KEY_MOUSE_OVER_SEARCH_BUTTON, Boolean.class)))
        {
            updateSearchBoxState(false);
        }
    }


    protected void onSearchButtonClick(final Event event)
    {
        searchBox.setFocus(true);
        if(BooleanUtils.isTrue(getValue(MODEL_KEY_FULLTEXTSEARCH_IS_EXPANDED, Boolean.class)))
        {
            doSearch();
        }
        else
        {
            updateSearchBoxState(true);
        }
    }


    @InextensibleMethod
    private void updateSearchBoxState(final boolean isFulltextsearchExpanded)
    {
        setValue(MODEL_KEY_FULLTEXTSEARCH_IS_EXPANDED, Boolean.valueOf(isFulltextsearchExpanded));
        UITools.modifySClass(fullTextSearchContainer, SCLASS_CONTAINER_COLLAPSED, !isFulltextsearchExpanded);
        UITools.modifySClass(fullTextSearchContainer, SCLASS_CONTAINER_EXPANDED, isFulltextsearchExpanded);
    }


    protected boolean doSearch()
    {
        setValue(SIMPLE_SEARCH_TEXT_QUERY, searchBox.getValue());
        return doSimpleSearch();
    }


    @Override
    protected void adjustWidgetModel(final AdvancedSearch advancedSearch, final AdvancedSearchData searchData,
                    final boolean rootTypeChanged, final DataType dataType)
    {
        setValue(SEARCH_MODEL, searchData);
        setValue(MODEL_DATA_TYPE, dataType);
    }


    @Override
    protected AdvancedSearchData createAdvancedSearchDataWithInitContext()
    {
        final AdvancedSearchData result = super.createAdvancedSearchDataWithInitContext();
        final ValueComparisonOperator operator = handlePreferredGlobalOperator(result);
        result.setGlobalOperator(operator);
        return result;
    }


    protected ValueComparisonOperator handlePreferredGlobalOperator(final AdvancedSearchData searchData)
    {
        final FulltextSearch configuration = loadFullTextConfiguration(searchData.getTypeCode());
        return ValueComparisonOperator.valueOf(configuration.getOperator().name());
    }


    @Override
    protected boolean doSimpleSearch()
    {
        boolean searchExecuted = false;
        if(searchBox != null)
        {
            sendOutput(SOCKET_OUT_SEARCH_DATA, buildQueryData());
            searchExecuted = true;
        }
        return searchExecuted;
    }


    protected void applyFilters(final AdvancedSearchData queryData)
    {
        final Set<FullTextSearchFilter> invalidFilters = getInvalidFilters(queryData.getTypeCode());
        final Set<String> invalidFiltersNames = invalidFilters.stream().map(FullTextSearchFilter::getName)
                        .collect(Collectors.toSet());
        final List<SearchConditionData> conditions = getValue(MODEL_FIELD_QUERIES, List.class);
        if(conditions != null)
        {
            conditions.forEach(condition -> {
                if(!invalidFiltersNames.contains(condition.getFieldType().getName()))
                {
                    queryData.addFilterQueryRawCondition(condition.getFieldType(), condition.getOperator(), condition.getValue());
                }
            });
        }
        handleInvalidFilters(invalidFilters);
    }


    protected void applyAdditionFilters(final AdvancedSearchData queryData)
    {
        final Set<FullTextSearchFilter> invalidFilters = getInvalidFilters(queryData.getTypeCode());
        final Set<String> invalidFiltersNames = invalidFilters.stream().map(FullTextSearchFilter::getName)
                        .collect(Collectors.toSet());
        final List<SearchConditionData> conditions = getValue(MODEL_ADDITION_FIELD_QUERIES, List.class);
        if(conditions != null && !conditions.isEmpty())
        {
            conditions.forEach(condition -> {
                if(!invalidFiltersNames.contains(condition.getFieldType().getName()))
                {
                    queryData.addFilterQueryRawCondition(condition.getFieldType(), condition.getOperator(), condition.getValue());
                }
            });
        }
        handleInvalidFilters(invalidFilters);
    }


    protected AdvancedSearchData buildQueryData()
    {
        if(searchBox != null)
        {
            final String query = StringUtils.defaultIfBlank(getSearchText(), StringUtils.EMPTY);
            setValue(SIMPLE_SEARCH_TEXT_QUERY, query);
            final AdvancedSearchData searchData = getValue(SEARCH_MODEL, AdvancedSearchData.class);
            final AdvancedSearchData queryData = buildQueryData(query, searchData.getTypeCode());
            queryData.setTokenizable(true);
            queryData.setAdvancedSearchMode(AdvancedSearchMode.SIMPLE);
            queryData.setSelectedFacets(getValue(MODEL_KEY_SELECTED_FACETS, Map.class));
            applyFilters(queryData);
            applyAdditionFilters(queryData);
            return queryData;
        }
        return null;
    }


    protected Set<FullTextSearchFilter> getInvalidFilters(final String typeCode)
    {
        final SearchFilterValidationStrategy filterValidationStrategy = getSearchFilterValidationStrategy(typeCode);
        final Map<String, FullTextSearchFilter> filterMap = (Map<String, FullTextSearchFilter>)getValue(
                        MODEL_FIELD_QUERY_POPUP_FILTERS, Map.class);
        final Collection<FullTextSearchFilter> filters = filterMap != null ? filterMap.values() : Collections.emptySet();
        return filters.stream().filter(filter -> !filterValidationStrategy.isValid(typeCode, filter.getName(), filter.getValue(), filter.getOperator()))
                        .collect(Collectors.toSet());
    }


    protected SearchFilterValidationStrategy getSearchFilterValidationStrategy(final String typeCode)
    {
        final FulltextSearch configuration = loadFullTextConfiguration(typeCode);
        return getSearchFilterValidationStrategyRegistry()
                        .getStrategy(StringUtils.isEmpty(configuration.getPreferredSearchStrategy())
                                        ? cockpitProperties.getProperty(PROPERTY_SEARCH_STRATEGY)
                                        : configuration.getPreferredSearchStrategy());
    }


    protected void handleInvalidFilters(final Collection<FullTextSearchFilter> invalidFilters)
    {
        if(CollectionUtils.isNotEmpty(invalidFilters))
        {
            invalidFilters.forEach(invalidFilter -> {
                if(invalidFilter.isEnabled())
                {
                    invalidFilter.setEnabled(false);
                    getNotificationService().notifyUser(getWidgetInstanceManager(), NOTIFICATION_EVENT_TYPE_FILTER_INVALID,
                                    Level.WARNING, invalidFilter.getName());
                }
            });
            updateFilterCounter((Map<String, FullTextSearchFilter>)getValue(MODEL_FIELD_QUERY_POPUP_FILTERS, Map.class));
        }
    }


    protected DataType getCurrentDataType()
    {
        return getValue(MODEL_DATA_TYPE, DataType.class);
    }


    protected WidgetComponentRenderer<Popup, FulltextSearch, AdvancedSearchData> getFieldQueryPopupRenderer()
    {
        return fieldQueryPopupRenderer;
    }


    @WireVariable
    public void setFieldQueryPopupRenderer(
                    final WidgetComponentRenderer<Popup, FulltextSearch, AdvancedSearchData> fieldQueryPopupRenderer)
    {
        this.fieldQueryPopupRenderer = fieldQueryPopupRenderer;
    }


    protected Button getSearchButton()
    {
        return searchButton;
    }


    public void setSearchButton(final Button searchButton)
    {
        this.searchButton = searchButton;
    }


    protected Button getFieldQueryButton()
    {
        return fieldQueryButton;
    }


    public void setFieldQueryButton(final Button fieldQueryButton)
    {
        this.fieldQueryButton = fieldQueryButton;
    }


    protected Div getFullTextSearchContainer()
    {
        return fullTextSearchContainer;
    }


    public void setFullTextSearchContainer(final Div fullTextSearchContainer)
    {
        this.fullTextSearchContainer = fullTextSearchContainer;
    }


    protected Div getFullTextSearchBox()
    {
        return fullTextSearchBox;
    }


    public void setFullTextSearchBox(final Div fullTextSearchBox)
    {
        this.fullTextSearchBox = fullTextSearchBox;
    }


    protected Label getFieldQueryFiltersCounterLabel()
    {
        return fieldQueryFiltersCounterLabel;
    }


    public void setFieldQueryFiltersCounterLabel(final Label fieldQueryFiltersCounterLabel)
    {
        this.fieldQueryFiltersCounterLabel = fieldQueryFiltersCounterLabel;
    }


    protected Popup getFieldQueryPopup()
    {
        return fieldQueryPopup;
    }


    public void setFieldQueryPopup(final Popup fieldQueryPopup)
    {
        this.fieldQueryPopup = fieldQueryPopup;
    }


    public Button getClearButton()
    {
        return clearButton;
    }


    public void setClearButton(final Button clearButton)
    {
        this.clearButton = clearButton;
    }


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }


    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    protected SearchFilterValidationStrategyRegistry getSearchFilterValidationStrategyRegistry()
    {
        return searchFilterValidationStrategyRegistry;
    }


    public void setSearchFilterValidationStrategyRegistry(
                    final SearchFilterValidationStrategyRegistry searchFilterValidationStrategyRegistry)
    {
        this.searchFilterValidationStrategyRegistry = searchFilterValidationStrategyRegistry;
    }


    protected void extractAndRememberSelectedFacets(final Collection<FacetData> allFacets)
    {
        if(allFacets == null)
        {
            return;
        }
        final Map<String, Set<String>> selectedFacets = new HashMap<>();
        allFacets.stream().forEach(facet -> {
            final Set<String> setOfFacets = facet.getFacetValueNames().stream()
                            .filter(facetValueName -> facet.getFacetValue(facetValueName).isSelected())
                            .map(selectedFacet -> facet.getFacetValue(selectedFacet).getName()).collect(Collectors.toCollection(HashSet::new));
            selectedFacets.put(facet.getName(), setOfFacets);
        });
        setValue(MODEL_KEY_SELECTED_FACETS, selectedFacets);
    }
}
