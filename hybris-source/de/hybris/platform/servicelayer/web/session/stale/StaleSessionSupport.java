package de.hybris.platform.servicelayer.web.session.stale;

import javax.servlet.FilterChain;

public interface StaleSessionSupport
{
    StaleSessionCheckingFilterChain getStaleSessionCheckingFilterChain(FilterChain paramFilterChain);
}
