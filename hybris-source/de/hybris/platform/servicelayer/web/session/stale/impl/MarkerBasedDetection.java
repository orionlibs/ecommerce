package de.hybris.platform.servicelayer.web.session.stale.impl;

import de.hybris.platform.core.Constants;
import de.hybris.platform.servicelayer.user.listener.PasswordChangeEvent;
import de.hybris.platform.servicelayer.user.listener.PasswordChangeListener;
import de.hybris.platform.servicelayer.web.session.stale.StaleSessionDetector;
import java.util.Objects;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarkerBasedDetection implements StaleSessionDetector.Detection, PasswordChangeListener
{
    static final String STALE_SESSION_MARKER = "de.hybris.platform.service.layer.web.session.stale.impl.MARKER";
    private static final Logger LOG = LoggerFactory.getLogger(MarkerBasedDetection.class);
    private final SessionContext sessionContext;
    private final String initialSessionId;
    private final String initialUserId;
    private final boolean isStaleSession;
    private boolean markerResetRequested;


    public MarkerBasedDetection(SessionContext sessionContext)
    {
        this.sessionContext = Objects.<SessionContext>requireNonNull(sessionContext, "sessionContext cannot be null.");
        this.initialSessionId = getSessionId();
        this.initialUserId = getSessionUserId();
        this.isStaleSession = detectStaleSession();
    }


    static String calculateMarker(String encodedPassword)
    {
        if(encodedPassword == null)
        {
            encodedPassword = "NULL";
        }
        return DigestUtils.sha1Hex(encodedPassword);
    }


    public boolean isStaleSession()
    {
        return this.isStaleSession;
    }


    public void passwordChanged(PasswordChangeEvent event)
    {
        if(event != null && Objects.equals(event.getUserId(), this.initialUserId))
        {
            LOG.debug("Password changed for `{}`. Marker will be reset.", this.initialUserId);
            this.markerResetRequested = true;
        }
    }


    public void close()
    {
        String currentUserId = getSessionUserId();
        Object sessionMarker = getSessionMarker();
        if(isAnonymousUser(currentUserId))
        {
            if(sessionMarker != null)
            {
                clearSessionMarker();
            }
            return;
        }
        if(sessionMarker == null || this.isStaleSession || this.markerResetRequested || !currentUserId.equals(this.initialUserId))
        {
            resetSessionMarker();
        }
    }


    private boolean detectStaleSession()
    {
        Object sessionMarker = getSessionMarker();
        if(isAnonymousUser(this.initialUserId) || sessionMarker == null)
        {
            return false;
        }
        String currentMarker = getCurrentMarker();
        boolean staleSession = !sessionMarker.equals(currentMarker);
        if(staleSession)
        {
            LOG.debug("Detected stale session `{}` for user `{}`, `{}`<>`{}`.", new Object[] {this.initialSessionId, this.initialUserId, currentMarker, sessionMarker});
        }
        return staleSession;
    }


    private boolean isAnonymousUser(String userId)
    {
        return (userId == null || Constants.USER.ANONYMOUS_CUSTOMER.equals(userId));
    }


    private Object getSessionMarker()
    {
        return this.sessionContext.getSessionAttribute("de.hybris.platform.service.layer.web.session.stale.impl.MARKER");
    }


    private void setSessionMarker(String marker)
    {
        LOG.debug("Setting session marker for session `{}` to {}.", getSessionId(), marker);
        this.sessionContext.setSessionAttribute("de.hybris.platform.service.layer.web.session.stale.impl.MARKER", marker);
    }


    private void clearSessionMarker()
    {
        LOG.debug("Clearing session marker for anonymous session `{}`.", getSessionId());
        setSessionMarker(null);
    }


    private void resetSessionMarker()
    {
        String currentMarker = getCurrentMarker();
        setSessionMarker(currentMarker);
    }


    private String getCurrentMarker()
    {
        return calculateMarker(getSessionUserPassword() + "_" + getSessionUserPassword());
    }


    private String getSessionId()
    {
        return this.sessionContext.getSessionId();
    }


    private String getSessionUserId()
    {
        return this.sessionContext.getSessionUserId();
    }


    private String getSessionUserPassword()
    {
        return this.sessionContext.getSessionUserPassword();
    }


    private String getSessionUserToken()
    {
        return this.sessionContext.getSessionUserToken();
    }
}
