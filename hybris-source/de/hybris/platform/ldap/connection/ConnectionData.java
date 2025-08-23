package de.hybris.platform.ldap.connection;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.ldap.connection.ssl.JNDISocketFactory;
import de.hybris.platform.ldap.jalo.LDAPConfigProxyItem;
import de.hybris.platform.ldap.jalo.LDAPManager;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

public class ConnectionData
{
    private static final Logger LOG = Logger.getLogger(ConnectionData.class.getName());
    private LDAPConfigProxyItem config;
    private String baseDN = "";
    private int version = 3;
    public static final String LDAP = "ldap";
    public static final String DSML = "dsml";
    private String protocol = "ldap";
    private String url = "";
    private String userDN = "";
    private char[] pwd = null;
    private String referralType = "follow";
    private String aliasType = "searching";
    private boolean useSSL = false;
    private String cacerts = "";
    private String clientcerts = "";
    private char[] caKeystorePwd = null;
    private char[] clientKeystorePwd = null;
    private String caKeystoreType = "";
    private String clientKeystoreType = null;
    private String sslSocketFactory = null;
    private boolean tracing = false;
    private boolean sslTracing = false;
    private static final String DEFAULT_CTX = "com.sun.jndi.ldap.LdapCtxFactory";
    private static final String DEFAULT_DSML_CTX = "TODO";
    private boolean useGSSAPI = false;
    private Properties extraProperties = null;


    public ConnectionData()
    {
    }


    public ConnectionData(LDAPConfigProxyItem config) throws NamingException
    {
        this.baseDN = config.getRootDN();
        this.url = config.getProviderURLasString();
        this.userDN = config.getPrincipal();
        this.pwd = config.getCredentials();
        this.tracing = config.getTracing();
        this.cacerts = config.getCaCerts();
        this.clientcerts = config.getClientCerts();
        this
                        .caKeystorePwd = (config.getCaKeyStorePwd() != null) ? config.getCaKeyStorePwd().toCharArray() : null;
        this
                        .clientKeystorePwd = (config.getClientKeyStorePwd() != null) ? config.getClientKeyStorePwd().toCharArray() : null;
        this.caKeystoreType = config.getCaKeyStoreType();
        this.clientKeystoreType = config.getClientKeyStoreType();
        this.version = Integer.parseInt(config.getJNDIVersion().getCode().substring(1, 2));
        if(LOG.isDebugEnabled())
        {
            System.setProperty("javax.net.debug", "ssl handshake verbose");
        }
        if(config.getSecurityProtocol().equalsIgnoreCase("ssl"))
        {
            try
            {
                JNDISocketFactory.init(this.cacerts, this.clientcerts, this.caKeystorePwd, this.clientKeystorePwd, this.caKeystoreType, this.clientKeystoreType);
                if(LOG.isDebugEnabled())
                {
                    JNDISocketFactory.setDebugOn();
                }
                this.sslSocketFactory = config.getSocketFactory();
            }
            catch(Exception e)
            {
                NamingException namingException = new NamingException("error pre-initialising SSL for JNDI connection: " + e.toString());
                namingException.setRootCause(e);
                throw namingException;
            }
        }
        this.config = config;
    }


    public ConnectionData(int version, String url, String userDN, char[] pwd, boolean tracing, String referralType, String aliasType, boolean useSSL, String cacerts, String clientcerts, char[] caKeystorePwd, char[] clientKeystorePwd, String caKeystoreType, String clientKeystoreType,
                    boolean useGSSAPI, Properties extraProperties)
    {
        this.version = version;
        this.url = url;
        this.userDN = userDN;
        this.pwd = pwd;
        this.referralType = referralType;
        this.aliasType = aliasType;
        this.useSSL = useSSL;
        this.cacerts = cacerts;
        this.clientcerts = clientcerts;
        this.caKeystorePwd = caKeystorePwd;
        this.clientKeystorePwd = clientKeystorePwd;
        this.caKeystoreType = caKeystoreType;
        this.clientKeystoreType = clientKeystoreType;
        this.tracing = tracing;
        this.sslTracing = tracing;
        this.useGSSAPI = useGSSAPI;
        this.extraProperties = extraProperties;
    }


    public ConnectionData(int version, String url, String userDN, char[] pwd, boolean tracing, String referralType, String aliasType)
    {
        this.version = version;
        this.url = url;
        this.userDN = userDN;
        this.pwd = pwd;
        this.referralType = referralType;
        this.aliasType = aliasType;
        this.tracing = tracing;
        this.sslTracing = tracing;
    }


    public void setProtocol(String newProtocol)
    {
        if(newProtocol.equalsIgnoreCase("ldap"))
        {
            this.protocol = "ldap";
        }
        else if(newProtocol.equalsIgnoreCase("dsml"))
        {
            this.protocol = "dsml";
        }
        else
        {
            LOG.error("Unknown Protocol " + newProtocol);
        }
    }


