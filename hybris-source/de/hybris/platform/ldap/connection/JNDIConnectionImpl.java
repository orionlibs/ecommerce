package de.hybris.platform.ldap.connection;

import com.sun.jndi.ldap.LdapCtxFactory;
import de.hybris.platform.ldap.constants.LDAPConstants;
import de.hybris.platform.ldap.exception.LDAPOperationException;
import de.hybris.platform.ldap.exception.LDAPUnavailableException;
import de.hybris.platform.ldap.jalo.LDAPManager;
import de.hybris.platform.util.Config;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import javax.naming.CompositeName;
import javax.naming.CompoundName;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.ReferralException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class JNDIConnectionImpl implements LDAPConnection
{
    private static final Logger LOG = Logger.getLogger(JNDIConnectionImpl.class.getName());
    private final String principal;
    private final char[] credentials;
    protected int maxRetries;
    protected LdapContext ldapConnection;
    protected final JNDIConnectionManager connectionManager = new JNDIConnectionManager();
    protected final ConnectionData connectionData;
    protected static final String LDAP_RETRY_ERROR = "LDAP pooling failure: Max retries exceeded";


    JNDIConnectionImpl(ConnectionData connectionData) throws LDAPUnavailableException
    {
        this.connectionData = connectionData;
        if(connectionData == null)
        {
            throw new IllegalArgumentException("ConnectionData cannot be null");
        }
        this.principal = connectionData.getUserDN();
        this.credentials = connectionData.getCredentials();
        if(this.principal == null)
        {
            throw new IllegalArgumentException("principal cannot be null");
        }
        if(this.credentials == null)
        {
            throw new IllegalArgumentException("credentials cannot be null");
        }
        this.connectionManager.setConnection(connectionData);
        this.maxRetries = connectionData.getMaxRetries();
        this.connectionManager.dumpConfiguration();
        this.connectionManager.checkForFailBack();
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Establishing LDAP connection for principal '" + this.principal + "'");
            }
            this.ldapConnection = this.connectionManager.getDirContext(this, this.principal, this.credentials);
        }
        catch(NamingException e)
        {
            LOG.error(e.getMessage(), e);
        }
    }


    public boolean checkPassword(String searchbase, String login, char[] plainPassword)
    {
        searchbase = LDAPManager.getInstance().cleanse(searchbase);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("checkPassword( " + searchbase + ", " + login + ", ... )");
            this.connectionManager.dumpConfiguration();
        }
        InitialLdapContext ctx = null;
        Hashtable<String, String> env = null;
        boolean success = false;
        if(plainPassword.length == 0)
        {
            LOG.warn("Empty password submitted, which is not supported!");
            return false;
        }
        StringBuilder principal = new StringBuilder(login);
        try
        {
            env = this.connectionData.getJNDIEnvironment();
            env.put("java.naming.factory.initial", LdapCtxFactory.class.getCanonicalName());
            env.put("java.naming.security.principal", principal.toString());
            env.put("java.naming.security.credentials", new String(plainPassword));
            env.put("java.naming.security.authentication",
                            Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.JNDIAUTHENTICATION));
            if(StringUtils.isNotEmpty(Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.SECURITYPROTOCOL)))
            {
                env.put("java.naming.security.protocol",
                                Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.SECURITYPROTOCOL));
                env.put("java.naming.provider.url", this.connectionManager.getServerURL().replace("ldap:", "ldaps:"));
            }
            else
            {
                env.put("java.naming.provider.url", this.connectionManager.getServerURL());
            }
            ctx = new InitialLdapContext(env, null);
            success = true;
        }
        catch(NamingException e)
        {
            LOG.error("Authentication failed!");
            LOG.error("java.naming.provider.url: '" + this.connectionManager.getServerURL() + "'");
            LOG.error("java.naming.security.principal: '" + principal.toString() + "'");
            LOG.error(e.getMessage(), e);
        }
        finally
        {
            wipePassword(plainPassword, env);
            try
            {
                if(ctx != null)
                {
                    ctx.close();
                }
            }
            catch(NamingException e)
            {
                LOG.error("close() failed: Reason:" + e.toString(), e);
                this.connectionManager.dumpConfiguration();
            }
        }
        return success;
    }


    protected String appendRootDN(String base)
    {
        String currentBase = (base != null) ? base.trim() : "";
        String rootDN = this.connectionData.getRootDN();
        if(StringUtils.isNotEmpty(rootDN) && !StringUtils.endsWithIgnoreCase(currentBase, rootDN))
        {
            return currentBase + "," + currentBase;
        }
        return currentBase;
    }


    protected void wipePassword(char[] password, Hashtable<String, String> env)
    {
        for(int i = 0; i < password.length; i++)
        {
            password[i] = ' ';
        }
        if(env != null)
        {
            env.put("java.naming.security.credentials", new String(password));
        }
    }


    public void close()
    {
        try
        {
            if(this.ldapConnection != null)
            {
                this.ldapConnection.close();
            }
        }
        catch(NamingException e)
        {
            LOG.error("close() failed: Reason:" + e.toString(), e);
        }
    }


    protected LdapContext retryConnection(Exception reasonForRetry) throws LDAPUnavailableException, NamingException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("retry connection because of: " + reasonForRetry.getMessage());
        }
        this.connectionManager.failCurrentServer(reasonForRetry);
        this.ldapConnection = this.connectionManager.getDirContext(this, this.principal, this.credentials);
        return this.ldapConnection;
    }


    public LdapContext getCurrentInitialLdapContext()
    {
        return this.ldapConnection;
    }


    public Collection<LDAPGenericObject> searchOneLevel(String searchbase, String filter, int limit, int timeout) throws LDAPOperationException, LDAPUnavailableException, NamingException
    {
        return searchOneLevel(searchbase, filter, limit, timeout, new String[] {"1.1"});
    }


    public Collection<LDAPGenericObject> searchOneLevel(String searchbase, String filter, int limit, int timeout, String[] returnAttributes) throws LDAPOperationException, LDAPUnavailableException, NamingException
    {
        return rawSearchOneLevel(searchbase, filter, limit, timeout, returnAttributes);
    }


    protected Collection<LDAPGenericObject> rawSearchOneLevel(String searchbase, String filter, int limit, int timeout, String[] returnAttributes) throws LDAPOperationException, LDAPUnavailableException, NamingException
    {
        searchbase = appendRootDN(searchbase);
        searchbase = LDAPManager.getInstance().cleanse(searchbase);
        int count = 0;
        while(count < this.maxRetries)
        {
            try
            {
                SearchControls constraints = new SearchControls();
                constraints.setSearchScope(1);
                if(limit > -1)
                {
                    constraints.setCountLimit(limit);
                }
                if(timeout > -1)
                {
                    constraints.setTimeLimit(timeout);
                }
                constraints.setReturningAttributes(returnAttributes);
                NamingEnumeration<SearchResult> namingEnumeration = this.ldapConnection.search(searchbase, filter, constraints);
                List<LDAPGenericObject> result = new ArrayList();
                while(namingEnumeration.hasMoreElements())
                {
                    SearchResult searchResult = namingEnumeration.nextElement();
                    LDAPGenericObject genericObject = new LDAPGenericObject((LdapContext)searchResult.getObject(), searchResult.getName(), searchbase);
                    genericObject.setAttributes(searchResult.getAttributes());
                    result.add(genericObject);
                }
                return result;
            }
            catch(NamingException e)
            {
                if(JNDIConnectionManager.checkIfExceptionIsFailable(e))
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(" rawSearchOneLevel calling retryConnection()");
                    }
                    this.ldapConnection = retryConnection(e);
                }
                else
                {
                    LOG.warn("Operation failed: " + e.toString());
                    throw LDAPOperationException.createLDAPException(e.getMessage());
                }
                count++;
            }
        }
        throw new LDAPUnavailableException("LDAP pooling failure: Max retries exceeded");
    }


    public Collection<LDAPGenericObject> searchSubTree(String searchbase, String filter, int limit, int timeout) throws LDAPOperationException, LDAPUnavailableException, NamingException
    {
        return searchSubTree(searchbase, filter, limit, timeout, new String[] {"1.1"});
    }


    public Collection<LDAPGenericObject> searchSubTree(String searchbase, String filter, int limit, int timeout, String[] returnAttributes) throws LDAPOperationException, LDAPUnavailableException, NamingException
    {
        return rawSearchSubTree(searchbase, filter, limit, timeout, returnAttributes);
    }


    protected Collection<LDAPGenericObject> rawSearchSubTree(String searchbase, String filter, int limit, int timeout, String[] returnAttributes) throws LDAPOperationException, LDAPUnavailableException, NamingException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("OpenLDAP & Co. - rawSearchSubTree( " + searchbase + "," + filter + "," + limit + "," + timeout + ",<returnAttributes>) ");
        }
        searchbase = appendRootDN(searchbase);
        searchbase = LDAPManager.getInstance().cleanse(searchbase);
        if(returnAttributes == null || returnAttributes.length == 0)
        {
            returnAttributes = new String[] {"objectClass"};
        }
        int count = 0;
        while(count < this.maxRetries)
        {
            try
            {
                SearchControls constraints = new SearchControls();
                constraints.setSearchScope(2);
                constraints.setReturningAttributes(returnAttributes);
                if(limit > -1)
                {
                    constraints.setCountLimit(limit);
                }
                if(timeout > -1)
                {
                    constraints.setTimeLimit(timeout);
                }
                List<LDAPGenericObject> result = new ArrayList<>();
                boolean followReferral = true;
                boolean moreReferrals = true;
                int watchdog = 0;
                while(moreReferrals)
                {
                    watchdog++;
                    try
                    {
                        if(watchdog > 3)
                        {
                            LOG.info("Search '" + filter + "' aborted! (forced by 'watchdog timer')");
                            return result;
                        }
                        NamingEnumeration<SearchResult> namingEnumeration = this.ldapConnection.search(searchbase, filter, constraints);
                        while(namingEnumeration.hasMoreElements())
                        {
                            moreReferrals = false;
                            SearchResult searchResult = namingEnumeration.nextElement();
                            LDAPGenericObject genericObject = new LDAPGenericObject((LdapContext)searchResult.getObject(), searchResult.getName(), searchbase);
                            genericObject.setAttributes(searchResult.getAttributes());
                            result.add(genericObject);
                        }
                    }
                    catch(ReferralException e)
                    {
                        moreReferrals = e.skipReferral();
                        if(moreReferrals)
                        {
                            e.getReferralContext();
                        }
                    }
                }
                return result;
            }
            catch(NamingException e)
            {
                if(JNDIConnectionManager.checkIfExceptionIsFailable(e))
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(" rawSearchSubTree calling retryConnection()");
                    }
                    this.ldapConnection = retryConnection(e);
                }
                else
                {
                    LOG.warn("Operation failed: " + e.toString());
                    throw LDAPOperationException.createLDAPException(e.getMessage());
                }
            }
            catch(NullPointerException e)
            {
                LOG.warn(e.getMessage(), e);
                return null;
            }
            count++;
        }
        throw new LDAPUnavailableException("LDAP pooling failure: Max retries exceeded");
    }


    public Collection searchBaseEntry(String searchbase, String filter, int limit, int timeout) throws LDAPOperationException, LDAPUnavailableException, NamingException
    {
        return rawSearchBaseEntry(searchbase, filter, limit, timeout, new String[] {"objectClass"});
    }


    public Collection searchBaseEntry(String searchbase, String filter, int limit, int timeout, String[] returnAttributes) throws LDAPOperationException, LDAPUnavailableException, NamingException
    {
        return rawSearchBaseEntry(searchbase, filter, limit, timeout, returnAttributes);
    }


    protected Collection rawSearchBaseEntry(String searchbase, String filter, int limit, int timeout, String[] returnAttributes) throws LDAPOperationException, LDAPUnavailableException, NamingException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("rawSearchBaseEntry( " + searchbase + "," + filter + "," + limit + "," + timeout + "," + returnAttributes + ") ");
        }
        searchbase = appendRootDN(searchbase);
        searchbase = LDAPManager.getInstance().cleanse(searchbase);
        if(returnAttributes != null && returnAttributes.length == 0)
        {
            returnAttributes = new String[] {"objectClass"};
        }
        int count = 0;
        while(count < this.maxRetries)
        {
            try
            {
                SearchControls constraints = new SearchControls();
                constraints.setSearchScope(0);
                constraints.setCountLimit(limit);
                constraints.setTimeLimit(timeout);
                constraints.setReturningAttributes(returnAttributes);
                NamingEnumeration<SearchResult> namingEnumeration = this.ldapConnection.search(searchbase, filter, constraints);
                List<LDAPGenericObject> result = new ArrayList();
                while(namingEnumeration.hasMoreElements())
                {
                    SearchResult searchResult = namingEnumeration.nextElement();
                    LDAPGenericObject genericObject = new LDAPGenericObject((LdapContext)searchResult.getObject(), searchResult.getName(), searchbase);
                    genericObject.setAttributes(searchResult.getAttributes());
                    result.add(genericObject);
                }
                return result;
            }
            catch(NamingException e)
            {
                if(JNDIConnectionManager.checkIfExceptionIsFailable(e))
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(" rawSearchBaseEntry calling retryConnection()");
                    }
                    this.ldapConnection = retryConnection(e);
                }
                else
                {
                    LOG.warn("Operation failed: " + e.toString());
                    throw LDAPOperationException.createLDAPException(e.getMessage());
                }
            }
            catch(NullPointerException e)
            {
                LOG.warn(e.getMessage(), e);
                return null;
            }
            count++;
        }
        throw new LDAPUnavailableException("LDAP pooling failure: Max retries exceeded");
    }


    public synchronized Attributes read(String distinguishedName) throws LDAPOperationException, LDAPUnavailableException, NamingException
    {
        return read(distinguishedName, null);
    }


    public Attributes read(String distinguishedName, String[] returnAttributes) throws LDAPOperationException, LDAPUnavailableException, NamingException
    {
        if(distinguishedName == null)
        {
            throw new NullPointerException("dn must not be null");
        }
        if(returnAttributes == null)
        {
            throw new NullPointerException("attributes must not be null");
        }
        int count = 0;
        while(count < this.maxRetries)
        {
            try
            {
                return this.ldapConnection.getAttributes(distinguishedName, returnAttributes);
            }
            catch(NamingException e)
            {
                if(JNDIConnectionManager.checkIfExceptionIsFailable(e))
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(" read calling retryConnection()");
                    }
                    this.ldapConnection = retryConnection(e);
                }
                else
                {
                    LOG.warn("Operation failed: " + e.toString());
                    throw LDAPOperationException.createLDAPException(e.getMessage());
                }
            }
            catch(NullPointerException e)
            {
                LOG.error(e.getMessage(), e);
                return null;
            }
            count++;
        }
        throw new LDAPUnavailableException("LDAP pooling failure: Max retries exceeded");
    }


    public Collection<LDAPGenericObject> list(String searchbase) throws LDAPOperationException, LDAPUnavailableException, NamingException
    {
        return rawSearchOneLevel(searchbase, "(objectclass=*)", 0, 0, new String[] {"1.1"});
    }


    private static Properties nameParserSyntax = null;


    private static void setupLDAPSyntax()
    {
        nameParserSyntax = new Properties();
        nameParserSyntax.put("jndi.syntax.direction", "right_to_left");
        nameParserSyntax.put("jndi.syntax.separator", ",");
        nameParserSyntax.put("jndi.syntax.escape", "\\");
        nameParserSyntax.put("jndi.syntax.trimblanks", "true");
        nameParserSyntax.put("jndi.syntax.separator.typeval", "=");
    }


    public static Name getNameFromString(String iDN) throws NamingException
    {
        String distinguishedName = iDN;
        Name CompositeFormDN = null;
        CompoundName CompoundFormDN = null;
        if(iDN.indexOf("ldap://") != -1)
        {
            CompositeFormDN = new CompositeName(iDN);
            if(CompositeFormDN.size() != 0)
            {
                distinguishedName = CompositeFormDN.get(CompositeFormDN.size() - 1);
            }
        }
        if(nameParserSyntax == null)
        {
            setupLDAPSyntax();
        }
        CompoundFormDN = new CompoundName(distinguishedName, nameParserSyntax);
        return CompoundFormDN;
    }


    public static Name getNameFromSearchResult(SearchResult iDirectoryEntry, Name iBaseDN) throws InvalidNameException, NamingException
    {
        String RDN = applyJNDIRDNBugWorkAround(iDirectoryEntry.getName());
        Name JNDIRDN = getNameFromString(RDN);
        if(iDirectoryEntry.isRelative())
        {
            JNDIRDN.addAll(0, iBaseDN);
        }
        return JNDIRDN;
    }


    private static String applyJNDIRDNBugWorkAround(String iRDN)
    {
        String ReturnString;
        int SlashPos = iRDN.lastIndexOf("\\\\");
        if(SlashPos == iRDN.length() - 2)
        {
            ReturnString = iRDN.substring(0, SlashPos);
        }
        else
        {
            ReturnString = iRDN;
        }
        return ReturnString;
    }


    public void changePassword(DirContext ctx, String argRDN, String oldPassword, String newPassword) throws NamingException
    {
        throw new UnsupportedOperationException("Password chnaging not implemented, yet!");
    }


    protected byte[] encodePassword(String pass) throws UnsupportedEncodingException
    {
        String ATT_ENCODING = "Unicode";
        String pwd = "\"" + pass + "\"";
        byte[] _bytes = pwd.getBytes("Unicode");
        byte[] bytes = new byte[_bytes.length - 2];
        System.arraycopy(_bytes, 2, bytes, 0, _bytes.length - 2);
        return Base64.getEncoder().encode(bytes);
    }


    public JNDIConnectionManager getConnectionManager()
    {
        return this.connectionManager;
    }


    public boolean sendingEmptyBaseDNsearchQueries()
    {
        return true;
    }
}
