package de.hybris.platform.servicelayer.interceptor;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.util.Config;
import java.util.Collection;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisableLoginForImportedUserInterceptor implements PrepareInterceptor<UserModel>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DisableLoginForImportedUserInterceptor.class);
    private static final String ENABLED = "disable.login.for.imported.user.interceptor.enabled";
    private final SessionService sessionService;
    private final PasswordEncoderService passwordEncoderService;
    private volatile Boolean forcedEnableFlag = null;


    public DisableLoginForImportedUserInterceptor(SessionService sessionService, PasswordEncoderService passwordEncoderService)
    {
        this.sessionService = Objects.<SessionService>requireNonNull(sessionService, "sessionService mustn't be null");
        this.passwordEncoderService = Objects.<PasswordEncoderService>requireNonNull(passwordEncoderService, "passwordEncoderService mustn't be null");
    }


    public void onPrepare(UserModel user, InterceptorContext ctx) throws InterceptorException
    {
        if(user == null || ctx == null || !isEnabled())
        {
            return;
        }
        if(isCreationFromImpEx() || isModificationFromImpEx(user))
        {
            adjustUser(user);
        }
    }


    private void adjustUser(UserModel user)
    {
        if(user.isLoginDisabled() || !hasEmptyPassword(user))
        {
            return;
        }
        LOGGER.info("Disabling login for '{}' because of empty password.", user.getUid());
        user.setLoginDisabled(true);
    }


    private boolean hasEmptyPassword(UserModel user)
    {
        return (this.passwordEncoderService.isValid(user, null) || this.passwordEncoderService.isValid(user, ""));
    }


    private boolean isEnabled()
    {
        Boolean forced = this.forcedEnableFlag;
        if(forced != null)
        {
            return forced.booleanValue();
        }
        return Config.getBoolean("disable.login.for.imported.user.interceptor.enabled", true);
    }


    private boolean isCreationFromImpEx()
    {
        return Boolean.TRUE.equals(this.sessionService.getAttribute("impex.creation"));
    }


    private boolean isModificationFromImpEx(UserModel user)
    {
        if(!Boolean.TRUE.equals(this.sessionService.getAttribute("impex.modification")))
        {
            return false;
        }
        Object pks = this.sessionService.getAttribute("impex.modification.pk");
        if(!(pks instanceof Collection))
        {
            return false;
        }
        return ((Collection)pks).contains(user.getPk());
    }


    void forceEnabled(Boolean enabled)
    {
        this.forcedEnableFlag = enabled;
    }
}
