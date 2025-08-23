package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSSiteDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DefaultCMSSiteDao extends AbstractItemDao implements CMSSiteDao
{
    public Collection<CMSSiteModel> findAllCMSSites()
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {").append("pk").append("} ");
        query.append("FROM {").append("CMSSite").append("} ");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        SearchResult<CMSSiteModel> result = search(fQuery);
        return result.getResult();
    }


    public List<CMSSiteModel> findCMSSitesById(String id)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {st:").append("pk").append("} ");
        query.append("FROM {").append("CMSSite").append(" AS st} ");
        query.append("WHERE {st:").append("uid").append("} =").append("?").append("uid");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        fQuery.addQueryParameters(Collections.singletonMap("uid", id));
        SearchResult<CMSSiteModel> result = search(fQuery);
        return result.getResult();
    }
}
