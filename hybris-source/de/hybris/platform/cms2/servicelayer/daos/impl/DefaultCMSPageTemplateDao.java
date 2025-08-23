package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSPageTemplateDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.FlexibleSearchUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultCMSPageTemplateDao extends AbstractItemDao implements CMSPageTemplateDao
{
    protected static final String CATALOG_VERSIONS_QUERY_PARAM = "catalogVersions";


    public Collection<PageTemplateModel> findAllPageTemplatesByCatalogVersion(CatalogVersionModel catalogVersion)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {pt:").append("pk").append("} ");
        query.append("FROM {").append("PageTemplate").append(" AS pt} ");
        query.append("WHERE {pt:").append("catalogVersion").append("} =?").append("catalogVersion");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        fQuery.addQueryParameters(Collections.singletonMap("catalogVersion", catalogVersion));
        SearchResult<PageTemplateModel> result = search(fQuery);
        return result.getResult();
    }


    public Collection<PageTemplateModel> findAllPageTemplatesByCatalogVersion(CatalogVersionModel catalogVersion, boolean active)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {pt:").append("pk").append("} ");
        query.append("FROM {").append("PageTemplate").append(" AS pt} ");
        query.append("WHERE {pt:").append("active").append("} =?").append("active");
        query.append(" AND {").append("catalogVersion").append("} =?").append("catalogVersion");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        Map<String, Object> params = new HashMap<>();
        params.put("active", Boolean.valueOf(active));
        params.put("catalogVersion", catalogVersion);
        fQuery.addQueryParameters(params);
        SearchResult<PageTemplateModel> result = search(fQuery);
        return result.getResult();
    }


    public Collection<PageTemplateModel> findAllPageTemplatesByCatalogVersions(Collection<CatalogVersionModel> catalogVersions, boolean active)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {pt:pk}  FROM {PageTemplate AS pt}  WHERE {pt:active} =?active AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        queryParameters.put("active", Boolean.valueOf(active));
        SearchResult<PageTemplateModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public Collection<PageTemplateModel> findAllRestrictedPageTemplatesByCatalogVersion(CatalogVersionModel catalogVersion, boolean active, CMSPageTypeModel pageType)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {pt:").append("pk").append("} ");
        query.append("FROM {").append("PageTemplate").append(" AS pt ");
        query.append("JOIN ").append(GeneratedCms2Constants.Relations.VALIDPAGETYPESFORTEMPLATES)
                        .append("* AS rel ON {rel:source}={pt:PK} ");
        query.append("JOIN ").append("CMSPageType").append("* AS type ON {rel:target}={type:PK} } ");
        query.append("WHERE {pt:").append("active").append("} =?").append("active");
        query.append(" AND {").append("catalogVersion").append("} =?").append("catalogVersion");
        query.append(" AND {rel:qualifier} = '").append(GeneratedCms2Constants.Relations.VALIDPAGETYPESFORTEMPLATES).append("'");
        query.append(" AND {rel:target} =?").append("restrictedPageTypes");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        Map<String, Object> params = new HashMap<>();
        params.put("active", Boolean.valueOf(active));
        params.put("catalogVersion", catalogVersion);
        params.put("restrictedPageTypes", pageType);
        fQuery.addQueryParameters(params);
        SearchResult<PageTemplateModel> result = search(fQuery);
        return result.getResult();
    }


    public List<PageTemplateModel> findPageTemplatesByIdAndCatalogVersion(String id, CatalogVersionModel catalogVersion)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {pt:").append("pk").append("} ");
        query.append("FROM {").append("PageTemplate").append(" AS pt} ");
        query.append("WHERE {pt:").append("uid").append("} =?").append("uid").append(" AND ");
        query.append("{pt:").append("catalogVersion").append("} =?").append("catalogVersion");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        Map<String, Object> params = new HashMap<>();
        params.put("uid", id);
        params.put("catalogVersion", catalogVersion);
        fQuery.addQueryParameters(params);
        SearchResult<PageTemplateModel> result = search(fQuery);
        return result.getResult();
    }


    public Collection<PageTemplateModel> findAllRestrictedPageTemplatesByCatalogVersion(Collection<CatalogVersionModel> catalogVersions, boolean active, CMSPageTypeModel pageType)
    {
        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        query.append("SELECT {pt:").append("pk").append("} ");
        query.append("FROM {").append("PageTemplate").append(" AS pt ");
        query.append("JOIN ").append(GeneratedCms2Constants.Relations.VALIDPAGETYPESFORTEMPLATES)
                        .append("* AS rel ON {rel:source}={pt:PK} ");
        query.append("JOIN ").append("CMSPageType").append("* AS type ON {rel:target}={type:PK} } ");
        query.append("WHERE {pt:").append("active").append("} =?").append("active  AND ");
        query.append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, params));
        query.append(" AND {rel:qualifier} = '").append(GeneratedCms2Constants.Relations.VALIDPAGETYPESFORTEMPLATES).append("'");
        query.append(" AND {rel:target} =?").append("restrictedPageTypes");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        params.put("active", Boolean.valueOf(active));
        params.put("restrictedPageTypes", pageType);
        fQuery.addQueryParameters(params);
        SearchResult<PageTemplateModel> result = search(fQuery);
        return result.getResult();
    }
}
