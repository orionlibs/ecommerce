package de.hybris.platform.cockpit.daos.impl;

import de.hybris.platform.cockpit.daos.CockpitUIComponentAccessRightDao;
import de.hybris.platform.cockpit.model.CockpitUIComponentAccessRightModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Collections;
import java.util.List;

public class DefaultCockpitUIComponentAccessRightDao implements CockpitUIComponentAccessRightDao
{
    protected FlexibleSearchService flexibleSearchService;


    public CockpitUIComponentAccessRightModel findCockpitUIComponentAccessRight(String componentCode)
    {
        List<CockpitUIComponentAccessRightModel> result = this.flexibleSearchService.search("SELECT {pk} FROM {CockpitUIComponentAccessRight} WHERE {code} = ?code ORDER BY {creationtime} DESC", Collections.singletonMap("code", componentCode)).getResult();
        return result.isEmpty() ? null : result.get(0);
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
