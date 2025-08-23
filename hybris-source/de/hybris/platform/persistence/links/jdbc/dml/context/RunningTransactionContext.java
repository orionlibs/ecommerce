package de.hybris.platform.persistence.links.jdbc.dml.context;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.WritePersistenceGateway;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import java.util.Date;

class RunningTransactionContext extends NewTransactionContext
{
    public RunningTransactionContext(String relationCode, WritePersistenceGateway writePersistenceGateway, boolean parentIsSource, Date now)
    {
        super(relationCode, writePersistenceGateway, parentIsSource, now);
    }


    public void shiftExistingLink(long linkPK, int newPosition, long version, long parentPK, Long childPK)
    {
        PK pk = getPKFrom(linkPK);
        if(!shouldUseDirectPersistenceForTouching(pk))
        {
            super.shiftExistingLink(linkPK, newPosition, version, parentPK, childPK);
            return;
        }
        Item itemToShift = getItemByPK(pk);
        try
        {
            itemToShift.setAttribute(this.positionAttributeName, Integer.valueOf(newPosition));
        }
        catch(JaloInvalidParameterException | de.hybris.platform.jalo.JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public void touchExistingItem(long pkValue)
    {
        PK pk = getPKFrom(pkValue);
        if(shouldUseDirectPersistenceForTouching(pk))
        {
            super.touchExistingItem(pkValue);
        }
        else
        {
            Item itemToTouch = getItemByPK(pk);
            itemToTouch.setModificationTime(now());
        }
    }


    public boolean shouldUseDirectPersistenceForTouching(PK pk)
    {
        return false;
    }
}
