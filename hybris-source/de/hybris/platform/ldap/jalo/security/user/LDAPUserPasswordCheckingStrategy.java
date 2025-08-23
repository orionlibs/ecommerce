package de.hybris.platform.ldap.jalo.security.user;

import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.user.DefaultUserPasswordCheckingStrategy;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.ldap.constants.GeneratedLDAPConstants;
import de.hybris.platform.ldap.jalo.LDAPConfigProxyItem;
import de.hybris.platform.ldap.jalo.LDAPManager;
import org.apache.log4j.Logger;

public class LDAPUserPasswordCheckingStrategy extends DefaultUserPasswordCheckingStrategy
{
    private static final Logger LOG = Logger.getLogger(LDAPUserPasswordCheckingStrategy.class.getName());


    public boolean checkPassword(User user, String plainPassword)
    {
        boolean success = false;
        try
        {
            String login = user.getLogin();
            boolean isLdapAccount = LDAPManager.getInstance().isLdapaccountAsPrimitive(user);
            if(!isLdapAccount || (new LDAPConfigProxyItem()).getLocalAccountsOnlyAsString().indexOf(login.toLowerCase()) != -1)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("LOCAL authentication initiated for login '" + login + "'!");
                }
                success = super.checkPassword(user, plainPassword);
            }
            else
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("LDAP authentication initiated for login '" + login + "' -> '" + user
                                    .getAttribute(GeneratedLDAPConstants.Attributes.User.LDAPLOGIN) + "'!");
                }
                success = LDAPManager.getInstance().checkPassword(user, plainPassword.toCharArray());
            }
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
        return success;
    }
}
