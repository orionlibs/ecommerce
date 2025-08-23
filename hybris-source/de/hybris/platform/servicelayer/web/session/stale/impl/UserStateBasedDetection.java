package de.hybris.platform.servicelayer.web.session.stale.impl;

import de.hybris.platform.servicelayer.user.UserConstants;
import de.hybris.platform.servicelayer.web.session.stale.StaleSessionDetector;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserStateBasedDetection implements StaleSessionDetector.Detection
{
    private static final Logger LOG = LoggerFactory.getLogger(UserStateBasedDetection.class);
    private final SessionContext sessionContext;
    private final boolean isStaleSession;


    public UserStateBasedDetection(SessionContext sessionContext)
    {
        this.sessionContext = Objects.<SessionContext>requireNonNull(sessionContext, "sessionContext cannot be null.");
        this.isStaleSession = detectStaleSession();
    }


    public boolean isStaleSession()
    {
        return this.isStaleSession;
    }


    private boolean detectStaleSession()
    {
        if(this.sessionContext.getSessionUserId() == null || StringUtils.equals(UserConstants.ADMIN_EMPLOYEE_UID, this.sessionContext.getSessionUserId()) || StringUtils.equals(UserConstants.ANONYMOUS_CUSTOMER_UID, this.sessionContext.getSessionUserId()))
        {
            return false;
        }
        Date deactivationDate = this.sessionContext.getSessionUserDeactivationDate();
        boolean accountOutdated = (deactivationDate != null && deactivationDate.toInstant().isBefore(Instant.now()));
        boolean staleSession = (this.sessionContext.isSessionUserLoginDisabled() || accountOutdated);
        if(staleSession)
        {
            LOG.debug("Detected session `{}` for deactivated user `{}`, reason: `{}`.",
                            new Object[] {this.sessionContext.getSessionId(), this.sessionContext.getSessionUserId(), this.sessionContext.isSessionUserLoginDisabled() ? "login is disabled" : ("account is inactive since " + deactivationDate)});
        }
        return staleSession;
    }


    public void close()
    {
    }
}
