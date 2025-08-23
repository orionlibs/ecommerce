package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.servicelayer.daos.CMSMediaDao;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.apache.commons.lang3.StringUtils;

public class DefaultCMSMediaDao extends AbstractItemDao implements CMSMediaDao
{
    private static final String LIKE_LOWER = "}) LIKE LOWER(?";


    public SearchResult<MediaModel> findMediasForCatalogVersion(String mask, String mimeType, CatalogVersionModel catalogVersion, PageableData pageableData)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {").append("pk").append("} ");
        query.append("FROM {").append("Media").append("} ");
        query.append(" WHERE {").append("catalogVersion").append("} = ?").append("catalogVersion");
        if(mask != null && StringUtils.isNotEmpty(mask))
        {
            query.append(" AND (LOWER({").append("code").append("}) LIKE LOWER(?").append("code").append(")");
            query.append(" OR LOWER({").append("description").append("}) LIKE LOWER(?").append("description").append("))");
        }
        if(mimeType != null && StringUtils.isNotEmpty(mimeType))
        {
            query.append(" AND LOWER({").append("mime").append("}) LIKE LOWER(?").append("mime").append(")");
        }
        query.append(" ORDER BY {").append("code").append("} ASC");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        fQuery.addQueryParameter("code", "%" + mask + "%");
        fQuery.addQueryParameter("description", "%" + mask + "%");
        fQuery.addQueryParameter("mime", "%" + mimeType + "%");
        fQuery.addQueryParameter("catalogVersion", catalogVersion);
        fQuery.setStart(pageableData.getCurrentPage() * pageableData.getPageSize());
        fQuery.setCount(pageableData.getPageSize());
        fQuery.setNeedTotal(true);
        return search(fQuery);
    }
}
