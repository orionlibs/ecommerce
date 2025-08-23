package de.hybris.platform.persistence.security;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.persistence.ItemCacheKey;
import de.hybris.platform.persistence.ItemEJB;
import org.apache.log4j.Logger;

public class ItemACLCacheKey extends ItemCacheKey
{
    public static final String BASE_QUALIFIER = "aclcache";
    private static final String QUALI = "aclcache.item";
    private static final Logger log = Logger.getLogger(ItemACLCacheKey.class);
    protected final boolean cloned;
    protected PK itemPK = null;


    public ItemACLCacheKey()
    {
        this.cloned = false;
    }


    protected ItemACLCacheKey(PK itemPK)
    {
        this.itemPK = itemPK;
        this.cloned = true;
    }


    protected Object clone() throws CloneNotSupportedException
    {
        if(this.cloned)
        {
            throw new EJBInternalException(null, "you cannot clone a cloned ItemACLCacheKey again", 0);
        }
        return new ItemACLCacheKey(this.itemPK);
    }


    protected String getQualifier()
    {
        return "aclcache.item";
    }


    protected boolean isValid(ItemEJB item)
    {
        ACLCache ac = (ACLCache)getValueInternal();
        boolean isValid = (ac == null || item.getPK().equals(this.itemPK));
        if(!isValid)
        {
            throw new JaloSystemException(null, "ItemACLCacheKey not valid:\n  pc = " + ac + "\n  item pk = '" + this.itemPK + "' : acl cache pk = '" + item
                            .getPkString() + "'\n  item version = " + item.getACLTimestampInternal() + " : acl cache version = " + ac
                            .getVersion(), 0);
        }
        return isValid;
    }


    protected void dispose(boolean inRemove)
    {
        ACLCache ac = (ACLCache)getValueInternal();
        if(!inRemove && ac != null && ac.needsUpdate())
        {
            throw new JaloSystemException(null, "removing changed old acl cache from item cache will lose these changes : itemPK = " + this.itemPK, 0);
        }
        super.dispose(inRemove);
    }


    protected Object computeValue(ItemEJB item)
    {
        long t1 = System.currentTimeMillis();
        this.itemPK = item.getPK();
        ACLCache acl = ACLEntryJDBC.getACLCache(this.itemPK, item.getACLTimestampInternal(), item.getEntityContext()
                        .getPersistencePool()
                        .getDataSource());
        long t2 = System.currentTimeMillis();
        if(log.isDebugEnabled())
        {
            log.debug("Loaded item acl for item " + this.itemPK + " in " + t2 - t1 + "ms");
        }
        return acl;
    }


    protected Object cloneValue(Object value)
    {
        try
        {
            return ((ACLCache)value).clone();
        }
        catch(CloneNotSupportedException e)
        {
            throw new EJBInternalException(e, "EJBPropertyCache should be cloneable, but...", 0);
        }
    }


    protected Object createUnmodifiableViewOfValue(Object value)
    {
        return value;
    }
}
