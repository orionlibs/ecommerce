package de.hybris.platform.servicelayer.web.session.stale.impl;

import de.hybris.platform.servicelayer.web.session.stale.StaleSessionCheckingFilterChain;
import de.hybris.platform.servicelayer.web.session.stale.StaleSessionConfig;
import de.hybris.platform.servicelayer.web.session.stale.StaleSessionDetector;
import de.hybris.platform.servicelayer.web.session.stale.StaleSessionStrategy;
import de.hybris.platform.util.Utilities;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultStaleSessionCheckingFilterChain implements StaleSessionCheckingFilterChain
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultStaleSessionCheckingFilterChain.class);
    private final FilterChain targetFilterChain;
    private final StaleSessionDetector staleSessionDetector;
    private final StaleSessionStrategy staleSessionStrategy;
    private final StaleSessionConfig staleSessionConfig;
    private StaleSessionStrategy.Action staleSessionAction = null;


    public DefaultStaleSessionCheckingFilterChain(FilterChain targetFilterChain, StaleSessionDetector staleSessionDetector, StaleSessionStrategy staleSessionStrategy, StaleSessionConfig staleSessionConfig)
    {
        this.targetFilterChain = Objects.<FilterChain>requireNonNull(targetFilterChain, "targetFilterChain cannot be null.");
        this.staleSessionDetector = Objects.<StaleSessionDetector>requireNonNull(staleSessionDetector, "staleSessionDetector cannot be null.");
        this.staleSessionStrategy = Objects.<StaleSessionStrategy>requireNonNull(staleSessionStrategy, "staleSessionStrategy cannot be null.");
        this.staleSessionConfig = Objects.<StaleSessionConfig>requireNonNull(staleSessionConfig, "staleSessionConfig cannot be null.");
    }


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        if(!isStaleSessionCheckingEnabled(request))
        {
            this.targetFilterChain.doFilter((ServletRequest)request, (ServletResponse)response);
            return;
        }
        filterWithStaleSessionChecking(request, response);
    }


    public StaleSessionStrategy.Action getStaleSessionAction()
    {
        return this.staleSessionAction;
    }


    private boolean isStaleSessionCheckingEnabled(HttpServletRequest request)
    {
        try
        {
            String extensionName = Utilities.getExtensionNameFromRequest(request);
            return this.staleSessionConfig.isStaleSessionCheckingEnabled(extensionName);
        }
        catch(Exception e)
        {
            LOG.warn("Failed to obtain extension name. Global config will be used.", e);
            return this.staleSessionConfig.isStaleSessionCheckingEnabled(null);
        }
    }


    private void filterWithStaleSessionChecking(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        StaleSessionDetector.Detection detection = this.staleSessionDetector.beginDetection(request);
        try
        {
            if(detection.isStaleSession())
            {
                this.staleSessionAction = this.staleSessionStrategy.onStaleSession(request, response);
            }
            else
            {
                this.targetFilterChain.doFilter((ServletRequest)request, (ServletResponse)response);
            }
            if(detection != null)
            {
                detection.close();
            }
        }
        catch(Throwable throwable)
        {
            if(detection != null)
            {
                try
                {
                    detection.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }
}
