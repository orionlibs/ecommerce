package com.hybris.backoffice.search.services.impl;

import com.google.common.collect.Lists;
import com.hybris.backoffice.search.cache.BackofficeFacetSearchConfigCache;
import com.hybris.backoffice.search.daos.FacetSearchConfigDAO;
import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.List;

public abstract class AbstractBackofficeFacetSearchConfigService<FSC, FSCM, ITM, IT> implements BackofficeFacetSearchConfigService<FSC, FSCM, ITM, IT>
{
    protected TypeService typeService;
    protected FacetSearchConfigDAO facetSearchConfigDAO;
    protected BackofficeFacetSearchConfigCache backofficeFacetSearchConfigCache;


    public FSCM getFacetSearchConfigModel(String typeCode)
    {
        return findFacetSearchConfigModelInternal(typeCode);
    }


    protected abstract FSCM findFacetSearchConfigModelInternal(String paramString);


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setFacetSearchConfigDAO(FacetSearchConfigDAO facetSearchConfigDAO)
    {
        this.facetSearchConfigDAO = facetSearchConfigDAO;
    }


    public void setBackofficeFacetSearchConfigCache(BackofficeFacetSearchConfigCache backofficeFacetSearchConfigCache)
    {
        this.backofficeFacetSearchConfigCache = backofficeFacetSearchConfigCache;
    }


    protected List<ComposedTypeModel> getWithSuperTypeCodes(ComposedTypeModel composedType)
    {
        List<ComposedTypeModel> typeCodes = Lists.newArrayList();
        typeCodes.add(composedType);
        typeCodes.addAll(composedType.getAllSuperTypes());
        return typeCodes;
    }
}
