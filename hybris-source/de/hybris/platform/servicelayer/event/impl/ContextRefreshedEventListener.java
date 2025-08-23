package de.hybris.platform.servicelayer.event.impl;

import de.hybris.platform.spring.ctx.CloseAwareApplicationContext;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent>
{
    private static final Logger LOG = Logger.getLogger(ContextRefreshedEventListener.class.getName());


    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        if(event.getApplicationContext() instanceof CloseAwareApplicationContext)
        {
            CloseAwareApplicationContext ctx = (CloseAwareApplicationContext)event.getApplicationContext();
            PlatformClusterEventSender clusterSender = (PlatformClusterEventSender)ctx.getBean("platformClusterEventSender", PlatformClusterEventSender.class);
            clusterSender.registerBinaryListenerHook();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Registered a binary listener for " +
                                ToStringBuilder.reflectionToString(clusterSender) + " in context <<" + ctx + ">>");
            }
        }
    }
}
