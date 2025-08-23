package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.logging.HybrisLogListener;
import de.hybris.platform.util.logging.HybrisLoggingEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.context.support.AbstractApplicationContext;

public class CronJobToJspLogListener implements HybrisLogListener
{
    private static final Logger LOGGER = Logger.getLogger(CronJobToJspLogListener.class);
    private final JspContext jspc;
    private final CronJob cronjob;


    public CronJobToJspLogListener(CronJob cronjob, JspContext jspc)
    {
        this.jspc = jspc;
        this.cronjob = cronjob;
    }


    public void log(HybrisLoggingEvent event)
    {
        try
        {
            CronJob curCronjob = CronJob.getCurrentlyExecutingCronJob();
            if(curCronjob != null && curCronjob.equals(this.cronjob))
            {
                CronJobUtils.sendMsgToJsp("" + event.getLevel() + " - " + event.getLevel() + "<br>", this.jspc);
            }
        }
        catch(IllegalStateException e)
        {
            LOGGER.error("Have to log cronjob specific, but no cronjob is set");
        }
    }


    public boolean isEnabledFor(Level level)
    {
        CronJob curCronjob = CronJob.getCurrentlyExecutingCronJobFailSave();
        if(curCronjob != null && curCronjob.equals(this.cronjob))
        {
            AbstractBeanFactory beanFactory = (AbstractBeanFactory)((AbstractApplicationContext)Registry.getGlobalApplicationContext()).getBeanFactory();
            if(beanFactory.isCurrentlyInCreation(ComposedType.class.getName()) || beanFactory
                            .isCurrentlyInCreation(EnumerationType.class.getName()) || beanFactory
                            .isCurrentlyInCreation(EnumerationValue.class.getName()))
            {
                return level.isGreaterOrEqual((Priority)Level.WARN);
            }
            return this.cronjob.isEnabledFor(level);
        }
        return false;
    }
}
