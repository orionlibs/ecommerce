package de.hybris.platform.servicelayer.internal.service;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.exceptions.ConfigurationException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public final class ServicelayerUtils
{
    public static ApplicationContext getApplicationContext()
    {
        return Registry.getApplicationContext();
    }


    public static void enforceBeanScope(ApplicationContext ctx, String beanName, String scope)
    {
        if(!(ctx instanceof AbstractApplicationContext))
        {
            throw new ConfigurationException("used ApplicationContext is no instance of AbstractApplicationContext.");
        }
        AbstractApplicationContext genCtx = (AbstractApplicationContext)ctx;
        BeanDefinition def = genCtx.getBeanFactory().getMergedBeanDefinition(beanName);
        String defScope = def.getScope();
        if(!scope.equals(defScope))
        {
            throw new ConfigurationException("bean '" + beanName + "' should be declared with \"scope='" + scope + "'\", but has \"scope='" + defScope + "'\". Definition was found in " + ctx
                            .getDisplayName());
        }
    }


    public static boolean isSystemInitialized()
    {
        return (Registry.hasCurrentTenant() && Registry.getCurrentTenantNoFallback().getJaloConnection().isSystemInitialized());
    }


    public static boolean isSystemNotInitialized()
    {
        return !isSystemInitialized();
    }
}
