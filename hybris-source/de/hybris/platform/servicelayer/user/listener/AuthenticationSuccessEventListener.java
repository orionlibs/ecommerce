package de.hybris.platform.servicelayer.user.listener;

import de.hybris.platform.servicelayer.internal.service.ServicelayerUtils;
import de.hybris.platform.servicelayer.user.UserAuditLoginStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;

public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent>
{
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationSuccessEventListener.class);
    private UserAuditLoginStrategy userAuditLoginStrategy;


    public void onApplicationEvent(AuthenticationSuccessEvent event)
    {
        if(ServicelayerUtils.isSystemNotInitialized())
        {
            LOG.debug("system is not initialized, skipping user login audit");
            return;
        }
        Authentication authentication = event.getAuthentication();
        if(authentication == null)
        {
            LOG.warn("No authentication provided");
            return;
        }
        try
        {
            this.userAuditLoginStrategy.auditUserOnCorrectCredentials(authentication.getName());
        }
        catch(Exception e)
        {
            LOG.error(String.format("error on managing successful login: %s", new Object[] {event.getAuthentication()}), e);
        }
    }


    @Required
    public void setUserAuditLoginStrategy(UserAuditLoginStrategy userAuditLoginStrategy)
    {
        this.userAuditLoginStrategy = userAuditLoginStrategy;
    }
}
