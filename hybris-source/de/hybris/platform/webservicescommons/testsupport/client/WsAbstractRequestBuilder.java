package de.hybris.platform.webservicescommons.testsupport.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import de.hybris.platform.core.Registry;
import de.hybris.platform.util.Utilities;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.UriBuilder;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.logging.LoggingFeature;

public abstract class WsAbstractRequestBuilder<T extends WsAbstractRequestBuilder<?>>
{
    public static final String EMBEDDEDSERVER_HTTP_PORT_CONFIG_KEY = "embeddedserver.http.port";
    public static final String EMBEDDEDSERVER_HTTPS_PORT_CONFIG_KEY = "embeddedserver.ssl.port";
    public static final String WEBSERVICES_REQUIRED_CHANNEL_CONFIG_KEY = "webservicescommons.required.channel";
    private String host = "localhost";
    private boolean useHttps = getDefaultUseHttps();
    private int port = getDefaultUseHttps() ? getDefaultHttpsPort() : getDefaultHttpPort();
    private final Map<String, Object> queryParams = new HashMap<>();
    private String extensionName = null;
    private String path = null;
    private ClientConfig clientConfig = getDefaultClientConfig();


    protected abstract T getThis();


    private static int getDefaultHttpPort()
    {
        return Registry.getCurrentTenant().getConfig().getInt("embeddedserver.http.port", 8001);
    }


    private static int getDefaultHttpsPort()
    {
        return Registry.getCurrentTenant().getConfig().getInt("embeddedserver.ssl.port", 8002);
    }


    private static boolean getDefaultUseHttps()
    {
        return Registry.getCurrentTenant().getConfig().getString("webservicescommons.required.channel", "https").equals("https");
    }


    private static ClientConfig getDefaultClientConfig()
    {
        ClientConfig config = new ClientConfig();
        config.connectorProvider(org.glassfish.jersey.jdk.connector.internal.JdkConnector::new);
        JacksonJsonProvider provider = (JacksonJsonProvider)(new JacksonJaxbJsonProvider()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        config.register(provider);
        Logger loggerForJerseyLoggingFilter = Logger.getLogger(WsAbstractRequestBuilder.class.getName());
        config.register(new LoggingFeature(loggerForJerseyLoggingFilter, Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY,
                        Integer.valueOf(8192)));
        config.property("jersey.config.client.httpUrlConnection.setMethodWorkaround", Boolean.TRUE);
        config.property("jersey.config.client.suppressHttpComplianceValidation", Boolean.TRUE);
        return config;
    }


    protected Client createClient()
    {
        try
        {
            TrustManager[] trustAllCerts = {(TrustManager)new DummyTrustManager()};
            SSLContext sc = SSLContext.getDefault();
            sc.init(null, trustAllCerts, new SecureRandom());
            return ClientBuilder.newBuilder().withConfig((Configuration)this.clientConfig).hostnameVerifier((HostnameVerifier)new DummyHostnameVerifier()).sslContext(sc)
                            .build();
        }
        catch(GeneralSecurityException gse)
        {
            throw new RuntimeException(gse);
        }
    }


    public T useDefaultHttpPort()
    {
        this.port = getDefaultHttpPort();
        this.useHttps = false;
        return getThis();
    }


    public T useDefaultHttpsPort()
    {
        this.port = getDefaultHttpsPort();
        this.useHttps = true;
        return getThis();
    }


    public T useSpecificPort(int port, boolean useHttps)
    {
        this.port = port;
        this.useHttps = useHttps;
        return getThis();
    }


    public T host(String host)
    {
        this.host = host;
        return getThis();
    }


    public T extensionName(String extensionName)
    {
        this.extensionName = extensionName;
        return getThis();
    }


    public T clientConfig(ClientConfig clientConfig)
    {
        setClientConfig(clientConfig);
        return getThis();
    }


    public T path(String path)
    {
        if(StringUtils.isEmpty(this.path))
        {
            this.path = path;
        }
        else
        {
            this.path = String.join("/", new CharSequence[] {this.path, path});
        }
        return getThis();
    }


    public T queryParam(String paramName, Object paramValue)
    {
        this.queryParams.put(paramName, paramValue);
        return getThis();
    }


    public T registerConfig(Class<?> providerClass)
    {
        this.clientConfig.register(providerClass);
        return getThis();
    }


    public T registerConfig(Object provider)
    {
        this.clientConfig.register(provider);
        return getThis();
    }


    public Invocation.Builder build()
    {
        WebTarget wt = createWebTarget(this.host, this.port, this.useHttps, this.extensionName, this.path);
        for(Map.Entry<String, Object> queryParam : this.queryParams.entrySet())
        {
            wt = wt.queryParam(queryParam.getKey(), new Object[] {queryParam.getValue()});
        }
        return wt.request();
    }


    protected WebTarget createWebTarget(String host, int port, boolean useHttps, String extensionName, String path)
    {
        String fromUri = useHttps ? ("https://" + host + "/") : ("http://" + host + "/");
        UriBuilder uriBuilder = UriBuilder.fromUri(fromUri).port(port);
        if(!StringUtils.isEmpty(extensionName))
        {
            uriBuilder.path(Utilities.getWebroot(extensionName));
        }
        if(!StringUtils.isEmpty(path))
        {
            uriBuilder.path(path);
        }
        return createClient().target(uriBuilder.build(new Object[0]));
    }


    protected String getHost()
    {
        return this.host;
    }


    protected void setHost(String host)
    {
        this.host = host;
    }


    protected boolean isUseHttps()
    {
        return this.useHttps;
    }


    protected void setUseHttps(boolean useHttps)
    {
        this.useHttps = useHttps;
    }


    protected int getPort()
    {
        return this.port;
    }


    protected void setPort(int port)
    {
        this.port = port;
    }


    protected String getExtensionName()
    {
        return this.extensionName;
    }


    protected void setExtensionName(String extensionName)
    {
        this.extensionName = extensionName;
    }


    protected String getPath()
    {
        return this.path;
    }


    protected void setPath(String path)
    {
        this.path = path;
    }


    protected Map<String, Object> getQueryParams()
    {
        return this.queryParams;
    }


    protected void setClientConfig(ClientConfig clientConfig)
    {
        this.clientConfig = clientConfig;
    }


    protected ClientConfig getClientConfig()
    {
        return this.clientConfig;
    }
}
