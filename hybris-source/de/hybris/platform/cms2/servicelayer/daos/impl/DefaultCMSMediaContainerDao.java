package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.servicelayer.daos.CMSMediaContainerDao;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class DefaultCMSMediaContainerDao extends AbstractItemDao implements CMSMediaContainerDao
{
    public MediaContainerModel getMediaContainerForQualifier(String qualifier, CatalogVersionModel catalogVersion)
    {
        ServicesUtil.validateParameterNotNull(qualifier, "Qualifier cannot be null");
        StringBuilder query = new StringBuilder();
        query.append("SELECT {").append("pk").append("} ");
        query.append("FROM {").append("MediaContainer").append("} ");
        query.append("WHERE {").append("qualifier").append("} = ?").append("qualifier");
        query.append(" AND {").append("catalogVersion").append("} = ?").append("catalogVersion");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        fQuery.addQueryParameter("qualifier", qualifier);
        fQuery.addQueryParameter("catalogVersion", catalogVersion);
        SearchResult<MediaContainerModel> result = search(fQuery);
        ServicesUtil.validateIfSingleResult(result.getResult(), "No media container with given qualifier [" + qualifier + "] was found", "More than one media container with given qualifier [" + qualifier + "] was found");
        return result.getResult().iterator().next();
    }


    public SearchResult<MediaContainerModel> findMediaContainersForCatalogVersion(String text, CatalogVersionModel catalogVersion, PageableData pageableData)
    {
        ServicesUtil.validateParameterNotNull(text, "Text cannot be null");
        StringBuilder query = new StringBuilder();
        query.append("SELECT {").append("pk").append("} ");
        query.append("FROM {").append("MediaContainer").append("} ");
        query.append("WHERE LOWER({").append("qualifier").append("}) LIKE LOWER(?").append("qualifier").append(")");
        query.append(" AND {").append("catalogVersion").append("} = ?").append("catalogVersion");
        query.append(" ORDER BY {").append("qualifier").append("} ASC");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        fQuery.addQueryParameter("qualifier", "%" + text + "%");
        fQuery.addQueryParameter("catalogVersion", catalogVersion);
        fQuery.setStart(pageableData.getCurrentPage() * pageableData.getPageSize());
        fQuery.setCount(pageableData.getPageSize());
        fQuery.setNeedTotal(true);
        return search(fQuery);
    }
}
