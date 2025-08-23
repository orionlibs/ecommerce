package de.hybris.platform.personalizationwebservices.queries;

import de.hybris.platform.personalizationwebservices.data.CatalogVersionWsDTO;
import java.util.List;
import java.util.Map;

public interface RestQueryExecutor
{
    Object execute(Map<String, String> paramMap);


    List<CatalogVersionWsDTO> getCatalogsForWriteAccess(Map<String, String> paramMap);


    List<CatalogVersionWsDTO> getCatalogsForReadAccess(Map<String, String> paramMap);
}
