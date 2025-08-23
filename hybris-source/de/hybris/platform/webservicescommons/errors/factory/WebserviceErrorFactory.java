package de.hybris.platform.webservicescommons.errors.factory;

import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import java.util.List;

public interface WebserviceErrorFactory
{
    List<ErrorWsDTO> createErrorList(Object paramObject);
}
