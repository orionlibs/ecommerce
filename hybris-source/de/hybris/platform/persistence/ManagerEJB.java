package de.hybris.platform.persistence;

import de.hybris.platform.core.Initialization;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.persistence.framework.PersistencePool;
import de.hybris.platform.util.jeeapi.YRemoveException;
import java.util.Map;

public abstract class ManagerEJB
{
    private final PersistencePool persistencePool = Registry.getCurrentTenant().getPersistencePool();


    protected PersistencePool getPersistencePool()
    {
        return this.persistencePool;
    }


    public void notifyItemRemove(ItemRemote item)
    {
    }


    public boolean canRemoveItem(ItemRemote item) throws ConsistencyCheckException
    {
        return true;
    }


    public boolean isReinitialization(Map params)
    {
        return !Initialization.forceClean(params);
    }


    public void removeItem(ItemRemote item) throws ConsistencyCheckException
    {
        try
        {
            prepareItemRemove(item);
            item.remove();
        }
        catch(YRemoveException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
    }


    public void prepareItemRemove(ItemRemote item) throws ConsistencyCheckException
    {
    }


    public void ejbCreate()
    {
    }


    public void ejbPostCreate()
    {
    }
}
