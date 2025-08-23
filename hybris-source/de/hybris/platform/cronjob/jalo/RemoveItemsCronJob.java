package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import org.apache.log4j.Logger;

public class RemoveItemsCronJob extends GeneratedRemoveItemsCronJob
{
    private static final Logger log = Logger.getLogger(RemoveItemsCronJob.class.getName());


    @ForceJALO(reason = "something else")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        Media itemPKs = getItemPKs(ctx);
        if(itemPKs != null)
        {
            try
            {
                itemPKs.remove(ctx);
            }
            catch(ConsistencyCheckException e)
            {
                log.error("cannot remove item pks media due to " + e.getMessage(), (Throwable)e);
            }
        }
        super.remove(ctx);
    }


    @ForceJALO(reason = "something else")
    public void setItemsFound(int param)
    {
        setProperty(getSession().getSessionContext(), "itemsFound", Integer.valueOf(param));
    }


    @ForceJALO(reason = "something else")
    public void setItemsDeleted(int param)
    {
        setProperty(getSession().getSessionContext(), "itemsDeleted", Integer.valueOf(param));
    }


    @ForceJALO(reason = "something else")
    public void setItemsRefused(int param)
    {
        setProperty(getSession().getSessionContext(), "itemsRefused", Integer.valueOf(param));
    }


    public void resetItemsCounter()
    {
        setItemsDeleted(0);
        setItemsRefused(0);
    }
}
