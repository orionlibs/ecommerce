package de.hybris.platform.metrics;

import com.codahale.metrics.MetricRegistry;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.SmartLifecycle;

public abstract class AbstractJmxReporterFactoryBean<T> extends AbstractFactoryBean<T> implements SmartLifecycle
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractJmxReporterFactoryBean.class);
    private MetricRegistry metricRegistry;
    private String domain;
    private boolean running = false;
    private boolean enabled = true;


    public void start()
    {
        if(isEnabled() && !isRunning())
        {
            invokeOnDelegate("start");
        }
    }


    public void stop()
    {
        if(isRunning())
        {
            invokeOnDelegate("stop");
        }
    }


    private void invokeOnDelegate(String method)
    {
        try
        {
            Class<?> clazz = Objects.<Class<?>>requireNonNull(getObjectType(), "Object instance is required");
            clazz.getMethod(method, new Class[0]).invoke(getObject(), new Object[0]);
            this.running = false;
        }
        catch(NoSuchMethodException e)
        {
            LOG.debug(e.getMessage(), e);
            LOG.error("Method {} must be implemented on {} class", method, getObjectType());
        }
        catch(Exception e)
        {
            LOG.debug(e.getMessage(), e);
            LOG.error(e.getMessage());
        }
    }


    public boolean isRunning()
    {
        return this.running;
    }


    public void destroy() throws Exception
    {
        stop();
    }


    public boolean isAutoStartup()
    {
        return true;
    }


    public void stop(Runnable runnable)
    {
        stop();
        runnable.run();
    }


    public int getPhase()
    {
        return 0;
    }


    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }


    protected boolean isEnabled()
    {
        return this.enabled;
    }


    @Required
    public void setDomain(String domain)
    {
        this.domain = domain;
    }


    protected String getDomain()
    {
        return this.domain;
    }


    @Required
    public void setMetricRegistry(MetricRegistry metricRegistry)
    {
        this.metricRegistry = metricRegistry;
    }


    protected MetricRegistry getMetricRegistry()
    {
        return this.metricRegistry;
    }
}
