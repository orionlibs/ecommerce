package de.hybris.platform.masterserver.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.masterserver.StatisticsConfig;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.backoff.BackoffStrategy;
import de.hybris.platform.util.backoff.ExponentialBackoffStrategy;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class StatisticsPublisher implements InitializingBean, DisposableBean
{
    private static final String STATISTICS_HTTP_READ_TIMEOUT = "tomcat.statistics.http.read.timeout";
    private static final String STATISTICS_HTTP_CONNECT_TIMEOUT = "tomcat.statistics.http.connect.timeout";
    private static final String STATISTICS_MAX_ATTEMPTS = "tomcat.statistics.max.attempts";
    private static final String STATISTICS_BACKOFF_FACTOR = "tomcat.statistics.backoff.factor";
    private static final String STATISTICS_SEND_DELAY = "tomcat.statistics.send.delay";
    private static final int DEFAULT_HTTP_READ_TIMEOUT = (int)TimeUnit.SECONDS.toMillis(120L);
    private static final int DEFAULT_HTTP_CONNECTION_TIMEOUT = (int)TimeUnit.SECONDS.toMillis(30L);
    private static final int DEFAULT_SEND_DELAY = (int)TimeUnit.MINUTES.toMillis(1L);
    private static final int DEFAULT_MAX_ATTEMPTS = 10;
    private static final double DEFAULT_BACKOFF_FACTOR = 1.1D;
    private static final Logger LOG = LoggerFactory.getLogger(StatisticsPublisher.class);
    private final AtomicBoolean alreadyRun = new AtomicBoolean(false);
    private Tenant tenant = null;
    private StatisticsPublisherWorker statisticsPublisherWorker = null;
    private StatisticsConfig statisticsConfig = StatisticsConfig.defaultConfig();


    public void afterPropertiesSet() throws Exception
    {
        this.tenant = Registry.getCurrentTenantNoFallback();
        startWorkerViaTenantListener();
    }


    public void destroy() throws Exception
    {
        stopStatisticsPublisher();
    }


    protected StatisticsPublisherWorker createWorker()
    {
        RestStatisticsSender restStatisticsSender = new RestStatisticsSender(createRestTemplate());
        ExponentialBackoffStrategy exponentialBackoffStrategy = createExponentialBackoffStrategy();
        Duration sendingInterval = getDefaultStatisticsGatewaySendStatsInterval();
        return new StatisticsPublisherWorker((StatisticsSender)restStatisticsSender, (BackoffStrategy)exponentialBackoffStrategy, sendingInterval);
    }


    void stopStatisticsPublisher()
    {
        if(this.statisticsPublisherWorker != null)
        {
            this.statisticsPublisherWorker.stop();
            this.alreadyRun.set(false);
        }
    }


    private void startWorkerViaTenantListener()
    {
        Registry.registerTenantListener((TenantListener)new Object(this));
    }


    private boolean shouldStartWorkerViaTenantListener(Tenant tenant)
    {
        return (this.tenant.equals(tenant) && this.tenant.getJaloConnection().isSystemInitialized() &&
                        !RedeployUtilities.isShutdownInProgress() && Registry.getMasterTenant().equals(this.tenant));
    }


    private boolean shouldNotRunStatisticsPublisherWorker()
    {
        if(!isStatisticsPublisherEnabled() || Registry.isStandaloneMode())
        {
            return true;
        }
        return !this.alreadyRun.compareAndSet(false, true);
    }


    void startStatisticsPublisherWorker()
    {
        if(shouldNotRunStatisticsPublisherWorker())
        {
            return;
        }
        if(this.statisticsPublisherWorker == null)
        {
            this.statisticsPublisherWorker = createWorker();
        }
        RegistrableThread registrableThread = (new RegistrableThread((Runnable)this.statisticsPublisherWorker, "StatisticsPublisherThread")).withInitialInfo(
                        OperationInfo.builder().asSuspendableOperation().build());
        registrableThread.start();
        LOG.info("Statistics thread started");
    }


    protected void setStatisticsConfig(StatisticsConfig statisticsConfig)
    {
        this.statisticsConfig = Objects.<StatisticsConfig>requireNonNull(statisticsConfig);
    }


    boolean isStatisticsPublisherEnabled()
    {
        return this.statisticsConfig.isStatisticsPublisherEnabled();
    }


    private Duration getHttpReadTimeOut()
    {
        return Duration.ofMillis(Config.getInt("tomcat.statistics.http.read.timeout", DEFAULT_HTTP_READ_TIMEOUT));
    }


    private Duration getHttpConnectionTimeOut()
    {
        return Duration.ofMillis(Config.getInt("tomcat.statistics.http.connect.timeout", DEFAULT_HTTP_CONNECTION_TIMEOUT));
    }


    private int getHttpRequestMaxRetries()
    {
        return Config.getInt("tomcat.statistics.max.attempts", 10);
    }


    private double getBackoffFactor()
    {
        return Config.getDouble("tomcat.statistics.backoff.factor", 1.1D);
    }


    private Duration getHttpRequestDelay()
    {
        return Duration.ofMillis(Config.getInt("tomcat.statistics.send.delay", DEFAULT_SEND_DELAY));
    }


    ExponentialBackoffStrategy createExponentialBackoffStrategy()
    {
        return new ExponentialBackoffStrategy(getHttpRequestMaxRetries(), getHttpRequestDelay(),
                        getBackoffFactor());
    }


    private Duration getDefaultStatisticsGatewaySendStatsInterval()
    {
        return DefaultStatisticsGateway.getSendStatsInterval();
    }


    private RestTemplate createRestTemplate()
    {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setReadTimeout((int)getHttpReadTimeOut().toMillis());
        httpRequestFactory.setConnectTimeout((int)getHttpConnectionTimeOut().toMillis());
        return new RestTemplate((ClientHttpRequestFactory)httpRequestFactory);
    }
}
