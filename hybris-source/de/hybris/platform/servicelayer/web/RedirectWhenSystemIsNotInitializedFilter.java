package de.hybris.platform.servicelayer.web;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import java.io.IOException;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class RedirectWhenSystemIsNotInitializedFilter extends AbstractCheckedUriFilter
{
    private static final Logger LOG = Logger.getLogger(RedirectWhenSystemIsNotInitializedFilter.class);
    private static final String DEFULT_REDIRCT = "DEFAULT";
    private final Map<String, String> redirectUrls;


    public RedirectWhenSystemIsNotInitializedFilter(Map<String, String> redirectUrls)
    {
        this.redirectUrls = redirectUrls;
    }


    public RedirectWhenSystemIsNotInitializedFilter(String defaultRedirectUrl)
    {
        this.redirectUrls = (Map<String, String>)ImmutableMap.of("DEFAULT", defaultRedirectUrl);
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        if(request instanceof HttpServletRequest)
        {
            Tenant tenant = Registry.getCurrentTenantNoFallback();
            TenantAvailabilityService availabilityService = TenantAvailabilityService.createDefault();
            if(tenant != null && availabilityService.isTenantAvailable(tenant.getTenantID()))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("System is initialized. Normal filter chain processing.");
                }
                request.setAttribute("initialized", Boolean.TRUE);
                filterChain.doFilter(request, response);
            }
            else
            {
                request.setAttribute("initialized", Boolean.FALSE);
                processWhenNotInitialized(request, response, filterChain);
            }
        }
        else
        {
            filterChain.doFilter(request, response);
        }
    }


    private void processWhenNotInitialized(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        LOG.warn("System is NOT initialized. Redirect processing.");
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        String requestURI = httpRequest.getRequestURI();
        if(isUrlPathsContainsUri(requestURI))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Request URI: " + requestURI + " is on list of url paths (" + getUrlPaths() + "). Normal filter chain processing.");
            }
            filterChain.doFilter(request, response);
        }
        else
        {
            String redirectTo = null;
            if(MapUtils.isEmpty(this.redirectUrls))
            {
                redirectTo = getRedirectToFromConfig(httpRequest, httpResponse);
            }
            else
            {
                String redirect = getRedirectUrlForTenant();
                if(StringUtils.isNotBlank(redirect))
                {
                    redirectTo = httpResponse.encodeRedirectURL(httpRequest.getContextPath() + httpRequest.getContextPath());
                }
                else
                {
                    redirectTo = getRedirectToFromConfig(httpRequest, httpResponse);
                }
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Request URI: " + requestURI + " is not on list of url paths (" + getUrlPaths() + "). Redirecting to: " + redirectTo);
            }
            httpResponse.sendRedirect(redirectTo);
        }
    }


    private String getRedirectToFromConfig(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
    {
        String redirectTo = MasterTenant.getInstance().getConfig().getString("webapps.redirection.url", "/maintenance");
        redirectTo = httpResponse.encodeRedirectURL(redirectTo);
        return redirectTo;
    }


    private String getRedirectUrlForTenant()
    {
        Tenant tenant = Registry.getCurrentTenantNoFallback();
        if(this.redirectUrls.containsKey(tenant.getTenantID()))
        {
            return this.redirectUrls.get(tenant.getTenantID());
        }
        return this.redirectUrls.get("DEFAULT");
    }


    public void afterPropertiesSet() throws ServletException
    {
        for(String redirectUrl : this.redirectUrls.values())
        {
            if(!StringUtils.isBlank(redirectUrl))
            {
                getUrlPaths().add(redirectUrl);
            }
        }
    }
}
