package de.hybris.platform.metrics;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricRegistryListener;
import com.codahale.metrics.Reporter;
import java.io.Closeable;
import javax.management.MBeanServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HybrisJmxReporter implements Reporter, Closeable
{
    private final MetricRegistry registry;
    private final JmxListener listener;


    protected HybrisJmxReporter(MBeanServer mBeanServer, String domain, MetricRegistry registry, MetricFilter filter, MetricTimeUnits timeUnits)
    {
        this.registry = registry;
        this.listener = new JmxListener(mBeanServer, domain, filter, timeUnits);
    }


    public void start()
    {
        this.registry.addListener((MetricRegistryListener)this.listener);
    }


    public void stop()
    {
        this.registry.removeListener((MetricRegistryListener)this.listener);
        this.listener.unregisterAll();
    }


    public void close()
    {
        stop();
    }


    public static Builder forRegistry(MetricRegistry registry)
    {
        return new Builder(registry);
    }


    protected static final Logger LOGGER = LoggerFactory.getLogger(HybrisJmxReporter.class);
}
