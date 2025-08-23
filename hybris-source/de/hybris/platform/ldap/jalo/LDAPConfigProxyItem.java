package de.hybris.platform.ldap.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.config.ConfigProxyItem;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.AttributeAccess;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.ldap.constants.GeneratedLDAPConstants;
import de.hybris.platform.ldap.constants.LDAPConstants;
import de.hybris.platform.ldap.exception.LDAPUnavailableException;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

public class LDAPConfigProxyItem extends ConfigProxyItem
{
    private static final int maxRetries = 10;
    private static List<Hashtable<String, String>> environments;
    private static final Logger log = Logger.getLogger(LDAPConfigProxyItem.class.getName());
    public static final String DELIMITER = ";";
    private List<String> providerlist = null;

    static
    {
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.LOGINFIELD, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.JNDIAUTHENTICATION, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.LOCALACCOUNTSONLY, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.JNDICREDENTIALS, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.JNDIFACTORY, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.SOCKETFACTORY, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.JNDIPRINCIPALS, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.JNDIVERSION, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.SECURITYPROTOCOL, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.SERVERROOTDN, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.POOLENABLED, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.POOLINITSIZE, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.POOLMAXSIZE, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.POOLPREFSIZE, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.POOLTIMEOUT, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.MINIMUMFAILBACKTIME, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.SERVERURL, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.CACERTS, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.CAKEYSTOREPWD, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.CAKEYSTORETYPE, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.CLIENTCERTS, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.CLIENTKEYSTOREPWD, (AttributeAccess)new Object());
        registerAccessFor(ConfigProxyItem.class, GeneratedLDAPConstants.Attributes.LDAPConfigProxyItem.CLIENTKEYSTORETYPE, (AttributeAccess)new Object());
    }

    public List<String> getProviderURLasList()
    {
        if(this.providerlist == null)
        {
            this.providerlist = new ArrayList<>();
            StringTokenizer tokenizer = new StringTokenizer(getProviderURLasString(), ";");
            while(tokenizer.hasMoreTokens())
            {
                String entry = tokenizer.nextToken().trim();
                this.providerlist.add(entry.startsWith("ldap://") ? entry : ("ldap://" + entry));
            }
        }
        return this.providerlist;
    }


    public String getProviderURLasString()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.SERVERURL);
        return (value != null) ? value : LDAPConstants.CONFIG.DEFAULT.SERVERURL;
    }


    public void setProviderURL(List<String> value)
    {
        StringBuilder serverlist = new StringBuilder();
        for(int ix = 0; ix < value.size(); ix++)
        {
            String entry = ((String)value.get(ix)).toLowerCase();
            serverlist.append(entry);
            if(ix < value.size() - 1)
            {
                serverlist.append(";");
            }
        }
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.SERVERURL, serverlist.toString());
    }


    public List getLocalAccountsOnlyAsList()
    {
        List<User> accounts = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(getLocalAccountsOnlyAsString(), ";");
        while(tokenizer.hasMoreTokens())
        {
            User user = UserManager.getInstance().getUserByLogin(tokenizer.nextToken().trim());
            accounts.add(user);
        }
        return accounts;
    }


    public String getLocalAccountsOnlyAsString()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.LOCALACCOUNTSONLY);
        return (value != null) ? value : "";
    }


    public void setLocalAccountsOnly(List<User> value)
    {
        StringBuilder accounts = new StringBuilder();
        for(int ix = 0; ix < value.size(); ix++)
        {
            String entry = ((User)value.get(ix)).getLogin().toLowerCase();
            accounts.append(entry);
            if(ix < value.size() - 1)
            {
                accounts.append(";");
            }
        }
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.LOCALACCOUNTSONLY, accounts.toString());
    }


    public boolean isHybrisPoolingDisabled()
    {
        return Registry.getMasterTenant().getConfig()
                        .getBoolean(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.HYBIRSPOOLDISABLED, true);
    }


    public List<Hashtable<String, String>> getServerEnvironments()
    {
        if(log.isDebugEnabled())
        {
            log.debug("HYBRIS JNDI POOLING DISBALED: " + isHybrisPoolingDisabled());
        }
        if(isHybrisPoolingDisabled() || environments == null ||
                        Registry.getMasterTenant()
                                        .getConfig()
                                        .getBoolean("ldap.force.pool.rebuilding", false))
        {
            environments = new ArrayList<>();
            for(int i = 0; i < getProviderURLasList().size(); i++)
            {
                Hashtable<String, String> jndiBaseConfig = getJNDIEnvironment();
                if(log.isDebugEnabled())
                {
                    log.debug("server entry nr." + i + " " + (String)getProviderURLasList().get(i));
                }
                jndiBaseConfig.put("java.naming.provider.url", getProviderURLasList().get(i));
                environments.add(jndiBaseConfig);
            }
        }
        return environments;
    }


    private void dumpConfiguration()
    {
        if(log.isDebugEnabled())
        {
            for(int i = 0; i < getProviderURLasList().size(); i++)
            {
                log.debug("Server: " + (String)getProviderURLasList().get(i) + " [" + i + "]");
                Hashtable<String, String> config = environments.get(i);
                dumpConfiguration(config);
                log.debug("------------------------------------------");
            }
        }
    }


    private void dumpConfiguration(Hashtable<String, String> table)
    {
        if(log.isDebugEnabled())
        {
            for(Iterator<String> it2 = table.keySet().iterator(); it2.hasNext(); )
            {
                String key = it2.next();
                log.debug("key:  " + key);
                if(!"java.naming.security.credentials".equals(key))
                {
                    log.debug(key + " -> " + key);
                }
            }
        }
    }


    public int getMaxRetries()
    {
        return 10;
    }


    public int getMinFailbackTime()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.MINIMUMFAILBACKTIME);
        return Integer.parseInt(value);
    }


    public void setMinFailbackTime(Integer minFailbackTime)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.MINIMUMFAILBACKTIME, minFailbackTime.toString());
    }


    public String getRootDN()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.SERVERROOTDN);
        return (value != null) ? value : LDAPConstants.CONFIG.DEFAULT.ROOTDN;
    }


    public void setRootDN(String rootDN)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.SERVERROOTDN,
                        (rootDN != null) ? rootDN : LDAPConstants.CONFIG.DEFAULT.ROOTDN);
    }


    public void setInitSize(Integer value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.POOLINITSIZE,
                        (value != null) ? value.toString() : String.valueOf(1));
    }


    public int getInitSize()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.POOLINITSIZE);
        return Integer.parseInt(value);
    }


    public int getPoolTimeOut()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.POOLTIMEOUT);
        return Integer.parseInt(value);
    }


    public void setPoolTimeOut(Integer value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.POOLTIMEOUT,
                        (value != null) ? value.toString() : String.valueOf(5000));
    }


    public void setMaxSize(Integer value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.POOLMAXSIZE,
                        (value != null) ? value.toString() : String.valueOf(50));
    }


    public int getMaxSize()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.POOLMAXSIZE);
        return Integer.parseInt(value);
    }


    public void setPrefSize(Integer value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.POOLPREFSIZE,
                        (value != null) ? value.toString() : String.valueOf(10));
    }


    public int getPrefSize()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.POOLPREFSIZE);
        return Integer.parseInt(value);
    }


    public void setUsePooling(boolean value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.POOLENABLED, value ? "true" : "false");
    }


    public boolean getUsePooling()
    {
        return true;
    }


    public void setAuthentication(EnumerationValue value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.JNDIAUTHENTICATION, value.getCode());
    }


    public EnumerationValue getAuthentication()
    {
        EnumerationType type = EnumerationManager.getInstance().getEnumerationType(GeneratedLDAPConstants.TC.JNDIAUTHENTICATIONENUM);
        return
                        EnumerationManager.getInstance().getEnumerationValue(type, LDAPConstants.CONFIG.DEFAULT.AUTHENTICATION);
    }


    public void setLoginField(String value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.LOGINFIELD,
                        (value != null) ? value : LDAPConstants.CONFIG.DEFAULT.LOGINFIELD);
    }


    public String getLoginField()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.LOGINFIELD);
        return (value != null) ? value : LDAPConstants.CONFIG.DEFAULT.LOGINFIELD;
    }


    public void setCredentials(char[] value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.JNDICREDENTIALS, new String(value));
    }


    public char[] getCredentials()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.JNDICREDENTIALS);
        return (value != null) ? value.toCharArray() : LDAPConstants.CONFIG.DEFAULT.CREDENTIALS.toCharArray();
    }


    public void setFactory(String value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.JNDIFACTORY,
                        (value != null) ? value : LDAPConstants.CONFIG.DEFAULT.FACTORY);
    }


    public boolean getTracing()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.TRACING);
        return (value != null) ? Boolean.parseBoolean(value) : false;
    }


    public String getFactory()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.JNDIFACTORY);
        return (value != null) ? value : LDAPConstants.CONFIG.DEFAULT.FACTORY;
    }


    public void setSocketFactory(String value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.SOCKETFACTORY, (value != null) ? value :
                        LDAPConstants.CONFIG.DEFAULT.SOCKETFACTORY);
    }


    public String getSocketFactory()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.SOCKETFACTORY);
        return value;
    }


    public int getJNDIConnectionTimeout()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.JNDICONNECTIONTIMEOUT);
        return Integer.parseInt(value);
    }


    public void setJNDIConnectionTimeout(Integer value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.JNDICONNECTIONTIMEOUT,
                        (value != null) ? value.toString() : String.valueOf(20000));
    }


    public void setCaCerts(String value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.CACERTS, (value != null) ? value : LDAPConstants.CONFIG.DEFAULT.CACERTS);
    }


    public String getCaCerts()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.CAKEYSTOREFILE);
        return value;
    }


    public void setClientCerts(String value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.CLIENTCERTS, (value != null) ? value :
                        LDAPConstants.CONFIG.DEFAULT.CLIENTCERTS);
    }


    public String getClientCerts()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.CLIENTKEYSTOREFILE);
        return value;
    }


    public void setCaKeyStorePwd(String value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.CAKEYSTOREPWD, (value != null) ? value :
                        LDAPConstants.CONFIG.DEFAULT.CAKEYSTOREPWD);
    }


    public String getCaKeyStorePwd()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.CAPASSPHRASE);
        if(value != null && value.length() == 0)
        {
            value = null;
        }
        return value;
    }


    public void setClientKeyStorePwd(String value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.CLIENTKEYSTOREPWD, (value != null) ? value :
                        LDAPConstants.CONFIG.DEFAULT.CLIENTKEYSTOREPWD);
    }


    public String getClientKeyStorePwd()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.CLIENTPASSPHRASE);
        if(value != null && value.length() == 0)
        {
            value = null;
        }
        return value;
    }


    public void setCaKeyStoreType(String value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.CAKEYSTORETYPE, (value != null) ? value :
                        LDAPConstants.CONFIG.DEFAULT.CAKEYSTORETYPE);
    }


    public String getCaKeyStoreType()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.CAKEYSTORETYPE);
        return value;
    }


    public void setClientKeyStoreType(String value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.CLIENTKEYSTORETYPE, (value != null) ? value :
                        LDAPConstants.CONFIG.DEFAULT.CLIENTKEYSTORETYPE);
    }


    public String getClientKeyStoreType()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.CLIENTKEYSTORETYPE);
        return value;
    }


    public void setPrincipal(String value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.JNDIPRINCIPALS, (value != null) ? value :
                        LDAPConstants.CONFIG.DEFAULT.PRINCIPAL);
    }


    public String getPrincipal()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.JNDIPRINCIPALS);
        return (value != null) ? value : LDAPConstants.CONFIG.DEFAULT.PRINCIPAL;
    }


    public void setJNDIVersion(EnumerationValue value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.JNDIVERSION, (value != null) ? value.getCode() :
                        LDAPConstants.CONFIG.DEFAULT.JNDIVERSION);
    }


    public EnumerationValue getJNDIVersion()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.JNDIVERSION);
        EnumerationType type = EnumerationManager.getInstance().getEnumerationType(GeneratedLDAPConstants.TC.LDAPVERSIONENUM);
        return (value != null) ? EnumerationManager.getInstance()
                        .getEnumerationValue(type, value) : EnumerationManager.getInstance()
                        .getEnumerationValue(type, LDAPConstants.CONFIG.DEFAULT.JNDIVERSION);
    }


    public void setSecurityProtocol(String value)
    {
        setValue(LDAPConstants.CONFIG.EXTERNALQUALIFIER.SECURITYPROTOCOL, (value != null) ? value :
                        LDAPConstants.CONFIG.DEFAULT.SECURITYPROTOCOL);
    }


    public String getSecurityProtocol()
    {
        String value = Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.SECURITYPROTOCOL);
        return (value != null) ? value : LDAPConstants.CONFIG.DEFAULT.SECURITYPROTOCOL;
    }


    public Hashtable<String, String> getJNDIEnvironment()
    {
        Hashtable<String, String> env = new Hashtable<>();
        env.put("java.naming.ldap.version", getJNDIVersion().getCode().substring(1, 2));
        env.put("java.naming.factory.initial", getFactory());
        if(getSecurityProtocol() != null && getSecurityProtocol().equalsIgnoreCase("ssl") && getSocketFactory() != null)
        {
            env.put("java.naming.ldap.factory.socket", getSocketFactory());
        }
        env.put("java.naming.security.protocol", getSecurityProtocol());
        env.put("java.naming.security.principal", getPrincipal());
        env.put("java.naming.security.credentials", new String(getCredentials()));
        env.put("java.naming.security.authentication", getAuthentication().getCode());
        env.put("com.sun.jndi.ldap.connect.pool", String.valueOf(getUsePooling()));
        if(getUsePooling())
        {
            env.put("com.sun.jndi.ldap.connect.pool.prefsize", String.valueOf(getPrefSize()));
            env.put("com.sun.jndi.ldap.connect.pool.initsize", String.valueOf(getInitSize()));
            env.put("com.sun.jndi.ldap.connect.pool.timeout", String.valueOf(getPoolTimeOut()));
            env.put("com.sun.jndi.ldap.connect.pool.maxsize", String.valueOf(getMaxSize()));
        }
        env.put("com.sun.jndi.ldap.connect.timeout", String.valueOf(getJNDIConnectionTimeout()));
        return env;
    }


    public void dumpEnvironmentValues(Hashtable env)
    {
        for(Iterator<Map.Entry> it = env.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = it.next();
            if(!"java.naming.security.credentials".equals(entry.getKey()))
            {
                log.debug("" + entry.getKey() + " -> " + entry.getKey());
            }
        }
    }


    public void testConnection(LDAPConfigProxyItem config) throws LDAPUnavailableException
    {
        throw new UnsupportedOperationException("method 'LDAPConfigProxyItem#testConnection' is disabled, yet!");
    }
}
