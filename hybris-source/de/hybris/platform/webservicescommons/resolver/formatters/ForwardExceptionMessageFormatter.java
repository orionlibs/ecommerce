package de.hybris.platform.webservicescommons.resolver.formatters;

import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;

public class ForwardExceptionMessageFormatter extends AbstractExceptionMessageFormatter
{
    protected String formatMessage(String extensionName, Exception exception, ErrorWsDTO errorWsDTO)
    {
        return errorWsDTO.getMessage();
    }
}
