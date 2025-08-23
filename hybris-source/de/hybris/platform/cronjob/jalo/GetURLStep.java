package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInternalException;
import java.io.IOException;
import org.apache.log4j.Logger;

public class GetURLStep extends GeneratedGetURLStep
{
    private static final Logger log = Logger.getLogger(GetURLStep.class.getName());


    protected boolean canUndo(CronJob cronJob)
    {
        return false;
    }


    protected void performStep(CronJob cronJob) throws AbortCronJobException
    {
        JobMedia media = prepareMedia(cronJob);
        try
        {
            media.setLocked(true);
            if(cronJob instanceof URLCronJob)
            {
                String url = ((URLCronJob)cronJob).getURL();
                if(url != null && !"".equals(url))
                {
                    if(!url.equals(media.getURL()) && media.hasData())
                    {
                        media.removeData(true);
                    }
                    media.setURL(url);
                    media.setDataByURL();
                }
                cronJob.addLog("performStep finished without exceptions [STEP:" + this + "; CronJob: " + cronJob + "]", (Step)this);
            }
            else
            {
                cronJob.addLog("can not perform step [STEP:" + this + "; CronJob: " + cronJob + "]", (Step)this);
            }
        }
        catch(JaloBusinessException e)
        {
            throw new JaloInternalException(e);
        }
        catch(IOException e)
        {
            throw new JaloInternalException(e);
        }
        finally
        {
            media.setLocked(false);
        }
    }


    protected void undoStep(CronJob cronJob)
    {
    }


    @ForceJALO(reason = "something else")
    public boolean isAbortable()
    {
        return false;
    }
}