    public void clearPasswords()
    {
        if(this.pwd != null)
        {
            Arrays.fill(this.pwd, ' ');
        }
        if(this.caKeystorePwd != null)
        {
            Arrays.fill(this.caKeystorePwd, ' ');
        }
        if(this.clientKeystorePwd != null)
        {
            Arrays.fill(this.clientKeystorePwd, ' ');
        }
        this.pwd = null;
        this.caKeystorePwd = null;
        this.clientKeystorePwd = null;
    }


    public void setURL(String host, int port)
    {
        if(this.protocol.equals("ldap"))
        {
            this.url = "ldap://" + host + ":" + port;
        }
        else if(this.protocol.equals("dsml"))
        {
            this.url = "http://" + host + ":" + port;
        }
    }


    public void setURL(String URL)
    {
        if(this.protocol == "ldap")
        {
            if(URL.toLowerCase(LocaleHelper.getPersistenceLocale()).startsWith("ldap://"))
            {
                this.url = URL;
            }
            else
            {
                this.url = "ldap://" + URL;
            }
        }
        else if(this.protocol == "dsml")
        {
            if(URL.toLowerCase(LocaleHelper.getPersistenceLocale()).startsWith("http://"))
            {
                this.url = URL;
            }
            else if(URL.toLowerCase(LocaleHelper.getPersistenceLocale()).startsWith("dsml://"))
            {
                this.url = "http://" + URL.substring(7);
            }
            else
            {
                this.url = "http://" + URL;
            }
        }
        else if(URL.toLowerCase(LocaleHelper.getPersistenceLocale()).startsWith("ldap:"))
        {
            this.protocol = "ldap";
            this.url = URL;
        }
        else if(URL.toLowerCase(LocaleHelper.getPersistenceLocale()).startsWith("http:"))
        {
            this.protocol = "dsml";
            this.url = URL;
        }
        else if(URL.toLowerCase(LocaleHelper.getPersistenceLocale()).startsWith("dsml:"))
        {
            this.protocol = "dsml";
            this.url = "http:" + URL.substring(5);
        }
    }


    public String getURL()
    {
        return this.url;
    }


    public String getUserDN()
    {
        return this.userDN;
    }


    public String getHost()
    {
        if(this.url == null)
        {
            return null;
        }
        int protocolSeparator = this.url.indexOf("://") + 3;
        int portSeparator = this.url.indexOf(':', protocolSeparator);
        return this.url.substring(protocolSeparator, portSeparator);
    }


    public int getPort()
    {
        if(this.url == null)
        {
            return -1;
        }
        try
        {
            int protocolSeparator = this.url.indexOf("://") + 3;
            int portSeparator = this.url.indexOf(':', protocolSeparator) + 1;
            int serverDetails = this.url.indexOf('/', portSeparator);
            String port = (serverDetails == -1) ? this.url.substring(portSeparator) : this.url.substring(portSeparator, serverDetails);
            int portNumber = Integer.parseInt(port);
            if(portNumber > 65536 || portNumber <= 0)
            {
                return -1;
            }
            return portNumber;
        }
        catch(NumberFormatException nfe)
        {
            return -1;
        }
    }


    public String getRootDN()
    {
        return this.baseDN;
    }


    public String toString()
    {
        return "baseDN: " + this.baseDN + "\nversion: " + Integer.toString(this.version) + "\nurl: " + this.url + "\nuserDN: " + this.userDN + "\nreferralType: " + this.referralType + "\naliasType: " + this.aliasType + "\nuseSSL: " + this.useSSL + "\ncacerts: " + this.cacerts + "\nclientcerts: "
                        + this.clientcerts + "\ncaKeystoreType: " + this.caKeystoreType + "\nclientKeystoreType: " + this.clientKeystoreType + "\ncaKeystorePwd; " + new String(this.caKeystorePwd) + "\nclientKeystorePwd: " + new String(this.clientKeystorePwd) + "\ntracing: " + this.tracing
                        + "\nprotocol: " + this.protocol + "\nsslSocketFactory: " + this.sslSocketFactory + "\nuseGSSAPI: " + this.useGSSAPI;
    }


