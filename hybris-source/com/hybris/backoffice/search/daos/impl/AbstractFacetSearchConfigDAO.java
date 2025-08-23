package com.hybris.backoffice.search.daos.impl;

import com.hybris.backoffice.search.daos.FacetSearchConfigDAO;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractFacetSearchConfigDAO<T> implements FacetSearchConfigDAO<T>
{
    protected TypeService typeService;
    private FlexibleSearchService flexibleSearchService;


    public Collection<T> findSearchConfigurationsForTypes(List<ComposedTypeModel> composedTypes)
    {
        return this.flexibleSearchService.search(getQuery4FindSearchCfgForTypes(composedTypes)).getResult();
    }


    public Collection<T> findSearchConfigurationsForName(String facetSearchConfigName)
    {
        return this.flexibleSearchService.search(getQuery4FindSearchCfgForName(facetSearchConfigName)).getResult();
    }


    public Collection<T> findAllSearchConfigs()
    {
        if(isFacetSearchConfigModelCreated())
        {
            return this.flexibleSearchService.search(getQuery4FindAllSearchCfg()).getResult();
        }
        return Collections.emptyList();
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected abstract FlexibleSearchQuery getQuery4FindSearchCfgForTypes(List<ComposedTypeModel> paramList);


    protected abstract FlexibleSearchQuery getQuery4FindSearchCfgForName(String paramString);


    protected abstract FlexibleSearchQuery getQuery4FindAllSearchCfg();


    protected abstract boolean isFacetSearchConfigModelCreated();
}
