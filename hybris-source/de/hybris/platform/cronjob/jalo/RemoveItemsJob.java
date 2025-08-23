package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.core.PK;
import de.hybris.platform.hmc.jalo.AccessManager;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.security.JaloSecurityException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class RemoveItemsJob extends GeneratedRemoveItemsJob
{
    private static final Logger LOG = Logger.getLogger(RemoveItemsJob.class.getName());
    private static final long STATUS_UPDATE_INTERVAL = 10000L;


    protected CronJob.CronJobResult performCronJob(CronJob cronJob)
    {
        RemoveItemsCronJob cron = (RemoveItemsCronJob)cronJob;
        int counter = 1;
        int deleted = cron.getItemsDeletedAsPrimitive();
        int refused = cron.getItemsRefusedAsPrimitive();
        int toSkip = deleted + refused;
        boolean error = false;
        long timestamp = System.currentTimeMillis();
        BufferedReader bufferedReader = null;
        try
        {
            bufferedReader = new BufferedReader(new InputStreamReader(cron.getItemPKs().getDataFromStream()));
            if(!cron.isCreateSavedValuesAsPrimitive())
            {
                JaloSession.getCurrentSession().getSessionContext().removeAttribute("is.hmc.session");
            }
            while(bufferedReader.ready())
            {
                String line = bufferedReader.readLine();
                if(counter++ <= toSkip)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("skipped line " + counter + "'" + line + "' since it is already processed");
                    }
                    continue;
                }
                if(deleteItem(PK.parse(line)))
                {
                    deleted++;
                }
                else
                {
                    refused++;
                }
                if(System.currentTimeMillis() - timestamp > 10000L)
                {
                    cron.setItemsDeleted(deleted);
                    cron.setItemsRefused(refused);
                    timestamp = System.currentTimeMillis();
                }
            }
        }
        catch(Exception e)
        {
            LOG.error("unknown error : " + e.getMessage(), e);
            error = true;
        }
        finally
        {
            cron.setItemsDeleted(deleted);
            cron.setItemsRefused(refused);
            if(bufferedReader != null)
            {
                try
                {
                    bufferedReader.close();
                }
                catch(IOException e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(e.getMessage());
                    }
                }
            }
        }
        return cron.getFinishedResult((!error && refused == 0));
    }


    private boolean deleteItem(PK pk)
    {
        boolean result = false;
        try
        {
            Item item = JaloSession.getCurrentSession().getItem(pk);
            result = deleteItem(item);
        }
        catch(JaloSecurityException e)
        {
            LOG.warn(e.getMessage());
        }
        catch(ConsistencyCheckException e)
        {
            LOG.error(e.getMessage());
        }
        catch(JaloItemNotFoundException e)
        {
            LOG.warn("skipping item " + pk + ". item was not found: " + e.getMessage());
        }
        catch(Exception e)
        {
            LOG.error("skipping item " + pk + ". unknown error: " + e.getMessage());
        }
        return result;
    }


    public static boolean deleteItem(Item item) throws ConsistencyCheckException, JaloSecurityException
    {
        AccessManager accessMan = AccessManager.getInstance();
        boolean result = false;
        if(accessMan.canRemoveInstance(item))
        {
            item.remove();
            result = true;
        }
        else
        {
            String user = JaloSession.getCurrentSession().getUser().getLogin();
            throw new JaloSecurityException("current user \"" + user + "\" has no right to delete item: " + item
                            .getPK().toString(), 0);
        }
        return result;
    }
}
