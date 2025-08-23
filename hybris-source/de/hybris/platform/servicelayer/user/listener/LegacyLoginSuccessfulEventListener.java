package de.hybris.platform.servicelayer.user.listener;

import de.hybris.platform.jalo.security.event.LegacyLoginSuccessfulEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.internal.service.ServicelayerUtils;
import de.hybris.platform.servicelayer.user.UserAuditLoginStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class LegacyLoginSuccessfulEventListener extends AbstractEventListener<LegacyLoginSuccessfulEvent>
{
    private static final Logger LOG = LoggerFactory.getLogger(LegacyLoginSuccessfulEventListener.class);
    private UserAuditLoginStrategy userAuditLoginStrategy;


    protected void onEvent(LegacyLoginSuccessfulEvent event)
    {
        if(ServicelayerUtils.isSystemNotInitialized())
        {
            LOG.debug("system is not initialized, skipping user login audit");
            return;
        }
        String uid = event.getUid();
        try
        {
            this.userAuditLoginStrategy.auditUserOnCorrectCredentials(uid);
        }
        catch(Exception e)
        {
            LOG.error(String.format("error on managing failed login: %s", new Object[] {uid}), e);
        }
    }


    @Required
    public void setUserAuditLoginStrategy(UserAuditLoginStrategy userAuditLoginStrategy)
    {
        this.userAuditLoginStrategy = userAuditLoginStrategy;
    }
}
