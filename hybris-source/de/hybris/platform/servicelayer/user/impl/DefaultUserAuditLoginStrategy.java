package de.hybris.platform.servicelayer.user.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.BruteForceLoginAttemptsModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserConstants;
import de.hybris.platform.servicelayer.user.UserService;
import java.time.Instant;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultUserAuditLoginStrategy extends AbstractUserAuditLoginStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultUserAuditLoginStrategy.class);
    private UserService userService;
    private UserAuditFactory userAuditFactory;


    public void auditUserOnWrongCredentials(String uid)
    {
        UserModel user;
        Preconditions.checkNotNull(uid, "Uid must not be null");
        if(StringUtils.equals(UserConstants.ADMIN_EMPLOYEE_UID, uid))
        {
            return;
        }
        try
        {
            user = this.userService.getUserForUID(uid);
        }
        catch(UnknownIdentifierException e)
        {
            return;
        }
        if(user.isLoginDisabled() || (user
                        .getDeactivationDate() != null && user.getDeactivationDate().toInstant().isBefore(Instant.now())))
        {
            LOG.warn("User account has been disabled");
            return;
        }
        user.getAllGroups().stream()
                        .map(PrincipalGroupModel::getMaxBruteForceLoginAttempts)
                        .filter(Objects::nonNull)
                        .min(Integer::compare)
                        .ifPresent(maxAttempts -> incrementAttemptsCounter(uid, user, maxAttempts));
    }


    private void incrementAttemptsCounter(String uid, UserModel user, Integer maxAttempts)
    {
        BruteForceLoginAttemptsModel attempts = loadOrCreateAttempts(uid);
        int numFailedLogins = numFailedLogins(attempts);
        if(numFailedLogins >= maxAttempts.intValue())
        {
            LOG.info("user:{} has reached the max number of failed logins ({}). The user login will be disabled", uid, maxAttempts);
            user.setLoginDisabled(true);
            attempts.setAttempts(Integer.valueOf(0));
            this.modelService.saveAll(new Object[] {user, attempts, this.userAuditFactory
                            .createBruteForceLoginDisabledAudit(user, Integer.valueOf(numFailedLogins))});
        }
        else
        {
            LOG.info("user:{} failed {} logins. Max failed logins allowed:{}", new Object[] {uid, Integer.valueOf(numFailedLogins), maxAttempts});
            attempts.setAttempts(Integer.valueOf(numFailedLogins));
            this.modelService.save(attempts);
        }
    }


    public void auditUserOnCorrectCredentials(String uid)
    {
        Preconditions.checkNotNull(uid, "Uid must not be null");
        loadAttempts(uid).ifPresent(attempts -> {
            attempts.setAttempts(Integer.valueOf(0));
            this.modelService.save(attempts);
        });
    }


    private int numFailedLogins(BruteForceLoginAttemptsModel attempts)
    {
        return (attempts.getAttempts() == null) ? 1 : (attempts.getAttempts().intValue() + 1);
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setUserAuditFactory(UserAuditFactory userAuditFactory)
    {
        this.userAuditFactory = userAuditFactory;
    }
}
