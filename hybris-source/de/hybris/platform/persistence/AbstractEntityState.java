package de.hybris.platform.persistence;

import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.framework.PersistencePool;
import de.hybris.platform.persistence.hjmp.EntityState;

public abstract class AbstractEntityState implements EntityState
{
    private final PersistencePool pool;
    private final ItemDeployment depl;


    protected AbstractEntityState(PersistencePool pool, ItemDeployment depl)
    {
        this.pool = pool;
        this.depl = depl;
    }


    protected String getTable()
    {
        return this.depl.getDatabaseTableName();
    }


    protected String getColumn(String qualifier)
    {
        return this.depl.getColumnName(qualifier, this.pool.getDatabase());
    }


    protected boolean checkPK(PK pk)
    {
        return (this.depl.getTypeCode() == pk.getTypeCode());
    }


    protected PersistencePool getPool()
    {
        return this.pool;
    }


    protected ItemDeployment getDeployment()
    {
        return this.depl;
    }
}
