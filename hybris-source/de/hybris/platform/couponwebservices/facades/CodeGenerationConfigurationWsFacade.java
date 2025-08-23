package de.hybris.platform.couponwebservices.facades;

import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.core.servicelayer.data.SortData;
import de.hybris.platform.couponwebservices.dto.CodeGenerationConfigurationWsDTO;
import java.util.List;

public interface CodeGenerationConfigurationWsFacade
{
    CodeGenerationConfigurationWsDTO getCodeGenerationConfigurationWsDTO(String paramString);


    SearchPageData<CodeGenerationConfigurationWsDTO> getCodeGenerationConfigurations(PaginationData paramPaginationData, List<SortData> paramList);
}
