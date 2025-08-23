package de.hybris.platform.webservicescommons.resolver;

import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.resolver.formatters.AbstractExceptionMessageFormatter;
import de.hybris.platform.webservicescommons.resolver.helpers.FallbackConfigurationHelper;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.configuration.PropertyConverter;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;

public class RestExceptionResolver extends AbstractRestHandlerExceptionResolver
{
    private Map<ExceptionMessageFormatterType, AbstractExceptionMessageFormatter> exceptionMapping;
    private FallbackConfigurationHelper fallbackConfigurationHelper;
    private String extensionName;


    protected int calculateStatusFromException(Exception ex)
    {
        String exceptionName = ex.getClass().getSimpleName();
        return ((Integer)getFallbackConfigurationHelper()
                        .getFirstValue(getExtensionName(), exceptionName, "status", Integer::valueOf)
                        .orElse(Integer.valueOf(400))).intValue();
    }


    protected boolean shouldDisplayStack(Exception ex)
    {
        String exceptionName = ex.getClass().getSimpleName();
        return ((Boolean)getFallbackConfigurationHelper()
                        .getFirstValue(getExtensionName(), exceptionName, "logstack", PropertyConverter::toBoolean)
                        .orElse(Boolean.valueOf(true))).booleanValue();
    }


    protected ErrorListWsDTO convertException(Exception ex)
    {
        ErrorListWsDTO errorListDto = super.convertException(ex);
        setErrorMessages(ex, errorListDto);
        return errorListDto;
    }


    protected ModelAndView doResolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception ex)
    {
        return doResolveExceptionInternal(httpServletRequest, httpServletResponse, handler, ex);
    }


    protected void setErrorMessages(Exception ex, ErrorListWsDTO errorListDto)
    {
        ExceptionMessageFormatterType exceptionMessageFormatterType = calculateExceptionMessageFormatter(ex);
        ((AbstractExceptionMessageFormatter)getExceptionMapping().get(exceptionMessageFormatterType)).setMessages(getExtensionName(), ex, errorListDto);
    }


    protected ExceptionMessageFormatterType calculateExceptionMessageFormatter(Exception ex)
    {
        String exceptionName = ex.getClass().getSimpleName();
        return getFallbackConfigurationHelper()
                        .getFirstValue(getExtensionName(), exceptionName, "messageFormatterType", ExceptionMessageFormatterType::valueOf)
                        .orElse(ExceptionMessageFormatterType.FORWARD);
    }


    protected Map<ExceptionMessageFormatterType, AbstractExceptionMessageFormatter> getExceptionMapping()
    {
        return this.exceptionMapping;
    }


    @Required
    public void setExceptionMapping(Map<ExceptionMessageFormatterType, AbstractExceptionMessageFormatter> exceptionMapping)
    {
        this.exceptionMapping = exceptionMapping;
    }


    protected FallbackConfigurationHelper getFallbackConfigurationHelper()
    {
        return this.fallbackConfigurationHelper;
    }


    @Required
    public void setFallbackConfigurationHelper(FallbackConfigurationHelper fallbackConfigurationHelper)
    {
        this.fallbackConfigurationHelper = fallbackConfigurationHelper;
    }


    protected String getExtensionName()
    {
        return this.extensionName;
    }


    @Required
    public void setExtensionName(String extensionName)
    {
        this.extensionName = extensionName;
    }
}
