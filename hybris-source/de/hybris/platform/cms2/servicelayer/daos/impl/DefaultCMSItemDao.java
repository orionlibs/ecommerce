package de.hybris.platform.cms2.servicelayer.daos.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSItemDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class DefaultCMSItemDao extends AbstractItemDao implements CMSItemDao
{
    public CMSItemModel findByUid(String uid, CatalogVersionModel catalogVersion)
    {
        Preconditions.checkArgument(StringUtils.isNotBlank(uid), "uid must neither be null nor empty");
        Preconditions.checkArgument((catalogVersion != null), "catalogVersion must neither be null nor empty");
        String query = "SELECT {pk} FROM {CMSItem} WHERE {uid} =?uid AND {catalogVersion} =?catalogVersion";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {CMSItem} WHERE {uid} =?uid AND {catalogVersion} =?catalogVersion");
        Map<String, Object> params = new HashMap<>();
        params.put("uid", uid);
        params.put("catalogVersion", catalogVersion);
        fQuery.addQueryParameters(params);
        SearchResult<CMSItemModel> result = search(fQuery);
        if(result.getCount() > 0)
        {
            return result.getResult().iterator().next();
        }
        return null;
    }


    public SearchResult<CMSItemModel> findByTypeCodeAndName(CatalogVersionModel catalogVersion, String typeCode, String name)
    {
        Preconditions.checkArgument((catalogVersion != null), "catalogVersion must neither be null nor empty");
        Preconditions.checkArgument(StringUtils.isNotBlank(typeCode), "TypeCode must neither be null nor empty");
        Preconditions.checkArgument(StringUtils.isNotBlank(name), "Name must neither be null nor empty");
        String query = "SELECT {pk} FROM {" + typeCode + "} WHERE LOWER({name}) = LOWER(?name) AND {catalogVersion} =?catalogVersion";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("catalogVersion", catalogVersion);
        fQuery.addQueryParameters(params);
        return search(fQuery);
    }
}
