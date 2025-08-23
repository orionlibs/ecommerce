package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSVersionGCDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DefaultCMSVersionGCDao extends AbstractCMSItemDao implements CMSVersionGCDao
{
    public List<CMSVersionModel> findRetainableVersions(Date maxAgeDate)
    {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT {version.pk} ")
                        .append("FROM {CMSVersion AS version} ")
                        .append("WHERE {retain} = ?retain ");
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("retain", Boolean.TRUE);
        if(Objects.isNull(maxAgeDate))
        {
            queryBuilder.append("OR {label} IS NOT NULL");
        }
        else
        {
            queryBuilder.append("OR ( {label} IS NOT NULL AND {version.creationtime} >= ?maxAgeDate )");
            queryParameters.put("maxAgeDate", maxAgeDate);
        }
        SearchResult<CMSVersionModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }
}
