package de.hybris.platform.solr.controller.core;

import java.util.Map;

public interface SolrInstance
{
    String getName();


    Map<String, String> getConfiguration();


    boolean isAutostart();


    int getPriority();


    String getHostName();


    int getPort();


    SolrServerMode getMode();


    String getZkHost();


    boolean isZkUpdateConfig();


    Map<String, String> getZkProperties();


    String getConfigDir();


    String getDataDir();


    String getLogDir();


    String getMemory();


    String getJavaOptions();


    String getAuthType();


    String getUser();


    String getPassword();


    boolean isSSLEnabled();


    String getSSLKeyStoreType();


    String getSSLKeyStore();


    String getSSLKeyStorePassword();


    String getSSLTrustStoreType();


    String getSSLTrustStore();


    String getSSLTrustStorePassword();


    boolean isSSLNeedClientAuth();


    boolean isSSLWantClientAuth();


    boolean isSSLClientHostnameVerification();


    boolean isSSLCheckPeerName();


    String getSSLClientKeyStoreType();


    String getSSLClientKeyStore();


    String getSSLClientKeyStorePassword();


    String getSSLClientTrustStoreType();


    String getSSLClientTrustStore();


    String getSSLClientTrustStorePassword();
}
