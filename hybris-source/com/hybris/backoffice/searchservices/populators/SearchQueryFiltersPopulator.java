package com.hybris.backoffice.searchservices.populators;

import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryConditionList;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.searchservices.enums.SnFieldType;
import de.hybris.platform.searchservices.model.SnFieldModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.searchservices.search.data.AbstractSnQuery;
import de.hybris.platform.searchservices.search.data.SnAndQuery;
import de.hybris.platform.searchservices.search.data.SnBucketsFacetFilter;
import de.hybris.platform.searchservices.search.data.SnEqualQuery;
import de.hybris.platform.searchservices.search.data.SnExistsQuery;
import de.hybris.platform.searchservices.search.data.SnFilter;
import de.hybris.platform.searchservices.search.data.SnGreaterThanOrEqualQuery;
import de.hybris.platform.searchservices.search.data.SnGreaterThanQuery;
import de.hybris.platform.searchservices.search.data.SnLessThanOrEqualQuery;
import de.hybris.platform.searchservices.search.data.SnLessThanQuery;
import de.hybris.platform.searchservices.search.data.SnMatchQuery;
import de.hybris.platform.searchservices.search.data.SnMatchTermQuery;
import de.hybris.platform.searchservices.search.data.SnMatchTermsQuery;
import de.hybris.platform.searchservices.search.data.SnMatchType;
import de.hybris.platform.searchservices.search.data.SnNotQuery;
import de.hybris.platform.searchservices.search.data.SnOrQuery;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchQueryFiltersPopulator implements Populator<SnSearchQueryConverterData, SnSearchQuery>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchQueryFiltersPopulator.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private BackofficeFacetSearchConfigService facetSearchConfigService;
    private TypeService typeService;
    private CatalogTypeService catalogTypeService;
    private UserService userService;
    private CatalogVersionService catalogVersionService;


    public void populate(SnSearchQueryConverterData snSearchQueryConverterData, SnSearchQuery snSearchQuery)
    {
        SearchQueryData searchQueryData = snSearchQueryConverterData.getSearchQueryData();
        if(Objects.nonNull(searchQueryData))
        {
            addFacetFilters(searchQueryData, snSearchQuery);
            addQueryFilters(searchQueryData, snSearchQuery);
        }
    }


    private void addFacetFilters(SearchQueryData searchQueryData, SnSearchQuery snSearchQuery)
    {
        Map<String, Set<String>> selectedFacets = searchQueryData.getSelectedFacets();
        if(MapUtils.isNotEmpty(selectedFacets))
        {
            selectedFacets.forEach((selectedFacetField, selectedFacetValues) -> {
                if(Objects.nonNull(selectedFacetValues) && !selectedFacetValues.isEmpty())
                {
                    SnBucketsFacetFilter facetFilter = new SnBucketsFacetFilter();
                    facetFilter.setFacetId(selectedFacetField);
                    facetFilter.setBucketIds((List)selectedFacetValues.stream().collect(Collectors.toList()));
                    snSearchQuery.getFacetFilters().add(facetFilter);
                }
            });
        }
    }


    private void addQueryFilters(SearchQueryData searchQueryData, SnSearchQuery snSearchQuery)
    {
        addSearchConditionsQueryFilters(searchQueryData, snSearchQuery);
        addItemTypeQueryFilters(searchQueryData, snSearchQuery);
        addCatalogVersionQueryFilters(searchQueryData, snSearchQuery);
    }


    private void addSearchConditionsQueryFilters(SearchQueryData searchQueryData, SnSearchQuery snSearchQuery)
    {
        List<? extends SearchQueryCondition> conditions = searchQueryData.getConditions();
        conditions.forEach(condition -> {
            AbstractSnQuery snQuery = addSearchConditionsQueryFilters(condition, snSearchQuery, searchQueryData);
            if(Objects.nonNull(snQuery))
            {
                SnFilter snFilter = new SnFilter();
                snFilter.setQuery(snQuery);
                snSearchQuery.getFilters().add(snFilter);
            }
        });
    }


    private AbstractSnQuery addSearchConditionsQueryFilters(SearchQueryCondition searchQueryCondition, SnSearchQuery snSearchQuery, SearchQueryData searchQueryData)
    {
        if(searchQueryCondition instanceof SearchQueryConditionList)
        {
            SearchQueryConditionList searchQueryConditionList = (SearchQueryConditionList)searchQueryCondition;
            List<SearchQueryCondition> subQueryConditions = searchQueryConditionList.getConditions();
            List<AbstractSnQuery> queries = new ArrayList<>(0);
            subQueryConditions.forEach(subQueryCondition -> {
                AbstractSnQuery subSnQuery = addSearchConditionsQueryFilters(subQueryCondition, snSearchQuery, searchQueryData);
                if(Objects.nonNull(subSnQuery))
                {
                    queries.add(subSnQuery);
                }
            });
            if(queries.isEmpty())
            {
                return null;
            }
            ValueComparisonOperator operator = searchQueryConditionList.getOperator();
            switch(null.$SwitchMap$com$hybris$cockpitng$search$data$ValueComparisonOperator[operator.ordinal()])
            {
                case 1:
                    return creatORFilteringCondition(queries);
                case 2:
                    return creatANDFilteringCondition(queries);
            }
            return null;
        }
        return addQueryFilterValue(searchQueryCondition, searchQueryData);
    }


    private AbstractSnQuery creatORFilteringCondition(List<AbstractSnQuery> queries)
    {
        SnOrQuery snOrQuery = new SnOrQuery();
        snOrQuery.setQueries(queries);
        return (AbstractSnQuery)snOrQuery;
    }


    private AbstractSnQuery creatANDFilteringCondition(List<AbstractSnQuery> queries)
    {
        SnAndQuery snAndQuery = new SnAndQuery();
        snAndQuery.setQueries(queries);
        return (AbstractSnQuery)snAndQuery;
    }


    private AbstractSnQuery addQueryFilterValue(SearchQueryCondition queryCondition, SearchQueryData searchQueryData)
    {
        if(Objects.isNull(queryCondition))
        {
            return null;
        }
        if(queryCondition.isFilteringCondition())
        {
            return createFilteringCondition(queryCondition, searchQueryData.getSearchType());
        }
        if(StringUtils.trim(queryCondition.getValue().toString()).equals(searchQueryData.getSearchQueryText()) && (queryCondition
                        .getDescriptor().getAttributeName().equals("pk") || queryCondition
                        .getDescriptor().getAttributeName().equals("code") || queryCondition
                        .getDescriptor().getAttributeName().equals("name")))
        {
            return null;
        }
        return createFilteringCondition(queryCondition, searchQueryData.getSearchType());
    }


    private AbstractSnQuery createFilteringCondition(SearchQueryCondition queryCondition, String searchType)
    {
        SnIndexTypeModel snIndexTypeModel = (SnIndexTypeModel)this.facetSearchConfigService.getIndexedTypeModel(searchType);
        if(Objects.isNull(snIndexTypeModel))
        {
            return null;
        }
        String attributeName = queryCondition.getDescriptor().getAttributeName();
        Optional<SnFieldModel> fieldModel = snIndexTypeModel.getFields().stream().filter(snFieldModel -> snFieldModel.getId().equalsIgnoreCase(attributeName)).findFirst();
        if(fieldModel.isEmpty())
        {
            return null;
        }
        return createSnQueryByOperator(queryCondition, fieldModel.get());
    }


    private AbstractSnQuery createSnQueryByOperator(SearchQueryCondition queryCondition, SnFieldModel fieldModel)
    {
        ValueComparisonOperator operator = queryCondition.getOperator();
        switch(null.$SwitchMap$com$hybris$cockpitng$search$data$ValueComparisonOperator[operator.ordinal()])
        {
            case 3:
            case 4:
            case 5:
                return createMatchFilteringCondition(queryCondition, fieldModel);
            case 6:
                return createGreaterEqualFilteringCondition(queryCondition, fieldModel, false);
            case 7:
                return createGreaterEqualFilteringCondition(queryCondition, fieldModel, true);
            case 8:
                return createLessEqualFilteringCondition(queryCondition, fieldModel, false);
            case 9:
                return createLessEqualFilteringCondition(queryCondition, fieldModel, true);
        }
        return createSnQueryByNotOrYesOperator(queryCondition, fieldModel);
    }


    private AbstractSnQuery createSnQueryByNotOrYesOperator(SearchQueryCondition queryCondition, SnFieldModel fieldModel)
    {
        ValueComparisonOperator operator = queryCondition.getOperator();
        switch(null.$SwitchMap$com$hybris$cockpitng$search$data$ValueComparisonOperator[operator.ordinal()])
        {
            case 10:
                return createInFilteringCondition(queryCondition, fieldModel);
            case 11:
                return createNotInFilteringCondition(queryCondition, fieldModel);
            case 12:
                return createIsNotEmptyFilteringCondition(fieldModel);
            case 13:
                return createIsEmptyFilteringCondition(fieldModel);
        }
        return null;
    }


    private AbstractSnQuery createMatchFilteringCondition(SearchQueryCondition queryCondition, SnFieldModel fieldModel)
    {
        SnEqualQuery snEqualQuery;
        AbstractSnQuery snQuery = null;
        if(fieldModel.getFieldType() == SnFieldType.TEXT)
        {
            SnMatchQuery snMatchQuery = new SnMatchQuery();
            snMatchQuery.setExpression(fieldModel.getId());
            snMatchQuery.setValue(convertQueryValueToSnValue(queryCondition, SnFieldType.TEXT));
            SnMatchQuery snMatchQuery1 = snMatchQuery;
        }
        else if(fieldModel.getFieldType() == SnFieldType.STRING)
        {
            SnMatchTermQuery snMatchTermQuery = new SnMatchTermQuery();
            snMatchTermQuery.setExpression(fieldModel.getId());
            snMatchTermQuery.setValue(convertQueryValueToSnValue(queryCondition, SnFieldType.STRING));
            SnMatchTermQuery snMatchTermQuery1 = snMatchTermQuery;
        }
        else
        {
            SnEqualQuery snEqualQuery1 = new SnEqualQuery();
            snEqualQuery1.setExpression(fieldModel.getId());
            snEqualQuery1.setValue(convertQueryValueToSnValue(queryCondition, fieldModel.getFieldType()));
            snEqualQuery = snEqualQuery1;
        }
        return (AbstractSnQuery)snEqualQuery;
    }


    private AbstractSnQuery createGreaterEqualFilteringCondition(SearchQueryCondition queryCondition, SnFieldModel fieldModel, boolean isGreaterEqualOperator)
    {
        SnGreaterThanQuery snGreaterThanQuery;
        AbstractSnQuery snQuery = null;
        if(isGreaterEqualOperator)
        {
            SnGreaterThanOrEqualQuery snGreaterThanOrEqualQuery = new SnGreaterThanOrEqualQuery();
            snGreaterThanOrEqualQuery.setExpression(fieldModel.getId());
            snGreaterThanOrEqualQuery.setValue(convertQueryValueToSnValue(queryCondition, fieldModel.getFieldType()));
            SnGreaterThanOrEqualQuery snGreaterThanOrEqualQuery1 = snGreaterThanOrEqualQuery;
        }
        else
        {
            SnGreaterThanQuery snGreaterThanQuery1 = new SnGreaterThanQuery();
            snGreaterThanQuery1.setExpression(fieldModel.getId());
            snGreaterThanQuery1.setValue(convertQueryValueToSnValue(queryCondition, fieldModel.getFieldType()));
            snGreaterThanQuery = snGreaterThanQuery1;
        }
        return (AbstractSnQuery)snGreaterThanQuery;
    }


    private AbstractSnQuery createLessEqualFilteringCondition(SearchQueryCondition queryCondition, SnFieldModel fieldModel, boolean isLessEqualOperator)
    {
        SnLessThanQuery snLessThanQuery;
        AbstractSnQuery snQuery = null;
        if(isLessEqualOperator)
        {
            SnLessThanOrEqualQuery snLessThanOrEqualQuery = new SnLessThanOrEqualQuery();
            snLessThanOrEqualQuery.setExpression(fieldModel.getId());
            snLessThanOrEqualQuery.setValue(convertQueryValueToSnValue(queryCondition, fieldModel.getFieldType()));
            SnLessThanOrEqualQuery snLessThanOrEqualQuery1 = snLessThanOrEqualQuery;
        }
        else
        {
            SnLessThanQuery snLessThanQuery1 = new SnLessThanQuery();
            snLessThanQuery1.setExpression(fieldModel.getId());
            snLessThanQuery1.setValue(convertQueryValueToSnValue(queryCondition, fieldModel.getFieldType()));
            snLessThanQuery = snLessThanQuery1;
        }
        return (AbstractSnQuery)snLessThanQuery;
    }


    private AbstractSnQuery createInFilteringCondition(SearchQueryCondition queryCondition, SnFieldModel fieldModel)
    {
        SnMatchTermsQuery snMatchTermsQuery;
        AbstractSnQuery snQuery = null;
        if(fieldModel.getFieldType() == SnFieldType.STRING)
        {
            SnMatchTermsQuery snMatchTermsQuery1 = new SnMatchTermsQuery();
            snMatchTermsQuery1.setExpression(fieldModel.getId());
            snMatchTermsQuery1.setMatchType(SnMatchType.ANY);
            snMatchTermsQuery1.setValues((List)convertQueryValueToSnValue(queryCondition, fieldModel.getFieldType()));
            snMatchTermsQuery = snMatchTermsQuery1;
        }
        return (AbstractSnQuery)snMatchTermsQuery;
    }


    private AbstractSnQuery createNotInFilteringCondition(SearchQueryCondition queryCondition, SnFieldModel fieldModel)
    {
        SnNotQuery snNotQuery = new SnNotQuery();
        snNotQuery.setQuery(createInFilteringCondition(queryCondition, fieldModel));
        return (AbstractSnQuery)snNotQuery;
    }


    private AbstractSnQuery createIsNotEmptyFilteringCondition(SnFieldModel fieldModel)
    {
        SnExistsQuery snQuery = new SnExistsQuery();
        snQuery.setExpression(fieldModel.getId());
        return (AbstractSnQuery)snQuery;
    }


    private AbstractSnQuery createIsEmptyFilteringCondition(SnFieldModel fieldModel)
    {
        SnNotQuery snNotQuery = new SnNotQuery();
        snNotQuery.setQuery(createIsNotEmptyFilteringCondition(fieldModel));
        return (AbstractSnQuery)snNotQuery;
    }


    private void addItemTypeQueryFilters(SearchQueryData searchQueryData, SnSearchQuery snSearchQuery)
    {
        SnIndexTypeModel snIndexTypeModel = (SnIndexTypeModel)this.facetSearchConfigService.getIndexedTypeModel(searchQueryData.getSearchType());
        if(Objects.isNull(snIndexTypeModel))
        {
            return;
        }
        Optional<SnFieldModel> snFieldModel = snIndexTypeModel.getFields().stream().filter(fieldModel -> fieldModel.getId().equalsIgnoreCase("itemtype")).findFirst();
        if(snFieldModel.isEmpty())
        {
            LOGGER.warn("No '{}' field found for '{}' indexed type. Too many results may be returned.", "itemtype", snIndexTypeModel.getId());
            return;
        }
        SnMatchTermsQuery snMatchTermsQuery = new SnMatchTermsQuery();
        snMatchTermsQuery.setMatchType(SnMatchType.ANY);
        snMatchTermsQuery.setExpression(((SnFieldModel)snFieldModel.get()).getId());
        List<Object> values = new ArrayList();
        snMatchTermsQuery.setValues(values);
        values.add(searchQueryData.getSearchType());
        ComposedTypeModel composedTypeForCode = this.typeService.getComposedTypeForCode(searchQueryData.getSearchType());
        if(searchQueryData.isIncludeSubtypes() &&
                        Objects.nonNull(composedTypeForCode) &&
                        !composedTypeForCode.getAllSubTypes().isEmpty())
        {
            composedTypeForCode.getAllSubTypes().forEach(subType -> values.add(subType.getCode()));
        }
        SnFilter snFilter = new SnFilter();
        snFilter.setQuery((AbstractSnQuery)snMatchTermsQuery);
        snSearchQuery.getFilters().add(snFilter);
    }


    private void addCatalogVersionQueryFilters(SearchQueryData searchQueryData, SnSearchQuery snSearchQuery)
    {
        SnIndexTypeModel snIndexTypeModel = (SnIndexTypeModel)this.facetSearchConfigService.getIndexedTypeModel(searchQueryData.getSearchType());
        if(Objects.isNull(snIndexTypeModel))
        {
            return;
        }
        Optional<SnFieldModel> snFieldModel = snIndexTypeModel.getFields().stream().filter(fieldModel -> fieldModel.getId().equalsIgnoreCase("catalogVersionPk")).findFirst();
        if(snFieldModel.isEmpty())
        {
            LOGGER.warn("No '{}' field found for '{}' indexed type. Too many results may be returned.", "catalogVersionPk", snIndexTypeModel.getId());
            return;
        }
        ComposedTypeModel composedTypeForCode = this.typeService.getComposedTypeForCode(searchQueryData.getSearchType());
        UserModel currentUser = getUserService().getCurrentUser();
        if(Objects.nonNull(composedTypeForCode) && getCatalogTypeService().isCatalogVersionAwareType(composedTypeForCode) && !getUserService().isAdmin(currentUser))
        {
            List<Object> catalogVersionPKs = (List<Object>)getCatalogVersionService().getAllReadableCatalogVersions((PrincipalModel)currentUser).stream().map(AbstractItemModel::getPk).map(PK::toString).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(catalogVersionPKs))
            {
                SnMatchTermsQuery snMatchTermsQuery = new SnMatchTermsQuery();
                snMatchTermsQuery.setMatchType(SnMatchType.ANY);
                snMatchTermsQuery.setExpression("catalogVersionPk");
                snMatchTermsQuery.setValues(catalogVersionPKs);
                SnFilter snFilter = new SnFilter();
                snFilter.setQuery((AbstractSnQuery)snMatchTermsQuery);
                snSearchQuery.getFilters().add(snFilter);
            }
        }
    }


    private Object convertQueryValueToSnValue(SearchQueryCondition queryCondition, SnFieldType fieldType)
    {
        Object value = queryCondition.getValue();
        if(value instanceof Map)
        {
            Map map = (Map)value;
            return map.values().toArray()[0];
        }
        if(value instanceof PK)
        {
            if(fieldType == SnFieldType.STRING)
            {
                return ((PK)value).getLongValueAsString();
            }
            if(fieldType == SnFieldType.LONG)
            {
                return Long.valueOf(((PK)value).getLongValue());
            }
            return ((PK)value).getLong();
        }
        if(value instanceof Date)
        {
            return dateTimeFormatter.format(((Date)value).toInstant().atZone(ZoneOffset.UTC));
        }
        if(value instanceof List)
        {
            return convertListQueryValueToSnValue(value, fieldType);
        }
        return value;
    }


    private Object convertListQueryValueToSnValue(Object value, SnFieldType fieldType)
    {
        List<Object> result = new ArrayList();
        List values = (List)value;
        values.forEach(object -> {
            if(object instanceof PK)
            {
                if(fieldType == SnFieldType.STRING)
                {
                    result.add(((PK)object).getLongValueAsString());
                }
                else if(fieldType == SnFieldType.LONG)
                {
                    result.add(Long.valueOf(((PK)object).getLongValue()));
                }
                else
                {
                    result.add(((PK)object).getLong());
                }
            }
        });
        return result;
    }


    public BackofficeFacetSearchConfigService getFacetSearchConfigService()
    {
        return this.facetSearchConfigService;
    }


    public void setFacetSearchConfigService(BackofficeFacetSearchConfigService facetSearchConfigService)
    {
        this.facetSearchConfigService = facetSearchConfigService;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public CatalogTypeService getCatalogTypeService()
    {
        return this.catalogTypeService;
    }


    public void setCatalogTypeService(CatalogTypeService catalogTypeService)
    {
        this.catalogTypeService = catalogTypeService;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }
}
