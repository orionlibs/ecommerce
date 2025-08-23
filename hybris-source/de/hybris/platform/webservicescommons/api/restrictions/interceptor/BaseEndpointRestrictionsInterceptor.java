package de.hybris.platform.webservicescommons.api.restrictions.interceptor;

import de.hybris.platform.webservicescommons.api.restrictions.data.EndpointContextData;
import de.hybris.platform.webservicescommons.api.restrictions.services.EndpointRestrictionsService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class BaseEndpointRestrictionsInterceptor implements HandlerInterceptor
{
    private static final Logger LOG = LoggerFactory.getLogger(BaseEndpointRestrictionsInterceptor.class);
    private final String specificConfigPrefix;
    private final EndpointRestrictionsService endpointRestrictionsService;
    private final int errorStatus;


    public BaseEndpointRestrictionsInterceptor(String specificConfigPrefix, EndpointRestrictionsService endpointRestrictionsService)
    {
        this(specificConfigPrefix, endpointRestrictionsService, HttpStatus.NOT_FOUND.value());
    }


    public BaseEndpointRestrictionsInterceptor(String specificConfigPrefix, EndpointRestrictionsService endpointRestrictionsService, int errorStatus)
    {
        this.specificConfigPrefix = specificConfigPrefix;
        this.endpointRestrictionsService = endpointRestrictionsService;
        this.errorStatus = validateErrorStatus(errorStatus);
    }


    protected static int validateErrorStatus(int errorStatus)
    {
        return HttpStatus.valueOf(errorStatus).value();
    }


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object endpointHandler) throws Exception
    {
        if(isEndpointDisabled(endpointHandler))
        {
            stopRequestProcessing(request, response);
            return false;
        }
        return true;
    }


    protected boolean isEndpointDisabled(Object endpointHandler)
    {
        if(isHandlerSupported(endpointHandler))
        {
            EndpointContextData endpointContextData = createEndpointContextData((HandlerMethod)endpointHandler);
            return getEndpointRestrictionsService().isEndpointDisabled(getSpecificConfigPrefix(), endpointContextData);
        }
        return false;
    }


    protected void stopRequestProcessing(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        logDisabledEndpoint(request);
        response.sendError(getErrorStatus());
    }


    protected void logDisabledEndpoint(HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        LOG.debug("API endpoint for request URI: {} is disabled", requestURI);
    }


    protected boolean isHandlerSupported(Object endpointHandler)
    {
        return (endpointHandler != null && HandlerMethod.class.isAssignableFrom(endpointHandler.getClass()));
    }


    protected EndpointContextData createEndpointContextData(HandlerMethod handlerMethod)
    {
        EndpointContextData endpointContextData = new EndpointContextData();
        endpointContextData.setMethod(handlerMethod.getMethod());
        return endpointContextData;
    }


    protected String getSpecificConfigPrefix()
    {
        return this.specificConfigPrefix;
    }


    protected EndpointRestrictionsService getEndpointRestrictionsService()
    {
        return this.endpointRestrictionsService;
    }


    protected int getErrorStatus()
    {
        return this.errorStatus;
    }
}
