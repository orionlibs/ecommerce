/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch;

import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.components.Actions;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.simplesearch.SimpleSearch;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.simplesearch.SortField;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.search.data.AutosuggestionQueryData;
import com.hybris.cockpitng.search.data.FullTextSearchData;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;
import org.zkoss.zul.Listbox;

public abstract class AbstractSearchController extends DefaultWidgetController
{
    public static final String SEARCH_MODEL = "searchModel";
    public static final String MODEL_INIT_CONTEXT = "initContext";
    public static final String SIMPLE_SEARCH_TEXT_QUERY = "simpleSearchTextQuery";
    public static final String MODEL_INITIALIZED_FROM_INIT_CTX = "InitializedFromInitCtx";
    public static final String SOCKET_OUT_AUTO_SUGGESTION_DATA = "autosuggestionQuery";
    public static final String SOCKET_OUT_SEARCH_DATA = "searchData";
    protected static final String SETTING_DISABLE_AUTO_SEARCH = "disableAutoSearch";
    protected static final String SETTING_SIMPLE_SEARCH_CONFIG_CTX_CODE = "simpleSearchConfigCtxCode";
    protected static final String SIMPLE_SEARCH_MODE_ACTIVE = "simpleSearchModeActive";
    protected static final String SETTING_ACTION_SLOT_COMPONENT_CTX = "actionSlotComponentName";
    protected static final String SETTING_ADVANCED_SEARCH_CONFIG_CTX_CODE = "advancedSearchConfigCtxCode";
    protected static final String SETTING_MIN_AUTO_SUGGESTIONS_QUERY_LENGTH = "minAutosuggestionsQueryLength";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSearchController.class);
    private static final String SCLASS_BANDPOPUP_AUTOSUGGESTION = "yw-bandpopup-autosuggestion";
    private static final String WORDS_SEPARATOR = " ";
    private static final String LABEL_BANDPOPUP_DIDYOUMEAN = "bandpopup.didyoumean";
    @WireVariable
    protected transient TypeFacade typeFacade;
    @WireVariable
    protected transient PermissionFacade permissionFacade;
    @Wire
    protected Bandbox searchBox;
    private Actions actionSlot;
    private Listbox autosuggestionListbox;
    private Bandpopup autosuggestionBandpopup;


    public void requestAutoSuggestions()
    {
        final String query = StringUtils.defaultIfBlank(getSearchText(), StringUtils.EMPTY);
        if(shouldPopulateAutoSuggestions(query))
        {
            populateAutoSuggestions(query);
        }
    }


    protected boolean shouldPopulateAutoSuggestions(final String query)
    {
        if(isSimpleSearchActive())
        {
            final int minQueryLength = getWidgetSettings().getInt(SETTING_MIN_AUTO_SUGGESTIONS_QUERY_LENGTH);
            return query.length() >= minQueryLength;
        }
        return false;
    }


    protected void populateAutoSuggestions(final String query)
    {
        final AdvancedSearchData searchData = getValue(SEARCH_MODEL, AdvancedSearchData.class);
        if(searchData == null)
        {
            LOG.warn("Cannot get value from '{}'", SEARCH_MODEL);
            return;
        }
        final String typeCode = searchData.getTypeCode();
        sendOutput(SOCKET_OUT_AUTO_SUGGESTION_DATA, new AutosuggestionQueryData(typeCode, query));
    }


    protected void initAutoSuggestionAndAutoCorrectionComponents()
    {
        searchBox.setInstant(true);
        searchBox.addEventListener(Events.ON_CHANGE, event -> requestAutoSuggestions());
        autosuggestionListbox = new Listbox();
        autosuggestionBandpopup = new Bandpopup();
        autosuggestionListbox.setHflex("1");
        autosuggestionBandpopup.setSclass(SCLASS_BANDPOPUP_AUTOSUGGESTION);
    }


    protected Bandpopup getAutosuggestionBandpopup()
    {
        return autosuggestionBandpopup;
    }


    protected void setAutosuggestionBandpopup(final Bandpopup autosuggestionBandpopup)
    {
        this.autosuggestionBandpopup = autosuggestionBandpopup;
    }


    protected Listbox getAutosuggestionListbox()
    {
        return autosuggestionListbox;
    }


    protected void setAutosuggestionListbox(final Listbox autosuggestionListbox)
    {
        this.autosuggestionListbox = autosuggestionListbox;
    }


    protected void processAutoSuggestions(final Map<String, Collection> autoSuggestions)
    {
        if(isSearchBoxOrRelatedViewsNull())
        {
            return;
        }
        clearSearchBoxAndRelatedViews();
        if(MapUtils.isEmpty(autoSuggestions))
        {
            return;
        }
        final String searchedPhrase = StringUtils.lowerCase(getSearchText().trim());
        final int numberOfWordsInASearchedPhrase = searchedPhrase.split(WORDS_SEPARATOR).length;
        final String lastWordFromSearchedPhrase;
        final String searchedPhraseWithoutLastWord;
        if(numberOfWordsInASearchedPhrase > 1)
        {
            final int endIndex = searchedPhrase.lastIndexOf(WORDS_SEPARATOR);
            lastWordFromSearchedPhrase = searchedPhrase.substring(endIndex + 1);
            searchedPhraseWithoutLastWord = searchedPhrase.substring(0, endIndex);
        }
        else
        {
            lastWordFromSearchedPhrase = searchedPhrase;
            searchedPhraseWithoutLastWord = StringUtils.EMPTY;
        }
        final Stream<Map.Entry<String, Collection>> streamOfAutoSuggestions = autoSuggestions.entrySet().stream();
        final Predicate<Map.Entry<String, Collection>> isLastWord = entry -> entry.getKey().contains(lastWordFromSearchedPhrase);
        final Map.Entry<String, Collection> lastEntry = streamOfAutoSuggestions.filter(isLastWord).findAny().orElse(null);
        if(Objects.isNull(lastEntry))
        {
            return;
        }
        lastEntry.getValue().stream().forEach(suggestionToAppend -> {
            final String completeSuggestion = getCompleteSuggestion(searchedPhraseWithoutLastWord, (String)suggestionToAppend);
            getAutosuggestionListbox().appendItem(completeSuggestion, completeSuggestion);
        });
        createListenerForBandPopup();
        openSearchBoxWithBandPopup();
    }


    protected boolean canReadType(final String typeCode)
    {
        if(!getPermissionFacade().canReadType(typeCode))
        {
            LOG.debug("Cannot read type - insufficient type permission for: {}", typeCode);
            return false;
        }
        return true;
    }


    protected boolean isSearchBoxOrRelatedViewsNull()
    {
        return searchBox == null || getAutosuggestionBandpopup() == null || getAutosuggestionListbox() == null;
    }


    protected void clearSearchBoxAndRelatedViews()
    {
        searchBox.getChildren().clear();
        getAutosuggestionBandpopup().getChildren().clear();
        getAutosuggestionListbox().getItems().clear();
    }


    protected String getCompleteSuggestion(final String searchedPhraseWithoutLastWord, final String suggestion)
    {
        Validate.notNull("All arguments are mandatory", searchedPhraseWithoutLastWord, suggestion);
        if(searchedPhraseWithoutLastWord.isEmpty())
        {
            return suggestion;
        }
        return searchedPhraseWithoutLastWord.concat(WORDS_SEPARATOR).concat(suggestion);
    }


    protected void createListenerForBandPopup()
    {
        getAutosuggestionBandpopup().appendChild(getAutosuggestionListbox());
        UITools.removeAllEventListeners(getAutosuggestionBandpopup(), Events.ON_CLICK);
        getAutosuggestionBandpopup().addEventListener(Events.ON_CLICK, event -> {
            searchBox.setText(getAutosuggestionListbox().getSelectedItem().getValue());
            searchBox.close();
            doSimpleSearch();
        });
    }


    protected void openSearchBoxWithBandPopup()
    {
        searchBox.appendChild(getAutosuggestionBandpopup());
        searchBox.open();
    }


    protected DataType loadDataTypeForCode(final String typeCode)
    {
        if(StringUtils.isNotBlank(typeCode))
        {
            final String type = typeCode.trim();
            try
            {
                return getTypeFacade().load(type);
            }
            catch(final TypeNotFoundException e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        return null;
    }


    protected void processFullTextSearchData(final FullTextSearchData fullTextSearchData)
    {
        if(fullTextSearchData == null || isSearchBoxOrRelatedViewsNull())
        {
            return;
        }
        clearSearchBoxAndRelatedViews();
        if(isSimpleSearchActive())
        {
            final String autoCorrection = fullTextSearchData.getAutocorrection();
            if(StringUtils.isNotBlank(autoCorrection))
            {
                final String label = getLabel(LABEL_BANDPOPUP_DIDYOUMEAN, new Object[]
                                {autoCorrection});
                getAutosuggestionListbox().appendItem(label, autoCorrection);
                createListenerForBandPopup();
                openSearchBoxWithBandPopup();
            }
        }
    }


    protected void setActionSlotTypeCode(final String typeCode)
    {
        if(StringUtils.isNotBlank(typeCode))
        {
            final String actionSlotComponentName = getWidgetSettings().getString(SETTING_ACTION_SLOT_COMPONENT_CTX);
            actionSlot.setConfig(String.format("component=%s,type=%s", actionSlotComponentName, typeCode));
        }
        else
        {
            actionSlot.setConfig(null);
        }
        actionSlot.reload();
    }


    protected abstract void adjustWidgetModel(AdvancedSearch advancedSearch, AdvancedSearchData searchData,
                    boolean rootTypeChanged, DataType dataType);


    protected abstract boolean doSimpleSearch();


    protected String getSearchText()
    {
        return searchBox.getText();
    }


    protected boolean isSimpleSearchActive()
    {
        return BooleanUtils.isNotFalse(getValue(SIMPLE_SEARCH_MODE_ACTIVE, Boolean.class));
    }


    protected AdvancedSearchData buildQueryData(final String searchText, final String typeCode)
    {
        final AdvancedSearchData queryData = createAdvancedSearchDataWithInitContext();
        queryData.setTypeCode(typeCode);
        queryData.setSearchQueryText(searchText);
        if(queryData.getGlobalOperator() == null)
        {
            queryData.setGlobalOperator(ValueComparisonOperator.OR);
        }
        queryData.setTokenizable(true);
        queryData.setIncludeSubtypes(Boolean.TRUE);
        applySimpleSearchConfiguration(searchText, typeCode, queryData);
        return queryData;
    }


    protected void applySimpleSearchConfiguration(final String searchText, final String typeCode,
                    final AdvancedSearchData queryData)
    {
        final SimpleSearch searchConfiguration = loadSimpleConfiguration(typeCode);
        if(searchConfiguration == null)
        {
            return;
        }
        final SortField sortField = searchConfiguration.getSortField();
        if(sortField != null)
        {
            queryData.setSortData(new SortData(sortField.getName(), sortField.isAsc()));
        }
        if(StringUtils.isNotEmpty(searchText))
        {
            final List<SearchConditionData> conditions = searchConfiguration.getField().stream()
                            .filter(field -> isApplicableForSimpleSearch(typeCode, field.getName()))
                            .map(field -> {
                                final FieldType searchField = new FieldType();
                                searchField.setName(field.getName());
                                return new SearchConditionData(searchField, searchText, ValueComparisonOperator.CONTAINS);
                            })
                            .collect(Collectors.toList());
            queryData.addConditionList(ValueComparisonOperator.OR, conditions);
        }
        queryData.setIncludeSubtypes(BooleanUtils.isTrue(searchConfiguration.isIncludeSubtypes()));
    }


    protected boolean isApplicableForSimpleSearch(final String typeCode, final String fieldName)
    {
        final DataAttribute attribute = typeFacade.getAttribute(typeCode, fieldName);
        if(attribute == null)
        {
            LOG.warn("Could not load DataAttribute for field [{}] of type [{}]", fieldName, typeCode);
            return false;
        }
        if(!attribute.getDefinedType().isAtomic())
        {
            LOG.warn("SimpleSearch configured incorrectly for type [{}]. Non-atomic field [{}] has been omitted", typeCode, fieldName);
            return false;
        }
        return true;
    }


    protected SimpleSearch loadSimpleConfiguration(final String type)
    {
        return loadConfiguration(type, getWidgetSettings().getString(SETTING_SIMPLE_SEARCH_CONFIG_CTX_CODE), SimpleSearch.class);
    }


    /**
     * Creates a new instance of {@link AdvancedSearchData}. If {@link #MODEL_INIT_CONTEXT} in widgetModel is not null then
     * a copy of its
     * {@link com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext#getAdvancedSearchData()} is
     * returned
     *
     * @return the new instance of {@link AdvancedSearchData}.
     */
    protected AdvancedSearchData createAdvancedSearchDataWithInitContext()
    {
        final AdvancedSearchInitContext advancedSearchInitContext = getValue(MODEL_INIT_CONTEXT, AdvancedSearchInitContext.class);
        if(advancedSearchInitContext != null && advancedSearchInitContext.getAdvancedSearchData() != null)
        {
            return new AdvancedSearchData(advancedSearchInitContext.getAdvancedSearchData());
        }
        else
        {
            return new AdvancedSearchData();
        }
    }


    protected <T> T loadConfiguration(final String type, final String configurationContextCode, final Class<T> configurationClass)
    {
        return loadConfiguration(new DefaultConfigContext(configurationContextCode, StringUtils.trim(type)), configurationClass);
    }


    protected <T> T loadConfiguration(final ConfigContext configurationContext, final Class<T> configurationClass)
    {
        try
        {
            return getWidgetInstanceManager().loadConfiguration(configurationContext, configurationClass);
        }
        catch(final CockpitConfigurationException cce)
        {
            LOG.warn("Could not load search configuration for type [{}] ",
                            configurationContext.getAttribute(DefaultConfigContext.CONTEXT_TYPE), cce);
        }
        return null;
    }


    protected Actions getActionSlot()
    {
        return actionSlot;
    }


    protected void setActionSlot(final Actions actionSlot)
    {
        this.actionSlot = actionSlot;
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    protected PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }
}
