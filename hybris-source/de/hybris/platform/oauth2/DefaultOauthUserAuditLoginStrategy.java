package de.hybris.platform.oauth2;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.model.user.BruteForceLoginAttemptsModel;
import de.hybris.platform.core.model.user.BruteForceOAuthDisabledAuditModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.impl.AbstractUserAuditLoginStrategy;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import de.hybris.platform.webservicescommons.oauth2.client.ClientDetailsDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOauthUserAuditLoginStrategy extends AbstractUserAuditLoginStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultOauthUserAuditLoginStrategy.class);
    public static final String OAUTH2_MAX_AUTHENTICATION_ATTEMPTS = "oauth2.maxAuthenticationAttempts";
    private ConfigurationService configurationService;
    private ClientDetailsDao clientDetailsDao;


    public void auditUserOnWrongCredentials(String uid)
    {
        Preconditions.checkNotNull(uid, "Uid must not be null");
        int maxAttempts = this.configurationService.getConfiguration().getInt("oauth2.maxAuthenticationAttempts", -1);
        if(maxAttempts < 0)
        {
            return;
        }
        if(StringUtils.isEmpty(uid))
        {
            LOG.warn("empty authentication or clientId");
            return;
        }
        OAuthClientDetailsModel client = this.clientDetailsDao.findClientById(uid);
        if(client == null)
        {
            LOG.debug("unknown clientId:{}", uid);
            return;
        }
        if(client.getDisabled().booleanValue())
        {
            LOG.warn("User account has been disabled");
            return;
        }
        BruteForceLoginAttemptsModel attempts = loadOrCreateAttempts(client.getClientId());
        int numFailedLogins = getNumFailedLogins(attempts);
        if(numFailedLogins >= maxAttempts)
        {
            LOG.info("clientId:{} has reached the max number of failed authentications ({}). Client will be disabled", client
                            .getClientId(), Integer.valueOf(maxAttempts));
            client.setDisabled(Boolean.TRUE);
            attempts.setAttempts(Integer.valueOf(0));
            BruteForceOAuthDisabledAuditModel audit = (BruteForceOAuthDisabledAuditModel)this.modelService.create(BruteForceOAuthDisabledAuditModel.class);
            audit.setUid(client.getClientId());
            audit.setUserPK(client.getPk().getLong());
            audit.setFailedOAuthAuthorizations(Integer.valueOf(numFailedLogins));
            this.modelService.saveAll(new Object[] {client, attempts, audit});
            return;
        }
        LOG.info("clientId:{} failed {} authentications. Max allowed:{}", new Object[] {client.getClientId(),
                        Integer.valueOf(numFailedLogins),
                        Integer.valueOf(maxAttempts)});
        attempts.setAttempts(Integer.valueOf(numFailedLogins));
        this.modelService.save(attempts);
    }


    private int getNumFailedLogins(BruteForceLoginAttemptsModel attempts)
    {
        return (attempts.getAttempts() == null) ? 1 : (attempts.getAttempts().intValue() + 1);
    }


    public void auditUserOnCorrectCredentials(String uid)
    {
        Preconditions.checkNotNull(uid, "Uid must not be null");
        loadAttempts(uid).ifPresent(attempts -> {
            attempts.setAttempts(Integer.valueOf(0));
            this.modelService.save(attempts);
        });
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    @Required
    public void setClientDetailsDao(ClientDetailsDao clientDetailsDao)
    {
        this.clientDetailsDao = clientDetailsDao;
    }
}
