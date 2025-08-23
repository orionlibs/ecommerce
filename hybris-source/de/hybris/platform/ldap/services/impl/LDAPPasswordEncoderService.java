package de.hybris.platform.ldap.services.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ldap.connection.ConnectionData;
import de.hybris.platform.ldap.connection.LDAPConnection;
import de.hybris.platform.ldap.connection.LDAPConnectionFactory;
import de.hybris.platform.ldap.exception.LDAPUnavailableException;
import de.hybris.platform.ldap.jalo.LDAPConfigProxyItem;
import de.hybris.platform.ldap.jalo.LDAPManager;
import de.hybris.platform.servicelayer.user.impl.DefaulPasswordEncoderService;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

public class LDAPPasswordEncoderService extends DefaulPasswordEncoderService
{
    private static final Logger LOG = Logger.getLogger(LDAPPasswordEncoderService.class);


    public boolean isValid(UserModel user, String plainPassword)
    {
        boolean success = false;
        String login = user.getUid();
        boolean isLdapAccount = (user.getLdapaccount() != null && user.getLdapaccount().booleanValue());
        if(!isLdapAccount || (new LDAPConfigProxyItem()).getLocalAccountsOnlyAsString().indexOf(login.toLowerCase()) != -1)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("LOCAL authentication initiated for login '" + login + "'!");
            }
            success = super.isValid(user, plainPassword);
        }
        else
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("LDAP authentication initiated for login '" + login + "' -> '" + user.getLdaplogin() + "'!");
            }
            try
            {
                success = checkPassword(user, plainPassword.toCharArray());
            }
            catch(NamingException e)
            {
                LOG.error("login: '" + login + "' failed!");
                LOG.error(e.getMessage());
                return false;
            }
            catch(LDAPUnavailableException e)
            {
                LOG.error("login: '" + login + "' failed!");
                LOG.error(e.getMessage());
                return false;
            }
        }
        return success;
    }


    private boolean checkPassword(UserModel user, char[] plainPassword) throws NamingException, LDAPUnavailableException
    {
        if(user == null)
        {
            return false;
        }
        String searchbase = user.getDN();
        String login = (user.getLdaplogin() != null) ? user.getLdaplogin() : user.getUid();
        String domain = user.getDomain();
        if(domain != null && domain.length() > 0)
        {
            login = domain + "\\" + domain;
        }
        return checkPassword(searchbase, login, plainPassword);
    }


    private boolean checkPassword(String searchbase, String login, char[] plainPassword) throws NamingException, LDAPUnavailableException
    {
        LDAPConnection con = LDAPConnectionFactory.getLDAPConnection(new ConnectionData(LDAPManager.getInstance().getConfig()));
        return con.checkPassword(searchbase, login, plainPassword);
    }
}
