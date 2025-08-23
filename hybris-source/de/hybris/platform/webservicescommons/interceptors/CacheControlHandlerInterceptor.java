package de.hybris.platform.webservicescommons.interceptors;

import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

public class CacheControlHandlerInterceptor implements AsyncHandlerInterceptor
{
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";


    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        if(isEligibleForCacheControl(request, response, handler))
        {
            CacheControl cacheAnnotation = getCacheControlAnnotation(request, response, handler);
            if(cacheAnnotation != null)
            {
                response.setHeader("Cache-Control", createCacheControlHeaderField(cacheAnnotation));
            }
        }
        return super.preHandle(request, response, handler);
    }


    protected boolean isEligibleForCacheControl(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        return (HttpMethod.GET.name().equals(request.getMethod()) || HttpMethod.HEAD.name().equals(request.getMethod()));
    }


    protected CacheControl getCacheControlAnnotation(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        if(!(handler instanceof HandlerMethod))
        {
            return null;
        }
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        CacheControl cacheAnnotation = (CacheControl)handlerMethod.getMethodAnnotation(CacheControl.class);
        if(cacheAnnotation == null)
        {
            cacheAnnotation = (CacheControl)handlerMethod.getBeanType().getAnnotation(CacheControl.class);
        }
        return cacheAnnotation;
    }


    protected String createCacheControlHeaderField(CacheControl cacheAnnotation)
    {
        CacheControlDirective[] directives = cacheAnnotation.directive();
        StringBuilder cacheHeader = new StringBuilder();
        if(directives != null)
        {
            for(CacheControlDirective directive : directives)
            {
                if(cacheHeader.length() > 0)
                {
                    cacheHeader.append(", ");
                }
                cacheHeader.append(directive.toString());
            }
        }
        if(cacheAnnotation.maxAge() >= 0)
        {
            if(cacheHeader.length() > 0)
            {
                cacheHeader.append(", ");
            }
            cacheHeader.append("max-age=").append(cacheAnnotation.maxAge());
        }
        if(cacheAnnotation.sMaxAge() >= 0)
        {
            if(cacheHeader.length() > 0)
            {
                cacheHeader.append(", ");
            }
            cacheHeader.append("s-maxage=").append(cacheAnnotation.sMaxAge());
        }
        return cacheHeader.toString();
    }
}
