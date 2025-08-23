package de.hybris.platform.webservicescommons.resolver;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

@Deprecated(since = "2105", forRemoval = true)
public class RestHandlerExceptionResolver extends AbstractRestHandlerExceptionResolver
{
    public static final String PROPERTY_ROOT_KEY = "webservicescommons.resthandlerexceptionresolver.";
    public static final String GLOBAL_STATUS_PROPERTY_KEY = "webservicescommons.resthandlerexceptionresolver.%s.status";
    public static final String EXTENSION_STATUS_PROPERTY_KEY = "webservicescommons.resthandlerexceptionresolver.%s.%s.status";
    public static final String GLOBAL_LOGSTACK_PROPERTY_KEY = "webservicescommons.resthandlerexceptionresolver.%s.logstack";
    public static final String EXTENSION_LOGSTACK_PROPERTY_KEY = "webservicescommons.resthandlerexceptionresolver.%s.%s.logstack";
    public static final String DEFAULT_STATUS_PROPERTY = "webservicescommons.resthandlerexceptionresolver.DEFAULT.status";
    public static final String DEFAULT_LOGSTACK_PROPERTY = "webservicescommons.resthandlerexceptionresolver.DEFAULT.logstack";
    private static final int UNDEFINED_ERROR_STATUS = 400;
    private ConfigurationService configurationService;
    private String propertySpecificKey;


    protected int calculateStatusFromException(Exception e)
    {
        if(this.propertySpecificKey != null)
        {
            Integer integer = getIntegerConfiguration(
                            String.format("webservicescommons.resthandlerexceptionresolver.%s.%s.status", new Object[] {this.propertySpecificKey, e.getClass().getSimpleName()}));
            if(integer != null)
            {
                return integer.intValue();
            }
        }
        Integer status = getIntegerConfiguration(String.format("webservicescommons.resthandlerexceptionresolver.%s.status", new Object[] {e.getClass().getSimpleName()}));
        if(status != null)
        {
            return status.intValue();
        }
        status = getIntegerConfiguration("webservicescommons.resthandlerexceptionresolver.DEFAULT.status");
        if(status != null)
        {
            return status.intValue();
        }
        return 400;
    }


    protected boolean shouldDisplayStack(Exception e)
    {
        Boolean displayStack = null;
        if(this.propertySpecificKey != null)
        {
            displayStack = getBooleanConfiguration(
                            String.format("webservicescommons.resthandlerexceptionresolver.%s.%s.logstack", new Object[] {this.propertySpecificKey, e.getClass().getSimpleName()}));
            if(displayStack != null)
            {
                return displayStack.booleanValue();
            }
        }
        displayStack = getBooleanConfiguration(String.format("webservicescommons.resthandlerexceptionresolver.%s.logstack", new Object[] {e.getClass().getSimpleName()}));
        if(displayStack != null)
        {
            return displayStack.booleanValue();
        }
        displayStack = getBooleanConfiguration("webservicescommons.resthandlerexceptionresolver.DEFAULT.logstack");
        if(displayStack != null)
        {
            return displayStack.booleanValue();
        }
        return true;
    }


    protected ModelAndView doResolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception ex)
    {
        return doResolveExceptionInternal(httpServletRequest, httpServletResponse, handler, ex);
    }


    protected Integer getIntegerConfiguration(String key)
    {
        return (this.configurationService == null) ? null : this.configurationService.getConfiguration().getInteger(key, null);
    }


    protected Boolean getBooleanConfiguration(String key)
    {
        return (this.configurationService == null) ? null : this.configurationService.getConfiguration().getBoolean(key, null);
    }


    public void setPropertySpecificKey(String propertySpecificKey)
    {
        this.propertySpecificKey = propertySpecificKey;
    }


    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
