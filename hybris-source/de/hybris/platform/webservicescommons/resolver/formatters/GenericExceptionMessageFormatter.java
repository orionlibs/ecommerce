package de.hybris.platform.webservicescommons.resolver.formatters;

import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.resolver.helpers.FallbackConfigurationHelper;

public class GenericExceptionMessageFormatter extends AbstractExceptionMessageFormatter
{
    protected static final String DEFAULT_EXCEPTION_MESSAGE = "The application has encountered an error";
    private final FallbackConfigurationHelper fallbackConfigurationHelper;


    public GenericExceptionMessageFormatter(FallbackConfigurationHelper fallbackConfigurationHelper)
    {
        this.fallbackConfigurationHelper = fallbackConfigurationHelper;
    }


    protected String formatMessage(String extensionName, Exception exception, ErrorWsDTO errorWsDTO)
    {
        String exceptionName = exception.getClass().getSimpleName();
        return getFallbackConfigurationHelper()
                        .getFirstValue(extensionName, exceptionName, "message").orElse("The application has encountered an error");
    }


    protected FallbackConfigurationHelper getFallbackConfigurationHelper()
    {
        return this.fallbackConfigurationHelper;
    }
}
