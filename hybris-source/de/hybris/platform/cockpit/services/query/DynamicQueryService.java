package de.hybris.platform.cockpit.services.query;

import de.hybris.platform.cockpit.model.dynamicquery.DynamicQuery;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;

public interface DynamicQueryService
{
    List<TypedObject> getDynamicQueryResults(DynamicQuery paramDynamicQuery);


    List<DynamicQuery> getAllDynamicQuery();
}
