package de.hybris.platform.solr.controller.core.impl;

import de.hybris.platform.solr.controller.core.SolrInstance;
import de.hybris.platform.solr.controller.core.SolrServerMode;
import de.hybris.platform.solr.controller.util.StringUtils;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultSolrInstance implements SolrInstance
{
    public static final String AUTOSTART_PROPERTY = "autostart";
    public static final String AUTOSTART_DEFAULT_VALUE = Boolean.FALSE.toString();
    public static final String PRIORITY_PROPERTY = "priority";
    public static final String PRIORITY_DEFAULT_VALUE = "100";
    public static final String HOST_NAME_PROPERTY = "hostname";
    public static final String HOST_NAME_DEFAULT_VALUE = "localhost";
    public static final String PORT_PROPERTY = "port";
    public static final String PORT_DEFAULT_VALUE = "8983";
    public static final String MODE_PROPERTY = "mode";
    public static final String MODE_DEFAULT_VALUE = "standalone";
    public static final String ZK_HOST_PROPERTY = "zk.host";
    public static final String ZK_HOST_DEFAULT_VALUE = "";
    public static final String ZK_UPDATE_CONFIG_PROPERTY = "zk.upconfig";
    public static final String ZK_UPDATE_CONFIG_DEFAULT_VALUE = Boolean.TRUE.toString();
    public static final String ZK_PROPERTIES_PREFIX = "zk.prop.";
    public static final String CONFIG_DIR_PROPERTY = "config.dir";
    public static final String CONFIG_DIR_DEFAULT_VALUE = "";
    public static final String DATA_DIR_PROPERTY = "data.dir";
    public static final String DATA_DIR_DEFAULT_VALUE = "";
    public static final String LOG_DIR_PROPERTY = "log.dir";
    public static final String LOG_DIR_DEFAULT_VALUE = "";
    public static final String MEMORY_PROPERTY = "memory";
    public static final String MEMORY_DEFAULT_VALUE = "512m";
    public static final String JAVA_OPTIONS_PROPERTY = "javaoptions";
    public static final String JAVA_OPTIONS_DEFAULT_VALUE = "";
    public static final String AUTH_TYPE_PROPERTY = "authtype";
    public static final String AUTH_TYPE_DEFAULT_VALUE = "basic";
    public static final String USER_PROPERTY = "user";
    public static final String USER_DEFAULT_VALUE = "";
    public static final String PASSWORD_PROPERTY = "password";
    public static final String PASSWORD_DEFAULT_VALUE = "";
    public static final String SSL_ENABLED = "ssl.enabled";
    public static final String SSL_ENABLED_DEFAULT_VALUE = Boolean.TRUE.toString();
    public static final String SSL_KEY_STORE_TYPE = "ssl.keyStoreType";
    public static final String SSL_KEY_STORE_TYPE_DEFAULT_VALUE = "PKCS12";
    public static final String SSL_KEY_STORE = "ssl.keyStore";
    public static final String SSL_KEY_STORE_DEFAULT_VALUE = "";
    public static final String SSL_KEY_STORE_PASSWORD = "ssl.keyStorePassword";
    public static final String SSL_KEY_STORE_PASSWORD_DEFAULT_VALUE = "";
    public static final String SSL_TRUST_STORE_TYPE = "ssl.trustStoreType";
    public static final String SSL_TRUST_STORE_TYPE_DEFAULT_VALUE = "PKCS12";
    public static final String SSL_TRUST_STORE = "ssl.trustStore";
    public static final String SSL_TRUST_STORE_DEFAULT_VALUE = "";
    public static final String SSL_TRUST_STORE_PASSWORD = "ssl.trustStorePassword";
    public static final String SSL_TRUST_STORE_PASSWORD_DEFAULT_VALUE = "";
    public static final String SSL_NEED_CLIENT_AUTH = "ssl.needClientAuth";
    public static final String SSL_NEED_CLIENT_AUTH_DEFAULT_VALUE = Boolean.FALSE.toString();
    public static final String SSL_WANT_CLIENT_AUTH = "ssl.wantClientAuth";
    public static final String SSL_WANT_CLIENT_AUTH_DEFAULT_VALUE = Boolean.FALSE.toString();
    public static final String SSL_CLIENT_HOSTNAME_VERIFICATION = "ssl.clientHostnameVerification";
    public static final String SSL_CLIENT_HOSTNAME_VERIFICATION_DEFAULT_VALUE = Boolean.FALSE.toString();
    public static final String SSL_CHECK_PEER_NAME = "ssl.checkPeerName";
    public static final String SSL_CHECK_PEER_NAME_DEFAULT_VALUE = Boolean.TRUE.toString();
    public static final String SSL_CLIENT_KEY_STORE_TYPE = "ssl.client.keyStoreType";
    public static final String SSL_CLIENT_KEY_STORE_TYPE_DEFAULT_VALUE = "PKCS12";
    public static final String SSL_CLIENT_KEY_STORE = "ssl.client.keyStore";
    public static final String SSL_CLIENT_KEY_STORE_DEFAULT_VALUE = "";
    public static final String SSL_CLIENT_KEY_STORE_PASSWORD = "ssl.client.keyStorePassword";
    public static final String SSL_CLIENT_KEY_STORE_PASSWORD_DEFAULT_VALUE = "";
    public static final String SSL_CLIENT_TRUST_STORE_TYPE = "ssl.client.trustStoreType";
    public static final String SSL_CLIENT_TRUST_STORE_TYPE_DEFAULT_VALUE = "PKCS12";
    public static final String SSL_CLIENT_TRUST_STORE = "ssl.client.trustStore";
    public static final String SSL_CLIENT_TRUST_STORE_DEFAULT_VALUE = "";
    public static final String SSL_CLIENT_TRUST_STORE_PASSWORD = "ssl.client.trustStorePassword";
    public static final String SSL_CLIENT_TRUST_STORE_PASSWORD_DEFAULT_VALUE = "";
    private final String name;
    private final Map<String, String> configuration;


    public DefaultSolrInstance(String name)
    {
        this.name = name;
        this.configuration = initializeConfiguration();
    }


    public DefaultSolrInstance(String name, Map<String, String> configuration)
    {
        this.name = name;
        this.configuration = initializeConfiguration();
        if(configuration != null && !configuration.isEmpty())
        {
            this.configuration.putAll(configuration);
        }
    }


    public String getName()
    {
        return this.name;
    }


    public Map<String, String> getConfiguration()
    {
        return this.configuration;
    }


    public boolean isAutostart()
    {
        return Boolean.parseBoolean(this.configuration.get("autostart"));
    }


    public int getPriority()
    {
        return Integer.parseInt(this.configuration.get("priority"));
    }


    public String getHostName()
    {
        return this.configuration.get("hostname");
    }


    public int getPort()
    {
        return Integer.parseInt(this.configuration.get("port"));
    }


    public SolrServerMode getMode()
    {
        return SolrServerMode.valueOf(((String)this.configuration.get("mode")).toUpperCase(Locale.ROOT));
    }


    public String getZkHost()
    {
        return this.configuration.get("zk.host");
    }


    public boolean isZkUpdateConfig()
    {
        return Boolean.parseBoolean(this.configuration.get("zk.upconfig"));
    }


    public Map<String, String> getZkProperties()
    {
        return (Map<String, String>)this.configuration.entrySet().stream().filter(entry -> StringUtils.startsWith((String)entry.getKey(), "zk.prop."))
                        .collect(Collectors.toMap(entry -> StringUtils.removeStart((String)entry.getKey(), "zk.prop."), Map.Entry::getValue));
    }


    public String getConfigDir()
    {
        return this.configuration.get("config.dir");
    }


    public String getDataDir()
    {
        return this.configuration.get("data.dir");
    }


    public String getLogDir()
    {
        return this.configuration.get("log.dir");
    }


    public String getMemory()
    {
        return this.configuration.get("memory");
    }


    public String getJavaOptions()
    {
        return this.configuration.get("javaoptions");
    }


    public String getAuthType()
    {
        return this.configuration.get("authtype");
    }


    public String getUser()
    {
        return this.configuration.get("user");
    }


    public String getPassword()
    {
        return this.configuration.get("password");
    }


    public boolean isSSLEnabled()
    {
        return Boolean.parseBoolean(this.configuration.get("ssl.enabled"));
    }


    public String getSSLKeyStoreType()
    {
        return this.configuration.get("ssl.keyStoreType");
    }


    public String getSSLKeyStore()
    {
        return this.configuration.get("ssl.keyStore");
    }


    public String getSSLKeyStorePassword()
    {
        return this.configuration.get("ssl.keyStorePassword");
    }


    public String getSSLTrustStoreType()
    {
        return this.configuration.get("ssl.trustStoreType");
    }


    public String getSSLTrustStore()
    {
        return this.configuration.get("ssl.trustStore");
    }


    public String getSSLTrustStorePassword()
    {
        return this.configuration.get("ssl.trustStorePassword");
    }


    public boolean isSSLNeedClientAuth()
    {
        return Boolean.parseBoolean(this.configuration.get("ssl.needClientAuth"));
    }


    public boolean isSSLWantClientAuth()
    {
        return Boolean.parseBoolean(this.configuration.get("ssl.wantClientAuth"));
    }


    public boolean isSSLClientHostnameVerification()
    {
        return Boolean.parseBoolean(this.configuration.get("ssl.clientHostnameVerification"));
    }


    public boolean isSSLCheckPeerName()
    {
        return Boolean.parseBoolean(this.configuration.get("ssl.checkPeerName"));
    }


    public String getSSLClientKeyStoreType()
    {
        return this.configuration.get("ssl.client.keyStoreType");
    }


    public String getSSLClientKeyStore()
    {
        return this.configuration.get("ssl.client.keyStore");
    }


    public String getSSLClientKeyStorePassword()
    {
        return this.configuration.get("ssl.client.keyStorePassword");
    }


    public String getSSLClientTrustStoreType()
    {
        return this.configuration.get("ssl.client.trustStoreType");
    }


    public String getSSLClientTrustStore()
    {
        return this.configuration.get("ssl.client.trustStore");
    }


    public String getSSLClientTrustStorePassword()
    {
        return this.configuration.get("ssl.client.trustStorePassword");
    }


    protected final Map<String, String> initializeConfiguration()
    {
        Map<String, String> defaultConfig = new HashMap<>();
        defaultConfig.put("autostart", AUTOSTART_DEFAULT_VALUE);
        defaultConfig.put("priority", "100");
        defaultConfig.put("hostname", "localhost");
        defaultConfig.put("port", "8983");
        defaultConfig.put("mode", "standalone");
        defaultConfig.put("zk.host", "");
        defaultConfig.put("zk.upconfig", ZK_UPDATE_CONFIG_DEFAULT_VALUE);
        defaultConfig.put("config.dir", "");
        defaultConfig.put("data.dir", "");
        defaultConfig.put("log.dir", "");
        defaultConfig.put("memory", "512m");
        defaultConfig.put("javaoptions", "");
        defaultConfig.put("authtype", "basic");
        defaultConfig.put("user", "");
        defaultConfig.put("password", "");
        defaultConfig.put("ssl.enabled", SSL_ENABLED_DEFAULT_VALUE);
        defaultConfig.put("ssl.keyStoreType", "PKCS12");
        defaultConfig.put("ssl.keyStore", "");
        defaultConfig.put("ssl.keyStorePassword", "");
        defaultConfig.put("ssl.trustStoreType", "PKCS12");
        defaultConfig.put("ssl.trustStore", "");
        defaultConfig.put("ssl.trustStorePassword", "");
        defaultConfig.put("ssl.needClientAuth", SSL_NEED_CLIENT_AUTH_DEFAULT_VALUE);
        defaultConfig.put("ssl.wantClientAuth", SSL_WANT_CLIENT_AUTH_DEFAULT_VALUE);
        defaultConfig.put("ssl.clientHostnameVerification", SSL_CLIENT_HOSTNAME_VERIFICATION_DEFAULT_VALUE);
        defaultConfig.put("ssl.checkPeerName", SSL_CHECK_PEER_NAME_DEFAULT_VALUE);
        defaultConfig.put("ssl.client.keyStoreType", "PKCS12");
        defaultConfig.put("ssl.client.keyStore", "");
        defaultConfig.put("ssl.client.keyStorePassword", "");
        defaultConfig.put("ssl.client.trustStoreType", "PKCS12");
        defaultConfig.put("ssl.client.trustStore", "");
        defaultConfig.put("ssl.client.trustStorePassword", "");
        return defaultConfig;
    }


    public String toString()
    {
        return MessageFormat.format("[name: {0}, hostname: {1}, port: {2,number,#}, mode: {3}]", new Object[] {getName(), getHostName(),
                        Integer.valueOf(getPort()), getMode()});
    }
}
