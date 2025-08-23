package de.hybris.platform.cms2.servicelayer.daos.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSComponentDao;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchParameter;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import de.hybris.platform.util.FlexibleSearchUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSComponentDao extends AbstractCMSItemDao implements CMSComponentDao
{
    protected static final String CATALOG_VERSIONS_QUERY_PARAM = "catalogVersions";
    protected static final String QUERY_ALIAS = "c";
    private PaginatedFlexibleSearchService paginatedFlexibleSearchService;


    public SearchResult<AbstractCMSComponentModel> findByCatalogVersionAndMask(CatalogVersionModel catalogVersionModel, String mask, PageableData pageableData)
    {
        String query = "SELECT {pk} FROM {AbstractCMSComponent} WHERE {catalogVersion} =?catalogVersion AND LOWER({name}) LIKE ?name ";
        query = query + query;
        Map<String, Object> params = new HashMap<>();
        params.put("catalogVersion", catalogVersionModel);
        params.put("name", "%" + (StringUtils.isNotBlank(mask) ? mask.toLowerCase() : "") + "%");
        return getFlexibleSearchService()
                        .search(buildQuery(query, params, pageableData.getCurrentPage(), pageableData.getPageSize()));
    }


    public List<SimpleCMSComponentModel> findCMSComponentsOfContainerByIdAndCatalogVersion(String id, CatalogVersionModel catalogVersion)
    {
        return Collections.emptyList();
    }


    public List<AbstractCMSComponentModel> findCMSComponentsByIdAndCatalogVersions(String id, Collection<CatalogVersionModel> catalogVersions)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {pk} FROM {AbstractCMSComponent} WHERE {uid} =?uid AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        queryParameters.put("uid", id);
        SearchResult<AbstractCMSComponentModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public <T extends AbstractCMSComponentModel> SearchPageData<T> findCMSComponentsByIdsAndCatalogVersions(Collection<String> ids, Collection<CatalogVersionModel> catalogVersions, SearchPageData searchPageDataInput)
    {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(ids), "ids must neither be null nor empty");
        PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {c:pk} FROM {AbstractCMSComponent as c} WHERE ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{c:uid} in (?uid) AND ", "uid", "OR", ids, queryParameters));
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{c:catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        queryParameters.put("uid", ids);
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(queryBuilder.toString(), queryParameters);
        parameter.setFlexibleSearchQuery(fsQuery);
        parameter.setSearchPageData(searchPageDataInput);
        Map<String, String> sortCodeToQueryAlias = new HashMap<>();
        sortCodeToQueryAlias.put("uid".toLowerCase(Locale.getDefault()), "c");
        sortCodeToQueryAlias.put("name".toLowerCase(Locale.getDefault()), "c");
        sortCodeToQueryAlias.put("modifiedtime".toLowerCase(Locale.getDefault()), "c");
        parameter.setSortCodeToQueryAlias(sortCodeToQueryAlias);
        return getPaginatedFlexibleSearchService().search(parameter);
    }


    public List<AbstractCMSComponentModel> findCMSComponents(String id, String contentSlotId, Collection<CatalogVersionModel> catalogVersions)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append(
                        "SELECT {component:pk}  FROM {AbstractCMSComponent as component  JOIN " + GeneratedCms2Constants.Relations.ELEMENTSFORSLOT + " AS rel ON {rel:target}={component:pk}  JOIN ContentSlot AS slot ON {rel:source}={slot:pk}}  WHERE {component:uid}=?uid AND {slot:uid}=?slots AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{component:catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        queryParameters.put("uid", id);
        queryParameters.put("slots", contentSlotId);
        SearchResult<AbstractCMSComponentModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public List<AbstractCMSComponentModel> findCMSComponentsByIdAndCatalogVersion(String id, CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {pk} FROM {AbstractCMSComponent} WHERE {uid} =?uid AND {catalogVersion} =?catalogVersion";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {AbstractCMSComponent} WHERE {uid} =?uid AND {catalogVersion} =?catalogVersion");
        Map<String, Object> params = new HashMap<>();
        params.put("uid", id);
        params.put("catalogVersion", catalogVersion);
        fQuery.addQueryParameters(params);
        SearchResult<AbstractCMSComponentModel> result = search(fQuery);
        return result.getResult();
    }


    public List<AbstractCMSComponentModel> findCMSComponentsById(String id)
    {
        String query = "SELECT {pk} FROM {AbstractCMSComponent} WHERE {uid} =?uid";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {AbstractCMSComponent} WHERE {uid} =?uid");
        fQuery.addQueryParameter("uid", id);
        SearchResult<AbstractCMSComponentModel> result = search(fQuery);
        return result.getResult();
    }


    public List<AbstractCMSComponentContainerModel> findCMSComponentContainersByIdAndCatalogVersion(String id, CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {pk} FROM {AbstractCMSComponentContainer} WHERE {uid} =?uid AND {catalogVersion} =?catalogVersion";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {AbstractCMSComponentContainer} WHERE {uid} =?uid AND {catalogVersion} =?catalogVersion");
        Map<String, Object> params = new HashMap<>();
        params.put("uid", id);
        params.put("catalogVersion", catalogVersion);
        fQuery.addQueryParameters(params);
        SearchResult<AbstractCMSComponentContainerModel> result = search(fQuery);
        return result.getResult();
    }


    public List<AbstractCMSComponentModel> findAllCMSComponentsByCatalogVersion(CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {pk} FROM {AbstractCMSComponent} WHERE {catalogVersion} =?catalogVersion";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {AbstractCMSComponent} WHERE {catalogVersion} =?catalogVersion");
        Map<String, Object> params = new HashMap<>();
        params.put("catalogVersion", catalogVersion);
        fQuery.addQueryParameters(params);
        SearchResult<AbstractCMSComponentModel> result = search(fQuery);
        return result.getResult();
    }


    public <T extends AbstractCMSComponentModel> SearchPageData<T> findAllCMSComponentsByCatalogVersions(Collection<CatalogVersionModel> catalogVersions, SearchPageData searchPageData)
    {
        PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {pk} ")
                        .append("FROM {AbstractCMSComponent as ").append("c")
                        .append("} WHERE ")
                        .append(
                                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(queryBuilder.toString(), queryParameters);
        parameter.setFlexibleSearchQuery(fsQuery);
        parameter.setSearchPageData(searchPageData);
        Map<String, String> sortCodeToQueryAlias = new HashMap<>();
        sortCodeToQueryAlias.put("uid".toLowerCase(Locale.getDefault()), "c");
        sortCodeToQueryAlias.put("name".toLowerCase(Locale.getDefault()), "c");
        sortCodeToQueryAlias.put("modifiedtime".toLowerCase(Locale.getDefault()), "c");
        parameter.setSortCodeToQueryAlias(sortCodeToQueryAlias);
        return getPaginatedFlexibleSearchService().search(parameter);
    }


    public long getComponentReferenceCountOutsidePage(AbstractCMSComponentModel componentModel, AbstractPageModel pageModel)
    {
        Preconditions.checkNotNull(componentModel, "componentModel cannot be null");
        Preconditions.checkNotNull(pageModel, "pageModel cannot be null");
        Map<String, Object> queryParameters = new HashMap<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder
                        .append("SELECT COUNT(DISTINCT(CASE WHEN {s4p.page} IS NOT NULL THEN {s4p.page} ELSE 0 END)) ")
                        .append("FROM {" + GeneratedCms2Constants.Relations.ELEMENTSFORSLOT + " AS rel ")
                        .append("LEFT JOIN ContentSlotForPage AS s4p ON {rel.source} = {s4p.contentSlot}} ")
                        .append("WHERE {rel.target} =?AbstractCMSComponent AND ")
                        .append("({s4p.page} !=?AbstractPage OR {s4p.page} IS null)");
        queryParameters.put("AbstractCMSComponent", componentModel);
        queryParameters.put("AbstractPage", pageModel);
        FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(queryBuilder.toString());
        flexibleSearchQuery.addQueryParameters(queryParameters);
        flexibleSearchQuery.setResultClassList(Collections.singletonList(Long.class));
        return ((Long)getFlexibleSearchService().searchUnique(flexibleSearchQuery)).longValue();
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
