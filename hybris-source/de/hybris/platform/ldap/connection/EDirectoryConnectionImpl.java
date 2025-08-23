package de.hybris.platform.ldap.connection;

import de.hybris.platform.ldap.exception.LDAPUnavailableException;
import org.apache.log4j.Logger;

public class EDirectoryConnectionImpl extends JNDIConnectionImpl
{
    private static final Logger LOG = Logger.getLogger(EDirectoryConnectionImpl.class.getName());


    EDirectoryConnectionImpl(ConnectionData connectionData) throws LDAPUnavailableException
    {
        super(connectionData);
    }


    public boolean sendingEmptyBaseDNsearchQueries()
    {
        return false;
    }
}
