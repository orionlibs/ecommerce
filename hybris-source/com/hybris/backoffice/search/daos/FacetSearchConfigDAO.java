package com.hybris.backoffice.search.daos;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.Collection;
import java.util.List;

public interface FacetSearchConfigDAO<T>
{
    Collection<T> findSearchConfigurationsForTypes(List<ComposedTypeModel> paramList);


    Collection<T> findSearchConfigurationsForName(String paramString);


    Collection<T> findAllSearchConfigs();
}
