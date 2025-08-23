package de.hybris.platform.webservicescommons.api.restrictions.strategies.impl;

import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.webservicescommons.api.restrictions.data.EndpointContextData;
import de.hybris.platform.webservicescommons.api.restrictions.strategies.EndpointIdentificationStrategy;
import io.swagger.annotations.ApiOperation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class OperationIdIdentificationStrategy implements EndpointIdentificationStrategy
{
    private static final String ENDPOINT_ID_USING_FORMAT = "%sUsing%s";
    private final Map<Method, String> endpointIdCache = new ConcurrentHashMap<>();


    public Optional<String> findEndpointId(EndpointContextData endpointContextData)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("endpointContextData", endpointContextData);
        if(endpointContextData.getMethod() == null)
        {
            return Optional.empty();
        }
        return Optional.ofNullable(this.endpointIdCache.computeIfAbsent(endpointContextData.getMethod(), this::getByEndpointMethod));
    }


    protected String getByEndpointMethod(Method endpointMethod)
    {
        RequestMapping requestMapping = (RequestMapping)AnnotationUtils.findAnnotation(endpointMethod, RequestMapping.class);
        if(requestMapping == null)
        {
            return null;
        }
        String endpointId;
        if((endpointId = getByApiOperationNickname(endpointMethod)) == null)
        {
            endpointId = getByRequestMethod(requestMapping, endpointMethod);
        }
        return endpointId;
    }


    protected String getByApiOperationNickname(Method endpointMethod)
    {
        ApiOperation apiOperation = (ApiOperation)AnnotationUtils.findAnnotation(endpointMethod, ApiOperation.class);
        return (apiOperation != null && StringUtils.isNotBlank(apiOperation.nickname())) ? apiOperation.nickname() : null;
    }


    protected String getByRequestMethod(RequestMapping requestMapping, Method endpointMethod)
    {
        String requestMethodName = getRequestMethodName(requestMapping);
        return formatEndpointId(endpointMethod.getName(), requestMethodName);
    }


    protected String getRequestMethodName(RequestMapping requestMapping)
    {
        return ((requestMapping.method()).length > 0) ? requestMapping.method()[0].name() : RequestMethod.GET.name();
    }


    protected String formatEndpointId(String endpointMethodName, String requestMethodName)
    {
        return String.format("%sUsing%s", new Object[] {endpointMethodName, requestMethodName});
    }


    protected Map<Method, String> getEndpointIdCache()
    {
        return this.endpointIdCache;
    }
}
