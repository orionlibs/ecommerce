package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSContentCatalogDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.Collections;

public class DefaultCMSContentCatalogDao extends AbstractItemDao implements CMSContentCatalogDao
{
    private static final String COUNT_KEYS_FOR_TYPE = "SELECT COUNT({%s}) FROM {%s} WHERE {%s} =?cv";
    private static final String COUNT_CMSITEMMODEL_AND_VERSION_REALTIONSHIPS = String.format("SELECT COUNT({%s}) FROM {%s} WHERE {%s} =?cv", new Object[] {"pk", "CMSItem", "catalogVersion"});
    private static final String COUNT_CMSRELATIONMODEL_AND_VERSION_REALTIONSHIPS = String.format("SELECT COUNT({%s}) FROM {%s} WHERE {%s} =?cv", new Object[] {"pk", "CMSRelation", "catalogVersion"});
    private static final String FIND_ALL_CONTENT_CATALOGS = String.format("SELECT {%s} FROM {%s}", new Object[] {"pk", "ContentCatalog"});
    private static final String ORDER_BY_FIELD_PATTERN = "ORDER BY {%s}";
    private static final String FIND_CONTENT_CATALOG_BY_ID = String.format("SELECT {%s} FROM {%s} WHERE {%s} =?id", new Object[] {"pk", "ContentCatalog", "id"});


    public Collection<ContentCatalogModel> findAllContentCatalogs()
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(FIND_ALL_CONTENT_CATALOGS);
        SearchResult<ContentCatalogModel> result = search(fQuery);
        return result.getResult();
    }


    public Collection<ContentCatalogModel> findAllContentCatalogsOrderedBy(String orderField)
    {
        String qualifiedOrder = String.format("ORDER BY {%s}", new Object[] {orderField});
        String findAllContentOrderedCatalogs = String.format("%s %s", new Object[] {FIND_ALL_CONTENT_CATALOGS, qualifiedOrder});
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(findAllContentOrderedCatalogs);
        SearchResult<ContentCatalogModel> result = search(fQuery);
        return result.getResult();
    }


    public boolean hasCMSItems(CatalogVersionModel versionModel)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(COUNT_CMSITEMMODEL_AND_VERSION_REALTIONSHIPS);
        return executeRelationshipCount(fQuery, versionModel);
    }


    public boolean hasCMSRelations(CatalogVersionModel versionModel)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(COUNT_CMSRELATIONMODEL_AND_VERSION_REALTIONSHIPS);
        return executeRelationshipCount(fQuery, versionModel);
    }


    protected boolean executeRelationshipCount(FlexibleSearchQuery fQuery, CatalogVersionModel versionModel)
    {
        fQuery.addQueryParameter("cv", versionModel);
        fQuery.setResultClassList(Collections.singletonList(Integer.class));
        SearchResult<Integer> result = search(fQuery);
        return (result.getResult().size() == 1 && ((Integer)result.getResult().iterator().next()).intValue() > 0);
    }


    public ContentCatalogModel findContentCatalogById(String id)
    {
        ServicesUtil.validateParameterNotNull(id, "Catalog id must not be null");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(FIND_CONTENT_CATALOG_BY_ID);
        fQuery.addQueryParameter("id", id);
        fQuery.setResultClassList(Collections.singletonList(ContentCatalogModel.class));
        SearchResult<ContentCatalogModel> result = search(fQuery);
        ServicesUtil.validateIfSingleResult(result.getResult(), "No catalog with given id [" + id + "] was found", "More than one catalog with given id [" + id + "] was found");
        return result.getResult().iterator().next();
    }
}
