package de.hybris.platform.personalizationwebservices.security;

import de.hybris.platform.personalizationwebservices.data.CatalogVersionWsDTO;
import java.util.List;

public interface QueryEndpointPermissionsChecker
{
    void checkCurrentUserAllowed(List<CatalogVersionWsDTO> paramList1, List<CatalogVersionWsDTO> paramList2);
}
