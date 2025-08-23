package de.hybris.platform.servicelayer.user.listener;

import de.hybris.platform.servicelayer.internal.service.ServicelayerUtils;
import de.hybris.platform.servicelayer.user.UserAuditLoginStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;

public class AuthenticationFailureEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent>
{
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFailureEventListener.class);
    private UserAuditLoginStrategy userAuditLoginStrategy;


    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event)
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
            this.userAuditLoginStrategy.auditUserOnWrongCredentials(authentication.getName());
        }
        catch(Exception e)
        {
            LOG.error(String.format("error on managing failed login: %s", new Object[] {event.getAuthentication()}), e);
        }
    }


    @Required
    public void setUserAuditLoginStrategy(UserAuditLoginStrategy userAuditLoginStrategy)
    {
        this.userAuditLoginStrategy = userAuditLoginStrategy;
    }
}
