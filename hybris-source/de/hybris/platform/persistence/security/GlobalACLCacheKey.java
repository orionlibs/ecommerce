package de.hybris.platform.persistence.security;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.persistence.ItemEJB;
import org.apache.log4j.Logger;

public class GlobalACLCacheKey extends ItemACLCacheKey
{
    private static final String QUALI = "aclcache.global".intern();
    private static final Logger log = Logger.getLogger(GlobalACLCacheKey.class);


    public GlobalACLCacheKey()
    {
    }


    protected GlobalACLCacheKey(PK principalPK)
    {
        super(principalPK);
    }


    protected Object clone() throws CloneNotSupportedException
    {
        if(this.cloned)
        {
            throw new EJBInternalException(null, "you cannot clone a cloned GlobalACLCacheKey again", 0);
        }
        return new GlobalACLCacheKey(this.itemPK);
    }


    protected String getQualifier()
    {
        return QUALI;
    }


    protected Object computeValue(ItemEJB item)
    {
        long t1 = System.currentTimeMillis();
        this.itemPK = item.getPK();
        ACLCache acl = ACLEntryJDBC.getGlobalACLCache(this.itemPK, item.getACLTimestampInternal(), item.getEntityContext()
                        .getPersistencePool()
                        .getDataSource());
        long t2 = System.currentTimeMillis();
        if(log.isDebugEnabled())
        {
            log.debug("Loaded global acl for principal " + this.itemPK + " in " + t2 - t1 + "ms");
        }
        return acl;
    }
}
