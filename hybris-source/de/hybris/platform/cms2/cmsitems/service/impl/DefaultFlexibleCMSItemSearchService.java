package de.hybris.platform.cms2.cmsitems.service.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.cmsitems.service.CMSItemSearchService;
import de.hybris.platform.cms2.cmsitems.service.FlexibleSearchAttributeValueConverter;
import de.hybris.platform.cms2.cmsitems.service.SortStatementFormatter;
import de.hybris.platform.cms2.common.service.SearchHelper;
import de.hybris.platform.cms2.data.CMSItemSearchData;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.enums.SortDirection;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.namedquery.Sort;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.FlexibleSearchUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFlexibleCMSItemSearchService implements CMSItemSearchService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFlexibleCMSItemSearchService.class);
    public static final String QUERY = "SELECT {c.PK} FROM { %s as c } WHERE {c.catalogVersion}=?catalogVersion AND {c.itemtype} IN (?primaryKeys)";
    public static final String UNKONW_TYPE_CODE = "Unknown typeCode.";
    @Deprecated(since = "2105", forRemoval = true)
    public static final String TYPECODES_QUERY_PARAM = "typeCodes";
    public static final String TYPECODES_ABSTRACT_PAGE = "AbstractPage";
    public static final String TYPE_PKS_QUERY_PARAM = "primaryKeys";
    public static final String CATALOG_VERSION_QUERY_PARAM = "catalogVersion";
    public static final String MASK_CHECK = " AND (LOWER({c.uid}) LIKE LOWER(?mask) OR LOWER({c.name}) LIKE LOWER(?mask))";
    public static final String MASK_CHECK_ABSTRACTPAGE = " AND (LOWER({c.uid}) LIKE LOWER(?mask) OR LOWER({c.name}) LIKE LOWER(?mask) OR {c.pk} IN ({{select {pk} FROM {ContentPage} WHERE LOWER({label}) LIKE LOWER(?mask)}}))";
    public static final String MASK_QUERY_PARAM = "mask";
    public static final String ITEM_SEARCH_PARAM_CHECK = " AND {c.%s}=?%s";
    public static final String ITEM_SEARCH_PARAM_CHECK_NULL = " AND {c.%s} IS NULL";
    public static final String EXCLUDED_TYPES_QUERY_PARAM = "excludedTypes";
    public static final String ORDER_BY = " ORDER BY ";
    private CatalogVersionService catalogVersionService;
    private FlexibleSearchService flexibleSearchService;
    private FlexibleSearchAttributeValueConverter flexibleSearchAttributeValueConverter;
    private TypeService typeService;
    private List<SortStatementFormatter> sortStatementFormatters;
    private SortStatementFormatter defaultSortStatementFormatter;
    private Map<String, List<String>> cmsItemSearchTypeBlacklistMap;
    private SearchHelper searchHelper;
    private PermissionCRUDService permissionCRUDService;


    public SearchResult<CMSItemModel> findCMSItems(CMSItemSearchData cmsItemSearchData, PageableData pageableData)
    {
        validateSearchData(cmsItemSearchData, pageableData);
        boolean hasMask = StringUtils.isNotBlank(cmsItemSearchData.getMask());
        boolean hasSort = StringUtils.isNotBlank(pageableData.getSort());
        boolean hasItemSearchParams = MapUtils.isNotEmpty(cmsItemSearchData.getItemSearchParams());
        List<ComposedTypeModel> composedTypes = getValidComposedTypes(cmsItemSearchData);
        String typeCode = getFirstCommonAncestorTypeCode(composedTypes);
        CatalogVersionModel catalogVersionModel = getCatalogVersionService().getCatalogVersion(cmsItemSearchData.getCatalogId(), cmsItemSearchData.getCatalogVersion());
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("catalogVersion", catalogVersionModel);
        StringBuilder queryBuilder = prepareQueryBuilder(composedTypes, typeCode, queryParameters);
        if(hasItemSearchParams)
        {
            appendSearchParams(cmsItemSearchData.getItemSearchParams(), queryBuilder, queryParameters, typeCode);
        }
        if(hasMask)
        {
            if(typeCode.equals("AbstractPage"))
            {
                queryBuilder.append(" AND (LOWER({c.uid}) LIKE LOWER(?mask) OR LOWER({c.name}) LIKE LOWER(?mask) OR {c.pk} IN ({{select {pk} FROM {ContentPage} WHERE LOWER({label}) LIKE LOWER(?mask)}}))");
            }
            else
            {
                queryBuilder.append(" AND (LOWER({c.uid}) LIKE LOWER(?mask) OR LOWER({c.name}) LIKE LOWER(?mask))");
            }
            queryParameters.put("mask", "%" + cmsItemSearchData.getMask() + "%");
        }
        if(hasSort)
        {
            appendSort(pageableData.getSort(), queryBuilder, typeCode);
        }
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString());
        query.addQueryParameters(queryParameters);
        query.setStart(pageableData.getCurrentPage() * pageableData.getPageSize());
        query.setCount(pageableData.getPageSize());
        query.setNeedTotal(true);
        return getFlexibleSearchService().search(query);
    }


    public boolean hasCommonAncestorForTypeCodes(List<String> typeCodes)
    {
        try
        {
            validateCommonAncestorTypeCode(typeCodes);
            return true;
        }
        catch(IllegalArgumentException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e.getMessage(), e);
            }
            return false;
        }
    }


    protected void validateCommonAncestorTypeCode(List<String> typeCodes)
    {
        List<ComposedTypeModel> composedTypes = getValidComposedTypes(typeCodes);
        getFirstCommonAncestorTypeCode(composedTypes);
    }


    protected void validateSearchData(CMSItemSearchData cmsItemSearchData, PageableData pageableData)
    {
        ServicesUtil.validateParameterNotNull(pageableData, "PageableData object cannot be null.");
        ServicesUtil.validateParameterNotNull(cmsItemSearchData, "CMSItemSearchData object cannot be null.");
        if(StringUtils.isNotBlank(cmsItemSearchData.getTypeCode()) && CollectionUtils.isNotEmpty(cmsItemSearchData.getTypeCodes()))
        {
            throw new IllegalArgumentException("Invalid query: either typeCode or typeCodes can be set.");
        }
    }


    protected StringBuilder prepareQueryBuilder(List<ComposedTypeModel> composedTypes, String typeCode, Map<String, Object> queryParameters)
    {
        StringBuilder queryBuilder = new StringBuilder(String.format("SELECT {c.PK} FROM { %s as c } WHERE {c.catalogVersion}=?catalogVersion AND {c.itemtype} IN (?primaryKeys)", new Object[] {typeCode}));
        List<PK> inheritedTypeCodes = getInheritedTypePrimaryKeys(composedTypes);
        queryParameters.put("primaryKeys", inheritedTypeCodes);
        appendTypeExclusions(composedTypes, queryBuilder, queryParameters);
        return queryBuilder;
    }


    protected List<ComposedTypeModel> getValidComposedTypes(CMSItemSearchData cmsItemSearchData)
    {
        List<String> typeCodes = getTypeCodesFromSearchData(cmsItemSearchData);
        return getValidComposedTypes(typeCodes);
    }


    protected List<ComposedTypeModel> getValidComposedTypes(List<String> typeCodes)
    {
        Objects.requireNonNull(getTypeService());
        List<ComposedTypeModel> validComposedTypes = (List<ComposedTypeModel>)typeCodes.stream().filter(typeCodeExists()).map(getTypeService()::getComposedTypeForCode).collect(Collectors.toList());
        if(validComposedTypes.isEmpty())
        {
            throw new IllegalArgumentException(String.format("No valid typeCode found in %s", new Object[] {typeCodes}));
        }
        return validComposedTypes;
    }


    protected Predicate<String> typeCodeExists()
    {
        return typeCode -> {
            try
            {
                getTypeService().getComposedTypeForCode(typeCode);
                return true;
            }
            catch(UnknownIdentifierException e)
            {
                LOG.info("Unknown typeCode.", (Throwable)e);
                return false;
            }
        };
    }


    protected String getFirstCommonAncestorTypeCode(List<ComposedTypeModel> composedTypes)
    {
        List<List<String>> superTypesLists = (List<List<String>>)composedTypes.stream().map(this::getAllSuperTypeCodes).collect(Collectors.toList());
        for(List<String> subList : superTypesLists)
        {
            int lastIdx = IntStream.range(0, subList.size()).filter(i -> ((String)subList.get(i)).equals("CMSItem")).findFirst().orElse(-1);
            List<String> newSubList = subList.subList(0, lastIdx + 1);
            superTypesLists.set(superTypesLists.indexOf(subList), newSubList);
        }
        return getFirstCommonElement(superTypesLists);
    }


    protected List<String> getAllSuperTypeCodes(ComposedTypeModel composedType)
    {
        return (List<String>)Stream.concat(Stream.of(composedType), composedType.getAllSuperTypes().stream()).map(TypeModel::getCode)
                        .collect(Collectors.toList());
    }


    protected List<PK> getInheritedTypePrimaryKeys(List<ComposedTypeModel> composedTypes)
    {
        return (List<PK>)composedTypes
                        .stream()
                        .map(this::getAllSubTypes)
                        .flatMap(Collection::stream)
                        .map(AbstractItemModel::getPk)
                        .distinct()
                        .collect(Collectors.toList());
    }


    protected List<ComposedTypeModel> getAllSubTypes(ComposedTypeModel composedType)
    {
        return
                        (List<ComposedTypeModel>)Stream.<T>concat(composedType.getAbstract().booleanValue() ? Stream.<T>empty() : Stream.<T>of((T)composedType), composedType.getAllSubTypes().stream())
                                        .collect(Collectors.toList());
    }


    protected String getFirstCommonElement(List<List<String>> collections)
    {
        List<String> common = new ArrayList<>();
        if(!collections.isEmpty())
        {
            Iterator<List<String>> iterator = collections.iterator();
            common.addAll(iterator.next());
            while(iterator.hasNext())
            {
                common.retainAll(iterator.next());
            }
        }
        return (String)common.stream().findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Unable to find a common ancestor for the given typeCodes."));
    }


    protected List<String> getTypeCodesFromSearchData(CMSItemSearchData cmsItemSearchData)
    {
        String typeCode = StringUtils.isNotBlank(cmsItemSearchData.getTypeCode()) ? cmsItemSearchData.getTypeCode() : "CMSItem";
        return CollectionUtils.isNotEmpty(cmsItemSearchData.getTypeCodes()) ? cmsItemSearchData.getTypeCodes() :
                        Arrays.<String>asList(new String[] {typeCode});
    }


    protected void appendTypeExclusions(List<ComposedTypeModel> composedTypes, StringBuilder queryBuilder, Map<String, Object> queryParameters)
    {
        Set<PK> allExcludedTypes = new HashSet<>();
        List<PK> notPermittedTypes = (List<PK>)composedTypes.stream().map(this::getAllSubTypes).flatMap(Collection::stream).filter(composedType -> !getPermissionCRUDService().canReadType(composedType)).map(AbstractItemModel::getPk).collect(Collectors.toList());
        allExcludedTypes.addAll(notPermittedTypes);
        List<PK> blacklistedTypes = (List<PK>)composedTypes.stream().filter(composedType -> getCmsItemSearchTypeBlacklistMap().containsKey(composedType.getCode())).flatMap(composedType -> ((List)getCmsItemSearchTypeBlacklistMap().get(composedType.getCode())).stream())
                        .map(typeCode -> getTypeService().getTypeForCode(typeCode).getPk()).collect(Collectors.toList());
        allExcludedTypes.addAll(blacklistedTypes);
        if(!allExcludedTypes.isEmpty())
        {
            queryBuilder.append(" AND ").append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{c.itemtype} NOT IN (?excludedTypes)", "excludedTypes", "OR", allExcludedTypes, queryParameters));
        }
    }


    protected void appendSearchParams(Map<String, String> itemSearchParams, StringBuilder queryBuilder, Map<String, Object> queryParameters, String typeCode)
    {
        itemSearchParams.forEach((field, value) -> {
            try
            {
                ComposedTypeModel composedType = getTypeService().getComposedTypeForCode(typeCode);
                AttributeDescriptorModel attributeDescriptorModel = getTypeService().getAttributeDescriptor(composedType, field);
                if("null".equalsIgnoreCase(value.trim()))
                {
                    queryBuilder.append(String.format(" AND {c.%s} IS NULL", new Object[] {field}));
                }
                else
                {
                    queryParameters.put(field, getFlexibleSearchAttributeValueConverter().convert(attributeDescriptorModel, value));
                    queryBuilder.append(String.format(" AND {c.%s}=?%s", new Object[] {field, field}));
                }
            }
            catch(UnknownIdentifierException e)
            {
                LOG.info(String.format("Unknown attribute [%s] in additionalParams for type: [%s]", new Object[] {field, typeCode}), (Throwable)e);
            }
        });
    }


    protected void appendSort(String sortNameAndDirection, StringBuilder queryBuilder, String typeCode)
    {
        List<Sort> sortList = (List<Sort>)getSearchHelper().convertSort(sortNameAndDirection, SortDirection.ASC).stream().filter(attributeExistsForType(typeCode)).collect(Collectors.toList());
        if(!sortList.isEmpty())
        {
            queryBuilder.append(" ORDER BY ");
            String commaSeparatedSorts = sortList.stream().map(sort -> buildSortStatement(typeCode, sort)).collect(Collectors.joining(", "));
            queryBuilder.append(commaSeparatedSorts);
        }
    }


    protected Predicate<Sort> attributeExistsForType(String typeCode)
    {
        return sort -> {
            try
            {
                getTypeService().getAttributeDescriptor(typeCode, sort.getParameter());
                return true;
            }
            catch(UnknownIdentifierException e)
            {
                LOG.info(String.format("Unknown attribute [%s] in sort for type: [%s]", new Object[] {sort.getParameter(), typeCode}), (Throwable)e);
                return false;
            }
        };
    }


    protected String buildSortStatement(String typeCode, Sort sort)
    {
        AttributeDescriptorModel attributeDescriptorModel = getTypeService().getAttributeDescriptor(typeCode, sort
                        .getParameter());
        SortStatementFormatter sortFormatter = getSortStatementFormatters().stream().filter(formatter -> formatter.isApplicable(attributeDescriptorModel)).findFirst().orElse(getDefaultSortStatementFormatter());
        return sortFormatter.formatSortStatement(sort) + " " + sortFormatter.formatSortStatement(sort);
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected FlexibleSearchAttributeValueConverter getFlexibleSearchAttributeValueConverter()
    {
        return this.flexibleSearchAttributeValueConverter;
    }


    @Required
    public void setFlexibleSearchAttributeValueConverter(FlexibleSearchAttributeValueConverter flexibleSearchAttributeValueConverter)
    {
        this.flexibleSearchAttributeValueConverter = flexibleSearchAttributeValueConverter;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected List<SortStatementFormatter> getSortStatementFormatters()
    {
        return this.sortStatementFormatters;
    }


    @Required
    public void setSortStatementFormatters(List<SortStatementFormatter> sortStatementFormatters)
    {
        this.sortStatementFormatters = sortStatementFormatters;
    }


    protected SortStatementFormatter getDefaultSortStatementFormatter()
    {
        return this.defaultSortStatementFormatter;
    }


    @Required
    public void setDefaultSortStatementFormatter(SortStatementFormatter defaultSortStatementFormatter)
    {
        this.defaultSortStatementFormatter = defaultSortStatementFormatter;
    }


    protected Map<String, List<String>> getCmsItemSearchTypeBlacklistMap()
    {
        return this.cmsItemSearchTypeBlacklistMap;
    }


    @Required
    public void setCmsItemSearchTypeBlacklistMap(Map<String, List<String>> cmsItemSearchTypeBlacklistMap)
    {
        this.cmsItemSearchTypeBlacklistMap = cmsItemSearchTypeBlacklistMap;
    }


    protected SearchHelper getSearchHelper()
    {
        return this.searchHelper;
    }


    @Required
    public void setSearchHelper(SearchHelper searchHelper)
    {
        this.searchHelper = searchHelper;
    }


    protected PermissionCRUDService getPermissionCRUDService()
    {
        return this.permissionCRUDService;
    }


    @Required
    public void setPermissionCRUDService(PermissionCRUDService permissionCRUDService)
    {
        this.permissionCRUDService = permissionCRUDService;
    }
}
