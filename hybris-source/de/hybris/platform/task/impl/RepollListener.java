package de.hybris.platform.task.impl;

import de.hybris.platform.task.TaskEngine;
import de.hybris.platform.task.TaskService;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class RepollListener implements ApplicationListener, ApplicationContextAware
{
    private static final Logger LOG = Logger.getLogger(RepollListener.class.getName());
    private ApplicationContext ctx;


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.ctx = applicationContext;
    }


    public void onApplicationEvent(ApplicationEvent event)
    {
        if(event instanceof RepollEvent)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Received repoll event: " + event);
                LOG.debug("using applicationContext: " + this.ctx);
            }
            TaskEngine engine = ((TaskService)this.ctx.getBean("taskService")).getEngine();
            if(engine != null)
            {
                RepollEvent repollEvent = (RepollEvent)event;
                engine.repollIfNecessary(repollEvent.getNodeId(), repollEvent.getNodeGroupId());
            }
        }
    }
}
