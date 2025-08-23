package de.hybris.platform.servicelayer.web;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.suspend.SystemIsSuspendedException;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.masterserver.StatisticsConfig;
import de.hybris.platform.spring.HybrisContextLoaderListener;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.filter.OncePerRequestFilter;

public abstract class AbstractPlatformFilterChain extends OncePerRequestFilter
{
    private static final Logger LOG = Logger.getLogger(AbstractPlatformFilterChain.class.getName());
    private static final String ORG_SPRINGFRAMEWORK_SECURITY_FILTER_CHAIN_PROXY = "org.springframework.security.filterChainProxy";
    private static final String PLATFORM_FIREWALL = "platformFirewall";
    private AbstractPlatformPreFilter preFilter;
    private final Filter[] internalFilters;
    private Collection<String> excludedUrlPaths;
    private static final String SUSPENDED = "Application is in the SUSPENDED state";
    private StatisticsConfig statisticsConfig = StatisticsConfig.defaultConfig();


    protected Collection<String> getExcludedUrlPaths()
    {
        return this.excludedUrlPaths;
    }


    public void setExcludedUrlPaths(List<String> excludedUrlPaths)
    {
        this.excludedUrlPaths = (excludedUrlPaths != null && !excludedUrlPaths.isEmpty()) ? excludedUrlPaths : null;
    }


    public void setPreFilter(AbstractPlatformPreFilter preFilter)
    {
        this.preFilter = preFilter;
    }


    public AbstractPlatformFilterChain(List<Filter> internalFilters)
    {
        if(CollectionUtils.isEmpty(internalFilters))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Creating new PlatformFilterChain with no internal chain!");
            }
            this.internalFilters = null;
        }
        else
        {
            if(!isStatisticsPublisherEnabled())
            {
                internalFilters.add(new StatisticsGatewayFilter(this, isBackOfficeFilterChain()));
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Creating new PlatformFilterChain with internal chain: " + internalFilters);
            }
            this.internalFilters = internalFilters.<Filter>toArray(new Filter[internalFilters.size()]);
        }
    }


    protected void setStatisticsConfig(StatisticsConfig statisticsConfig)
    {
        this.statisticsConfig = Objects.<StatisticsConfig>requireNonNull(statisticsConfig);
    }


    private boolean isStatisticsPublisherEnabled()
    {
        return this.statisticsConfig.isStatisticsPublisherEnabled();
    }


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        if(this.preFilter != null)
        {
            this.preFilter.doFilter((ServletRequest)request, (ServletResponse)response, filterChain);
            if(this.preFilter.mustStopProcessingChain())
            {
                return;
            }
        }
        try
        {
            RegistrableThread.registerThread(
                            OperationInfo.builder().withCategory(OperationInfo.Category.WEB_REQUEST).asNotSuspendableOperation().build());
        }
        catch(SystemIsSuspendedException e)
        {
            LOG.info("System is " + e.getSystemStatus() + ". Request will not be processed.");
            response.sendError(503, "Application is in the SUSPENDED state");
            return;
        }
        try
        {
            processStandardFilterChain(request, response, filterChain);
        }
        finally
        {
            RegistrableThread.unregisterThread();
        }
    }


    private void processStandardFilterChain(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        boolean handledTenant = false;
        try
        {
            handledTenant = activateTenantFromContext(getServletContext());
            switchHttpFirewallToDefault();
            if(this.internalFilters == null)
            {
                filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
            }
            else
            {
                InternalFilterChain internalFilterChain = new InternalFilterChain(this, filterChain, this.internalFilters);
                internalFilterChain.doFilter((ServletRequest)request, (ServletResponse)response);
            }
        }
        finally
        {
            if(handledTenant)
            {
                unsetTenant();
            }
        }
    }


    private static void switchHttpFirewallToDefault()
    {
        try
        {
            FilterChainProxy proxy = (FilterChainProxy)Registry.getApplicationContext().getBean("org.springframework.security.filterChainProxy", FilterChainProxy.class);
            try
            {
                proxy.setFirewall((HttpFirewall)Registry.getApplicationContext().getBean("platformFirewall", HttpFirewall.class));
            }
            catch(BeansException e)
            {
                LOG.error("Error when switching to platform firewall. Using spring defaults.", (Throwable)e);
            }
        }
        catch(NoSuchBeanDefinitionException noSuchBeanDefinitionException)
        {
        }
    }


    private void unsetTenant()
    {
        Registry.unsetCurrentTenant();
    }


    private boolean activateTenantFromContext(ServletContext servletContext)
    {
        String tenantIDFromContext = HybrisContextLoaderListener.getTenantIDForWebapp(servletContext);
        if(tenantIDFromContext != null)
        {
            Registry.setCurrentTenantByID(tenantIDFromContext);
            return true;
        }
        return false;
    }


    protected abstract boolean isBackOfficeFilterChain();


    protected boolean shouldBreakFiltering(String requestURI)
    {
        Collection<String> excludedUrlPathsCollection = getExcludedUrlPaths();
        if(excludedUrlPathsCollection != null)
        {
            if(StringUtils.isNotBlank(requestURI))
            {
                for(String excludedUrl : excludedUrlPathsCollection)
                {
                    if(requestURI.contains(excludedUrl))
                    {
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug("Request URI: " + requestURI + " is on list of url paths (" + excludedUrlPathsCollection + "). Break filter chain.");
                        }
                        return true;
                    }
                }
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Request URI: " + requestURI + " is not on list of excluded url paths (" + excludedUrlPathsCollection + "). Normal filter chain processing.");
            }
        }
        return false;
    }


    protected static void throwUncheckedException(Throwable exception)
    {
        PlatformFilterChain.throwAnyException(exception);
    }


    protected static <E extends Throwable> void throwAnyException(Throwable exception) throws E
    {
        throw (E)exception;
    }
}
