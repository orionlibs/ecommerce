package de.hybris.platform.servicelayer.security.spring;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.web.session.CachedPersistedSessionRepository;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;

public class HybrisSessionFixationProtectionStrategy extends SessionFixationProtectionStrategy
{
    private static final Logger LOG = Logger.getLogger(HybrisSessionFixationProtectionStrategy.class.getName());
    private boolean migrateSessionAttributes = true;


    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response)
    {
        HttpSession originalSession = request.getSession(false);
        if(originalSession != null && request.isRequestedSessionIdValid())
        {
            String originalSessionId = originalSession.getId();
            createNewSessionAndMigrate(request, originalSessionId, getAttributesAndinvalidateOldSession(originalSession));
        }
    }


    protected HttpSession createNewSessionAndMigrate(HttpServletRequest request, String originalSessionId, Map<String, Object> attributesToMigrate)
    {
        HttpSession newSession = request.getSession(true);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Started new http session: " + newSession.getId());
        }
        String newHttpSessionId = newSession.getId();
        if(originalSessionId.equals(newHttpSessionId))
        {
            LOG.error("Your servlet container did not change the http session ID when a new session was created. You will not be adequately protected against session-fixation attacks");
        }
        if(MapUtils.isNotEmpty(attributesToMigrate))
        {
            for(Map.Entry<String, Object> entry : attributesToMigrate.entrySet())
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Migrating key: " + (String)entry.getKey());
                }
                newSession.setAttribute(entry.getKey(), entry.getValue());
                if("jalosession".equals(entry.getKey()))
                {
                    JaloSession jaloSession = (JaloSession)entry.getValue();
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("moving JaloSession: " + jaloSession.getSessionID() + " from http session " + originalSessionId + " to " + newHttpSessionId);
                    }
                    jaloSession.setHttpSessionId(newSession
                                    .getId());
                }
            }
        }
        return newSession;
    }


    protected Map<String, Object> getAttributesAndinvalidateOldSession(HttpSession originalSession)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("invalidating http session with id " + originalSession.getId() + " and migrating attributes...");
        }
        Map<String, Object> attributesToMigrate = new HashMap<>();
        for(Enumeration<String> enumer = originalSession.getAttributeNames(); enumer.hasMoreElements(); )
        {
            String key = enumer.nextElement();
            if(keyCanBeMigrated(key))
            {
                attributesToMigrate.put(key, originalSession.getAttribute(key));
            }
        }
        originalSession.removeAttribute("jalosession");
        CachedPersistedSessionRepository.executeWithoutClosingJaloSessionOnDelete(() -> originalSession.invalidate());
        return attributesToMigrate;
    }


    protected boolean keyCanBeMigrated(String key)
    {
        return (this.migrateSessionAttributes || key.startsWith("SPRING_SECURITY_") || key.equals("jalosession") || "tenantID"
                        .equalsIgnoreCase(key));
    }


    public void setMigrateSessionAttributes(boolean migrateSessionAttributes)
    {
        super.setMigrateSessionAttributes(migrateSessionAttributes);
        this.migrateSessionAttributes = migrateSessionAttributes;
    }
}
