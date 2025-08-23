package de.hybris.platform.persistence;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.c2l.LocalizableItemEJB;
import de.hybris.platform.persistence.property.EJBPropertyContainer;
import de.hybris.platform.persistence.security.ACLCache;
import de.hybris.platform.persistence.security.GlobalACLCacheKey;
import de.hybris.platform.persistence.type.ComposedTypeRemote;

public abstract class GenericItemEJB extends LocalizableItemEJB implements GenericItemRemote, GenericItemHome
{
    protected int typeCode()
    {
        return 99;
    }


    public PK ejbCreate(PK pk, ComposedTypeRemote type, EJBPropertyContainer props)
    {
        return doCreateInternal(pk, type, null, props);
    }


    public void ejbPostCreate(PK pk, ComposedTypeRemote type, EJBPropertyContainer props)
    {
        doPostCreateInternal(pk, type, null);
    }


    protected boolean hasModifiedCaches()
    {
        ACLCache globalAclCache;
        switch(typeCode())
        {
            case 4:
            case 5:
                globalAclCache = (ACLCache)getCachedValueForReadingIfAvailable((ItemCacheKey)new GlobalACLCacheKey());
                return ((globalAclCache != null && globalAclCache.needsUpdate()) || super.hasModifiedCaches());
        }
        return super.hasModifiedCaches();
    }
}
