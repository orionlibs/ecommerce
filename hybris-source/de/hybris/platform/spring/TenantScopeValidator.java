package de.hybris.platform.spring;

import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.WebApplicationContext;

@Deprecated(since = "5.0", forRemoval = true)
public class TenantScopeValidator implements ApplicationListener
{
    private static final Logger LOG = Logger.getLogger(TenantScopeValidator.class);


    public void onApplicationEvent(ApplicationEvent event)
    {
        if(event instanceof ContextRefreshedEvent)
        {
            ApplicationContext ctx = ((ContextRefreshedEvent)event).getApplicationContext();
            if(ctx instanceof WebApplicationContext)
            {
                WebApplicationContext wac = (WebApplicationContext)ctx;
                Object thisScope = (wac instanceof ConfigurableApplicationContext) ? ((ConfigurableApplicationContext)wac).getBeanFactory().getRegisteredScope("tenant") : null;
                Object parentScope = (wac.getParentBeanFactory() == null) ? null : wac.getParentBeanFactory().getBean("scope.tenant");
                if(thisScope != null && thisScope.equals(parentScope))
                {
                    LOG.warn("**************************************************");
                    LOG.warn("*");
                    LOG.warn("* WebApplicationContext of servlet " + wac.getServletContext().getServletContextName() + " has a wrong configured tenant scope.");
                    LOG.warn("* This may cause serious problems.");
                    LOG.warn("* For information on how to configure it correctly please see https://jira.hybris.com/browse/PLA-8070");
                    LOG.warn("*");
                    LOG.warn("**************************************************");
                }
            }
            else
            {
                Map<String, CustomScopeConfigurer> beans = ctx.getBeansOfType(CustomScopeConfigurer.class);
                if(beans.size() > 1)
                {
                    LOG.warn("**************************************************");
                    LOG.warn("*");
                    LOG.warn("* Global ApplicationContext defines more than one CustomScopeConfigurer.");
                    LOG.warn("* This may cause serious problems.");
                    LOG.warn("* For information on how to configure it correctly please see https://jira.hybris.com/browse/PLA-10054");
                    LOG.warn("*");
                    if(ctx instanceof AbstractApplicationContext)
                    {
                        LOG.warn("* Found beans:");
                        for(String key : beans.keySet())
                        {
                            BeanDefinition def = ((AbstractApplicationContext)ctx).getBeanFactory().getBeanDefinition(key);
                            LOG.warn("*   " + key + " at " + (
                                            (def.getResourceDescription() == null) ? "unknown location" :
                                                            def.getResourceDescription()));
                        }
                    }
                    LOG.warn("*");
                    LOG.warn("**************************************************");
                }
            }
        }
    }
}
