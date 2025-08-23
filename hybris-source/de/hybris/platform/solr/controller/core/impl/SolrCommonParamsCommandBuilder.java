package de.hybris.platform.solr.controller.core.impl;

import de.hybris.platform.solr.controller.SolrControllerException;
import de.hybris.platform.solr.controller.core.CommandBuilder;
import de.hybris.platform.solr.controller.core.SolrInstance;
import de.hybris.platform.solr.controller.core.SolrServerMode;
import de.hybris.platform.solr.controller.util.StringUtils;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SolrCommonParamsCommandBuilder implements CommandBuilder
{
    private static final Logger LOG = Logger.getLogger(SolrCommonParamsCommandBuilder.class.getName());
    protected static final String NO_CERT_STORE_PASS_MSG = "Failed to load solr instance parameters. Please configure password for {0}";
    protected static final String NO_KEY_STORE_MSG = "{0} has not been configured. {0} parameters will not be configured for solr";
    protected static final String ZK_DATA_PATH = "zoo_data";
    protected static final String BASIC_AUTH_TYPE = "basic";
    private final Map<String, String> configuration;
    private final SolrInstance solrInstance;


    public SolrCommonParamsCommandBuilder(Map<String, String> configuration, SolrInstance solrInstance)
    {
        this.configuration = configuration;
        this.solrInstance = solrInstance;
    }


    public Map<String, String> getConfiguration()
    {
        return this.configuration;
    }


    public SolrInstance getSolrInstance()
    {
        return this.solrInstance;
    }


    public void build(ProcessBuilder processBuilder) throws SolrControllerException
    {
        List<String> commandParams = new ArrayList<>();
        if(SolrServerMode.CLOUD.equals(this.solrInstance.getMode()))
        {
            commandParams.add("-c");
            if(StringUtils.isNotBlank(this.solrInstance.getZkHost()))
            {
                commandParams.add("-z");
                commandParams.add(this.solrInstance.getZkHost());
            }
            commandParams.add("-DzkServerDataDir=" + Paths.get(this.solrInstance.getDataDir(), new String[] {"zoo_data"}).toString());
        }
        if(StringUtils.isNotBlank(this.solrInstance.getHostName()))
        {
            commandParams.add("-h");
            commandParams.add(this.solrInstance.getHostName());
        }
        commandParams.add("-p");
        commandParams.add(Integer.toString(this.solrInstance.getPort()));
        if(StringUtils.isNotBlank(this.solrInstance.getMemory()))
        {
            commandParams.add("-m");
            commandParams.add(this.solrInstance.getMemory());
        }
        if(StringUtils.isNotBlank(this.solrInstance.getJavaOptions()))
        {
            commandParams.add("-a");
            commandParams.add(this.solrInstance.getJavaOptions());
        }
        processBuilder.command().addAll(commandParams);
        String authType = this.solrInstance.getAuthType();
        String user = this.solrInstance.getUser();
        String password = this.solrInstance.getPassword();
        if("basic".equalsIgnoreCase(authType) && StringUtils.isNotBlank(user) && StringUtils.isNotBlank(password))
        {
            processBuilder.environment().put("SOLR_AUTH_TYPE", authType);
            processBuilder.environment().put("SOLR_AUTHENTICATION_OPTS", "-Dbasicauth=" + user + ":" + password);
        }
        else
        {
            processBuilder.environment().put("SOLR_AUTH_TYPE", "");
            processBuilder.environment().put("SOLR_AUTHENTICATION_OPTS", "");
        }
        boolean sslEnabled = this.solrInstance.isSSLEnabled();
        if(sslEnabled)
        {
            validateCertSettings();
            processBuilder.environment().put("SOLR_SSL_ENABLED", Boolean.TRUE.toString());
            processBuilder.environment().put("SOLR_SSL_NEED_CLIENT_AUTH", Boolean.toString(this.solrInstance.isSSLNeedClientAuth()));
            processBuilder.environment().put("SOLR_SSL_WANT_CLIENT_AUTH", Boolean.toString(this.solrInstance.isSSLWantClientAuth()));
            processBuilder.environment().put("SOLR_SSL_CLIENT_HOSTNAME_VERIFICATION", Boolean.toString(this.solrInstance.isSSLClientHostnameVerification()));
            processBuilder.environment().put("SOLR_SSL_CHECK_PEER_NAME", Boolean.toString(this.solrInstance.isSSLCheckPeerName()));
            putCertParamsIfStoreIsNotEmptyOrLogMsg(this.solrInstance.getSSLKeyStore(), processBuilder.environment(), this::retrieveServerKeyStoreProps, MessageFormat.format("{0} has not been configured. {0} parameters will not be configured for solr", new Object[] {"server key store"}));
            putCertParamsIfStoreIsNotEmptyOrLogMsg(this.solrInstance.getSSLTrustStore(), processBuilder.environment(), this::retrieveServerTrustStoreProps, MessageFormat.format("{0} has not been configured. {0} parameters will not be configured for solr", new Object[] {"server trust store"}));
            putCertParamsIfStoreIsNotEmptyOrLogMsg(this.solrInstance.getSSLClientKeyStore(), processBuilder.environment(), this::retrieveClientKeyStoreProps, MessageFormat.format("{0} has not been configured. {0} parameters will not be configured for solr", new Object[] {"client key store"}));
            putCertParamsIfStoreIsNotEmptyOrLogMsg(this.solrInstance.getSSLClientTrustStore(), processBuilder.environment(), this::retrieveClientTrustStoreProps, MessageFormat.format("{0} has not been configured. {0} parameters will not be configured for solr", new Object[] {"client trust store"}));
        }
        else
        {
            processBuilder.environment().put("SOLR_SSL_ENABLED", Boolean.FALSE.toString());
        }
    }


    private void putCertParamsIfStoreIsNotEmptyOrLogMsg(String certStore, Map<String, String> target, Supplier<Map> parameterSupplier, String message)
    {
        if(StringUtils.isNotBlank(certStore))
        {
            target.putAll(parameterSupplier.get());
        }
        else
        {
            LOG.log(Level.INFO, message);
        }
    }


    private Map retrieveServerKeyStoreProps()
    {
        Map<String, String> props = new HashMap<>();
        props.put("SOLR_SSL_KEY_STORE", this.solrInstance.getSSLKeyStore());
        props.put("SOLR_SSL_KEY_STORE_TYPE", this.solrInstance.getSSLKeyStoreType());
        props.put("SOLR_SSL_KEY_STORE_PASSWORD", this.solrInstance.getSSLKeyStorePassword());
        return props;
    }


    private Map retrieveServerTrustStoreProps()
    {
        Map<String, String> props = new HashMap<>();
        props.put("SOLR_SSL_TRUST_STORE", this.solrInstance.getSSLTrustStore());
        props.put("SOLR_SSL_TRUST_STORE_TYPE", this.solrInstance.getSSLTrustStoreType());
        props.put("SOLR_SSL_TRUST_STORE_PASSWORD", this.solrInstance.getSSLTrustStorePassword());
        return props;
    }


    private Map retrieveClientKeyStoreProps()
    {
        Map<String, String> props = new HashMap<>();
        props.put("SOLR_SSL_CLIENT_KEY_STORE", this.solrInstance.getSSLClientKeyStore());
        props.put("SOLR_SSL_CLIENT_KEY_STORE_TYPE", this.solrInstance.getSSLClientKeyStoreType());
        props.put("SOLR_SSL_CLIENT_KEY_STORE_PASSWORD", this.solrInstance.getSSLClientKeyStorePassword());
        return props;
    }


    private Map retrieveClientTrustStoreProps()
    {
        Map<String, String> props = new HashMap<>();
        props.put("SOLR_SSL_CLIENT_TRUST_STORE", this.solrInstance.getSSLClientTrustStore());
        props.put("SOLR_SSL_CLIENT_TRUST_STORE_TYPE", this.solrInstance.getSSLClientTrustStoreType());
        props.put("SOLR_SSL_CLIENT_TRUST_STORE_PASSWORD", this.solrInstance.getSSLClientTrustStorePassword());
        return props;
    }


    private void validateCertSettings() throws SolrControllerException
    {
        if(StringUtils.isNotBlank(this.solrInstance.getSSLKeyStore()) && StringUtils.isBlank(this.solrInstance.getSSLKeyStorePassword()))
        {
            throw new SolrControllerException(MessageFormat.format("Failed to load solr instance parameters. Please configure password for {0}", new Object[] {"key store"}));
        }
        if(StringUtils.isNotBlank(this.solrInstance.getSSLTrustStore()) && StringUtils.isBlank(this.solrInstance.getSSLTrustStorePassword()))
        {
            throw new SolrControllerException(MessageFormat.format("Failed to load solr instance parameters. Please configure password for {0}", new Object[] {"trust store"}));
        }
        if(StringUtils.isNotBlank(this.solrInstance.getSSLClientKeyStore()) && StringUtils.isBlank(this.solrInstance.getSSLClientKeyStorePassword()))
        {
            throw new SolrControllerException(MessageFormat.format("Failed to load solr instance parameters. Please configure password for {0}", new Object[] {"client key store"}));
        }
        if(StringUtils.isNotBlank(this.solrInstance.getSSLClientTrustStore()) && StringUtils.isBlank(this.solrInstance.getSSLClientTrustStorePassword()))
        {
            throw new SolrControllerException(MessageFormat.format("Failed to load solr instance parameters. Please configure password for {0}", new Object[] {"client trust store"}));
        }
    }
}
