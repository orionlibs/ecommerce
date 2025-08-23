package de.hybris.platform.webservicescommons.resolver.formatters;

import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;

public abstract class AbstractExceptionMessageFormatter
{
    public void setMessages(String extensionName, Exception exception, ErrorListWsDTO errorListWsDTO)
    {
        ServicesUtil.validateParameterNotNull(extensionName, "extensionName cannot be null");
        ServicesUtil.validateParameterNotNull(exception, "exception cannot be null");
        ServicesUtil.validateParameterNotNull(errorListWsDTO, "errorListWsDTO cannot be null");
        ServicesUtil.validateParameterNotNull(errorListWsDTO.getErrors(), "errorListWsDTO.getErrors() cannot be null");
        errorListWsDTO.getErrors().stream().forEach(error -> error.setMessage(formatMessage(extensionName, exception, error)));
    }


    protected abstract String formatMessage(String paramString, Exception paramException, ErrorWsDTO paramErrorWsDTO);
}
