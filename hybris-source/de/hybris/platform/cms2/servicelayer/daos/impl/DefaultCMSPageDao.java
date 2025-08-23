package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSPageDao;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchParameter;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import de.hybris.platform.util.FlexibleSearchUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSPageDao extends AbstractItemDao implements CMSPageDao
{
    protected static final String AND = "AND";
    protected static final String OR = "OR";
    protected static final String CATALOG_VERSIONS_QUERY_PARAM = "catalogVersions";
    protected static final String PAGE_STATUSES_QUERY_PARAM = "pageStatusCodes";
    protected static final String CONTENT_SLOTS_QUERY_PARAM = "contentSlots";
    protected static final String LABELS_QUERY_PARAM = "labels";
    protected static final String PAGE_QUERY_ALIAS = "page";
    private PaginatedFlexibleSearchService paginatedFlexibleSearchService;


    public Collection<ContentPageModel> findPagesByLabel(String label, Collection<CatalogVersionModel> catalogVersions)
    {
        return findPagesByLabelAndPageStatuses(label, catalogVersions, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
    }


    public <T extends AbstractPageModel> Collection<T> findPagesByLabelAndPageStatuses(String label, Collection<CatalogVersionModel> catalogVersions, List<CmsPageStatus> pageStatuses)
    {
        return findPagesByLabelsAndPageStatuses(Collections.singletonList(label), catalogVersions, pageStatuses);
    }


    public <T extends AbstractPageModel> Collection<T> findPagesByLabelsAndPageStatuses(Collection<String> labels, Collection<CatalogVersionModel> catalogVersions, List<CmsPageStatus> pageStatuses)
    {
        Map<String, Object> queryParameters = new HashMap<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder
                        .append("SELECT {page.pk} ")
                        .append("FROM {ContentPage AS page ")
                        .append("JOIN CmsPageStatus AS stat ")
                        .append("ON { page.pageStatus} = {stat.PK}} ")
                        .append("WHERE ")
                        .append(
                                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{label} in (?labels) ", "labels", "OR", labels, queryParameters))
                        .append("AND ")
                        .append(
                                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        appendPageStatusesToQuery(queryBuilder, pageStatuses, queryParameters, "AND");
        SearchResult<T> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public Collection<ContentPageModel> findDefaultContentPageByLabelAndCatalogVersions(String label, Collection<CatalogVersionModel> catalogVersions)
    {
        return findDefaultContentPageByLabelAndCatalogVersionsAndPageStatuses(label, catalogVersions, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
    }


    public Collection<ContentPageModel> findDefaultContentPageByLabelAndCatalogVersionsAndPageStatuses(String label, Collection<CatalogVersionModel> catalogVersions, List<CmsPageStatus> pageStatuses)
    {
        Map<String, Object> queryParameters = new HashMap<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT {page.pk} ")
                        .append("FROM {ContentPage AS page ")
                        .append("JOIN CmsPageStatus AS stat ")
                        .append("ON { page.pageStatus } = {stat.PK}} ")
                        .append("WHERE {label} = ?label AND ")
                        .append("{defaultPage} = ?btrue AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        appendPageStatusesToQuery(queryBuilder, pageStatuses, queryParameters, "AND");
        queryParameters.put("label", label);
        queryParameters.put("btrue", Boolean.TRUE);
        SearchResult<ContentPageModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public Collection<AbstractPageModel> findDefaultPageByTypeAndCatalogVersions(ComposedTypeModel composedType, Collection<CatalogVersionModel> catalogVersions)
    {
        return findDefaultPageByTypeAndCatalogVersionsAndPageStatuses(composedType, catalogVersions, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
    }


    public Collection<AbstractPageModel> findDefaultPageByTypeAndCatalogVersionsAndPageStatuses(ComposedTypeModel composedType, Collection<CatalogVersionModel> catalogVersions, List<CmsPageStatus> pageStatuses)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {p.pk} ")
                        .append("FROM {" + composedType.getCode() + " as p ")
                        .append("JOIN CmsPageStatus AS stat ")
                        .append("ON { p.pageStatus } = {stat.PK}} ")
                        .append("WHERE {defaultPage} = ?btrue AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        appendPageStatusesToQuery(queryBuilder, pageStatuses, queryParameters, "AND");
        queryParameters.put("btrue", Boolean.TRUE);
        SearchResult<AbstractPageModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public Collection<AbstractPageModel> findAllPagesByCatalogVersion(CatalogVersionModel catalogVersion)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {p:").append("pk").append("} ");
        query.append("FROM {").append("AbstractPage").append(" AS p} ");
        query.append("WHERE {p:").append("catalogVersion").append("} =?").append("catalogVersion");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        fQuery.addQueryParameters(Collections.singletonMap("catalogVersion", catalogVersion));
        SearchResult<AbstractPageModel> result = search(fQuery);
        return result.getResult();
    }


    public Collection<AbstractPageModel> findAllPagesByTypeAndCatalogVersions(ComposedTypeModel composedType, Collection<CatalogVersionModel> catalogVersions)
    {
        return findAllPagesByTypeAndCatalogVersionsAndPageStatuses(composedType, catalogVersions, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
    }


    public Collection<AbstractPageModel> findAllPagesByTypeAndCatalogVersionsAndPageStatuses(ComposedTypeModel composedType, Collection<CatalogVersionModel> catalogVersions, List<CmsPageStatus> pageStatuses)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {c.pk} ")
                        .append("FROM {" + composedType.getCode() + " as c ")
                        .append("JOIN CmsPageStatus as stat ")
                        .append("ON { c.pageStatus} = {stat.PK}} ").append("WHERE ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        appendPageStatusesToQuery(queryBuilder, pageStatuses, queryParameters, "AND");
        SearchResult<AbstractPageModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public Collection<ContentPageModel> findAllContentPagesByCatalogVersions(Collection<CatalogVersionModel> catalogVersions)
    {
        return findAllContentPagesByCatalogVersionsAndPageStatuses(catalogVersions, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
    }


    public Collection<ContentPageModel> findAllContentPagesByCatalogVersionsAndPageStatuses(Collection<CatalogVersionModel> catalogVersions, List<CmsPageStatus> pageStatuses)
    {
        return findAllPagesByCatalogVersionAndPageStatuses("ContentPage", catalogVersions, pageStatuses);
    }


    public Collection<AbstractPageModel> findAllPagesByCatalogVersionAndPageStatuses(CatalogVersionModel catalogVersion, List<CmsPageStatus> pageStatuses)
    {
        return findAllPagesByCatalogVersionAndPageStatuses("AbstractPage", Arrays.asList(new CatalogVersionModel[] {catalogVersion}, ), pageStatuses);
    }


    protected <T extends AbstractPageModel> Collection<T> findAllPagesByCatalogVersionAndPageStatuses(String typeCode, Collection<CatalogVersionModel> catalogVersions, List<CmsPageStatus> pageStatuses)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {c.pk} ").append("FROM {" + typeCode + " AS c JOIN CmsPageStatus AS stat ")
                        .append("ON { c.pageStatus} = {stat.PK}} ")
                        .append("WHERE ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{c.catalogVersion} IN (?catalogVersions) ", "catalogVersions", "OR", catalogVersions, queryParameters));
        appendPageStatusesToQuery(queryBuilder, pageStatuses, queryParameters, "AND");
        SearchResult<T> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public Collection<AbstractPageModel> findPagesByPageTemplateContentSlots(Collection<ContentSlotModel> contentSlots, Collection<CatalogVersionModel> catalogVersions)
    {
        return findPagesByPageTemplateContentSlotsAndPageStatuses(contentSlots, catalogVersions, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
    }


    public Collection<AbstractPageModel> findPagesByPageTemplateContentSlotsAndPageStatuses(Collection<ContentSlotModel> contentSlots, Collection<CatalogVersionModel> catalogVersions, List<CmsPageStatus> pageStatuses)
    {
        if(CollectionUtils.isEmpty(contentSlots))
        {
            return Collections.emptyList();
        }
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {ap.pk} ")
                        .append("FROM {").append("AbstractPage").append(" AS ap ")
                        .append("JOIN ").append("PageTemplate").append(" AS pt ")
                        .append("ON {ap.").append("masterTemplate").append("} = {pt.").append("pk")
                        .append("} ")
                        .append("JOIN ").append("ContentSlotForTemplate").append(" AS csft ")
                        .append("ON {pt.").append("pk").append("} = {csft.").append("pageTemplate")
                        .append("} ")
                        .append("JOIN ").append("CmsPageStatus").append(" as stat ")
                        .append("ON {ap.").append("pageStatus").append("} = {stat.PK}} ")
                        .append("WHERE ");
        queryBuilder.append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{csft.contentSlot} in (?contentSlots)", "contentSlots", "OR", contentSlots, queryParameters));
        queryBuilder.append(" AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{ap.catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        appendPageStatusesToQuery(queryBuilder, pageStatuses, queryParameters, "AND");
        SearchResult<AbstractPageModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public List<AbstractPageModel> findPagesByIdAndCatalogVersion(String id, CatalogVersionModel catalogVersion)
    {
        return findPagesByIdAndCatalogVersionAndPageStatuses(id, catalogVersion, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
    }


    public List<AbstractPageModel> findPagesByIdAndCatalogVersionAndPageStatuses(String id, CatalogVersionModel catalogVersion, List<CmsPageStatus> pageStatuses)
    {
        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        query.append("SELECT {p:").append("pk").append("} ");
        query.append("FROM {").append("AbstractPage").append(" AS p ");
        query.append("JOIN ").append("CmsPageStatus").append(" as stat ");
        query.append("ON {p.").append("pageStatus").append("} = {stat.PK}} ");
        query.append("WHERE {p:").append("uid").append("} =?").append("uid").append(" AND ");
        query.append("{p:").append("catalogVersion").append("} =?").append("catalogVersion");
        appendPageStatusesToQuery(query, pageStatuses, params, "AND");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        params.put("uid", id);
        params.put("catalogVersion", catalogVersion);
        fQuery.addQueryParameters(params);
        SearchResult<AbstractPageModel> result = search(fQuery);
        return result.getResult();
    }


    public List<AbstractPageModel> findPagesById(String id, Collection<CatalogVersionModel> catalogVersions)
    {
        return findPagesByIdAndPageStatuses(id, catalogVersions, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
    }


    public <T extends AbstractPageModel> List<T> findPagesByIdAndPageStatuses(String id, Collection<CatalogVersionModel> catalogVersions, List<CmsPageStatus> pageStatuses)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {c:pk} ")
                        .append(" FROM {AbstractPage as c ")
                        .append(" JOIN CmsPageStatus as stat ")
                        .append(" ON { c.pageStatus} = {stat.PK}} ")
                        .append(" WHERE {c:uid} =?uid AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{c.catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        appendPageStatusesToQuery(queryBuilder, pageStatuses, queryParameters, "AND");
        queryParameters.put("uid", id);
        SearchResult<T> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public List<AbstractPageModel> findDefaultPagesByType(ComposedTypeModel composedType, Collection<CatalogVersionModel> catalogVersions)
    {
        return findPagesByType(composedType, catalogVersions, Boolean.TRUE.booleanValue());
    }


    public List<AbstractPageModel> findPagesByType(ComposedTypeModel composedType, Collection<CatalogVersionModel> catalogVersions, boolean isDefault)
    {
        return findPagesByTypeAndPageStatuses(composedType, catalogVersions, isDefault, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
    }


    public List<AbstractPageModel> findPagesByTypeAndPageStatuses(ComposedTypeModel composedType, Collection<CatalogVersionModel> catalogVersions, boolean isDefault, List<CmsPageStatus> pageStatuses)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {c.pk} ")
                        .append("FROM {" + composedType.getCode() + " as c ")
                        .append("JOIN CmsPageStatus as stat ")
                        .append("ON {c.pageStatus} = {stat.PK}} ")
                        .append("WHERE {c.defaultPage} = ?defaultPage AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{c.catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        appendPageStatusesToQuery(queryBuilder, pageStatuses, queryParameters, "AND");
        queryParameters.put("defaultPage", Boolean.valueOf(isDefault));
        SearchResult<AbstractPageModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public Collection<AbstractPageModel> findPagesByContentSlots(Collection<ContentSlotModel> contentSlots, Collection<CatalogVersionModel> catalogVersions)
    {
        return findPagesByContentSlotsAndPageStatuses(contentSlots, catalogVersions, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
    }


    public Collection<AbstractPageModel> findPagesByContentSlotsAndPageStatuses(Collection<ContentSlotModel> contentSlots, Collection<CatalogVersionModel> catalogVersions, List<CmsPageStatus> pageStatuses)
    {
        if(Stream.<Collection>of(new Collection[] {contentSlots, catalogVersions, pageStatuses}).anyMatch(CollectionUtils::isEmpty))
        {
            return Collections.emptyList();
        }
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {ap.pk} ")
                        .append("FROM {AbstractPage AS ap ")
                        .append("JOIN ContentSlotForPage AS csfp ")
                        .append("ON {ap.pk}={csfp.page} ")
                        .append("JOIN CmsPageStatus as stat ")
                        .append("ON {ap.pageStatus}={stat.PK}} ")
                        .append("WHERE ");
        queryBuilder.append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{csfp.contentSlot} in (?contentSlots)", "contentSlots", "OR", contentSlots, queryParameters));
        queryBuilder.append(" AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{ap.catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        appendPageStatusesToQuery(queryBuilder, pageStatuses, queryParameters, "AND");
        SearchResult<AbstractPageModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public ContentPageModel findHomepage(Collection<CatalogVersionModel> catalogVersions)
    {
        return findHomepageByPageStatuses(catalogVersions, Arrays.asList(new CmsPageStatus[] {CmsPageStatus.ACTIVE}));
    }


    public ContentPageModel findHomepageByPageStatuses(Collection<CatalogVersionModel> catalogVersions, List<CmsPageStatus> pageStatuses)
    {
        Collection<ContentPageModel> homepages = findHomepagesByPageStatuses(catalogVersions, pageStatuses);
        return homepages.isEmpty() ? null : homepages.iterator().next();
    }


    public Collection<ContentPageModel> findHomepagesByPageStatuses(Collection<CatalogVersionModel> catalogVersions, List<CmsPageStatus> pageStatuses)
    {
        Map<String, Object> queryParameters = new HashMap<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT {page.pk} ")
                        .append(" FROM {ContentPage AS page ")
                        .append(" JOIN CmsPageStatus AS stat ")
                        .append(" ON { page.pageStatus} = {stat.PK}} ")
                        .append(" WHERE {homepage}=?").append("homepage").append(" AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        appendPageStatusesToQuery(queryBuilder, pageStatuses, queryParameters, "AND");
        queryParameters.put("homepage", Boolean.TRUE);
        SearchResult<ContentPageModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    protected void appendPageStatusesToQuery(StringBuilder queryBuilder, List<CmsPageStatus> pageStatuses, Map<String, Object> queryParameters, String queryConcatOperator)
    {
        if(CollectionUtils.isNotEmpty(pageStatuses))
        {
            queryBuilder.append(" " + queryConcatOperator + " ");
            queryBuilder.append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{stat.code} in (?pageStatusCodes) ", "pageStatusCodes", "OR", (Collection)pageStatuses
                            .stream().map(CmsPageStatus::getCode).collect(Collectors.toList()), queryParameters));
        }
    }


    public <T extends AbstractPageModel> SearchPageData<T> findAllPagesByTypeAndCatalogVersions(ComposedTypeModel composedType, Collection<CatalogVersionModel> catalogVersions, SearchPageData searchPageData)
    {
        StringBuilder query = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        query.append("SELECT {page.pk} ");
        query.append("FROM {" + composedType.getCode() + " as page ");
        query.append("JOIN CmsPageStatus as stat ON {page.pageStatus} = {stat.PK}} ");
        query.append("WHERE ");
        query.append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{page.catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        appendPageStatusesToQuery(query, Collections.singletonList(CmsPageStatus.ACTIVE), queryParameters, "AND");
        PaginatedFlexibleSearchParameter paginatedSearchParameter = new PaginatedFlexibleSearchParameter();
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(query.toString(), queryParameters);
        paginatedSearchParameter.setFlexibleSearchQuery(fsQuery);
        paginatedSearchParameter.setSearchPageData(searchPageData);
        Map<String, String> sortCodeToQueryAlias = new HashMap<>();
        sortCodeToQueryAlias.put("uid", "page");
        sortCodeToQueryAlias.put("name", "page");
        sortCodeToQueryAlias.put("modifiedtime", "page");
        paginatedSearchParameter.setSortCodeToQueryAlias(sortCodeToQueryAlias);
        return getPaginatedFlexibleSearchService().search(paginatedSearchParameter);
    }


    protected PaginatedFlexibleSearchService getPaginatedFlexibleSearchService()
    {
        return this.paginatedFlexibleSearchService;
    }


    @Required
    public void setPaginatedFlexibleSearchService(PaginatedFlexibleSearchService paginatedFlexibleSearchService)
    {
        this.paginatedFlexibleSearchService = paginatedFlexibleSearchService;
    }
}
