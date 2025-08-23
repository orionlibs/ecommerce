package de.hybris.platform.metrics.dropwizard;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.config.ConfigIntf;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class MetricsReporterService implements InitializingBean, DisposableBean
{
    public static final String METRICS_REPORTER_ENABLED = "metrics.reporter.%s.enabled";
    public static final String METRICS_REPORTER_FILTER = "metrics.reporter.%s.filters.";
    public static final String METRICS_REPORTER_PERIOD_SECONDS = "metrics.reporter.%s.period.seconds";
    public static final Duration DEFAULT_METRICS_REPORTER_PERIOD_SECONDS_VALUE = Duration.ofSeconds(60L);
    public static final String DEFAULT_METRICS_REPORTER_FILTER_VALUE = "";
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsReporterService.class);
    private static final String INVALID_PROPERTY_MSG = "invalid property:{} value: '{}' - using '{}' instead";
    private static final long EXECUTOR_TERMINATION_TIMEOUT = 5L;
    private final String metricsReporterEnabledProperty;
    private final String metricsReporterFilterPropertiesTemplate;
    private final String metricsReporterPeriodProperty;
    private final MetricRegistry metricRegistry;
    private final Tenant tenant;
    private final Map<String, String> reporterFilters = new ConcurrentSkipListMap<>();
    private final String reporterName;
    private final MetricsReporterFactory metricsReporterFactory;
    private ScheduledReporter metricReporter;
    private Duration reportingPeriod;
    private boolean reporterEnabled;
    private ScheduledExecutorService executorService;
    private ConfigIntf.ConfigChangeListener configChangeListener;


    public MetricsReporterService(MetricRegistry registry, String reporterName, MetricsReporterFactory metricsReporterFactory)
    {
        this.metricsReporterEnabledProperty = String.format("metrics.reporter.%s.enabled", new Object[] {reporterName});
        this.metricsReporterFilterPropertiesTemplate = String.format("metrics.reporter.%s.filters.", new Object[] {reporterName});
        this.metricsReporterPeriodProperty = String.format("metrics.reporter.%s.period.seconds", new Object[] {reporterName});
        this.reporterName = reporterName;
        this.metricRegistry = registry;
        this.tenant = Objects.<Tenant>requireNonNull(Registry.getCurrentTenantNoFallback(),
                        String.format("cannot create %s without tenant", new Object[] {MetricsReporterService.class.getSimpleName()}));
        this.metricsReporterFactory = metricsReporterFactory;
        registerConfigChangeListener();
    }


    public Duration getReportingPeriod()
    {
        return this.reportingPeriod;
    }


    public boolean isReporterEnabled()
    {
        return this.reporterEnabled;
    }


    public Map<String, String> getReporterFilters()
    {
        return Map.copyOf(this.reporterFilters);
    }


    protected ScheduledExecutorService createDefaultExecutor(String name, String tenantID)
    {
        return Executors.newSingleThreadScheduledExecutor(runnable -> {
            RegistrableThread thread = (new RegistrableThread(runnable)).withInitialInfo(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
            thread.setName(String.format("%s-%s-%s", new Object[] {name, tenantID, Long.valueOf(thread.getId())}));
            return (Thread)thread;
        });
    }


    private void readReporterMetricConfiguration()
    {
        this.reportingPeriod = getReportingPeriod(
                        String.valueOf(
                                        Config.getString(this.metricsReporterPeriodProperty,
                                                        String.valueOf(DEFAULT_METRICS_REPORTER_PERIOD_SECONDS_VALUE.toSeconds()))), DEFAULT_METRICS_REPORTER_PERIOD_SECONDS_VALUE);
        Map<String, String> parametersByPattern = Config.getParametersByPattern(this.metricsReporterFilterPropertiesTemplate);
        parametersByPattern.forEach((k, v) -> this.reporterFilters.put(k, getReporterFilterValue(v, "")));
        this.reporterEnabled = getReportingEnabled(Config.getString(this.metricsReporterEnabledProperty, ""), false);
    }


    private void registerConfigChangeListener()
    {
        this.configChangeListener = ((key, newValue) -> {
            if(key.equals(this.metricsReporterEnabledProperty))
            {
                this.reporterEnabled = getReportingEnabled(newValue, this.reporterEnabled);
                stopAndOrCreateMetricReporter();
            }
            else if(key.equals(this.metricsReporterPeriodProperty))
            {
                this.reportingPeriod = getReportingPeriod(newValue, this.reportingPeriod);
                stopAndOrCreateMetricReporter();
            }
            else if(key.startsWith(this.metricsReporterFilterPropertiesTemplate))
            {
                this.reporterFilters.compute(key, ());
                stopAndOrCreateMetricReporter();
            }
        });
        this.tenant.getConfig().registerConfigChangeListener(this.configChangeListener);
    }


    private boolean getReportingEnabled(String newValue, boolean defaultValue)
    {
        if(StringUtils.isNotBlank(newValue))
        {
            return Boolean.parseBoolean(newValue);
        }
        return (Config.isCloudEnvironment() || defaultValue);
    }


    private Duration getReportingPeriod(String reportingPeriod, Duration defaultReportingPeriod)
    {
        try
        {
            int newValue = Integer.parseInt(reportingPeriod);
            if(newValue <= 0)
            {
                LOGGER.warn("invalid property:{} value: '{}' - using '{}' instead", new Object[] {this.metricsReporterPeriodProperty, reportingPeriod, defaultReportingPeriod});
                return defaultReportingPeriod;
            }
            return Duration.ofSeconds(newValue);
        }
        catch(NumberFormatException e)
        {
            LOGGER.warn("invalid property:{} value: '{}' - using '{}' instead", new Object[] {this.metricsReporterPeriodProperty, reportingPeriod, defaultReportingPeriod});
            return defaultReportingPeriod;
        }
    }


    private String getReporterFilterValue(String reporterFilter, String defaultFilter)
    {
        try
        {
            new SpelExpressionMetricFilter(reporterFilter);
            return reporterFilter;
        }
        catch(Exception ex)
        {
            LOGGER.warn("invalid property:{} value: '{}' - using '{}' instead", new Object[] {this.metricsReporterFilterPropertiesTemplate, reporterFilter, defaultFilter, ex});
            return defaultFilter;
        }
    }


    private synchronized void stopAndOrCreateMetricReporter()
    {
        if(this.metricReporter != null)
        {
            stopMetricReporter();
        }
        if(this.reporterEnabled)
        {
            LOGGER.debug("Starting custom metric reporter {} for tenant {}", this.reporterName, this.tenant.getTenantID());
            this.metricReporter = this.metricsReporterFactory.createMetricReporter(this.metricRegistry, this.reporterName, this.metricsReporterFactory
                            .getMetricFilter(getReporterFilters()), this.executorService);
            this.metricReporter.start(this.reportingPeriod.toSeconds(), TimeUnit.SECONDS);
        }
    }


    public void afterPropertiesSet()
    {
        if(this.executorService == null)
        {
            this.executorService = createDefaultExecutor(this.reporterName, this.tenant.getTenantID());
        }
        readReporterMetricConfiguration();
        stopAndOrCreateMetricReporter();
    }


    public void destroy()
    {
        stopMetricReporter();
        stopExecutorService();
        unregisterConfigChangeListener();
    }


    private void unregisterConfigChangeListener()
    {
        if(this.configChangeListener != null)
        {
            this.tenant.getConfig().unregisterConfigChangeListener(this.configChangeListener);
        }
    }


    private void stopExecutorService()
    {
        if(this.executorService == null)
        {
            return;
        }
        try
        {
            this.executorService.shutdown();
            if(!this.executorService.awaitTermination(5L, TimeUnit.SECONDS))
            {
                this.executorService.shutdownNow();
                if(!this.executorService.awaitTermination(1L, TimeUnit.SECONDS))
                {
                    LOGGER.error("ScheduledExecutorService did not terminate");
                }
            }
        }
        catch(InterruptedException ex)
        {
            this.executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


    private void stopMetricReporter()
    {
        LOGGER.debug("Stopping custom metric reporter for tenant {}", this.tenant.getTenantID());
        if(this.metricReporter != null)
        {
            this.metricReporter.stop();
        }
        this.metricReporter = null;
    }
}