    public Hashtable getJNDIEnvironment() throws NamingException
    {
        checkData();
        Hashtable<Object, Object> env = new Hashtable<>();
        if(this.protocol == "dsml")
        {
            env.put("java.naming.factory.initial", "TODO");
        }
        else if(this.protocol == "ldap")
        {
            env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        }
        setupBasicProperties(env, this.url, this.tracing, this.referralType, this.aliasType);
        if(this.pwd != null && this.userDN != null)
        {
            setupSimpleSecurityProperties(env, this.userDN, this.pwd);
        }
        if(this.useSSL)
        {
            if(this.tracing)
            {
                this.sslTracing = true;
            }
            setupSSLProperties(env, this.cacerts, this.clientcerts, this.caKeystorePwd, this.clientKeystorePwd, this.caKeystoreType, this.clientKeystoreType, this.sslTracing, this.sslSocketFactory);
        }
        if(this.useGSSAPI)
        {
            env.put("java.naming.security.authentication", "GSSAPI");
        }
        if(this.extraProperties != null && this.extraProperties.size() > 0)
        {
            Enumeration<Object> extraKeys = this.extraProperties.keys();
            while(extraKeys.hasMoreElements())
            {
                Object keyObj = extraKeys.nextElement();
                if(keyObj instanceof String)
                {
                    String key = (String)keyObj;
                    String value = this.extraProperties.getProperty(key);
                    if(value != null)
                    {
                        env.put(key, value);
                    }
                }
            }
        }
        return env;
    }


    public void checkData() throws NamingException
    {
        if(this.url == null)
        {
            throw new NamingException("URL not specified in openContext()!");
        }
        if(this.version < 2 || this.version > 3)
        {
            throw new NamingException("Incorrect ldap Version! (was " + this.version + ")");
        }
        if(this.useSSL && this.cacerts == null)
        {
            throw new NamingException("Cannot use SSL without a trusted CA certificates JKS file.");
        }
        if(this.referralType == null)
        {
            this.referralType = "follow";
        }
        if(this.aliasType == null)
        {
            this.aliasType = "finding";
        }
        if("followthrowignore".indexOf(this.referralType) == -1)
        {
            throw new NamingException("unknown referral type: " + this.referralType + " (setting to 'follow')");
        }
    }


    public void putExtraProperty(String key, String property)
    {
        if(this.extraProperties == null)
        {
            this.extraProperties = new Properties();
        }
        this.extraProperties.put(key, property);
    }


    public static void setupBasicProperties(Hashtable<String, PrintStream> env, String url, boolean tracing, String referralType, String aliasType) throws NamingException
    {
        if(url == null)
        {
            throw new NamingException("URL not specified in openContext()!");
        }
        if(tracing)
        {
            env.put("com.sun.jndi.ldap.trace.ber", System.out);
        }
        env.put("java.naming.ldap.version", "3");
        if(env.get("java.naming.factory.initial") == null)
        {
            env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        }
        env.put("java.naming.ldap.deleteRDN", "false");
        env.put("java.naming.referral", referralType);
        env.put("java.naming.ldap.attributes.binary", "photo jpegphoto jpegPhoto");
        env.put("java.naming.ldap.derefAliases", aliasType);
        env.put("java.naming.security.authentication", "none");
        env.put("java.naming.provider.url", url);
        env.put("com.sun.jndi.ldap.connect.timeout",
                        String.valueOf(LDAPManager.getInstance().getConfig().getJNDIConnectionTimeout()));
    }


    public static void setupSimpleSecurityProperties(Hashtable<String, String> env, String userDN, char[] pwd)
    {
        env.put("java.naming.security.authentication", "simple");
        env.put("java.naming.security.principal", userDN);
        env.put("java.naming.security.credentials", new String(pwd));
    }


    public static void setupSSLProperties(Hashtable<String, String> env, String cacerts, String clientcerts, char[] caKeystorePwd, char[] clientKeystorePwd, String caKeystoreType, String clientKeystoreType, boolean sslTracing, String sslSocketFactory) throws NamingException
    {
        if(cacerts == null)
        {
            throw new NamingException("Cannot use SSL without a trusted CA certificates JKS file.");
        }
        env.put("java.naming.security.protocol", "ssl");
        JNDISocketFactory.init(cacerts, clientcerts, caKeystorePwd, clientKeystorePwd, caKeystoreType, clientKeystoreType);
        env.put("java.naming.ldap.factory.socket", sslSocketFactory);
        if(clientcerts != null && clientKeystorePwd != null && clientKeystorePwd.length > 0)
        {
            env.put("java.naming.security.authentication", "EXTERNAL");
        }
        if(sslTracing)
        {
            System.setProperty("javax.net.debug", "ssl handshake verbose");
        }
    }


    public int getMinFailbackTime()
    {
        return this.config.getMinFailbackTime();
    }


    public int getMaxRetries()
    {
        return this.config.getMaxRetries();
    }


    public int getMaxSize()
    {
        return this.config.getMaxSize();
    }


    public List<Hashtable<String, String>> getLDAPEnvironments()
    {
        return this.config.getServerEnvironments();
    }


    public char[] getCredentials()
    {
        return this.pwd;
    }
}
