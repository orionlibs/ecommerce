package de.hybris.platform.webservicescommons.initializer;

import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class HybrisPropertiesWebApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableWebApplicationContext>
{
    private static final Logger LOGGER = Logger.getLogger(HybrisPropertiesWebApplicationContextInitializer.class);


    public void initialize(ConfigurableWebApplicationContext ctx)
    {
        Objects.requireNonNull(ctx.getEnvironment().getPropertySources());
        Optional.<ConfigurableWebApplicationContext>ofNullable(ctx).map(ApplicationContext::getParent).map(this::getHybrisProperties).map(p -> new PropertiesPropertySource("hybrisProperties", p)).ifPresent(ctx.getEnvironment().getPropertySources()::addFirst);
    }


    protected Properties getHybrisProperties(ApplicationContext ctx)
    {
        try
        {
            return (Properties)ctx.getBean("hybrisProperties", Properties.class);
        }
        catch(BeansException e)
        {
            LOGGER.warn("Could not find hybris properties bean.", (Throwable)e);
            return null;
        }
    }
}
