package de.hybris.platform.jalo.user;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.JaloImplementationManager;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.Config;
import org.apache.log4j.Logger;

public class Customer extends GeneratedCustomer
{
    private static final Logger LOG = Logger.getLogger(Customer.class.getName());
    public static final String LOGIN_ANONYMOUS_ALWAYS_DISABLED = "login.anonymous.always.disabled";


    @Deprecated(since = "ages", forRemoval = false)
    public static void registerAsJaloObject()
    {
        JaloImplementationManager.clearJaloObjectMapping(Customer.class);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean checkPassword(LoginToken token)
    {
        return (isNotAnonymousOrAnonymousLoginIsAllowed() && super.checkPassword(token));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean checkPassword(SessionContext ctx, String plainPassword)
    {
        return (isNotAnonymousOrAnonymousLoginIsAllowed() && super.checkPassword(ctx, plainPassword));
    }


    @SLDSafe(portingClass = "UserService", portingMethod = "isAdminGroup(UserModel)")
    @Deprecated(since = "ages", forRemoval = false)
    public boolean isAdmin()
    {
        return UserManager.getInstance().isAdmin(this);
    }


    @SLDSafe
    @Deprecated(since = "ages", forRemoval = false)
    public Boolean isLoginDisabled(SessionContext ctx)
    {
        return super.isLoginDisabled(ctx);
    }


    @SLDSafe(portingClass = "ModifySystemUsersInterceptor", portingMethod = "onValidate(final Object model, final InterceptorContext ctx)")
    public void setLoginDisabled(SessionContext ctx, Boolean disabled)
    {
        super.setLoginDisabled(ctx, disabled);
        if(Boolean.FALSE.equals(disabled) && isAnonymousAndAnonymousLoginIsDisabled())
        {
            LOG.warn("Changing logingDisabled for anonymous account has no effect as long as 'login.anonymous.always.disabled' is set to true. If you really want to enable login for the anonymous account you've got to set it to false!");
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected boolean isAnonymousAndAnonymousLoginIsDisabled()
    {
        return (isAnonymousCustomer() && Config.getBoolean("login.anonymous.always.disabled", true));
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected boolean isNotAnonymousOrAnonymousLoginIsAllowed()
    {
        return (!isAnonymousCustomer() || !Config.getBoolean("login.anonymous.always.disabled", true));
    }
}
