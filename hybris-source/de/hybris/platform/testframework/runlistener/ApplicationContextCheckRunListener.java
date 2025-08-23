package de.hybris.platform.testframework.runlistener;

import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.springframework.context.ApplicationContext;

public class ApplicationContextCheckRunListener extends RunListener
{
    private static final Logger LOG = Logger.getLogger(ApplicationContextCheckRunListener.class);
    private ApplicationContext oldContext;
    private Description classDescription;


    public void testRunStarted(Description description) throws Exception
    {
        if(!HybrisJUnit4Test.intenseChecksActivated())
        {
            return;
        }
        this.oldContext = Registry.getApplicationContext();
        this.classDescription = description;
        LOG.info("saved application context for " + description.getClassName());
    }


    public void testRunFinished(Result result) throws Exception
    {
        if(!HybrisJUnit4Test.intenseChecksActivated())
        {
            return;
        }
        long executionTime = System.currentTimeMillis();
        ApplicationContext newContext = Registry.getApplicationContext();
        for(String bean : newContext.getBeanDefinitionNames())
        {
            try
            {
                if(!this.oldContext.containsBeanDefinition(bean))
                {
                    LOG.error(this.classDescription.getClassName() + ": There is a new bean called '" + this.classDescription.getClassName() + "'");
                }
                if(!this.oldContext.getBean(bean).getClass().equals(newContext.getBean(bean).getClass()))
                {
                    LOG.error(this.classDescription.getClassName() + ": The implementation of '" + this.classDescription.getClassName() + "' has changed from '" + bean + "' to '" + this.oldContext
                                    .getBean(bean).getClass() + "'!");
                }
            }
            catch(Exception exception)
            {
            }
        }
        for(String bean : this.oldContext.getBeanDefinitionNames())
        {
            try
            {
                if(!newContext.containsBeanDefinition(bean))
                {
                    LOG.error(this.classDescription.getClassName() + ": Bean '" + this.classDescription.getClassName() + "' disappeared!");
                }
                if(!this.oldContext.getBean(bean).getClass().equals(newContext.getBean(bean).getClass()))
                {
                    LOG.error(this.classDescription.getClassName() + ": The implementation of '" + this.classDescription.getClassName() + "' has changed from '" + bean + "' to '" + this.oldContext
                                    .getBean(bean).getClass() + "'!");
                }
            }
            catch(Exception exception)
            {
            }
        }
        LOG.info("finished analyzing application context for " + this.classDescription.getClassName() + " in " +
                        System.currentTimeMillis() - executionTime + "ms!");
    }
}
