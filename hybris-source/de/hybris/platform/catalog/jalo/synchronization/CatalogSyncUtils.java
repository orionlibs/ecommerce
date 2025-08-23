package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.persistence.hjmp.HJMPUtils;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.jeeapi.YNoSuchEntityException;

public class CatalogSyncUtils
{
    public static final int ITEM_SYNC_TIMESTAMP_CODE = 619;


    public static boolean shouldRetryAfter(Exception e)
    {
        if(Utilities.getRootCauseOfType(e, ConcurrentModificationDuringSyncException.class) != null)
        {
            return true;
        }
        if(indicatesThatItemSyncTimestampHasBeenConcurrentlyRemoved(e))
        {
            return true;
        }
        return ((HJMPUtils.isOptimisticLockingEnabled() && HJMPUtils.isConcurrentModificationException(e)) ||
                        HJMPUtils.isDuplicateKeyExceptionOnTypeCode(e, 619));
    }


    private static boolean indicatesThatItemSyncTimestampHasBeenConcurrentlyRemoved(Exception e)
    {
        PK pk = null;
        YNoSuchEntityException hjmpException = (YNoSuchEntityException)Utilities.getRootCauseOfType(e, YNoSuchEntityException.class);
        if(hjmpException != null)
        {
            pk = hjmpException.getPk();
        }
        JaloObjectNoLongerValidException jaloException = (JaloObjectNoLongerValidException)Utilities.getRootCauseOfType(e, JaloObjectNoLongerValidException.class);
        if(jaloException != null)
        {
            pk = jaloException.getJaloObjectPK();
        }
        return (pk != null && 619 == pk.getTypeCode());
    }
}
