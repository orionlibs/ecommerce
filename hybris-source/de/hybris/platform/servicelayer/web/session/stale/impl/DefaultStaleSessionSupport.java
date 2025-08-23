package de.hybris.platform.servicelayer.web.session.stale.impl;

import de.hybris.platform.servicelayer.web.session.stale.StaleSessionCheckingFilterChain;
import de.hybris.platform.servicelayer.web.session.stale.StaleSessionConfig;
import de.hybris.platform.servicelayer.web.session.stale.StaleSessionDetector;
import de.hybris.platform.servicelayer.web.session.stale.StaleSessionStrategy;
import de.hybris.platform.servicelayer.web.session.stale.StaleSessionSupport;
import java.util.Objects;
import javax.servlet.FilterChain;

public class DefaultStaleSessionSupport implements StaleSessionSupport
{
    private final StaleSessionStrategy staleSessionStrategy;
    private final StaleSessionDetector staleSessionDetector;
    private final StaleSessionConfig staleSessionConfig;


    public DefaultStaleSessionSupport(StaleSessionStrategy staleSessionStrategy, StaleSessionDetector staleSessionDetector, StaleSessionConfig staleSessionConfig)
    {
        this.staleSessionStrategy = Objects.<StaleSessionStrategy>requireNonNull(staleSessionStrategy, "staleSessionStrategy cannot be null.");
        this.staleSessionDetector = Objects.<StaleSessionDetector>requireNonNull(staleSessionDetector, "staleSessionDetector cannot be null.");
        this.staleSessionConfig = Objects.<StaleSessionConfig>requireNonNull(staleSessionConfig, "staleSessionConfig cannot be null.");
    }


    public StaleSessionCheckingFilterChain getStaleSessionCheckingFilterChain(FilterChain filterChain)
    {
        Objects.requireNonNull(filterChain, "filterChain cannot be null.");
        return (StaleSessionCheckingFilterChain)new DefaultStaleSessionCheckingFilterChain(filterChain, this.staleSessionDetector, this.staleSessionStrategy, this.staleSessionConfig);
    }
}
