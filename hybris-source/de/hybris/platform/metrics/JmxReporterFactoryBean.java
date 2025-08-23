package de.hybris.platform.metrics;

public class JmxReporterFactoryBean extends AbstractJmxReporterFactoryBean<HybrisJmxReporter>
{
    public Class<?> getObjectType()
    {
        return HybrisJmxReporter.class;
    }


    protected HybrisJmxReporter createInstance()
    {
        return HybrisJmxReporter.forRegistry(getMetricRegistry())
                        .inDomain(getDomain())
                        .build();
    }
}
