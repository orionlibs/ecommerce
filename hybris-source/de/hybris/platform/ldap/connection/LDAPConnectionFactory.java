package de.hybris.platform.ldap.connection;

import de.hybris.platform.ldap.constants.LDAPConstants;
import de.hybris.platform.ldap.exception.LDAPUnavailableException;
import de.hybris.platform.util.Config;
import java.lang.reflect.Constructor;

public final class LDAPConnectionFactory
{
    public static LDAPConnection getLDAPConnection(ConnectionData connectiondata) throws LDAPUnavailableException
    {
        LDAPConnection connection = null;
        String config = Config.getParameter(LDAPConstants.SERVER.PROPERTY_NAME);
        if(config == null)
        {
            throw new IllegalStateException("missing property: " + LDAPConstants.SERVER.PROPERTY_NAME);
        }
        if(config.equalsIgnoreCase(LDAPConstants.SERVER.LDAP))
        {
            JNDIConnectionImpl jNDIConnectionImpl = new JNDIConnectionImpl(connectiondata);
        }
        else if(config.equalsIgnoreCase(LDAPConstants.SERVER.ACTIVEDIRECTORY))
        {
            ActiveDirectoryConnectionImpl activeDirectoryConnectionImpl = new ActiveDirectoryConnectionImpl(connectiondata);
        }
        else if(config.equalsIgnoreCase(LDAPConstants.SERVER.EDIRECTORY))
        {
            EDirectoryConnectionImpl eDirectoryConnectionImpl = new EDirectoryConnectionImpl(connectiondata);
        }
        else
        {
            try
            {
                Class<?> clazz = Class.forName(config);
                Constructor<?> con = clazz.getDeclaredConstructor(new Class[] {ConnectionData.class});
                connection = (LDAPConnection)con.newInstance(new Object[] {connectiondata});
            }
            catch(Exception e)
            {
                throw new IllegalStateException("Unsupported LDAP Server!", e);
            }
        }
        return connection;
    }
}
