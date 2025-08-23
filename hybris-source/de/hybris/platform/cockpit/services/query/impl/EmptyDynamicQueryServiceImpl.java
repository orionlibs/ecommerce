package de.hybris.platform.cockpit.services.query.impl;

import de.hybris.platform.cockpit.model.dynamicquery.DynamicQuery;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.query.DynamicQueryService;
import java.util.Collections;
import java.util.List;

public class EmptyDynamicQueryServiceImpl implements DynamicQueryService
{
    public List<DynamicQuery> getAllDynamicQuery()
    {
        return Collections.emptyList();
    }


    public List<TypedObject> getDynamicQueryResults(DynamicQuery query)
    {
        return Collections.emptyList();
    }
}
