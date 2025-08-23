package com.hybris.backoffice.search.services;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.Collection;

public interface BackofficeFacetSearchConfigService<FSC, FSCM, ITM, IT>
{
    FSC getFacetSearchConfig(String paramString);


    Collection<FSC> getAllMappedFacetSearchConfigs();


    Collection<ComposedTypeModel> getAllMappedTypes();


    FSCM getFacetSearchConfigModel(String paramString);


    ITM getIndexedTypeModel(String paramString);


    IT getIndexedType(FSC paramFSC, String paramString);


    boolean isValidSearchConfiguredForType(String paramString);


    boolean isValidSearchConfiguredForName(String paramString);
}
