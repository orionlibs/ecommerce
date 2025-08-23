package de.hybris.platform.metrics;

import com.codahale.metrics.jmx.JmxReporter;
import java.util.concurrent.TimeUnit;

public class DropwizardJmxReporterFactoryBean extends AbstractJmxReporterFactoryBean<JmxReporter>
{
    public Class<?> getObjectType()
    {
        return JmxReporter.class;
    }


    protected JmxReporter createInstance()
    {
        return JmxReporter.forRegistry(getMetricRegistry())
                        .convertRatesTo(TimeUnit.SECONDS)
                        .convertDurationsTo(TimeUnit.MILLISECONDS)
                        .inDomain(getDomain())
                        .build();
    }
}
