package de.hybris.platform.ldap.connection;

import de.hybris.platform.ldap.exception.LDAPUnavailableException;
import java.util.Hashtable;
import java.util.Iterator;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import org.apache.log4j.Logger;

public class JNDIConnectionManager
{
    private static final Logger LOG = Logger.getLogger(JNDIConnectionManager.class.getName());
    private long lastErrorTime;
    private int currentServer;
    private LdapContext currentPool;
    private boolean threadLock;
    private ConnectionData connectionData;


    public JNDIConnectionManager()
    {
        this.lastErrorTime = 0L;
        this.currentServer = 0;
    }


    public JNDIConnectionManager(ConnectionData connectionData)
    {
        this.lastErrorTime = 0L;
        this.currentServer = 0;
        this.connectionData = connectionData;
    }


    public void setConnection(ConnectionData connectionData)
    {
        this.connectionData = connectionData;
    }


    protected void failCurrentServer(Exception reasonForFailure)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("current server fails: " + reasonForFailure.getMessage());
        }
        if(this.threadLock)
        {
            while(this.threadLock)
            {
                pause(100L);
            }
            return;
        }
        this.threadLock = true;
        incrementServer(reasonForFailure);
        this.threadLock = false;
    }


    private synchronized void incrementServer(Exception reasonForFailure)
    {
        pause(1000L);
        LOG.debug("Check to make sure the current server really *is* dead before allowing failover...");
        if(isCurrentServerAvailable())
        {
            return;
        }
        this.lastErrorTime = System.currentTimeMillis();
        LOG.error("communication failure reported with internal server id: " + this.currentServer + "[" + getCurrentServerURL() + "] " + reasonForFailure
                        .getMessage());
        this.currentServer++;
        if(this.currentServer >= this.connectionData.getLDAPEnvironments().size())
        {
            this.currentServer = 0;
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("switching to server with internal id: " + this.currentServer + "[" + getCurrentServerURL() + "]");
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Clearing out the currentPool, so it will be rebuilded as soon as the failed ldap operation is retried...");
        }
        this.currentPool = null;
    }


    public LdapContext getDirContext(JNDIConnectionImpl conn, String connectDN, char[] connectPassword) throws LDAPUnavailableException, NamingException
    {
        LdapContext context;
        if(this.currentPool == null)
        {
            rebuildConnectionPool();
        }
        if(conn.sendingEmptyBaseDNsearchQueries())
        {
            context = (LdapContext)this.currentPool.lookup("");
        }
        else
        {
            context = (LdapContext)this.currentPool.lookup(this.connectionData.getRootDN());
        }
        context.addToEnvironment("java.naming.security.principal", connectDN);
        context.addToEnvironment("java.naming.security.credentials", new String(connectPassword));
        return context;
    }


    private synchronized void rebuildConnectionPool()
    {
        LdapContext newDirContext = null;
        int count = 1;
        int max = this.connectionData.getMaxRetries();
        while(count <= max)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("rebuilding connection pool [" + count + "/" + max + "]");
            }
            try
            {
                newDirContext = new InitialLdapContext(getCurrentEnvironment(), null);
                this.currentPool = newDirContext;
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("switching to server: " + getCurrentServerURL());
                }
                return;
            }
            catch(NamingException e)
            {
                StringBuilder logMsg = new StringBuilder();
                logMsg.append("unable to bind to ");
                logMsg.append(getCurrentServerURL());
                logMsg.append(" reason: ");
                if(e instanceof javax.naming.CommunicationException)
                {
                    logMsg.append("CommunicationException (" + e.getMessage() + ")");
                }
                else
                {
                    logMsg.append(e.getMessage());
                }
                if(checkIfExceptionIsFailable(e))
                {
                    LOG.error(logMsg);
                    failCurrentServer(e);
                }
                else
                {
                    LOG.fatal(logMsg, e);
                }
                count++;
            }
        }
    }


    private Hashtable getCurrentEnvironment()
    {
        return this.connectionData.getLDAPEnvironments().get(this.currentServer);
    }


    public final void dumpConfiguration()
    {
        dumpConfiguration(this.connectionData.getLDAPEnvironments().get(this.currentServer));
    }


    protected final void dumpConfiguration(Hashtable<String, String> environment)
    {
        if(LOG.isDebugEnabled())
        {
            for(Iterator<String> it = environment.keySet().iterator(); it.hasNext(); )
            {
                String key = it.next();
                if(!"java.naming.security.credentials".equals(key))
                {
                    LOG.debug(key + " -> " + key);
                }
            }
        }
    }


    public String getServerURL()
    {
        return getCurrentServerURL();
    }


    private String getCurrentServerURL()
    {
        Hashtable env = getCurrentEnvironment();
        String serverURL = (String)env.get("java.naming.provider.url");
        return serverURL;
    }


    public void checkForFailBack()
    {
        if(this.currentServer == 0)
        {
            return;
        }
        if(System.currentTimeMillis() - this.lastErrorTime < this.connectionData.getMinFailbackTime())
        {
            return;
        }
        LOG.error("Setting current server to first server in list for retry cycle.");
        this.currentServer = 0;
        this.currentPool = null;
    }


    private static void pause(long time)
    {
        long startTime = System.currentTimeMillis();
        do
        {
            try
            {
                long sleepTime = time - System.currentTimeMillis() - startTime;
                Thread.sleep((sleepTime > 0L) ? sleepTime : 1000L);
            }
            catch(InterruptedException interruptedException)
            {
            }
        }
        while(System.currentTimeMillis() - startTime < time);
    }


    public boolean isCurrentServerAvailable()
    {
        try
        {
            DirContext ldapConnection = (DirContext)this.currentPool.lookup(this.connectionData.getRootDN());
            ldapConnection.addToEnvironment("java.naming.security.principal", "");
            ldapConnection.addToEnvironment("java.naming.security.credentials", "");
        }
        catch(NullPointerException e)
        {
            LOG.error(e.toString());
            return false;
        }
        catch(NamingException e)
        {
            boolean isCommFailure = checkIfExceptionIsFailable(e);
            if(isCommFailure)
            {
                return false;
            }
        }
        return true;
    }


    protected static boolean checkIfExceptionIsFailable(NamingException exception)
    {
        if(exception instanceof javax.naming.CommunicationException)
        {
            return true;
        }
        if(exception.getMessage().indexOf("ds locked (-663)") != -1)
        {
            return true;
        }
        return false;
    }
}
