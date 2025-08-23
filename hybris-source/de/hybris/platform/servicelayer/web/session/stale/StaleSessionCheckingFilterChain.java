package de.hybris.platform.servicelayer.web.session.stale;

import javax.servlet.FilterChain;

public interface StaleSessionCheckingFilterChain extends FilterChain
{
    StaleSessionStrategy.Action getStaleSessionAction();
}
