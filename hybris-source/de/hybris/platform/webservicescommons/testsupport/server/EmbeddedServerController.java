package de.hybris.platform.webservicescommons.testsupport.server;

import de.hybris.platform.core.Registry;
import de.hybris.platform.embeddedserver.api.EmbeddedApplication;
import de.hybris.platform.embeddedserver.api.EmbeddedServer;
import de.hybris.platform.embeddedserver.api.EmbeddedServerBuilder;
import de.hybris.platform.embeddedserver.api.EmbeddedServerBuilderContext;
import de.hybris.platform.embeddedserver.base.EmbeddedExtension;
import de.hybris.platform.util.Utilities;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;

public class EmbeddedServerController implements InitializingBean
{
    private static final String EMBEDDEDSERVER_HTTP_PORT_CONFIG_KEY = "embeddedserver.http.port";
    private static final int APPLICATION_PING_TIMEOUT = 2000;
    private static final int APPLICATION_PING_RETRIES = 5;
    private static final int APPLICATION_PING_WAIT_INTERVAL = 1000;
    private static final Logger LOG = Logger.getLogger(EmbeddedServerController.class);
    private EmbeddedServerBuilder embeddedServerBuilder;
    private EmbeddedServer embeddedServer;
    private String originalCacheSuffix;


    public void start(String[] webExtensionNames)
    {
        LOG.debug("Creating embedded server");
        this.embeddedServer = createEmbeddedServer(webExtensionNames);
        beforeStart();
        LOG.info("Starting embedded server " + this.embeddedServer.toString());
        this.embeddedServer.start();
        LOG.debug("Ensuring that web apps are started");
        ensureWebAppsAreStarted(webExtensionNames);
    }


    public void stop()
    {
        LOG.debug("Preparing to stop embedded server");
        if(this.embeddedServer != null)
        {
            try
            {
                if(this.embeddedServer.isRunning())
                {
                    LOG.info("Stopping embedded server" + this.embeddedServer.toString());
                    this.embeddedServer.stop();
                    this.embeddedServer = null;
                }
            }
            finally
            {
                afterStop();
            }
        }
    }


    protected void beforeStart()
    {
        setCacheSuffix();
    }


    protected void afterStop()
    {
        restoreOriginalCacheSuffix();
    }


    public EmbeddedExtension getEmbeddedExtension(String extensionName)
    {
        return (new EmbeddedExtension(Utilities.getExtensionInfo(extensionName))).withContext(Utilities.getWebroot(extensionName));
    }


    public EmbeddedServer createEmbeddedServer(String[] webExtensionNames)
    {
        EmbeddedServerBuilderContext embeddedServerCtx = this.embeddedServerBuilder.needEmbeddedServer();
        for(String webExtensionName : webExtensionNames)
        {
            embeddedServerCtx.withApplication((EmbeddedApplication)getEmbeddedExtension(webExtensionName));
        }
        return embeddedServerCtx.build();
    }


    public EmbeddedServerBuilder getEmbeddedServerBuilder()
    {
        return this.embeddedServerBuilder;
    }


    public void setEmbeddedServerBuilder(EmbeddedServerBuilder embeddedServerBuilder)
    {
        this.embeddedServerBuilder = embeddedServerBuilder;
    }


    protected void setCacheSuffix()
    {
        LOG.debug("Setting different cache suffix");
        Registry.getCurrentTenant().getConfig().setParameter("webservicescommons.cacheSuffix", "testCache");
    }


    protected void restoreOriginalCacheSuffix()
    {
        LOG.debug("Restoring original cache suffix");
        if(this.originalCacheSuffix == null)
        {
            Registry.getCurrentTenant().getConfig().removeParameter("webservicescommons.cacheSuffix");
        }
        else
        {
            Registry.getCurrentTenant().getConfig().setParameter("webservicescommons.cacheSuffix", this.originalCacheSuffix);
            this.originalCacheSuffix = null;
        }
    }


    public void afterPropertiesSet()
    {
        this
                        .originalCacheSuffix = Registry.getCurrentTenant().getConfig().getString("webservicescommons.cacheSuffix", null);
    }


    private int getDefaultHttpPort()
    {
        return Registry.getCurrentTenant().getConfig().getInt("embeddedserver.http.port", 8001);
    }


    public Optional<HttpStatus> getWebAppHeadStatus(String webExtentionName)
    {
        HttpURLConnection connection = null;
        try
        {
            URL url = new URL("http", "localhost", getDefaultHttpPort(), Utilities.getWebroot(webExtentionName));
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            int responseCode = connection.getResponseCode();
            return Optional.of(HttpStatus.valueOf(responseCode));
        }
        catch(IOException e)
        {
            LOG.warn("Problem when trying to get web app " + webExtentionName + " status");
            return (Optional)Optional.empty();
        }
        finally
        {
            if(connection != null)
            {
                connection.disconnect();
            }
        }
    }


    public boolean ensureWebAppsAreStarted(String[] webExtentionNames)
    {
        List<String> notStarted = (List<String>)Arrays.<String>stream(webExtentionNames).filter(webApp -> !ensureWebAppIsStarted(webApp)).collect(Collectors.toList());
        notStarted
                        .forEach(webApp -> LOG.warn("Application " + webApp + " is not started!"));
        return notStarted.isEmpty();
    }


    public boolean ensureWebAppIsStarted(String webExtentionName)
    {
        Supplier<Boolean> webAppStartedCondition = () -> {
            Optional<HttpStatus> status = getWebAppHeadStatus(webExtentionName);
            return Boolean.valueOf((status.isPresent() && !((HttpStatus)status.get()).is5xxServerError()));
        };
        boolean webAppStarted = retry(webAppStartedCondition, 5, 1000);
        return webAppStarted;
    }


    private static boolean retry(Supplier<Boolean> condition, int maxRetries, int interval)
    {
        int trial = 1;
        while(trial <= maxRetries)
        {
            Boolean result = condition.get();
            if(Boolean.TRUE.equals(result))
            {
                return true;
            }
            try
            {
                Thread.sleep(interval);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            trial++;
        }
        return false;
    }
}
