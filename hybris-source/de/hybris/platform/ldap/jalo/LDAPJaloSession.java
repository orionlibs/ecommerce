package de.hybris.platform.ldap.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.PasswordCheckingStrategy;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.ldap.jalo.security.user.LDAPUserPasswordCheckingStrategy;

public class LDAPJaloSession extends JaloSession
{
    static
    {
        UserManager.getInstance().setPasswordCheckingStrategy((PasswordCheckingStrategy)new LDAPUserPasswordCheckingStrategy());
    }
}
