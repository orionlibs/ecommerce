package de.hybris.platform.persistence;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.BridgeImplementation;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.security.EJBSecurityException;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.persistence.type.TypeManagerEJB;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.ItemPropertyValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class ItemEJBImpl extends BridgeImplementation implements Item.ItemImpl
{
    private volatile transient ItemRemote remote;
    private final Tenant tenant;
    private final PK jaloPK;
    private volatile PK typeKeyCache;


    public ItemPermissionFacade getPermissionFacade()
    {
        return (ItemPermissionFacade)getRemote();
    }


    public Class getJaloObjectClass()
    {
        return getComposedType().getJaloClass();
    }


    protected ItemEJBImpl(Tenant tenant, ItemRemote remoteObject)
    {
        this.tenant = Objects.<Tenant>requireNonNull(tenant);
        setRemote(remoteObject);
        this.jaloPK = EJBTools.getPK(remoteObject);
        this.typeKeyCache = remoteObject.getTypeKey();
    }


    public long getHJMPTS()
    {
        return getRemote().getHJMPTS();
    }


    public void setRemote(ItemRemote remote)
    {
        this.remote = remote;
    }


    public PK getPK()
    {
        return this.jaloPK;
    }


    public void invalidateLocalCaches()
    {
        this.typeKeyCache = null;
    }


    public PK getTypeKey()
    {
        if(this.typeKeyCache == null)
        {
            this.typeKeyCache = getRemote().getTypeKey();
        }
        return this.typeKeyCache;
    }


    private void clearRemote()
    {
        setRemote(null);
    }


    public Date getCreationTime()
    {
        return getRemote().getCreationTime();
    }


    public void setCreationTime(Date time)
    {
        getRemote().setCreationTime(time);
    }


    public Date getModificationTime()
    {
        return getRemote().getModifiedTime();
    }


    public void setModificationTime(Date timestamp)
    {
        getRemote().setModifiedTime(timestamp);
    }


    public JaloSession getSession()
    {
        return JaloSession.getCurrentSession();
    }


    public final void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        removeInternal(ctx);
        clearRemote();
    }


    protected abstract void removeInternal(SessionContext paramSessionContext) throws ConsistencyCheckException;


    public ComposedType getComposedType()
    {
        try
        {
            PK tpk = getTypeKey();
            return (tpk != null) ?
                            (ComposedType)WrapperFactory.getCachedItem(getCache(), tpk) :
                            (ComposedType)wrap(getRemote().getComposedType());
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
        catch(IllegalArgumentException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public void setComposedType(ComposedType type) throws JaloInvalidParameterException
    {
        try
        {
            ((TypeManagerEJB)TypeManager.getInstance().getRemote()).changeItemType(getRemote(), (ComposedTypeRemote)
                            unwrap(type));
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 0);
        }
        finally
        {
            this.typeKeyCache = null;
        }
    }


    public Item getOwner(SessionContext ctx)
    {
        return (Item)wrap(getRemote().getOwner());
    }


    public void setOwner(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
        if(isHandlingOwnerAsReferenceSupported())
        {
            ItemPropertyValue ownerRef = unwrapToRef(item);
            getRemote().setOwnerRef(ownerRef);
            return;
        }
        getRemote().setOwner((ItemRemote)unwrap(item));
    }


    protected boolean isHandlingOwnerAsReferenceSupported()
    {
        return false;
    }


    protected ItemRemote getRemote()
    {
        if(this.remote == null)
        {
            refreshRemote();
        }
        return this.remote;
    }


    private synchronized void refreshRemote() throws JaloObjectNoLongerValidException
    {
        PK pk = getPK();
        try
        {
            setRemote(this.tenant.getSystemEJB().findRemoteObjectByPK(pk));
        }
        catch(EJBItemNotFoundException e)
        {
            throw new JaloObjectNoLongerValidException(pk, e, "refreshRemote(" + pk + ") object no longer valid", 0);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 0);
        }
    }


    public int checkItemPermission(PK princpalPK, PK rightPK)
    {
        return getPermissionFacade().checkItemPermission(princpalPK, rightPK);
    }


    public Collection getRestrictedPrincipals()
    {
        return (Collection)wrap(getPermissionFacade().getRestrictedPrincipalPKs());
    }


    public Collection getPrincipalPermissions(PK principalPK, boolean negative)
    {
        return (Collection)wrap(getPermissionFacade().getPermissionPKs(principalPK, negative));
    }


    public Map getPermissionMap(List<? extends Item> userRights)
    {
        return (Map)wrap(getPermissionFacade().getPrincipalToBooleanListMap(toPKList(userRights)));
    }


    public void setPermissionMap(List<UserRight> userRights, Map<Principal, List<Boolean>> permissionMap) throws JaloSecurityException
    {
        try
        {
            Map<PK, List<Boolean>> principalPKMap = new HashMap<>();
            for(Iterator<Map.Entry<Principal, List<Boolean>>> it = permissionMap.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry<Principal, List<Boolean>> mapEntry = it.next();
                principalPKMap.put(((Principal)mapEntry.getKey()).getPK(), mapEntry.getValue());
            }
            getPermissionFacade().setPrincipalToBooleanListMap(toPKList((List)userRights), principalPKMap);
        }
        catch(EJBSecurityException e)
        {
            throw new JaloSecurityException(e, e.getErrorCode());
        }
    }


    public void addPermission(PK principalPK, PK rightPK, boolean negative)
    {
        try
        {
            getPermissionFacade().setPermission(principalPK, rightPK, negative);
        }
        catch(EJBSecurityException e)
        {
            throw new JaloInternalException(e);
        }
    }


    public void clearPermission(PK principalPK, PK rightPK)
    {
        try
        {
            getPermissionFacade().removePermission(principalPK, rightPK);
        }
        catch(EJBSecurityException e)
        {
            throw new JaloInternalException(e);
        }
    }


    private static final List<PK> toPKList(List<? extends Item> itemList)
    {
        if(itemList == null)
        {
            return null;
        }
        List<PK> ret = new ArrayList<>();
        for(Iterator<? extends Item> it = itemList.iterator(); it.hasNext(); )
        {
            Item item = it.next();
            ret.add((item != null) ? item.getPK() : null);
        }
        return ret;
    }


    public Object wrap(Object object)
    {
        return WrapperFactory.wrap(getCache(), object);
    }


    public Object unwrap(Object object)
    {
        return WrapperFactory.unwrap(getCache(), object);
    }


    public ItemPropertyValue unwrapToRef(Item item)
    {
        return (ItemPropertyValue)WrapperFactory.unwrap(getCache(), item, true);
    }


    protected final Cache getCache()
    {
        return this.tenant.getCache();
    }
}
