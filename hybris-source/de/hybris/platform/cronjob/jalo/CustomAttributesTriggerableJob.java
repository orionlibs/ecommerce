package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import java.util.HashMap;
import java.util.Map;

public class CustomAttributesTriggerableJob extends Job implements TriggerableJob
{
    public static final String ATTRIBUTE_CRON_JOB_THREE_ID = "attributeCronJobThree";
    public static final String ATTRIBUTE_CRON_JOB_TWO_ID = "attributeCronJobTwo";
    public static final String ATTRIBUTE_CRON_JOB_ONE_ID = "attributeCronJobOne";
    public static final String ATTRIBUTE_THREE_JOB_ID = "attributteThree";
    public static final String ATTRIBUTE_TWO_JOB_ID = "attributteTwo";
    public static final String ATTRIBUTE_ONE_JOB_ID = "attributteOne";
    public static String STATICCJOBCODE = "CustomAttributesTriggerableJob";
    public static String STATICCRONJOBCODE = "CustomAttributesTriggerableCronJob";


    protected CronJob.CronJobResult performCronJob(CronJob cronJob) throws AbortCronJobException
    {
        try
        {
            Thread.sleep(1000L);
        }
        catch(Exception e1)
        {
            return cronJob.getFinishedResult(false);
        }
        return cronJob.getFinishedResult(true);
    }


    public CronJob newExecution()
    {
        ComposedType type = (ComposedType)TypeManager.getInstance().getType(STATICCRONJOBCODE);
        try
        {
            Map<Object, Object> map = new HashMap<>();
            map.put("job", this);
            return (CronJob)type.newInstance(map);
        }
        catch(JaloGenericCreationException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @ForceJALO(reason = "something else")
    protected Map<String, String> getConfigAttributes(CronJob cronjob)
    {
        Map<String, String> jobParams = super.getConfigAttributes(cronjob);
        jobParams.put("attributteOne", "attributeCronJobOne");
        jobParams.put("attributteTwo", "attributeCronJobTwo");
        jobParams.put("attributteThree", "attributeCronJobThree");
        return jobParams;
    }
}
