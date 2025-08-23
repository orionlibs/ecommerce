/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch.engine;

import com.google.common.collect.Lists;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionDataList;
import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.Parameter;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.search.AutosuggestionSupport;
import com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacade;
import com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacadeStrategy;
import com.hybris.cockpitng.search.data.AutosuggestionQueryData;
import com.hybris.cockpitng.search.data.FullTextSearchData;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryConditionList;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.search.data.pageable.FullTextSearchPageable;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import com.hybris.cockpitng.service.ExceptionTranslationService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public class AdvancedSearchEngineController extends DefaultWidgetController
{
    public static final int DEFAULT_PAGE_SIZE_VALUE = 50;
    public static final String DEFAULT_PAGE_SIZE_KEY = "defaultPageSize";
    public static final String SEARCH_DATA_INPUT = "searchData";
    public static final String UPDATE_SEARCH_DATA_INPUT = "updateSearchData";
    public static final String CHANGE_SEARCH_CONTEXT = "changeSearchContext";
    public static final String AUTOSUGGESTION_DATA_INPUT = "autosuggestionQuery";
    public static final String PAGEABLE_OUTPUT = "pageable";
    public static final String FULL_TEXT_SEARCH_DATA_OUTPUT = "fullTextSearchData";
    public static final String AUTOSUGGESTION_OUTPUT = "autosuggestions";
    public static final String UPDATE_PAGEABLE_OUTPUT = "updatePageable";
    public static final String MODEL_SEARCH_CONTEXT = "searchContext";
    private static final Logger LOG = LoggerFactory.getLogger(AdvancedSearchEngineController.class);
    private int pageSize = DEFAULT_PAGE_SIZE_VALUE;
    @WireVariable
    private transient FieldSearchFacade fieldSearchFacade;
    @WireVariable
    private transient ExceptionTranslationService exceptionTranslationService;
    @WireVariable
    private transient NotificationService notificationService;
    @WireVariable
    private transient PermissionFacade permissionFacade;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        final int defaultPageSize = getWidgetSettings().getInt(DEFAULT_PAGE_SIZE_KEY);
        if(defaultPageSize > 0)
        {
            pageSize = defaultPageSize;
        }
        else
        {
            getWidgetSettings().put(DEFAULT_PAGE_SIZE_KEY, Integer.valueOf(DEFAULT_PAGE_SIZE_VALUE));
        }
    }


    @SocketEvent(socketId = SEARCH_DATA_INPUT)
    public void onSearchDataInput(final AdvancedSearchData searchData)
    {
        Pageable pageableOutput = null;
        try
        {
            final AdvancedSearchQueryData searchQueryData = buildQueryData(searchData);
            if(getPermissionFacade().canReadType(searchData.getTypeCode()))
            {
                pageableOutput = processAdvancedSearchQueryData(searchQueryData, searchData);
            }
            else
            {
                LOG.warn("Current user cannot read type {}", searchData.getTypeCode());
                getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(getWidgetInstanceManager()),
                                NotificationEventTypes.EVENT_TYPE_GENERAL, NotificationEvent.Level.FAILURE);
                pageableOutput = new PageableList<>(Lists.newArrayList(), DEFAULT_PAGE_SIZE_VALUE, searchData.getTypeCode());
            }
        }
        catch(final RuntimeException re)
        {
            LOG.error(re.getLocalizedMessage(), re);
            getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(getWidgetInstanceManager()),
                            NotificationEventTypes.EVENT_TYPE_GENERAL, NotificationEvent.Level.FAILURE, re);
        }
        sendOutput(PAGEABLE_OUTPUT, pageableOutput);
    }


    @SocketEvent(socketId = UPDATE_SEARCH_DATA_INPUT)
    public void updateSearchData(final AdvancedSearchData searchData)
    {
        Pageable pageableOutput = null;
        try
        {
            final AdvancedSearchQueryData searchQueryData = buildQueryData(searchData);
            if(getPermissionFacade().canReadType(searchData.getTypeCode()))
            {
                pageableOutput = processAdvancedSearchQueryData(searchQueryData, searchData);
            }
            else
            {
                LOG.warn("Current user cannot read type {}", searchData.getTypeCode());
                getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(getWidgetInstanceManager()),
                                NotificationEventTypes.EVENT_TYPE_GENERAL, NotificationEvent.Level.FAILURE);
                pageableOutput = new PageableList<>(Lists.newArrayList(), DEFAULT_PAGE_SIZE_VALUE, searchData.getTypeCode());
            }
        }
        catch(final RuntimeException re)
        {
            LOG.error(re.getLocalizedMessage(), re);
            getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(getWidgetInstanceManager()),
                            NotificationEventTypes.EVENT_TYPE_GENERAL, NotificationEvent.Level.FAILURE, re);
        }
        sendOutput(UPDATE_PAGEABLE_OUTPUT, pageableOutput);
    }


    protected Pageable processAdvancedSearchQueryData(final AdvancedSearchQueryData searchQueryData,
                    final AdvancedSearchData searchData)
    {
        Pageable pageable = null;
        final String queryId = searchQueryData.getQueryId();
        final Context searchContext = getValue(MODEL_SEARCH_CONTEXT, Context.class);
        if(searchContext == null || searchContext.getAttributeNames().isEmpty())
        {
            pageable = getFieldSearchFacade().search(searchQueryData);
        }
        else
        {
            final Context context = new DefaultContext();
            searchContext.getAttributeNames().forEach(name -> context.addAttribute(name, searchContext.getAttribute(name)));
            pageable = getFieldSearchFacade().search(searchQueryData, context);
        }
        if(pageable instanceof FullTextSearchPageable)
        {
            pageable = new PageableWithFullTextDataCallback((FullTextSearchPageable)pageable)
            {
                @Override
                public void onPageLoaded()
                {
                    final FullTextSearchData fullTextSearchData = super.getFullTextSearchData();
                    if(fullTextSearchData == null)
                    {
                        getNotificationService().notifyUser(
                                        getNotificationService().getWidgetNotificationSource(getWidgetInstanceManager()),
                                        NotificationEventTypes.EVENT_TYPE_GENERAL, NotificationEvent.Level.FAILURE);
                    }
                    else
                    {
                        fullTextSearchData.getContext().addAttribute(FieldSearchFacadeStrategy.CONTEXT_ORIGINAL_QUERY, searchData);
                        sendOutput(FULL_TEXT_SEARCH_DATA_OUTPUT, fullTextSearchData);
                    }
                }
            };
        }
        else
        {
            sendOutput(FULL_TEXT_SEARCH_DATA_OUTPUT, null);
        }
        pageable.setQueryId(queryId);
        return pageable;
    }


    @SocketEvent(socketId = AUTOSUGGESTION_DATA_INPUT)
    public void onAutoSuggestionInput(final AutosuggestionQueryData suggestionQueryData)
    {
        if(fieldSearchFacade instanceof AutosuggestionSupport)
        {
            try
            {
                final Context searchContext = getValue(MODEL_SEARCH_CONTEXT, Context.class);
                if(searchContext == null || searchContext.getAttributeNames().isEmpty())
                {
                    sendOutput(AUTOSUGGESTION_OUTPUT,
                                    ((AutosuggestionSupport)fieldSearchFacade).getAutosuggestionsForQuery(suggestionQueryData));
                }
                else
                {
                    final Context context = new DefaultContext();
                    searchContext.getAttributeNames().forEach(name -> context.addAttribute(name, searchContext.getAttribute(name)));
                    sendOutput(AUTOSUGGESTION_OUTPUT,
                                    ((AutosuggestionSupport)fieldSearchFacade).getAutosuggestionsForQuery(suggestionQueryData, context));
                }
            }
            catch(final RuntimeException re)
            {
                LOG.warn(
                                "Couldn't send autosuggestion for query {} and type {}. Probably Solr is not indexed for the current language.",
                                suggestionQueryData.getQueryText(), suggestionQueryData.getSearchType(), re);
            }
        }
    }


    @SocketEvent(socketId = CHANGE_SEARCH_CONTEXT)
    public void changeSearchContext(final Context context)
    {
        setValue(MODEL_SEARCH_CONTEXT, context);
    }


    protected AdvancedSearchQueryData buildQueryData(final AdvancedSearchData searchData)
    {
        final List<SearchQueryCondition> entries = new ArrayList<>();
        final List<SearchQueryCondition> fqEntries = new ArrayList<>();
        searchData.getSearchFields().stream().filter(field -> !AdvancedSearchData.ORPHANED_SEARCH_CONDITIONS_KEY.equals(field))
                        .forEach(field -> {
                            final List<SearchConditionData> conditions = searchData.getConditions(field);
                            entries.addAll(convertToQuery(conditions, field));
                        });
        searchData.getFilterQueryFields().forEach(fqField -> {
            final List<SearchConditionData> fqConditions = searchData.getFilterQueryRawConditions(fqField);
            fqEntries.addAll(convertToFilterQuery(fqConditions, fqField));
        });
        final List<SearchConditionData> orphanConditions = searchData
                        .getConditions(AdvancedSearchData.ORPHANED_SEARCH_CONDITIONS_KEY);
        final AdvancedSearchQueryData.Builder builder;
        if(CollectionUtils.isNotEmpty(fqEntries))
        {
            builder = prepareQueryBuilder(searchData.getTypeCode(), searchData.getGlobalOperator(),
                            Lists.newArrayList(prepareFilteringConditionsList(searchData.getGlobalOperator(), fqEntries, entries,
                                            convertToQuery(orphanConditions, AdvancedSearchData.ORPHANED_SEARCH_CONDITIONS_KEY))));
        }
        else if(CollectionUtils.isNotEmpty(orphanConditions) && CollectionUtils.isNotEmpty(entries))
        {
            final SearchQueryConditionList entriesList = new SearchQueryConditionList(searchData.getGlobalOperator(), entries);
            final SearchQueryConditionList orphansList = new SearchQueryConditionList(searchData.getGlobalOperator(),
                            convertToQuery(orphanConditions, AdvancedSearchData.ORPHANED_SEARCH_CONDITIONS_KEY));
            builder = prepareQueryBuilder(searchData.getTypeCode(), searchData.getGlobalOperator(),
                            Arrays.asList(entriesList, orphansList));
        }
        else if(CollectionUtils.isNotEmpty(orphanConditions))
        {
            builder = prepareQueryBuilder(searchData.getTypeCode(), searchData.getGlobalOperator(),
                            convertToQuery(orphanConditions, AdvancedSearchData.ORPHANED_SEARCH_CONDITIONS_KEY));
        }
        else
        {
            builder = prepareQueryBuilder(searchData.getTypeCode(), searchData.getGlobalOperator(), entries);
        }
        builder.pageSize(pageSize).sortData(searchData.getSortData());
        builder.includeSubtypes(BooleanUtils.toBoolean(searchData.getIncludeSubtypes())).tokenizable(searchData.isTokenizable());
        builder.searchQueryText(searchData.getSearchQueryText());
        builder.selectedFacets(searchData.getSelectedFacets());
        builder.advancedSearchMode(searchData.getAdvancedSearchMode());
        return builder.build();
    }


    private static SearchQueryConditionList prepareFilteringConditionsList(final ValueComparisonOperator globalOperator,
                    final List<SearchQueryCondition> filteringConditions, final List<SearchQueryCondition> conditions,
                    final List<SearchQueryCondition> orphanConditions)
    {
        if(CollectionUtils.isEmpty(conditions) && CollectionUtils.isEmpty(orphanConditions))
        {
            return new SearchQueryConditionList(ValueComparisonOperator.AND, filteringConditions);
        }
        final SearchQueryConditionList filteringConditionsList = new SearchQueryConditionList(ValueComparisonOperator.AND,
                        new SearchQueryConditionList(ValueComparisonOperator.AND, filteringConditions));
        if(!CollectionUtils.isEmpty(conditions))
        {
            filteringConditions.add(new SearchQueryConditionList(globalOperator, conditions));
        }
        if(!CollectionUtils.isEmpty(orphanConditions))
        {
            filteringConditions.add(new SearchQueryConditionList(globalOperator, orphanConditions));
        }
        return filteringConditionsList;
    }


    private static AdvancedSearchQueryData.Builder prepareQueryBuilder(final String typeCode,
                    final ValueComparisonOperator operator, final List<SearchQueryCondition> conditions)
    {
        final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(typeCode);
        builder.conditions(conditions).globalOperator(operator);
        return builder;
    }


    private static List<SearchQueryCondition> convertToQuery(final List<SearchConditionData> conditions, final String field)
    {
        final List<SearchQueryCondition> entries = new ArrayList<>();
        if(conditions != null)
        {
            for(int index = 0; index < conditions.size(); index++)
            {
                final SearchConditionData searchConditionData = conditions.get(index);
                collectEntries(entries, searchConditionData, field, index);
            }
        }
        return entries;
    }


    private static List<SearchQueryCondition> convertToFilterQuery(final List<SearchConditionData> conditions, final String field)
    {
        final List<SearchQueryCondition> queryConditions = convertToQuery(conditions, field);
        queryConditions.forEach(condition -> condition.setFilteringCondition(true));
        return queryConditions;
    }


    private static void collectEntries(final List<SearchQueryCondition> entries, final SearchConditionData searchConditionData,
                    final String field, final int index)
    {
        if(searchConditionData instanceof SearchConditionDataList)
        {
            final SearchConditionDataList list = (SearchConditionDataList)searchConditionData;
            final SearchQueryConditionList entryList = new SearchQueryConditionList();
            entryList.setOperator(list.getOperator());
            entryList.setConditions(new LinkedList<>());
            for(final SearchConditionData data : list.getConditions())
            {
                collectEntries(entryList.getConditions(), data, field, index);
            }
            entries.add(entryList);
        }
        else
        {
            getQueryEntry(entries, field, index, searchConditionData);
        }
    }


    private static void getQueryEntry(final List<SearchQueryCondition> entries, final String field, final int index,
                    final SearchConditionData data)
    {
        final Object value = data.getValue();
        final ValueComparisonOperator operator = data.getOperator();
        if(operator != null && (value != null || !operator.isRequireValue()))
        {
            final Map<String, String> editorParameters = data.getFieldType().getEditorParameter().stream()
                            .collect(Collectors.toMap(Parameter::getName, Parameter::getValue));
            final SearchQueryCondition entry = new SearchQueryCondition();
            entry.setDescriptor(new SearchAttributeDescriptor(data.getFieldType().getName(), index, editorParameters));
            entry.setOperator(operator);
            entry.setValue(value);
            entries.add(entry);
        }
        else
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("Skipping %s since either value or operator is null.", field));
            }
        }
    }


    public FieldSearchFacade getFieldSearchFacade()
    {
        return fieldSearchFacade;
    }


    public void setFieldSearchFacade(final FieldSearchFacade fieldSearchFacade)
    {
        this.fieldSearchFacade = fieldSearchFacade;
    }


    public ExceptionTranslationService getExceptionTranslationService()
    {
        return exceptionTranslationService;
    }


    public void setExceptionTranslationService(final ExceptionTranslationService exceptionTranslationService)
    {
        this.exceptionTranslationService = exceptionTranslationService;
    }


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }


    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
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
