package de.hybris.platform.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Required;

public abstract class HybrisHealthCheck extends HealthCheck implements BeanNameAware
{
    private HealthCheckRegistry registry;
    private String beanName;


    @PostConstruct
    public void init()
    {
        this.registry.register(this.beanName, this);
    }


    public void setBeanName(String beanName)
    {
        this.beanName = beanName;
    }


    @Required
    public void setRegistry(HealthCheckRegistry registry)
    {
        this.registry = registry;
    }
}
