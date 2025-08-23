/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch;

import com.google.common.collect.Collections2;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchDataConditionEvaluator;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchDataParameters;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.backoffice.widgets.advancedsearch.impl.facet.DefaultFacetRenderer;
import com.hybris.backoffice.widgets.advancedsearch.impl.renderer.AdvancedSearchRenderer;
import com.hybris.backoffice.widgets.advancedsearch.util.AdvancedSearchDataUtil;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Actions;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.renderers.header.WidgetCaptionRenderer;
import com.hybris.cockpitng.renderers.header.WidgetCaptionWrapper;
import com.hybris.cockpitng.search.data.FullTextSearchData;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Tree;

/**
 * Widget controller of the <code>advanced search</code> widget.
 */
public class AdvancedSearchController extends AbstractSearchController implements WidgetCaptionRenderer
{
    public static final String SEARCH_MODEL_CACHE = "searchModelCache";
    public static final String ADVANCED_SEARCH_CONFIGURATION = "advancedSearchConfiguration";
    public static final String MODEL_SUBTYPE_CHANGED = "subtypeChanged";
    public static final String SCLASS_NON_COLLAPSIBLE_CONTAINER = "yw-non-collapsible";
    public static final String BUTTON_SEARCHMODE_TOOLTIP_I18N_KEY = "button.searchmode.tooltip";
    public static final String SEARCH = "search";
    public static final String WIDGET_SETTING_INSTANT_FACETS = "instantFacets";
    public static final String WIDGET_SETTING_ENABLE_QUICK_FACET_FILTER = "enableQuickFacetFilter";
    public static final String WIDGET_SETTING_MAX_INLINE_FACETS = "maxInlineFacets";
    public static final String WIDGET_SETTING_FACET_RENDER_LIMIT = "facetRenderLimit";
    public static final String WIDGET_SETTING_FACET_RENDER_INCREMENT_STEP = "facetRenderIncrementStep";
    public static final String WIDGET_SETTING_HIDE_TYPES_WITHOUT_CLAZZ = "hideTypesWithoutClazz";
    public static final String SOCKET_IN_REFRESH_RESULTS = "refreshResults";
    public static final String SOCKET_OUT_RESET = "reset";
    public static final String SOCKET_IN_INIT_CONTEXT = "initContext";
    public static final String SOCKET_IN_FULL_TEXT_SEARCH_DATA = "fullTextSearchData";
    public static final String SOCKET_IN_TYPE = "type";
    public static final String SOCKET_IN_AUTO_SUGGESTIONS = "autosuggestions";
    public static final String SOCKET_IN_ATTACH_PARAMETERS = "attachParameters";
    public static final String INSUFFICIENT_PERMISSION_TO_SEARCH = "insufficientPermissionToSearch";
    protected static final String ABSTRACT_TYPE_NO_SUBTYPE_TO_SEARCH = "abstractTypeNoSubtypeToSearch";
    protected static final String ACTION_SLOT_GROUP_COMMON = "common";
    protected static final String SORT_CONTROL_CNT = "sortControlCnt";
    protected static final String SOCKET_IN_SORTDATA = "sortData";
    protected static final String SETTING_DISABLE_SIMPLE_SEARCH = "disableSimpleSearch";
    protected static final String SETTING_ENABLED_NESTED_WIDGET_VIEW = "enableNestedWidgetView";
    protected static final String FORCE_SEARCH_FOR_CODE = "forceSearchForCode";
    protected static final String SETTING_DISABLE_SEARCH_MODEL_CACHE = "disableSearchModelCache";
    protected static final String SETTING_DISPLAY_IN_NON_COLLAPSIBLE_CONTAINER = "displayInNonCollapsibleContainer";
    protected static final String SEND_RESET_WHEN_DISABLE_AUTO_SEARCH = "sendResetWhenDisableAutoSearch";
    protected static final String DISABLE_SUBTYPES_CHECKBOX = "disableSubtypesCheckbox";
    protected static final String DISABLE_TYPES_SELECTOR = "disableTypesSelector";
    private static final Logger LOG = LoggerFactory.getLogger(AdvancedSearchController.class);
    private static final String YW_BTN_ENABLED = "yw-sort-enabled";
    private static final String ACTIVE_RADIO_BTN = "active-radio-button";
    private static final String SCLASS_TEXTSEARCH = "yw-textsearch";
    private static final String SCLASS_TEXTSEARCH_TITLE = "yw-textsearch-title";
    private static final String SCLASS_TOGGLE_ADVANCED_SEARCH = "yw-toggle-advanced-search";
    private static final String SCLASS_SEARCH_MODE_CONTAINER = "yw-search-mode-container";
    private static final String SCLASS_SEARCH_BUTTON = "yw-textsearch-searchbutton";
    private static final String SCLASS_TYPE_LABEL = "yw-type-label";
    private static final String SCLASS_GLOBAL_TYPE_SELECTOR_CONTAINER = "yw-global-typeSelector-container";
    private static final String SCLASS_GLOBAL_TYPE_SELECTOR = "yw-global-typeSelector";
    private static final String SCLASS_TYPE_SELECTOR_TREE = "yw-typeSelectorTree";
    private static final String SCLASS_SEARCHBOX = "yw-textsearch-searchbox";
    private static final String SCLASS_ADVANCEDSEARCH_HEADER_CONTAINER = "yw-advancedsearch-header-container";
    private static final String SCLASS_GLOBAL_OPERATOR_SELECTOR_CONTAINER = "w-global-operator-selector-container";
    private static final String SCLASS_OPERATOR_LABEL = "yw-operator-label";
    private static final String SCLASS_GLOBAL_OPERATOR_SELECTOR = "yw-global-operator-selector";
    private static final String SCLASS_INCLUDE_SUBTYPES = "yw-include-subtypes";
    private static final String SCLASS_ADVSEARCH_TOGGLE_OPEN = "yw-toggle-open";
    private static final String SCLASS_Y_BTN_PRIMARY = "y-btn-primary";
    private static final String SCLASS_Y_BTN_SECONDARY = "y-btn-secondary";
    private static final String SCLASS_TOP_CONTAINER_OPEN = "yw-topcontainer-open";
    private static final String SCLASS_YW_BUTTONS_CONTAINER = "yw-buttons-container";
    private static final String SCLASS_ADVANCEDSEARCH_ACTIONSLOT = "yw-advancedsearch-actionslot";
    private static final String NOTIFICATION_EVENT_TYPE_CHANGE = "TypeChange";
    private static final String INITIAL_TYPE_CODE = "initialTypeCode";
    private static final String ADVANCED_SEARCH = "advancedSearch";
    @WireVariable
    protected transient CockpitLocaleService cockpitLocaleService;
    @WireVariable
    protected transient DefaultFacetRenderer defaultFacetRenderer;
    protected Div facetContainer;
    @WireVariable
    private transient AdvancedSearchDataUtil advancedSearchDataUtil;
    @WireVariable
    private transient NotificationService notificationService;
    private transient AdvancedSearchRenderer renderer;
    private Label searchTitle;
    private Button searchButton;
    private transient WidgetCaptionWrapper widgetCaptionWrapper;
    private Button searchModeToggleButton;
    private Widgetslot nestedWidget;
    private Grid attributesGrid;
    private Div topContainer;
    private Div nonCollapsibleCaptionContainer;
    private Div searchComponentsContainer;
    private Div searchModeCaptionContainer;
    private Div advancedSearchModeCaptionContainer;
    private Tree typeSelectorTree;
    private Bandbox typeSelectorBBox;
    private Combobox operatorSelector;
    private Radiogroup sortControlCnt;
    private Checkbox includeSubtypes;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        initActionSlotComponent();
        initCaptionComponents();
        registerModelObservers();
        setWidgetTitle(getLabel(SEARCH));
        getDefaultFacetRenderer().initialize(facetContainer, getWidgetInstanceManager(), f -> {
            if(isSimpleSearchActive())
            {
                doSimpleSearch();
            }
        });
        getSearchModel().ifPresent(searchData -> {
            assignType(searchData.getTypeCode());
            updateIncludeSubtypesCheckbox(searchData);
        });
    }


    @SocketEvent(socketId = SOCKET_IN_REFRESH_RESULTS)
    public void onRefreshResults()
    {
        getSearchModel().ifPresent(searchData -> doSearch());
    }


    @SocketEvent(socketId = SOCKET_IN_TYPE)
    public void changeType(final String typeCode)
    {
        try
        {
            if(hasInternalStateChanged(typeCode))
            {
                resetFacetsOnChangeType();
            }
            resetSimpleSearchTerm();
            if(BooleanUtils.isTrue(getValue(MODEL_INITIALIZED_FROM_INIT_CTX, Boolean.class)))
            {
                setValue(MODEL_INITIALIZED_FROM_INIT_CTX, false);
                setValue(SEARCH_MODEL, null);
            }
            prepareModelForType(typeCode);
            topContainer.invalidate();
        }
        catch(final RuntimeException exception)
        {
            handleRuntimeException(exception);
        }
    }


    protected String getNotificationSource()
    {
        final String source = getNotificationService().getWidgetNotificationSource(getWidgetInstanceManager());
        if(StringUtils.isNotBlank(source) && !ADVANCED_SEARCH.equals(source))
        {
            return ADVANCED_SEARCH + "-" + source;
        }
        else
        {
            return ADVANCED_SEARCH;
        }
    }


    protected void handleRuntimeException(final RuntimeException exception)
    {
        LOG.error(exception.getLocalizedMessage(), exception);
        getNotificationService().notifyUser(getNotificationSource(), NotificationEventTypes.EVENT_TYPE_GENERAL,
                        NotificationEvent.Level.FAILURE, exception);
    }


    @SocketEvent(socketId = SOCKET_IN_FULL_TEXT_SEARCH_DATA)
    public void onFullTextSearchData(final FullTextSearchData fullTextSearchData)
    {
        if(fullTextSearchData == null)
        {
            return;
        }
        getDefaultFacetRenderer().adjustFacets(fullTextSearchData.getFacets());
        facetContainer.setVisible(shouldDisplayFacets());
        processFullTextSearchData(fullTextSearchData);
    }


    @SocketEvent(socketId = SOCKET_IN_ATTACH_PARAMETERS)
    public void attachParameters(final AdvancedSearchDataParameters parameters)
    {
        if(parameters != null)
        {
            final String typeCode = parameters.getTypeCode();
            final DataType dataType = loadDataTypeForCode(typeCode);
            if(dataType != null)
            {
                final AdvancedSearch advancedSearch = loadAdvancedConfiguration(typeCode);
                final AdvancedSearchData searchData = initOrLoadAdvancedSearchModel(advancedSearch, dataType);
                searchData.setParameters(parameters.getParameters());
            }
        }
    }


    @SocketEvent(socketId = SOCKET_IN_INIT_CONTEXT)
    public void initializeWithContext(final AdvancedSearchInitContext initContext)
    {
        if(initContext == null)
        {
            return;
        }
        final AdvancedSearchData searchData = initContext.getAdvancedSearchData();
        setValue(MODEL_INIT_CONTEXT, initContext);
        setValue(MODEL_INITIALIZED_FROM_INIT_CTX, Boolean.TRUE);
        if(searchData == null)
        {
            return;
        }
        AdvancedSearch config = initContext.getAdvancedSearchConfig();
        if(config == null)
        {
            config = loadAdvancedConfiguration(searchData.getTypeCode());
        }
        final DataType dataType = loadDataTypeForCode(searchData.getTypeCode());
        final String initialTypeCode = initContext.getInitialTypeCode();
        if(dataType != null)
        {
            setActionSlotTypeCode(dataType.getCode());
            resetSimpleSearchTerm();
            resetFacetsOnChangeType();
            final AdvancedSearchData copyOfSearchData = new AdvancedSearchData(searchData);
            warmUpFacadeWithSubtypes(dataType);
            setValue(INITIAL_TYPE_CODE, initialTypeCode);
            adjustWidgetModel(config, copyOfSearchData, true, dataType);
        }
        if(initialTypeCode != null)
        {
            performChangeType(initialTypeCode, false);
        }
        searchOnTypeChangeIfRequired(initialTypeCode != null ? initialTypeCode : searchData.getTypeCode(), true);
    }


    protected void onChangeType(final String typeCode, final boolean rootTypeChanged)
    {
        final Optional<String> currentTypeCode = getSearchModel().map(AdvancedSearchData::getTypeCode);
        final boolean isTypeChanging = ObjectUtils.notEqual(typeCode, currentTypeCode.orElse(null));
        performChangeType(typeCode, rootTypeChanged);
        searchOnTypeChangeIfRequired(typeCode, isTypeChanging);
    }


    protected void searchOnTypeChangeIfRequired(final String typeCode, final boolean isTypeChanging)
    {
        final boolean canReadType = canReadType(typeCode);
        if(hasInternalStateChanged(typeCode) || isTypeChanging)
        {
            boolean searchPerformed = false;
            if(shouldDoAutoSearch(loadAdvancedConfiguration(typeCode)) && canReadType)
            {
                searchPerformed = doSearch();
            }
            final boolean shouldSendReset = resetOnAutoSearchOff() && !searchPerformed;
            if(shouldSendReset)
            {
                sendOutput(SOCKET_OUT_RESET, Collections.emptyMap());
            }
        }
        if(!canReadType)
        {
            getNotificationService().notifyUser(getNotificationSource(), INSUFFICIENT_PERMISSION_TO_SEARCH,
                            NotificationEvent.Level.WARNING, typeCode);
        }
    }


    protected void warmUpFacadeWithSubtypes(final DataType dataType)
    {
        for(final String subtype : dataType.getSubtypes())
        {
            final DataType subtypeType = loadDataTypeForCode(subtype);
            if(subtypeType != null)
            {
                warmUpFacadeWithSubtypes(subtypeType);
            }
        }
    }


    @SocketEvent(socketId = SOCKET_IN_AUTO_SUGGESTIONS)
    public void onAutoSuggestions(final Map<String, Collection> autoSuggestions)
    {
        processAutoSuggestions(autoSuggestions);
    }


    @SocketEvent(socketId = SOCKET_IN_SORTDATA)
    public void processSortData(final SortData sortData)
    {
        if(sortData == null || sortControlCnt == null)
        {
            return;
        }
        getSearchModel().ifPresent(searchData -> searchData.setSortData(sortData));
        for(final Radio radio : sortControlCnt.getItems())
        {
            final Boolean ascending = Boolean
                            .valueOf(Objects.toString(radio.getAttribute(AdvancedSearchRenderer.RADIO_SORT_ORDER_ASC_ATTR)));
            final boolean selected = Objects.equals(radio.getValue(), sortData.getSortAttribute())
                            && Objects.equals(ascending, Boolean.valueOf(sortData.isAscending()));
            radio.setSelected(selected);
            UITools.modifySClass(radio, YW_BTN_ENABLED, selected);
        }
    }


    protected void updateIncludeSubtypesCheckbox(final AdvancedSearchData searchData)
    {
        final Boolean isIncludeSubtypesChecked = searchData.getIncludeSubtypes();
        if(isIncludeSubtypesChecked != null)
        {
            includeSubtypes.setChecked(isIncludeSubtypesChecked.booleanValue());
        }
    }


    protected void resetFacetsOnChangeType()
    {
        facetContainer.getChildren().clear();
        getDefaultFacetRenderer().adjustFacets(Collections.emptyList());
        getDefaultFacetRenderer().initialize(facetContainer, getWidgetInstanceManager(), f -> {
            if(isSimpleSearchActive())
            {
                doSimpleSearch();
            }
        });
        facetContainer.setVisible(shouldDisplayFacets());
    }


    protected void initActionSlotComponent()
    {
        final Actions actionSlot = new Actions();
        actionSlot.setWidgetInstanceManager(getWidgetInstanceManager());
        actionSlot.setGroup(ACTION_SLOT_GROUP_COMMON);
        UITools.modifySClass(actionSlot, SCLASS_ADVANCEDSEARCH_ACTIONSLOT, true);
        setActionSlot(actionSlot);
    }


    protected void initCaptionComponents()
    {
        if(isNestedWidgetViewEnabled())
        {
            attributesGrid.setVisible(!isSimpleSearchActive());
        }
        else
        {
            nestedWidget.setVisible(false);
        }
        searchComponentsContainer = new Div();
        searchComponentsContainer.setSclass(SCLASS_TEXTSEARCH);
        searchTitle = new Label();
        searchTitle.setValue(getLabel(SEARCH));
        searchTitle.setSclass(SCLASS_TEXTSEARCH_TITLE);
        searchComponentsContainer.appendChild(searchTitle);
        searchModeToggleButton = new Button();
        searchModeToggleButton.setTooltiptext(getLabel(BUTTON_SEARCHMODE_TOOLTIP_I18N_KEY));
        YTestTools.modifyYTestId(searchModeToggleButton, SCLASS_TOGGLE_ADVANCED_SEARCH);
        searchModeToggleButton.setSclass(SCLASS_TOGGLE_ADVANCED_SEARCH);
        searchComponentsContainer.appendChild(searchModeToggleButton);
        searchModeCaptionContainer = new Div();
        searchModeCaptionContainer.setSclass(SCLASS_SEARCH_MODE_CONTAINER);
        initAdvancedSearchCaptionComponents();
        initSimpleSearchCaptionComponents();
        initAutoSuggestionAndAutoCorrectionComponents();
        searchComponentsContainer.appendChild(searchModeCaptionContainer);
        searchButton = new Button();
        YTestTools.modifyYTestId(searchButton, SCLASS_SEARCH_BUTTON);
        searchButton.setSclass(SCLASS_SEARCH_BUTTON);
        searchButton.setLabel(getLabel(SEARCH));
        searchButton.addEventListener(Events.ON_CLICK, event -> doSearch());
        final Div buttonsContainer = new Div();
        buttonsContainer.setSclass(SCLASS_YW_BUTTONS_CONTAINER);
        buttonsContainer.appendChild(searchButton);
        buttonsContainer.appendChild(getActionSlot());
        searchComponentsContainer.appendChild(buttonsContainer);
        if(!isWidgetCaptionRenderedByController())
        {
            nonCollapsibleCaptionContainer.setVisible(true);
            nonCollapsibleCaptionContainer.appendChild(searchComponentsContainer);
            UITools.modifySClass(topContainer, SCLASS_NON_COLLAPSIBLE_CONTAINER, true);
            UITools.modifySClass(searchButton, SCLASS_Y_BTN_SECONDARY, true);
            searchModeToggleButton.addEventListener(Events.ON_CLICK, event -> {
                attributesGrid.setVisible(!attributesGrid.isVisible());
                updateOpenStateSClass(attributesGrid.isVisible());
                updateSearchMode(getValue(ADVANCED_SEARCH_CONFIGURATION, AdvancedSearch.class));
            });
            updateOpenStateSClass(attributesGrid.isVisible());
            updateSearchMode(null);
        }
        else
        {
            UITools.modifySClass(searchButton, SCLASS_Y_BTN_PRIMARY, true);
        }
    }


    protected void initAdvancedSearchCaptionComponents()
    {
        final Div typeSelectorContainer = new Div();
        typeSelectorContainer.setSclass(SCLASS_GLOBAL_TYPE_SELECTOR_CONTAINER);
        UITools.modifySClass(typeSelectorContainer, SCLASS_ADVANCEDSEARCH_HEADER_CONTAINER, true);
        final Label typeLabel = new Label(getLabel("type"));
        typeLabel.setSclass(SCLASS_TYPE_LABEL);
        typeSelectorContainer.appendChild(typeLabel);
        typeSelectorBBox = new Bandbox();
        typeSelectorBBox.setReadonly(true);
        typeSelectorBBox.setClass(SCLASS_GLOBAL_TYPE_SELECTOR);
        YTestTools.modifyYTestId(typeSelectorBBox, "typeSelectorBBox");
        typeSelectorBBox.addEventListener(Events.ON_OPEN, openEvent -> {
            if(!((OpenEvent)openEvent).isOpen())
            {
                typeSelectorBBox.invalidate();
            }
        });
        typeSelectorContainer.appendChild(typeSelectorBBox);
        final Bandpopup bandpopup = new Bandpopup();
        typeSelectorBBox.appendChild(bandpopup);
        typeSelectorTree = new Tree();
        typeSelectorTree.setSclass(SCLASS_TYPE_SELECTOR_TREE);
        typeSelectorTree.setRenderdefer(1);
        typeSelectorTree.setHflex("1");
        YTestTools.modifyYTestId(typeSelectorTree, "typeSelectorTree");
        bandpopup.appendChild(typeSelectorTree);
        final Div globalOperatorContainer = new Div();
        globalOperatorContainer.setSclass(SCLASS_GLOBAL_OPERATOR_SELECTOR_CONTAINER);
        UITools.modifySClass(globalOperatorContainer, SCLASS_ADVANCEDSEARCH_HEADER_CONTAINER, true);
        final Label operatorLabel = new Label(getLabel("global.operator_label"));
        operatorLabel.setSclass(SCLASS_OPERATOR_LABEL);
        globalOperatorContainer.appendChild(operatorLabel);
        operatorSelector = new Combobox();
        operatorSelector.setReadonly(true);
        operatorSelector.setSclass(SCLASS_GLOBAL_OPERATOR_SELECTOR);
        operatorSelector.addEventListener(Events.ON_SELECT, event -> onGlobalOperatorChange());
        YTestTools.modifyYTestId(operatorSelector, "operatorSelector");
        globalOperatorContainer.appendChild(operatorSelector);
        includeSubtypes = new Checkbox();
        includeSubtypes.setSclass(SCLASS_INCLUDE_SUBTYPES);
        includeSubtypes.setLabel(getLabel("include_subtypes"));
        includeSubtypes.addEventListener(Events.ON_CHECK, event -> onIncludeSubtypesChange());
        YTestTools.modifyYTestId(includeSubtypes, "includeSubtypes");
        advancedSearchModeCaptionContainer = new Div();
        advancedSearchModeCaptionContainer.setSclass(SCLASS_ADVANCEDSEARCH_HEADER_CONTAINER);
        if(!isTypesSelectorDisabledBySetting())
        {
            advancedSearchModeCaptionContainer.appendChild(typeSelectorContainer);
        }
        advancedSearchModeCaptionContainer.appendChild(globalOperatorContainer);
        if(!isIncludeSubtypesDisabledBySetting())
        {
            advancedSearchModeCaptionContainer.appendChild(includeSubtypes);
        }
    }


    protected void initSimpleSearchCaptionComponents()
    {
        searchBox = new Bandbox();
        searchBox.setInstant(true);
        searchBox.setButtonVisible(false);
        searchBox.setSclass(SCLASS_SEARCHBOX);
        searchBox.setText(StringUtils.defaultIfBlank(getValue(SIMPLE_SEARCH_TEXT_QUERY, String.class), StringUtils.EMPTY));
        searchBox.addEventListener(Events.ON_OK, event -> doSearch());
        searchBox.addEventListener(Events.ON_CHANGE, event -> requestAutoSuggestions());
        YTestTools.modifyYTestId(searchBox, SCLASS_SEARCHBOX);
    }


    protected boolean isWidgetCaptionRenderedByController()
    {
        return !isDisplayInNonCollapsibleContainer();
    }


    protected boolean isDisplayInNonCollapsibleContainer()
    {
        return BooleanUtils.isTrue(Boolean.valueOf(getWidgetSettings().getBoolean(SETTING_DISPLAY_IN_NON_COLLAPSIBLE_CONTAINER)));
    }


    @Override
    protected boolean doSimpleSearch()
    {
        if(searchBox == null)
        {
            return false;
        }
        final Optional<AdvancedSearchData> searchData = getSearchModel();
        if(searchData.isPresent())
        {
            final String query = StringUtils.defaultIfBlank(getSearchText(), StringUtils.EMPTY);
            setValue(SIMPLE_SEARCH_TEXT_QUERY, query);
            final AdvancedSearchData queryData = buildQueryData(query, searchData.get().getTypeCode());
            queryData.setTokenizable(true);
            queryData.setSelectedFacets(getDefaultFacetRenderer().getSelectedFacets());
            queryData.setAdvancedSearchMode(AdvancedSearchMode.SIMPLE);
            if(!hasInitialContext())
            {
                queryData.setGlobalOperator(searchData.get().getGlobalOperator());
            }
            if(this.advancedSearchDataUtil.useOrForGlobalOperator(queryData.getTypeCode()))
            {
                queryData.setGlobalOperator(ValueComparisonOperator.OR);
            }
            sendOutput(SOCKET_OUT_SEARCH_DATA, queryData);
            return true;
        }
        return false;
    }


    protected boolean hasInitialContext()
    {
        return Objects.nonNull(getValue(MODEL_INIT_CONTEXT, AdvancedSearchInitContext.class));
    }


    protected void assignType(final String typeCode)
    {
        try
        {
            prepareModelForType(typeCode);
            if(canResetFacets(getModel().getValue(MODEL_SUBTYPE_CHANGED, String.class), typeCode))
            {
                resetFacets();
            }
        }
        catch(final RuntimeException exception)
        {
            handleRuntimeException(exception);
        }
    }


    protected boolean canResetFacets(final String selectedTypeCode, final String searchDataTypeCode)
    {
        if(selectedTypeCode == null || searchDataTypeCode == null)
        {
            return false;
        }
        return !selectedTypeCode.equals(searchDataTypeCode);
    }


    protected void prepareModelForType(final String typeCode)
    {
        if(!isInitializedWithCtxForType(typeCode))
        {
            setValue(MODEL_INIT_CONTEXT, null);
            setValue(MODEL_INITIALIZED_FROM_INIT_CTX, false);
        }
        onChangeType(typeCode, true);
    }


    protected boolean isInitializedWithCtxForType(final String typeCode)
    {
        if(BooleanUtils.isTrue(getValue(MODEL_INITIALIZED_FROM_INIT_CTX, Boolean.class)))
        {
            final Optional<AdvancedSearchData> searchData = getAdvancedSearchDataFromInitCtx();
            return searchData.isPresent() && Objects.equals(searchData.get().getTypeCode(), typeCode);
        }
        return false;
    }


    protected Optional<AdvancedSearchData> getAdvancedSearchDataFromInitCtx()
    {
        final AdvancedSearchInitContext initCtx = getValue(MODEL_INIT_CONTEXT, AdvancedSearchInitContext.class);
        return initCtx != null && initCtx.getAdvancedSearchData() != null ? Optional.of(initCtx.getAdvancedSearchData())
                        : Optional.empty();
    }


    protected void resetSimpleSearchTerm()
    {
        searchBox.setText(StringUtils.EMPTY);
        setValue(SIMPLE_SEARCH_TEXT_QUERY, StringUtils.EMPTY);
    }


    protected void resetFacets()
    {
        final AdvancedSearch advancedSearch = getValue(ADVANCED_SEARCH_CONFIGURATION, AdvancedSearch.class);
        if(advancedSearch != null)
        {
            getDefaultFacetRenderer().clearAllFacets(Boolean.valueOf(!shouldDoAutoSearch(advancedSearch)));
        }
        facetContainer.setVisible(shouldDisplayFacets());
    }


    @Override
    protected void adjustWidgetModel(final AdvancedSearch advancedSearch, final AdvancedSearchData searchData,
                    final boolean rootTypeChanged, final DataType dataType)
    {
        Validate.notNull("Advanced search data cannot be null!", searchData);
        Validate.notBlank("Type code cannot be empty!", searchData.getTypeCode());
        final String type = searchData.getTypeCode();
        final boolean hasInternalStateChanged = hasInternalStateChanged(type);
        if(!dataType.isSearchable())
        {
            final AdvancedSearchData data = new AdvancedSearchData();
            data.setTypeCode(type);
            setValue(SEARCH_MODEL, data);
            updateSearchMode(advancedSearch);
            setTitleOfTheWidget(type);
            return;
        }
        setValue(ADVANCED_SEARCH_CONFIGURATION, advancedSearch);
        if(advancedSearch != null)
        {
            if(hasInternalStateChanged)
            {
                setValue(SEARCH_MODEL, searchData);
            }
            getRenderer().renderVisible(attributesGrid, sortControlCnt, getActionSlot(), advancedSearch, dataType);
            if(rootTypeChanged)
            {
                getRenderer().renderTypeSelector(typeSelectorBBox, typeSelectorTree, dataType);
            }
            getRenderer().rendererGlobalOperator(operatorSelector);
            adjustIncludeSubtypeControl(includeSubtypes, advancedSearch, searchData);
            topContainer.setVisible(true);
        }
        updateSearchMode(advancedSearch);
        setTitleOfTheWidget(type);
    }


    protected void setTitleOfTheWidget(final String type)
    {
        setWidgetTitle(getLabel("info.type.name.search", new String[]
                        {type}));
    }


    /**
     * @deprecated since 1811, use {@link #adjustIncludeSubtypeControl(Checkbox, AdvancedSearch, AdvancedSearchData)}
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected void adjustIncludeSubtypeControl(final Checkbox includeSubtypes, final AdvancedSearch advancedSearch)
    {
        if(advancedSearch == null || advancedSearch.getFieldList() == null)
        {
            return;
        }
        final boolean isVisible = !isIncludeSubtypesCheckboxDisabled(advancedSearch);
        includeSubtypes.setVisible(isVisible);
        if(isVisible)
        {
            includeSubtypes.setChecked(advancedSearch.getFieldList() == null || advancedSearch.getFieldList().isIncludeSubtypes());
        }
    }


    protected void adjustIncludeSubtypeControl(final Checkbox includeSubtypes, final AdvancedSearch advancedSearch,
                    final AdvancedSearchData searchData)
    {
        adjustIncludeSubtypeControl(includeSubtypes, advancedSearch);
        if(includeSubtypes.isVisible() && BooleanUtils.isFalse(searchData.getIncludeSubtypes()))
        {
            includeSubtypes.setChecked(false);
        }
    }


    protected boolean isIncludeSubtypesCheckboxDisabled(final AdvancedSearch advancedSearch)
    {
        final boolean disabledBySetting = isIncludeSubtypesDisabledBySetting();
        final boolean disabledByConfig = advancedSearch.getFieldList().isDisableSubtypesCheckbox();
        return disabledBySetting || disabledByConfig;
    }


    protected boolean isIncludeSubtypesDisabledBySetting()
    {
        return isTypesSelectorDisabledBySetting()
                        || BooleanUtils.isTrue(Boolean.valueOf(getWidgetSettings().getBoolean(DISABLE_SUBTYPES_CHECKBOX)));
    }


    protected boolean isTypesSelectorDisabledBySetting()
    {
        return BooleanUtils.isTrue(Boolean.valueOf(getWidgetSettings().getBoolean(DISABLE_TYPES_SELECTOR)));
    }


    protected boolean shouldDoAutoSearch(final AdvancedSearch config)
    {
        final boolean disabledByConfig = config != null && config.isDisableAutoSearch();
        final boolean disabledBySetting = BooleanUtils
                        .isTrue(Boolean.valueOf(getWidgetSettings().getBoolean(SETTING_DISABLE_AUTO_SEARCH)));
        return !disabledByConfig && !disabledBySetting;
    }


    protected boolean resetOnAutoSearchOff()
    {
        return getWidgetSettings().getBoolean(SEND_RESET_WHEN_DISABLE_AUTO_SEARCH);
    }


    protected boolean hasInternalStateChanged(final String type)
    {
        if(BooleanUtils.isTrue(getValue(MODEL_INITIALIZED_FROM_INIT_CTX, Boolean.class)))
        {
            return true;
        }
        final Optional<String> currentTypeCode = getSearchModel().map(AdvancedSearchData::getTypeCode);
        final Set<String> typeCodes = getForcedSearchTypeCodes();
        return !currentTypeCode.isPresent() || ObjectUtils.notEqual(type, currentTypeCode.get()) || typeCodes.contains(type)
                        || typeCodes.contains("*");
    }


    protected Set<String> getForcedSearchTypeCodes()
    {
        final Set<String> typeCodes = new HashSet<>();
        final String triggerSearchPerTypes = getWidgetSettings().getString(FORCE_SEARCH_FOR_CODE);
        if(StringUtils.isNotBlank(triggerSearchPerTypes))
        {
            typeCodes.addAll(Arrays.asList(triggerSearchPerTypes.split(",")));
        }
        return typeCodes;
    }


    protected void performChangeType(final String typeCode, final boolean rootTypeChanged)
    {
        final DataType dataType = loadDataTypeForCode(typeCode);
        if(dataType != null)
        {
            final AdvancedSearch advancedSearch = loadAdvancedConfiguration(typeCode);
            final AdvancedSearchData searchData = initOrLoadAdvancedSearchModel(advancedSearch, dataType);
            adjustWidgetModel(advancedSearch, searchData, rootTypeChanged, dataType);
            setActionSlotTypeCode(dataType.getCode());
        }
        else
        {
            setValue(SEARCH_MODEL, null);
            updateSearchMode(null);
            sendOutput(SOCKET_OUT_RESET, null);
            getNotificationService().notifyUser(getNotificationSource(), NOTIFICATION_EVENT_TYPE_CHANGE,
                            NotificationEvent.Level.FAILURE, typeCode);
        }
    }


    protected void registerModelObservers()
    {
        getModel().addObserver(MODEL_SUBTYPE_CHANGED, () -> {
            final String selectedType = getModel().getValue(MODEL_SUBTYPE_CHANGED, String.class);
            onChangeType(selectedType, false);
            performChangeType(selectedType, false);
        });
        getModel().addObserver(SIMPLE_SEARCH_MODE_ACTIVE, () -> facetContainer.setVisible(shouldDisplayFacets()));
    }


    protected boolean shouldDisplayFacets()
    {
        return isSimpleSearchActive() && CollectionUtils.isNotEmpty(getDefaultFacetRenderer().getAvailableFacets());
    }


    protected AdvancedSearchData initOrLoadAdvancedSearchModel(final AdvancedSearch advancedSearch, final DataType dataType)
    {
        Validate.notNull("Configuration may not be null", advancedSearch);
        Validate.notNull("Data type may not be null", dataType);
        Map<String, AdvancedSearchData> cache = getValue(SEARCH_MODEL_CACHE, Map.class);
        if(cache == null)
        {
            cache = new HashMap<>();
            setValue(SEARCH_MODEL_CACHE, cache);
        }
        if(isInitializedWithCtxForType(dataType.getCode()))
        {
            final Optional<AdvancedSearchData> searchModel = getSearchModel();
            if(searchModel.isPresent() && StringUtils.equals(searchModel.get().getTypeCode(), dataType.getCode()))
            {
                return searchModel.get();
            }
            final Optional<AdvancedSearchData> searchDataFromInitCtx = getAdvancedSearchDataFromInitCtx();
            if(searchDataFromInitCtx.isPresent())
            {
                return new AdvancedSearchData(searchDataFromInitCtx.get());
            }
        }
        if(cache.containsKey(dataType.getCode())
                        && BooleanUtils.isFalse(Boolean.valueOf(getWidgetSettings().getBoolean(SETTING_DISABLE_SEARCH_MODEL_CACHE))))
        {
            return cache.get(dataType.getCode());
        }
        else
        {
            final AdvancedSearchData model = getAdvancedSearchDataUtil().buildAdvancedSearchData(advancedSearch, dataType);
            cache.put(dataType.getCode(), model);
            return model;
        }
    }


    protected Optional<AdvancedSearchData> getSearchModel()
    {
        return Optional.ofNullable(getValue(SEARCH_MODEL, AdvancedSearchData.class));
    }


    public void onGlobalOperatorChange()
    {
        final ValueComparisonOperator currentGlobalOperator = getCurrentGlobalOperator();
        getSearchModel().ifPresent(searchData -> searchData.setGlobalOperator(currentGlobalOperator));
    }


    protected ValueComparisonOperator getCurrentGlobalOperator()
    {
        final Comboitem comboitem = operatorSelector.getSelectedItem();
        return comboitem == null ? null : comboitem.getValue();
    }


    public void onIncludeSubtypesChange()
    {
        getSearchModel().ifPresent(searchData -> searchData.setIncludeSubtypes(Boolean.valueOf(includeSubtypes.isChecked())));
    }


    @ViewEvent(componentID = SORT_CONTROL_CNT, eventName = Events.ON_CHECK)
    public void onSortChange(final CheckEvent event)
    {
        final Radio currentlyActive = (Radio)sortControlCnt.getAttribute(ACTIVE_RADIO_BTN);
        if(currentlyActive != null)
        {
            UITools.modifySClass(currentlyActive, YW_BTN_ENABLED, false);
        }
        if(event.getTarget() instanceof Radio)
        {
            final Radio targetRadio = (Radio)event.getTarget();
            UITools.modifySClass(targetRadio, YW_BTN_ENABLED, true);
            sortControlCnt.setAttribute(ACTIVE_RADIO_BTN, targetRadio);
            final boolean ascending = BooleanUtils
                            .toBoolean(Objects.toString(targetRadio.getAttribute(AdvancedSearchRenderer.RADIO_SORT_ORDER_ASC_ATTR)));
            getSearchModel().ifPresent(
                            searchData -> searchData.setSortData(new SortData(Objects.toString(targetRadio.getValue()), ascending)));
        }
    }


    public boolean doSearch()
    {
        final Optional<AdvancedSearchData> searchData = getSearchModel();
        if(!searchData.isPresent())
        {
            return false;
        }
        final AdvancedSearchData searchDataObj = searchData.get();
        final String typeCode = searchDataObj.getTypeCode();
        if(!isTypeWithSubtypesSearchable(typeCode, searchDataObj.getIncludeSubtypes()))
        {
            getNotificationService().notifyUser(getNotificationSource(), ABSTRACT_TYPE_NO_SUBTYPE_TO_SEARCH,
                            NotificationEvent.Level.WARNING, typeCode);
            return false;
        }
        if(!canReadType(typeCode))
        {
            getNotificationService().notifyUser(getNotificationSource(), INSUFFICIENT_PERMISSION_TO_SEARCH,
                            NotificationEvent.Level.WARNING, typeCode);
            return false;
        }
        final boolean searchExecuted;
        if(isSimpleSearchActive())
        {
            searchExecuted = doSimpleSearch();
        }
        else
        {
            searchExecuted = doAdvancedSearch();
        }
        recalculateNestedWidgetSize();
        return searchExecuted;
    }


    private boolean isTypeWithSubtypesSearchable(String typeCode, Boolean includeSubtypes)
    {
        DataType dataType = loadDataTypeForCode(typeCode);
        if(dataType == null)
        {
            return false;
        }
        return !dataType.isAbstract() || BooleanUtils.isTrue(includeSubtypes);
    }


    protected boolean doAdvancedSearch()
    {
        clearSimpleTextbox();
        final Optional<AdvancedSearchData> searchData = getSearchModel();
        if(searchData.isPresent())
        {
            final AdvancedSearchData searchDataObj = searchData.get();
            searchDataObj.setTokenizable(false);
            searchDataObj.setAdvancedSearchMode(AdvancedSearchMode.ADVANCED);
            final boolean isSearchPossible = validateMandatoryFields(searchDataObj);
            if(isSearchPossible)
            {
                sendOutput(SOCKET_OUT_SEARCH_DATA, searchDataObj);
                return true;
            }
        }
        return false;
    }


    @Override
    protected boolean isSimpleSearchActive()
    {
        return BooleanUtils.isNotFalse(getValue(SIMPLE_SEARCH_MODE_ACTIVE, Boolean.class));
    }


    /**
     * Validates that for each mandatory field at least 1 condition is provided. Highlights the missing conditions.
     */
    protected boolean validateMandatoryFields(final AdvancedSearchData searchData)
    {
        final AdvancedSearch config = getValue(AdvancedSearchController.ADVANCED_SEARCH_CONFIGURATION, AdvancedSearch.class);
        final Collection<String> mandatoryFields = Collections2.filter(searchData.getSearchFields(),
                        field -> AdvancedSearchDataConditionEvaluator.isMandatory(config, field));
        boolean mandatoryFieldsProvided = true;
        for(final String field : mandatoryFields)
        {
            mandatoryFieldsProvided &= AdvancedSearchDataConditionEvaluator.atLeastOneConditionProvided(searchData, field);
            if(AdvancedSearchDataConditionEvaluator.atLeastOneConditionProvided(searchData, field))
            {
                getRenderer().unmarkMandatoryField(field);
            }
            else
            {
                getRenderer().markEmptyMandatoryField(field);
            }
        }
        return mandatoryFieldsProvided;
    }


    public AdvancedSearchRenderer getRenderer()
    {
        if(renderer == null)
        {
            renderer = (AdvancedSearchRenderer)SpringUtil.getBean("advancedSearchRenderer", AdvancedSearchRenderer.class);
            renderer.setWidgetInstanceManager(getWidgetInstanceManager());
            renderer.setEditorsEventConsumer(event -> doSearch());
        }
        return renderer;
    }


    @Override
    public Component renderCaption(final WidgetCaptionWrapper captionWrapper)
    {
        if(!isWidgetCaptionRenderedByController())
        {
            return new Label(getLabel("caption.render.mode.ambiguity"));
        }
        this.widgetCaptionWrapper = captionWrapper;
        if(isNestedWidgetViewEnabled())
        {
            updateOpenStateSClass(attributesGrid.isVisible());
            searchModeToggleButton.addEventListener(Events.ON_CLICK, event -> {
                attributesGrid.setVisible(!attributesGrid.isVisible());
                if(attributesGrid.isVisible() && widgetCaptionWrapper.isCollapsed())
                {
                    widgetCaptionWrapper.setCollapsed(false);
                }
                updateSearchMode(getValue(ADVANCED_SEARCH_CONFIGURATION, AdvancedSearch.class));
                updateOpenStateSClass(attributesGrid.isVisible());
                recalculateNestedWidgetSize();
            });
        }
        else
        {
            updateOpenStateSClass(!captionWrapper.isCollapsed());
            searchModeToggleButton.addEventListener(Events.ON_CLICK, event -> {
                captionWrapper.setCollapsed(!captionWrapper.isCollapsed());
                attributesGrid.setVisible(!captionWrapper.isCollapsed());
                updateOpenStateSClass(!captionWrapper.isCollapsed());
                updateSearchMode(getValue(ADVANCED_SEARCH_CONFIGURATION, AdvancedSearch.class));
            });
            captionWrapper.addListener(WidgetCaptionWrapper.ON_WIDGET_COLLAPSE, (eventName, wrapper) -> {
                attributesGrid.setVisible(!captionWrapper.isCollapsed());
                updateOpenStateSClass(!captionWrapper.isCollapsed());
                updateSearchMode(getValue(ADVANCED_SEARCH_CONFIGURATION, AdvancedSearch.class));
            });
            captionWrapper.hideContainerControls(WidgetCaptionWrapper.CONTROL_COLLAPSE);
        }
        updateSearchMode(getValue(ADVANCED_SEARCH_CONFIGURATION, AdvancedSearch.class));
        return searchComponentsContainer;
    }


    protected void recalculateNestedWidgetSize()
    {
        if(nestedWidget != null)
        {
            Clients.resize(nestedWidget);
        }
    }


    protected void updateOpenStateSClass(final boolean isOpen)
    {
        UITools.modifySClass(searchModeToggleButton, SCLASS_ADVSEARCH_TOGGLE_OPEN, isOpen);
        UITools.modifySClass(searchComponentsContainer, SCLASS_ADVSEARCH_TOGGLE_OPEN, isOpen);
        UITools.modifySClass(topContainer, SCLASS_TOP_CONTAINER_OPEN, isOpen);
        if(isWidgetCaptionRenderedByController())
        {
            UITools.modifySClass(searchButton, SCLASS_Y_BTN_PRIMARY, isOpen);
        }
        else
        {
            UITools.modifySClass(searchButton, SCLASS_Y_BTN_SECONDARY, isOpen);
        }
    }


    protected void updateSearchMode(final AdvancedSearch config)
    {
        final boolean simpleSearchVisible = shouldShowSimpleSearch(config);
        final boolean simpleSearchDisabled = !isAttributesContainerCollapsed();
        final boolean isCurrentTypeSearchable = isCurrentTypeSearchable();
        final boolean simpleSearchDisabledByInitCtx = isSimpleSearchDisabledByInitCtx();
        setValue(SIMPLE_SEARCH_MODE_ACTIVE,
                        Boolean.valueOf(simpleSearchVisible && !simpleSearchDisabled && !simpleSearchDisabledByInitCtx));
        if(isCurrentTypeSearchable)
        {
            setSearchModeCaptionContainer(simpleSearchVisible, simpleSearchDisabled, simpleSearchDisabledByInitCtx);
            attributesGrid.setVisible(isDisplayInNonCollapsibleContainer() || simpleSearchDisabled || !simpleSearchVisible);
            updateOpenStateSClass(attributesGrid.isVisible());
        }
        else
        {
            final Optional<AdvancedSearchData> advData = getSearchModel();
            final boolean hasTypeSelected = advData.isPresent() && StringUtils.isNotBlank(advData.get().getTypeCode());
            if(hasTypeSelected)
            {
                searchTitle.setValue(getLabel("non.searchable.type", new Object[]
                                {advData.get().getTypeCode()}));
            }
            else
            {
                searchTitle.setValue(getLabel("no.type.selected.info"));
            }
            getAttributesGrid().setVisible(false);
            getActionSlot().setVisible(false);
        }
        searchTitle.setVisible(!isCurrentTypeSearchable);
        searchModeCaptionContainer.setVisible(isCurrentTypeSearchable);
        searchButton.setVisible(isCurrentTypeSearchable);
        searchModeToggleButton.setVisible(isCurrentTypeSearchable && simpleSearchVisible);
    }


    protected void setSearchModeCaptionContainer(final boolean simpleSearchVisible, final boolean simpleSearchDisabled,
                    final boolean simpleSearchDisabledByQuery)
    {
        if(CollectionUtils.isNotEmpty(searchModeCaptionContainer.getChildren()))
        {
            searchModeCaptionContainer.getChildren().clear();
        }
        if(simpleSearchVisible && !simpleSearchDisabled && !simpleSearchDisabledByQuery)
        {
            searchModeCaptionContainer.appendChild(searchBox);
            getActionSlot().setVisible(false);
        }
        else if(BooleanUtils.isNotTrue(Boolean.valueOf(getWidgetSettings().getBoolean("disableAdvancedSearchToolbar"))))
        {
            searchModeCaptionContainer.appendChild(advancedSearchModeCaptionContainer);
            getActionSlot().setVisible(true);
        }
    }


    protected boolean shouldShowSimpleSearch(final AdvancedSearch config)
    {
        final boolean disabledByConfig = config != null && config.isDisableSimpleSearch();
        final boolean disabledBySetting = BooleanUtils
                        .isTrue(Boolean.valueOf(getWidgetSettings().getBoolean(SETTING_DISABLE_SIMPLE_SEARCH)));
        return !disabledByConfig && !disabledBySetting;
    }


    protected boolean isSimpleSearchDisabledByInitCtx()
    {
        final AdvancedSearchInitContext initCtx = getValue(MODEL_INIT_CONTEXT, AdvancedSearchInitContext.class);
        return initCtx != null && Boolean.TRUE.equals(initCtx.getAttribute(AdvancedSearchInitContext.DISABLE_SIMPLE_SEARCH))
                        && BooleanUtils.isTrue(getValue(MODEL_INITIALIZED_FROM_INIT_CTX, Boolean.class));
    }


    protected boolean isNestedWidgetViewEnabled()
    {
        return BooleanUtils.isTrue(Boolean.valueOf(getWidgetSettings().getBoolean(SETTING_ENABLED_NESTED_WIDGET_VIEW)));
    }


    protected boolean isAttributesContainerCollapsed()
    {
        return isNestedWidgetViewEnabled() || !isWidgetCaptionRenderedByController() ? !attributesGrid.isVisible()
                        : (widgetCaptionWrapper != null && widgetCaptionWrapper.isCollapsed());
    }


    protected void clearSimpleTextbox()
    {
        if(searchBox != null)
        {
            searchBox.setValue(StringUtils.EMPTY);
        }
    }


    protected boolean isCurrentTypeSearchable()
    {
        return getSearchModel().map(model -> loadDataTypeForCode(model.getTypeCode())).map(DataType::isSearchable).isPresent();
    }


    protected AdvancedSearch loadAdvancedConfiguration(final String type)
    {
        return loadConfiguration(type, getWidgetSettings().getString(SETTING_ADVANCED_SEARCH_CONFIG_CTX_CODE),
                        AdvancedSearch.class);
    }


    public Grid getAttributesGrid()
    {
        return attributesGrid;
    }


    public void setAttributesGrid(final Grid attributesGrid)
    {
        this.attributesGrid = attributesGrid;
    }


    public Div getTopContainer()
    {
        return topContainer;
    }


    public void setTopContainer(final Div topContainer)
    {
        this.topContainer = topContainer;
    }


    public Tree getTypeSelectorTree()
    {
        return typeSelectorTree;
    }


    public void setTypeSelectorTree(final Tree typeSelectorTree)
    {
        this.typeSelectorTree = typeSelectorTree;
    }


    public Bandbox getTypeSelectorBBox()
    {
        return typeSelectorBBox;
    }


    public void setTypeSelectorBBox(final Bandbox typeSelectorBBox)
    {
        this.typeSelectorBBox = typeSelectorBBox;
    }


    public Combobox getOperatorSelector()
    {
        return operatorSelector;
    }


    public void setOperatorSelector(final Combobox operatorSelector)
    {
        this.operatorSelector = operatorSelector;
    }


    public Radiogroup getSortControlCnt()
    {
        return sortControlCnt;
    }


    public void setSortControlCnt(final Radiogroup sortControlCnt)
    {
        this.sortControlCnt = sortControlCnt;
    }


    public Checkbox getIncludeSubtypes()
    {
        return includeSubtypes;
    }


    public void setIncludeSubtypes(final Checkbox includeSubtypes)
    {
        this.includeSubtypes = includeSubtypes;
    }


    public Label getSearchTitle()
    {
        return searchTitle;
    }


    public void setSearchTitle(final Label searchTitle)
    {
        this.searchTitle = searchTitle;
    }


    public Div getSearchModeCaptionContainer()
    {
        return searchModeCaptionContainer;
    }


    public void setSearchModeCaptionContainer(final Div searchModeCaptionContainer)
    {
        this.searchModeCaptionContainer = searchModeCaptionContainer;
    }


    public Div getFacetContainer()
    {
        return facetContainer;
    }


    public void setFacetContainer(final Div facetContainer)
    {
        this.facetContainer = facetContainer;
    }


    public Div getSearchComponentsContainer()
    {
        return searchComponentsContainer;
    }


    public void setSearchComponentsContainer(final Div searchComponentsContainer)
    {
        this.searchComponentsContainer = searchComponentsContainer;
    }


    public Div getAdvancedSearchModeCaptionContainer()
    {
        return advancedSearchModeCaptionContainer;
    }


    public void setAdvancedSearchModeCaptionContainer(final Div advancedSearchModeCaptionContainer)
    {
        this.advancedSearchModeCaptionContainer = advancedSearchModeCaptionContainer;
    }


    public Button getSearchModeToggleButton()
    {
        return searchModeToggleButton;
    }


    public void setSearchModeToggleButton(final Button searchModeToggleButton)
    {
        this.searchModeToggleButton = searchModeToggleButton;
    }


    public Bandbox getSearchBox()
    {
        return searchBox;
    }


    public void setSearchBox(final Bandbox searchBox)
    {
        this.searchBox = searchBox;
    }


    public Button getSearchButton()
    {
        return searchButton;
    }


    public void setSearchButton(final Button searchButton)
    {
        this.searchButton = searchButton;
    }


    public Div getNonCollapsibleCaptionContainer()
    {
        return nonCollapsibleCaptionContainer;
    }


    public void setNonCollapsibleCaptionContainer(final Div nonCollapsibleCaptionContainer)
    {
        this.nonCollapsibleCaptionContainer = nonCollapsibleCaptionContainer;
    }


    public CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    public Widgetslot getNestedWidget()
    {
        return nestedWidget;
    }


    public void setNestedWidget(final Widgetslot nestedWidget)
    {
        this.nestedWidget = nestedWidget;
    }


    public DefaultFacetRenderer getDefaultFacetRenderer()
    {
        return defaultFacetRenderer;
    }


    protected AdvancedSearchDataUtil getAdvancedSearchDataUtil()
    {
        return advancedSearchDataUtil;
    }


    public void setAdvancedSearchDataUtil(final AdvancedSearchDataUtil advancedSearchDataUtil)
    {
        this.advancedSearchDataUtil = advancedSearchDataUtil;
    }


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }
}
