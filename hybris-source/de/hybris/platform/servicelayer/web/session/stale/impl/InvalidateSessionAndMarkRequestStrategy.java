package de.hybris.platform.servicelayer.web.session.stale.impl;

import de.hybris.platform.servicelayer.web.session.stale.StaleSessionStrategy;
import de.hybris.platform.util.WebSessionFunctions;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidateSessionAndMarkRequestStrategy implements StaleSessionStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(InvalidateSessionAndMarkRequestStrategy.class);


    public StaleSessionStrategy.Action onStaleSession(HttpServletRequest request, HttpServletResponse response)
    {
        Objects.requireNonNull(request, "request cannot be null.");
        HttpSession sessionToInvalidate = request.getSession();
        LOG.info("Invalidating stale session.");
        WebSessionFunctions.markRequestAsStale(request);
        WebSessionFunctions.invalidateSession(sessionToInvalidate);
        return StaleSessionStrategy.Action.CONTINUE_REQUEST_PROCESSING;
    }
}
