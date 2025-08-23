package de.hybris.platform.persistence.framework;

import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;

public class EntityInstanceContextImpl implements EntityInstanceContext
{
    private PK pk;
    private final PersistencePool pool;
    private final ItemDeployment deployment;


    EntityInstanceContextImpl(PersistencePool pool, ItemDeployment deployment)
    {
        this.pool = pool;
        this.deployment = deployment;
        setPK(null);
    }


    public PK getPK()
    {
        return this.pk;
    }


    public void setPK(PK pk)
    {
        checkItemTypeCode(pk);
        this.pk = pk;
    }


    protected void checkItemTypeCode(PK pk)
    {
        if(pk != null)
        {
            if(pk.getTypeCode() != this.deployment.getTypeCode())
            {
                throw new IllegalArgumentException("invalid pk " + pk + " for entity context with deployment " + this.deployment
                                .getName() + " ( " + pk.getTypeCode() + "<>" + this.deployment
                                .getTypeCode() + " )");
            }
        }
    }


    public PersistencePool getPersistencePool()
    {
        return this.pool;
    }


    public ItemDeployment getItemDeployment()
    {
        return this.deployment;
    }
}
