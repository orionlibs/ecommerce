/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.search;

import static de.hybris.platform.jalo.flexiblesearch.FlexibleSearch.ENABLE_CACHE_FOR_READ_ONLY_DATA_SOURCE;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import com.google.common.collect.Lists;
import com.hybris.backoffice.cockpitng.dataaccess.facades.common.PlatformFacadeStrategyHandleCache;
import com.hybris.backoffice.cockpitng.search.builder.ConditionQueryBuilder;
import com.hybris.backoffice.search.cache.BackofficeDeletedItemCache;
import com.hybris.backoffice.widgets.advancedsearch.engine.AdvancedSearchQueryData;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacadeStrategy;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryConditionList;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchConstants;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSearchFieldType;
import de.hybris.platform.core.GenericSearchOrderBy;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.core.model.type.ViewTypeModel;
import de.hybris.platform.genericsearch.GenericSearchQuery;
import de.hybris.platform.genericsearch.GenericSearchService;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchContext;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.internal.ReadOnlyConditionsHelper;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Platform specific implementation of {@link FieldSearchFacadeStrategy}
 * </p>
 */
public class DefaultPlatformFieldSearchFacadeStrategy<T extends ItemModel> implements FieldSearchFacadeStrategy<T>
{
    /**
     * Strategy name to be used in configuration when preferred search strategy is to be changed
     */
    public static final String STRATEGY_NAME = "flexible";
    public static final String ATTRIBUTE_PERMISSION_CHECK_PROPERTY = "backoffice.flexible.search.attribute.permission.check.enabled";
    public static final String BACKOFFICE_SEARCH_READ_REPLICA_TYPE_CODES_EXCLUDE = "backoffice.search.read-replica.type.codes.exclude";
    public static final String BACKOFFICE_SEARCH_READ_REPLICA_ENABLED = "backoffice.search.read-replica.enabled";
    public static final String BACKOFFICE_CACHE_ON_READ_REPLICA_ENABLED = "backoffice.cache.on.read-replica.enabled";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPlatformFieldSearchFacadeStrategy.class);
    private static final String SORT_ENUM_BY_LOCALIZED_NAME = "cockpitng.search.sort.enum-by-localized-name";
    private GenericSearchService genericSearchService;
    private ConditionQueryBuilder genericMultiConditionQueryBuilder;
    private TypeService typeService;
    private PlatformFacadeStrategyHandleCache platformFacadeStrategyHandleCache;
    private CommonI18NService commonI18NService;
    private CockpitProperties cockpitProperties;
    private PermissionFacade permissionFacade;
    private ConfigurationService configurationService;
    private BackofficeDeletedItemCache deletedItemCache;


    @Override
    public boolean canHandle(final String typeCode)
    {
        return platformFacadeStrategyHandleCache.canHandle(typeCode);
    }


    @Override
    public Pageable search(final SearchQueryData searchQueryData)
    {
        return searchInternal(searchQueryData);
    }


    protected Pageable searchInternal(final SearchQueryData searchQueryData)
    {
        validateParameterNotNull(searchQueryData, "Parameter 'searchQueryData' must not be null!");
        final SearchQueryData adjustedSearchQueryData = adjustSearchQuery(searchQueryData);
        if(isValidQueryData(adjustedSearchQueryData)
                        && (searchQueryData.getAttributes().isEmpty() || !adjustedSearchQueryData.getAttributes().isEmpty()))
        {
            return new BackofficeFlexibleSearchPageable(buildQuery(adjustedSearchQueryData), adjustedSearchQueryData);
        }
        else
        {
            return new EmptyBackofficeFlexibleSearchPageable(adjustedSearchQueryData);
        }
    }


    boolean isValidQueryData(final SearchQueryData data)
    {
        return data.getConditions().stream().allMatch(this::isValidQueryCondition);
    }


    boolean isValidQueryCondition(final SearchQueryCondition cond)
    {
        if(cond instanceof SearchQueryConditionList)
        {
            return ((SearchQueryConditionList)cond).getConditions().stream().allMatch(this::isValidQueryCondition);
        }
        if(ValueComparisonOperator.CONTAINS.equals(cond.getOperator()))
        {
            return isNotEmpty(cond.getValue());
        }
        return true;
    }


    private static boolean isNotEmpty(final Object value)
    {
        if(value == null)
        {
            return false;
        }
        if(value instanceof Collection)
        {
            final Collection<?> collection = (Collection<?>)value;
            if(collection.isEmpty())
            {
                return false;
            }
            return collection.stream().allMatch(DefaultPlatformFieldSearchFacadeStrategy::isNotEmpty);
        }
        return true;
    }


    /**
     * This method allows filtering or altering the search data before the real query will be passed to the search engine.
     *
     * @param searchQueryData
     *           query to adjust
     */
    protected SearchQueryData adjustSearchQuery(final SearchQueryData searchQueryData)
    {
        final AdvancedSearchQueryData.Builder builder = createSearchQueryDataBuilderWithAttributes(searchQueryData);
        return builder.build();
    }


    protected AdvancedSearchQueryData.Builder createSearchQueryDataBuilderWithAttributes(final SearchQueryData searchQueryData)
    {
        final String searchType = searchQueryData.getSearchType();
        final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(searchType);
        builder.globalOperator(searchQueryData.getGlobalComparisonOperator());
        builder.includeSubtypes(searchQueryData.isIncludeSubtypes());
        builder.pageSize(searchQueryData.getPageSize());
        builder.sortData(searchQueryData.getSortData());
        builder.conditions(parseSearchQueryAttributes(searchQueryData));
        builder.tokenizable(searchQueryData.isTokenizable());
        if(searchQueryData instanceof AdvancedSearchQueryData)
        {
            builder.advancedSearchMode(((AdvancedSearchQueryData)searchQueryData).getAdvancedSearchMode());
        }
        return builder;
    }


    protected List<SearchQueryCondition> parseSearchQueryAttributes(final SearchQueryData searchQueryData)
    {
        final List<SearchQueryCondition> conditions = new LinkedList<>();
        for(final SearchQueryCondition condition : searchQueryData.getConditions())
        {
            conditions.addAll(adjustConditionRecursively(condition, searchQueryData));
        }
        excludeNoReadPermissionSubTypes(conditions, searchQueryData);
        return conditions;
    }


    private List<SearchQueryCondition> adjustConditionRecursively(final SearchQueryCondition condition,
                    final SearchQueryData searchQueryData)
    {
        final List<SearchQueryCondition> finalConditions = new LinkedList<>();
        if(condition instanceof SearchQueryConditionList)
        {
            final List<SearchQueryCondition> conditions = Lists.newArrayList();
            for(final SearchQueryCondition aCondition : ((SearchQueryConditionList)condition).getConditions())
            {
                conditions.addAll(adjustConditionRecursively(aCondition, searchQueryData));
            }
            finalConditions.add(new SearchQueryConditionList(condition.getOperator(), conditions));
        }
        else
        {
            SimpleSearchHelper.processDefaultComparisonOperator(searchQueryData, condition);
            finalConditions.addAll(adjustCondition(condition.getValue(), searchQueryData, condition.getDescriptor(),
                            condition.getOperator(), condition.isFilteringCondition()));
        }
        return finalConditions;
    }


    /**
     * Security threat issue: https://cxjira.sap.com/browse/DRB-450 | Type level permission | Type/Subtype | To exclude
     * unauthorized subtype from search results. |
     *
     * @param conditions
     * @param searchQueryData
     */
    protected void excludeNoReadPermissionSubTypes(final List<SearchQueryCondition> conditions,
                    final SearchQueryData searchQueryData)
    {
        if(!searchQueryData.isIncludeSubtypes())
        {
            return;
        }
        final ComposedTypeModel typeModel = typeService.getComposedTypeForCode(searchQueryData.getSearchType());
        final Collection<ComposedTypeModel> subTypes = typeModel.getAllSubTypes();
        if(CollectionUtils.isEmpty(subTypes))
        {
            return;
        }
        final StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        final StringBuilder excludedSubtypeCodes = new StringBuilder();
        final Collection<ComposedTypeModel> excludedSubtypes = subTypes.stream().filter(type -> {
            final boolean isSubtypeExcluded = isNeedToExcludeNoReadPermissionSubType(type.getCode(), conditions);
            if(isSubtypeExcluded)
            {
                excludedSubtypeCodes.append(type.getCode()).append(" ");
            }
            return isSubtypeExcluded;
        }).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(excludedSubtypes))
        {
            conditions.add(new SearchQueryCondition(new SearchAttributeDescriptor(ItemModel.ITEMTYPE), excludedSubtypes,
                            ValueComparisonOperator.NOT_IN, true));
            LOG.debug("add filter condition for no read permission sub-types[ {}]", excludedSubtypeCodes);
        }
        stopwatch.stop();
        LOG.debug("Exclude unauthorized subtype from search results cost {} ms", stopwatch.getTime());
    }


    /**
     * Security threat issue: https://cxjira.sap.com/browse/ECP-5491 | Attribute/type level permission | Type/Subtype | To exclude
     * subtype from search results if any following conditions match:
     * 1. No read permission for subtype
     * 2. If attribute level permission check enabled and the search condition contains at least one unauthorized attribute of this subtype.
     * @param typeCode
     * @param conditions
     */
    protected boolean isNeedToExcludeNoReadPermissionSubType(final String typeCode, final List<SearchQueryCondition> conditions)
    {
        if(!getPermissionFacade().canReadType(typeCode))
        {
            return true;
        }
        if(!isAttributePermissionCheckEnabled())
        {
            return false;
        }
        for(final SearchQueryCondition condition : conditions)
        {
            if(hasNoReadPermissionAttribute(typeCode, condition))
            {
                return true;
            }
        }
        return false;
    }


    /**
     * Hierarchy check if the condition contains at least one no read permission attribute
     *
     * @param typeCode
     * @param condition
     */
    private boolean hasNoReadPermissionAttribute(final String typeCode, final SearchQueryCondition condition)
    {
        if(condition instanceof SearchQueryConditionList)
        {
            for(final SearchQueryCondition aCondition : ((SearchQueryConditionList)condition).getConditions())
            {
                if(hasNoReadPermissionAttribute(typeCode, aCondition))
                {
                    return true;
                }
            }
            return false;
        }
        return !getPermissionFacade().canReadProperty(typeCode, condition.getDescriptor().getAttributeName());
    }


    protected boolean isAttributePermissionCheckEnabled()
    {
        return Config.getBoolean(ATTRIBUTE_PERMISSION_CHECK_PROPERTY, true);
    }


    private List<SearchQueryCondition> adjustCondition(final Object attributeValue, final SearchQueryData searchQueryData,
                    final SearchAttributeDescriptor descriptor, final ValueComparisonOperator operator, final boolean filteringCondition)
    {
        final TypeModel attributeType = typeService
                        .getAttributeDescriptor(searchQueryData.getSearchType(), descriptor.getAttributeName()).getAttributeType();
        final List<SearchQueryCondition> conditions = new LinkedList<>();
        if(isAtomicTypeNotAssignableFromValue(attributeType, attributeValue) && operator.isRequireValue())
        {
            if(attributeValue instanceof String && Number.class.isAssignableFrom(((AtomicTypeModel)attributeType).getJavaClass()))
            {
                try
                {
                    final Number number = NumberUtils.createNumber((String)attributeValue);
                    conditions.add(new SearchQueryCondition(descriptor, number, ValueComparisonOperator.EQUALS, filteringCondition));
                }
                catch(final NumberFormatException e)
                {
                    LOG.warn(e.getMessage());
                }
            }
            else if(attributeValue instanceof String
                            && Boolean.class.isAssignableFrom(((AtomicTypeModel)attributeType).getJavaClass()))
            {
                final Boolean boolValue = BooleanUtils.toBooleanObject((String)attributeValue);
                if(boolValue != null)
                {
                    conditions
                                    .add(new SearchQueryCondition(descriptor, boolValue, ValueComparisonOperator.EQUALS, filteringCondition));
                }
                else
                {
                    LOG.warn("Boolean value expected for attribute {}: {}", descriptor.getAttributeName(), attributeValue);
                }
            }
            else if(isListOrSet(attributeValue) && isPK(attributeType))
            {
                conditions.add(getPKListCondition((Collection)attributeValue, descriptor, operator, filteringCondition));
            }
            else if(isPK(attributeType))
            {
                final Optional<PK> optionalPk = createPKFromValue(attributeValue);
                optionalPk.ifPresent(pk -> conditions
                                .add(new SearchQueryCondition(descriptor, pk, ValueComparisonOperator.EQUALS, filteringCondition)));
            }
            else if(isListOrSet(attributeValue)
                            && isAtomicTypeAssignableWithCollectionContent(attributeType, (Collection)attributeValue))
            {
                final List values = new ArrayList((Collection)attributeValue);
                conditions.add(new SearchQueryCondition(descriptor, values, operator, filteringCondition));
            }
            else
            {
                LOG.warn("Cannot search by attribute: {}", descriptor.getAttributeName());
            }
        }
        else
        {
            final ValueComparisonOperator comparisonOperator = operator == null
                            ? searchQueryData.getValueComparisonOperator(descriptor)
                            : operator;
            conditions.add(new SearchQueryCondition(descriptor, attributeValue, comparisonOperator, filteringCondition));
        }
        return conditions;
    }


    private SearchQueryCondition getPKListCondition(final Collection attributeValue, final SearchAttributeDescriptor descriptor,
                    final ValueComparisonOperator operator, final boolean filteringCondition)
    {
        final List<Optional<PK>> pkOptionalList = (List)attributeValue.stream().map(this::createPKFromValue)
                        .collect(Collectors.toList());
        final List<PK> pkList = pkOptionalList.stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        return new SearchQueryCondition(descriptor, pkList, operator, filteringCondition);
    }


    private static boolean isAtomicTypeAssignableWithCollectionContent(final TypeModel attributeType, final Collection collection)
    {
        final Optional notAssignableElement = collection.stream()
                        .filter(element -> isAtomicTypeNotAssignableFromValue(attributeType, element)).findAny();
        return !notAssignableElement.isPresent();
    }


    private static boolean isAtomicTypeNotAssignableFromValue(final TypeModel attributeType, final Object attributeValue)
    {
        return attributeValue != null && attributeType instanceof AtomicTypeModel
                        && !((AtomicTypeModel)attributeType).getJavaClass().isAssignableFrom(attributeValue.getClass());
    }


    private Optional<PK> createPKFromValue(final Object value)
    {
        long pkValue = 0;
        if(value instanceof Number)
        {
            pkValue = ((Number)value).longValue();
        }
        else if(value instanceof String)
        {
            pkValue = NumberUtils.toLong((String)value);
        }
        else if(value instanceof PK)
        {
            return Optional.ofNullable((PK)value);
        }
        else
        {
            LOG.warn("Cannot convertToExecutedSyncTask {} to PK", value);
        }
        if(pkValue > 0)
        {
            return Optional.ofNullable(PK.fromLong(pkValue));
        }
        return Optional.empty();
    }


    private static boolean isPK(final TypeModel attributeType)
    {
        return PK.class.getCanonicalName().equalsIgnoreCase(attributeType.getCode());
    }


    private static boolean isListOrSet(final Object object)
    {
        return object instanceof Set || object instanceof List;
    }


    /**
     * Uses {@link GenericQuery} to build flexible search query based on {@link SearchQueryData} information.
     *
     * @param searchQueryData
     * @return String representing flexible search query
     */
    protected GenericSearchQuery buildQuery(final SearchQueryData searchQueryData)
    {
        validateParameterNotNull(searchQueryData, "Parameter 'searchQueryData' must not be null!");
        validateParameterNotNull(searchQueryData.getSearchType(), "Parameter 'searchQueryData.typeCode' must not be empty!");
        final String typeCode = searchQueryData.getSearchType();
        final GenericQuery query = new GenericQuery(typeCode);
        if(CollectionUtils.isNotEmpty(searchQueryData.getConditions()))
        {
            final List<GenericCondition> conditions = new ArrayList<>();
            final List<GenericCondition> filteringConditions = new ArrayList<>();
            for(final SearchQueryCondition condition : searchQueryData.getConditions())
            {
                final List<GenericCondition> genericConditions = genericMultiConditionQueryBuilder.buildQuery(query, typeCode,
                                condition, searchQueryData);
                if(condition.isFilteringCondition())
                {
                    filteringConditions.addAll(genericConditions);
                }
                else
                {
                    conditions.addAll(genericConditions);
                }
            }
            final GenericConditionList allConditions = GenericCondition.createConditionList(conditions,
                            getConditionsOperator(searchQueryData));
            final GenericConditionList allFilteringConditions = GenericCondition.createConditionList(filteringConditions,
                            Operator.AND);
            joinConditionsWithFilteringConditions(allConditions, allFilteringConditions).ifPresent(query::addCondition);
        }
        final GenericSearchOrderBy orderBy = createSortCondition(query, typeCode, searchQueryData);
        if(orderBy != null)
        {
            query.addOrderBy(orderBy);
        }
        query.setTypeExclusive(!searchQueryData.isIncludeSubtypes());
        return new GenericSearchQuery(query);
    }


    protected Optional<GenericCondition> joinConditionsWithFilteringConditions(final GenericConditionList conditionList,
                    final GenericConditionList filteringConditionList)
    {
        if(conditionList.isEmpty() && filteringConditionList.isEmpty())
        {
            return Optional.empty();
        }
        else if(conditionList.isEmpty())
        {
            return Optional.of(filteringConditionList);
        }
        else if(filteringConditionList.isEmpty())
        {
            return Optional.of(conditionList);
        }
        else
        {
            return Optional.of(GenericCondition.createConditionList(Operator.AND, filteringConditionList, conditionList));
        }
    }


    protected GenericSearchOrderBy createSortCondition(final GenericQuery query, final String typeCode,
                    final SearchQueryData searchQueryData)
    {
        GenericSearchOrderBy ret = null;
        if(searchQueryData.getSortData() != null && StringUtils.isNotBlank(searchQueryData.getSortData().getSortAttribute()))
        {
            final String qualifier = searchQueryData.getSortData().getSortAttribute();
            final AttributeDescriptorModel attDescriptor = typeService.getAttributeDescriptor(typeCode, qualifier);
            if(isSortable(attDescriptor))
            {
                final boolean asc = searchQueryData.getSortData().isAscending();
                if(attDescriptor.getAttributeType() instanceof EnumerationMetaTypeModel && sorEnumByLocalizedNameEnabled())
                {
                    final ComposedTypeModel sortType = (ComposedTypeModel)attDescriptor.getAttributeType();
                    ret = createEnumSortOrder(query, sortType.getCode(), qualifier, asc);
                }
                else
                {
                    ret = new GenericSearchOrderBy(new GenericSearchField(typeCode, searchQueryData.getSortData().getSortAttribute()),
                                    searchQueryData.getSortData().isAscending());
                }
            }
        }
        return ret;
    }


    protected GenericSearchOrderBy createEnumSortOrder(final GenericQuery query, final String sortType, final String qualifier,
                    final boolean asc)
    {
        final String aliasCode = String.format("%s_sort", sortType);
        query.addOuterJoin(sortType, aliasCode, GenericCondition.createJoinCondition(new GenericSearchField(qualifier),
                        new GenericSearchField(aliasCode, ItemModel.PK)));
        final GenericSearchOrderBy orderBy = new GenericSearchOrderBy(new GenericSearchField(aliasCode, EnumerationValueModel.NAME),
                        asc);
        orderBy.getField().addFieldType(GenericSearchFieldType.LOCALIZED);
        final LanguageModel langModel = commonI18NService.getCurrentLanguage();
        orderBy.getField().setLanguagePK(langModel.getPk());
        return orderBy;
    }


    protected boolean sorEnumByLocalizedNameEnabled()
    {
        boolean ret = true;
        final String booleanString = cockpitProperties.getProperty(SORT_ENUM_BY_LOCALIZED_NAME);
        if(StringUtils.isNotEmpty(booleanString))
        {
            ret = Boolean.parseBoolean(booleanString);
        }
        return ret;
    }


    /**
     * Returns logical operator which joins query conditions (or, and)
     *
     * @param searchQueryData
     * @return OR operator
     */
    protected Operator getConditionsOperator(final SearchQueryData searchQueryData)
    {
        return Operator.getOperatorByStringCode(searchQueryData.getGlobalComparisonOperator().getOperatorCode());
    }


    @Override
    public boolean isSortable(final DataType type, final String attributeQualifier, final Context context)
    {
        final DataAttribute attribute = type.getAttribute(attributeQualifier);
        if(attribute == null || !attribute.isSearchable())
        {
            return false;
        }
        final DataType valueType = attribute.getValueType();
        if(valueType == null)
        {
            return false;
        }
        else
        {
            final boolean isAttributeTypeSingle = DataAttribute.AttributeType.SINGLE.equals(attribute.getAttributeType());
            return isAttributeTypeSingle && (valueType.isAtomic() || valueType.isEnum());
        }
    }


    private boolean isSortable(final AttributeDescriptorModel attributeDescriptor)
    {
        boolean ret = false;
        if(attributeDescriptor != null)
        {
            final TypeModel attributeType = attributeDescriptor.getAttributeType();
            ret = attributeType instanceof AtomicTypeModel || attributeType instanceof ComposedTypeModel
                            || BooleanUtils.toBoolean(attributeDescriptor.getLocalized());
        }
        return ret;
    }


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    public void setConfigurationService(final ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected GenericSearchService getGenericSearchService()
    {
        return genericSearchService;
    }


    @Required
    public void setGenericSearchService(final GenericSearchService genericSearchService)
    {
        this.genericSearchService = genericSearchService;
    }


    public TypeService getTypeService()
    {
        return typeService;
    }


    @Required
    public void setTypeService(final TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setPlatformFacadeStrategyHandleCache(final PlatformFacadeStrategyHandleCache platformFacadeStrategyHandleCache)
    {
        this.platformFacadeStrategyHandleCache = platformFacadeStrategyHandleCache;
    }


    @Required
    public void setCommonI18NService(final CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    protected CockpitProperties getCockpitProperties()
    {
        return this.cockpitProperties;
    }


    protected PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    @Required
    public void setGenericMultiConditionQueryBuilder(final ConditionQueryBuilder genericMultiConditionQueryBuilder)
    {
        this.genericMultiConditionQueryBuilder = genericMultiConditionQueryBuilder;
    }


    @Override
    public String getStrategyName()
    {
        return STRATEGY_NAME;
    }


    public BackofficeDeletedItemCache getDeletedItemCache()
    {
        return deletedItemCache;
    }


    public void setDeletedItemCache(final BackofficeDeletedItemCache deletedItemCache)
    {
        this.deletedItemCache = deletedItemCache;
    }


    private class BackofficeFlexibleSearchPageable implements Pageable<T>
    {
        private final SearchQueryData searchQueryData;
        private String typeCode;
        private GenericSearchQuery query;
        private int pageSize;
        private int totalCount = -1;
        private int currentStart = 0;
        private List<T> currentPageCache;
        private boolean initialized;
        private String queryId;
        private CockpitEvent cockpitEvent;


        /**
         * @param query
         * @param searchQueryData
         */
        public BackofficeFlexibleSearchPageable(final GenericSearchQuery query, final SearchQueryData searchQueryData)
        {
            super();
            if(searchQueryData != null)
            {
                this.typeCode = searchQueryData.getSearchType();
                this.pageSize = searchQueryData.getPageSize();
                if(searchQueryData.getPageSize() <= 0)
                {
                    throw new IllegalArgumentException("Page size must be a positive number");
                }
            }
            this.query = query;
            this.searchQueryData = searchQueryData;
            this.initialized = false;
            this.cockpitEvent = null;
        }


        private void initialize()
        {
            if(!initialized)
            {
                getCurrentPage();
                initialized = true;
            }
        }


        @Override
        public List<T> getCurrentPage()
        {
            if(CollectionUtils.isNotEmpty(currentPageCache))
            {
                return currentPageCache;
            }
            List<T> result = Collections.emptyList();
            if(typeService.getTypeForCode(getTypeCode()) instanceof ViewTypeModel)
            {
                final JaloSession session = JaloSession.getCurrentSession();
                final SearchContext ctx = session.createSearchContext();
                ctx.setRange(initialized ? (getPageNumber() * getPageSize()) : 0, getPageSize());
                ctx.setProperty(GenericSearchConstants.DONT_NEED_TOTAL, Boolean.FALSE);
                final de.hybris.platform.jalo.SearchResult jaloSearchResult = session.search(query.getQuery(), ctx);
                if(jaloSearchResult != null)
                {
                    result = jaloSearchResult.getResult();
                    totalCount = jaloSearchResult.getTotalCount();
                    if(CollectionUtils.isNotEmpty(result))
                    {
                        currentStart = jaloSearchResult.getRequestedStart();
                        cacheCurrentPage(result);
                    }
                }
            }
            else
            {
                query = buildQuery(searchQueryData);
                query.setNeedTotal(true);
                if(pageSize > 0)
                {
                    query.setCount(pageSize);
                }
                query.setStart(currentStart);
                final SearchResult<T> searchResult = searchWithReadReplicaIfPossible(query);
                totalCount = 0;
                if(searchResult != null)
                {
                    totalCount = searchResult.getTotalCount();
                    result = updateReadReplicaSearchResult4DeleteEvent(searchResult);
                    if(CollectionUtils.isNotEmpty(result))
                    {
                        currentStart = searchResult.getRequestedStart();
                        cacheCurrentPage(result);
                    }
                }
            }
            initialized = true;
            return result;
        }


        protected SearchResult<T> searchWithReadReplicaIfPossible(final GenericSearchQuery query)
        {
            try
            {
                if(isQueryForReadReplica(query))
                {
                    final SessionContext ctx = JaloSession.getCurrentSession().createLocalSessionContext();
                    ctx.setAttribute(ReadOnlyConditionsHelper.CTX_ENABLE_FS_ON_READ_REPLICA, true);
                    ctx.setAttribute(ENABLE_CACHE_FOR_READ_ONLY_DATA_SOURCE, isCacheForBackOfficeOnReadOnlyReplicaEnabled());
                }
                return getGenericSearchService().search(query);
            }
            finally
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }


        private boolean isCacheForBackOfficeOnReadOnlyReplicaEnabled()
        {
            return getPropertyValueOrDefaultFalse(BACKOFFICE_CACHE_ON_READ_REPLICA_ENABLED);
        }


        private boolean isQueryForReadReplica(final GenericSearchQuery genericSearchQuery)
        {
            if(!this.isReadOnlyReplicaEnabled())
            {
                return false;
            }
            final String excludedTypeCodes = configurationService.getConfiguration()
                            .getString(BACKOFFICE_SEARCH_READ_REPLICA_TYPE_CODES_EXCLUDE,
                                            "");
            final Set<String> typeCodes = Set.of(excludedTypeCodes.split(","));
            return !typeCodes.contains(genericSearchQuery.getQuery().getInitialTypeCode());
        }


        private boolean isReadOnlyReplicaEnabled()
        {
            return getPropertyValueOrDefaultFalse(BACKOFFICE_SEARCH_READ_REPLICA_ENABLED);
        }


        private boolean getPropertyValueOrDefaultFalse(final String propertyName)
        {
            try
            {
                return configurationService.getConfiguration().getBoolean(propertyName, false);
            }
            catch(final ConversionException e)
            {
                LOG.debug("{} property hasn't been set properly to boolean value, true is taken instead", propertyName);
                return false;
            }
        }


        private List<T> updateReadReplicaSearchResult4DeleteEvent(final SearchResult<T> searchResult)
        {
            if(isReadOnlyReplicaEnabled() && Objects.nonNull(getDeletedItemCache()))
            {
                final List<T> searchResultValue = searchResult.getResult();
                final List<T> updatedResult = new ArrayList<>(searchResultValue.size());
                // Due to searchResult.getResult() method will return Collections.unmodifiableList,
                // so here is not directly remove should be deleted records from this collection
                updatedResult.addAll(searchResultValue);
                if(isItemDeleteEvent())
                {
                    final Collection<Object> deletedItems = this.cockpitEvent.getDataAsCollection();
                    // Cache deleted item
                    deletedItems.stream().map(ItemModel.class::cast).filter(Objects::nonNull).forEach(deletedItemModel -> {
                        updatedResult.removeIf(updatedItem -> {
                            final boolean isCanBeDeleted = Objects.isNull(updatedItem) ||
                                            deletedItemModel.getPk().equals(updatedItem.getPk());
                            if(isCanBeDeleted)
                            {
                                totalCount -= 1;
                                return true;
                            }
                            else
                            {
                                return false;
                            }
                        });
                        getDeletedItemCache().storeDeletedItem(deletedItemModel);
                    });
                }
                // Filter history deleted Items
                updatedResult.removeIf(item -> {
                    final boolean isCanBeDeleted = Objects.isNull(item) || getDeletedItemCache().isExistingInCache(item);
                    if(isCanBeDeleted)
                    {
                        totalCount -= 1;
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                });
                return updatedResult;
            }
            else
            {
                return Objects.nonNull(searchResult) ? searchResult.getResult() : Collections.<T>emptyList();
            }
        }


        private boolean isItemDeleteEvent()
        {
            return Objects.nonNull(this.cockpitEvent) &&
                            ObjectCRUDHandler.OBJECTS_DELETED_EVENT.equals(this.cockpitEvent.getName()) &&
                            Objects.nonNull(this.cockpitEvent.getDataAsCollection()) &&
                            !this.cockpitEvent.getDataAsCollection().isEmpty();
        }


        @Override
        public void refresh()
        {
            invalidateCurrentPageCache();
            initialized = false;
            cockpitEvent = null;
        }


        private void cacheCurrentPage(final List<T> result)
        {
            currentPageCache = result;
        }


        private void invalidateCurrentPageCache()
        {
            currentPageCache = null;
        }


        @Override
        public int getPageSize()
        {
            return pageSize;
        }


        @Override
        public String getTypeCode()
        {
            return typeCode;
        }


        @Override
        public boolean hasNextPage()
        {
            if(pageSize <= 0)
            {
                return false;
            }
            initialize();
            return totalCount > (currentStart + pageSize);
        }


        @Override
        public boolean hasPreviousPage()
        {
            initialize();
            return currentStart > 0;
        }


        @Override
        public List<T> setPageSize(final int pageSize)
        {
            this.pageSize = pageSize;
            invalidateCurrentPageCache();
            return getCurrentPage();
        }


        /**
         * Returns count of all items in the collection that is being paged. Important: the returned value is valid only after
         * first call to {@link #getCurrentPage()}.
         *
         * @return total items count
         */
        @Override
        public int getTotalCount()
        {
            initialize();
            return totalCount;
        }


        @Override
        public List<T> nextPage()
        {
            if(hasNextPage())
            {
                currentStart += pageSize;
                invalidateCurrentPageCache();
                return getCurrentPage();
            }
            return Collections.emptyList();
        }


        @Override
        public List<T> previousPage()
        {
            if(hasPreviousPage())
            {
                currentStart -= pageSize;
                if(currentStart < 0)
                {
                    currentStart = 0;
                }
                invalidateCurrentPageCache();
                return getCurrentPage();
            }
            return Collections.emptyList();
        }


        @Override
        public int getPageNumber()
        {
            initialize();
            return currentStart / pageSize;
        }


        @Override
        public void setPageNumber(final int pageNo)
        {
            initialize();
            final int newStart = pageNo * pageSize;
            if(newStart != currentStart)
            {
                currentStart = newStart;
                invalidateCurrentPageCache();
            }
        }


        @Override
        public SortData getSortData()
        {
            return this.searchQueryData.getSortData();
        }


        @Override
        public void setSortData(final SortData sortData)
        {
            if(ObjectUtils.notEqual(searchQueryData.getSortData(), sortData))
            {
                searchQueryData.setSortData(sortData);
                invalidateCurrentPageCache();
            }
        }


        @Override
        public List<T> getAllResults()
        {
            final GenericSearchQuery clonedQuery = new GenericSearchQuery(query.getQuery());
            clonedQuery.setStart(0);
            clonedQuery.setCount(getTotalCount());
            clonedQuery.setNeedTotal(false);
            if(typeService.getTypeForCode(getTypeCode()) instanceof ViewTypeModel)
            {
                final de.hybris.platform.jalo.SearchResult jaloSearchResult = JaloSession.getCurrentSession()
                                .search(clonedQuery.getQuery());
                if(jaloSearchResult != null)
                {
                    return jaloSearchResult.getResult();
                }
                return Collections.emptyList();
            }
            else
            {
                final SearchResult<T> searchResult = this.searchWithReadReplicaIfPossible(clonedQuery);
                return updateReadReplicaSearchResult4DeleteEvent(searchResult);
            }
        }


        @Override
        public String getQueryId()
        {
            return queryId;
        }


        @Override
        public void setQueryId(final String queryId)
        {
            this.queryId = queryId;
        }


        @Override
        public void setCockpitEvent(final CockpitEvent event)
        {
            this.cockpitEvent = event;
        }
    }


    private class EmptyBackofficeFlexibleSearchPageable extends BackofficeFlexibleSearchPageable
    {
        public EmptyBackofficeFlexibleSearchPageable(final SearchQueryData searchQueryData)
        {
            super(null, searchQueryData);
        }


        @Override
        public List<T> getCurrentPage()
        {
            return Collections.emptyList();
        }


        @Override
        public boolean hasNextPage()
        {
            return false;
        }


        @Override
        public boolean hasPreviousPage()
        {
            return false;
        }


        @Override
        public int getTotalCount()
        {
            return 0;
        }


        @Override
        public int getPageNumber()
        {
            return 0;
        }
    }
}
