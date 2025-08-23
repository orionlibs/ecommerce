package de.hybris.platform.servicelayer.web.session.stale.impl;

import de.hybris.platform.servicelayer.user.listener.PasswordChangeEvent;
import de.hybris.platform.servicelayer.user.listener.PasswordChangeListener;
import de.hybris.platform.servicelayer.web.session.stale.StaleSessionDetector;
import de.hybris.platform.util.WebSessionFunctions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

public class DefaultStaleSessionDetector implements StaleSessionDetector, PasswordChangeListener
{
    private static final String CURRENT_REQUEST_DETECTION = "de.hybris.platform.servicelayer.web.session.stale.impl.DETECTION";


    public StaleSessionDetector.Detection beginDetection(HttpServletRequest request)
    {
        Objects.requireNonNull(request, "request cannot be null.");
        StaleSessionDetector.Detection detection = createDetection(request);
        request.setAttribute("de.hybris.platform.servicelayer.web.session.stale.impl.DETECTION", detection);
        return detection;
    }


    public void passwordChanged(PasswordChangeEvent event)
    {
        if(event == null)
        {
            return;
        }
        HttpServletRequest currentRequest = WebSessionFunctions.getCurrentHttpServletRequest();
        if(currentRequest == null)
        {
            return;
        }
        Object detectorFromRequest = currentRequest.getAttribute("de.hybris.platform.servicelayer.web.session.stale.impl.DETECTION");
        if(!(detectorFromRequest instanceof PasswordChangeListener))
        {
            return;
        }
        ((PasswordChangeListener)detectorFromRequest).passwordChanged(event);
    }


    protected StaleSessionDetector.Detection createDetection(HttpServletRequest request)
    {
        Collection<StaleSessionDetector.Detection> delegates = new ArrayList<>();
        SessionContext sessionContext = createSessionContext(request);
        delegates.add(new MarkerBasedDetection(sessionContext));
        delegates.add(new UserStateBasedDetection(sessionContext));
        return (StaleSessionDetector.Detection)new DelegatingDetection(delegates);
    }


    protected SessionContext createSessionContext(HttpServletRequest request)
    {
        return (SessionContext)new RequestAndJaloBasedSessionContext(request);
    }
}
