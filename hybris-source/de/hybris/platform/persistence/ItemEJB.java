package de.hybris.platform.persistence;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.security.PermissionContainer;
import de.hybris.platform.persistence.framework.EntityInstance;
import de.hybris.platform.persistence.framework.EntityInstanceContext;
import de.hybris.platform.persistence.security.ACLCache;
import de.hybris.platform.persistence.security.ACLEntryJDBC;
import de.hybris.platform.persistence.security.EJBSecurityException;
import de.hybris.platform.persistence.security.GlobalACLCacheKey;
import de.hybris.platform.persistence.security.ItemACLCacheKey;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.persistence.type.TypeTools;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.config.PropertyActionReader;
import de.hybris.platform.util.jeeapi.YEJBException;
import de.hybris.platform.util.jeeapi.YRemoveException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public abstract class ItemEJB implements EntityInstance, ItemRemote, ItemHome, ItemPermissionFacade
{
    private static final Logger LOG = Logger.getLogger(ItemEJB.class.getName());
    protected static final boolean READ = false;
    protected static final boolean WRITE = true;
    protected EntityInstanceContext entityContext;
    private boolean clearEntityCachesHasBeenCalled = true;
    private boolean needsStoringFlag;


    public PK getPK()
    {
        return this.entityContext.getPK();
    }


    public PK getTypeKey()
    {
        PK pk = getTypePkString();
        return (pk != null && !PK.NULL_PK.equals(pk)) ? pk : null;
    }


    public static String quoteSQLStringLiteralForQuery(String str)
    {
        if(str == null)
        {
            return null;
        }
        if(str.length() == 0)
        {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < str.length(); i++)
        {
            char character = str.charAt(i);
            if(character == '\'')
            {
                stringBuilder.append(character).append(character);
            }
            else
            {
                stringBuilder.append(character);
            }
        }
        return stringBuilder.toString();
    }


    public void ejbHomeLoadItemData(ResultSet resultSet)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("got resultset " + resultSet + " - doing nothing");
        }
    }


    protected PK doCreateInternal(PK pk, ComposedTypeRemote type, ItemRemote template)
    {
        PK ret = performEjbCreate(pk, type);
        if(type == null && template != null)
        {
            copyComposedTypeFromTemplate(template);
        }
        if(template != null)
        {
            createFromTemplate(template);
        }
        return ret;
    }


    protected void createFromTemplate(ItemRemote template)
    {
        setCreationTimestampInternal(template.getCreationTime());
        setOwner(template.getOwner());
    }


    protected final void copyComposedTypeFromTemplate(ItemRemote template)
    {
        if(template.hasJNDIName(getOwnJNDIName()))
        {
            try
            {
                setComposedType(template.getComposedType());
            }
            catch(EJBInvalidParameterException e)
            {
                throw new EJBInternalException(e, "types of the same ejb should always be transferable", 0);
            }
        }
    }


    protected void postCreateFromTemplate(ItemRemote template)
    {
    }


    protected void doPostCreateInternal(PK pkBase, ComposedTypeRemote type, ItemRemote template)
    {
        if(template != null)
        {
            postCreateFromTemplate(template);
        }
        storeCaches();
    }


    private PK performEjbCreate(PK pk, ComposedTypeRemote type)
    {
        PK pkToUse;
        if(pk == null)
        {
            pkToUse = PK.createCounterPK(typeCode());
        }
        else
        {
            if(pk.getTypeCode() != typeCode())
            {
                throw new RuntimeException("typecodes do not match during ejbCreate! " + pk);
            }
            pkToUse = pk;
        }
        this.entityContext.setPK(pkToUse);
        setPkString(pkToUse);
        Date timestamp = new Date();
        setCreationTimestampInternal(timestamp);
        setModifiedTimestamp(timestamp);
        try
        {
            setComposedType(type);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new EJBInternalException(e, "wrong initial type '" + TypeTools.asString(type) + "' " + e.getMessage(), 0);
        }
        return null;
    }


    protected void removeACLEntries()
    {
        if(!PropertyActionReader.getPropertyActionReader().isActionDisabledForType("acl.removal", getComposedType().getCode()))
        {
            ACLEntryJDBC.removeAllEntries(getPK(), getEntityContext().getPersistencePool().getDataSource());
        }
    }


    protected boolean writeACLEntries()
    {
        Collection<Object> caches = getCachedValuesStartingWith("aclcache");
        if(caches == null || caches.isEmpty())
        {
            return false;
        }
        boolean changed = false;
        for(Iterator<Object> it = caches.iterator(); it.hasNext(); )
        {
            ACLCache cache = (ACLCache)it.next();
            if(cache.needsUpdate())
            {
                ACLEntryJDBC.writeCache(cache, getEntityContext().getPersistencePool().getDataSource());
                changed = true;
            }
        }
        return changed;
    }


    public void setEntityContext(EntityInstanceContext ctx)
    {
        this.entityContext = ctx;
    }


    public EntityInstanceContext getEntityContext()
    {
        return this.entityContext;
    }


    public void ejbStore()
    {
    }


    public void ejbLoad()
    {
        assertClearEntityCaches();
    }


    protected boolean hasModifiedCaches()
    {
        ACLCache aclCache = (ACLCache)getCachedValueForReadingIfAvailable((ItemCacheKey)new ItemACLCacheKey());
        return (aclCache != null && aclCache.needsUpdate());
    }


    public void ejbRemove()
    {
        assertClearEntityCaches();
    }


    protected void clearEntityCaches()
    {
        this.clearEntityCachesHasBeenCalled = true;
    }


    private final void assertClearEntityCaches()
    {
        this.clearEntityCachesHasBeenCalled = false;
        clearEntityCaches();
        if(!this.clearEntityCachesHasBeenCalled)
        {
            throw new JaloSystemException(null, "entity " + this.entityContext.getPK() + " did not call clearEntityCaches in EntityBeanAdapter", 61);
        }
    }


    public Date getCreationTime()
    {
        return getCreationTimestampInternal();
    }


    public void setCreationTime(Date date)
    {
        if(date == null)
        {
            throw new JaloInvalidParameterException("creation time cannot be null", 0);
        }
        setCreationTimestampInternal(date);
    }


    public Date getModifiedTime()
    {
        return getModifiedTimestampInternal();
    }


    public void setModifiedTime(Date timestamp)
    {
        setModifiedTimestamp(timestamp);
    }


    protected void setModifiedTimestamp(Date timestamp)
    {
        setModifiedTimestampInternal(timestamp);
    }


    public boolean wasModifiedSince(Date time)
    {
        return (getModifiedTimestampInternal() != null && getModifiedTimestampInternal().before(time));
    }


    public ComposedTypeRemote getComposedType()
    {
        PK tpk = getTypePkString();
        return (ComposedTypeRemote)EJBTools.instantiatePK(tpk);
    }


    public void setComposedType(ComposedTypeRemote type) throws EJBInvalidParameterException
    {
        PK old = getTypePkString();
        PK pk = EJBTools.getPK((ItemRemote)type);
        try
        {
            if(pk == null)
            {
                pk = EJBTools.getPK((ItemRemote)getDefaultType());
            }
        }
        catch(EJBItemNotFoundException e)
        {
            throw new EJBInvalidParameterException(e);
        }
        if(old != pk && (old == null || !old.equals(pk)))
        {
            if(old != null)
            {
                typeChanged(old, pk);
            }
            setTypePkString(pk);
        }
    }


    protected void typeChanged(PK oldTypePK, PK newTypePK)
    {
    }


    protected ComposedTypeRemote getDefaultType() throws EJBItemNotFoundException
    {
        return getEntityContext().getPersistencePool()
                        .getTenant()
                        .getSystemEJB()
                        .getTypeManager()
                        .getRootComposedType(typeCode());
    }


    public ItemRemote getOwner()
    {
        PK ownerPk = getOwnerPkString();
        if(ownerPk == null)
        {
            return null;
        }
        ItemRemote owner = EJBTools.instantiatePK(ownerPk);
        if(owner == null)
        {
            LOG.error("owner pk '" + ownerPk + "' invalid - setting to NULL");
            setOwnerPkString(null);
        }
        return owner;
    }


    public void setOwner(ItemRemote owner)
    {
        setOwnerPK(EJBTools.getPK(owner));
    }


    public void setOwnerRef(ItemPropertyValue ownerRef)
    {
        setOwnerPK((ownerRef == null) ? null : ownerRef.getPK());
    }


    private void setOwnerPK(PK ownerPK)
    {
        PK old = getOwnerPkString();
        if(old != ownerPK && (old == null || !old.equals(ownerPK)))
        {
            setOwnerPkString(ownerPK);
        }
    }


    protected void storeCaches()
    {
    }


    protected Object getCachedValueForReading(ItemCacheKey key)
    {
        ItemCacheKey cacheEntry = getOrAddCacheKey(key);
        return cacheEntry.getValueForReading(this);
    }


    protected Object getCachedValueForModification(ItemCacheKey key)
    {
        ItemCacheKey cacheEntry = getOrAddCacheKey(key);
        return cacheEntry.getValueForModification(this);
    }


    protected Object getCachedValueForReadingIfAvailable(ItemCacheKey key)
    {
        ItemCacheKey cachedKey = getCacheKey(key.getQualifier());
        return (cachedKey != null) ? cachedKey.getValueForReadingIfAvailable(this) : null;
    }


    protected final Collection<Object> getCachedValuesStartingWith(String qualifierPrefix)
    {
        Map<Object, ItemCacheKey> map = getCacheKeyMap();
        if(map == null || map.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        Collection<Object> ret = new ArrayList();
        for(Iterator<Map.Entry<Object, ItemCacheKey>> it = map.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry<Object, ItemCacheKey> mapEntry = it.next();
            String key = (String)mapEntry.getKey();
            if(key.startsWith(qualifierPrefix))
            {
                Object value = ((ItemCacheKey)mapEntry.getValue()).getValueForReadingIfAvailable(this);
                if(value != null)
                {
                    ret.add(value);
                }
            }
        }
        return ret;
    }


    protected final ItemCacheKey getCacheKey(Object quali)
    {
        Map<Object, ItemCacheKey> map = getCacheKeyMap();
        ItemCacheKey key = map.get(quali);
        if(key != null && !key.isValid(this))
        {
            map.remove(quali);
            key.dispose(false);
            key = null;
        }
        return key;
    }


    protected final ItemCacheKey getOrAddCacheKey(ItemCacheKey key)
    {
        Object quali = key.getQualifier();
        ItemCacheKey result = getCacheKey(quali);
        if(result == null)
        {
            getCacheKeyMap().put(quali, result = key);
        }
        return result;
    }


    public Collection<PK> getRestrictedPrincipalPKs()
    {
        return getACLCacheInternal(false).getRestrictedPrincipals();
    }


    public Map<ItemPropertyValue, List<Boolean>> getPrincipalToBooleanListMap(List<PK> rightPKs)
    {
        Map<ItemPropertyValue, List<Boolean>> ret = new HashMap<>();
        Map<PK, List<Boolean>> pkMap = getACLCacheInternal(false).getPermissionMap(rightPKs);
        for(Iterator<Map.Entry<PK, List<Boolean>>> it = pkMap.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry<PK, List<Boolean>> mapEntry = it.next();
            ret.put(new ItemPropertyValue(mapEntry.getKey()), mapEntry.getValue());
        }
        return ret;
    }


    public void setPrincipalToBooleanListMap(List<PK> rightPKs, Map<PK, List<Boolean>> principalPKToBooleanListMap) throws EJBSecurityException
    {
        ACLCache aclCache = getACLCacheInternal(true);
        aclCache.setPermissionMap(rightPKs, principalPKToBooleanListMap);
        logACLCacheChange(aclCache);
    }


    protected boolean skipSetPermission(ACLCache acl, PK principalPK, PK permissionPK, boolean negative)
    {
        int match = acl.findPermission(principalPK, permissionPK);
        boolean matchPositiveOrNegative = ((negative && match == 1) || (!negative && match == 0));
        return (matchPositiveOrNegative && !acl.isPermissionRemoved(principalPK, permissionPK));
    }


    public boolean setPermission(PK principalPK, PK permissionPK, boolean negative) throws EJBSecurityException
    {
        ACLCache readACL = getACLCacheInternal(false);
        if(skipSetPermission(readACL, principalPK, principalPK, negative))
        {
            return false;
        }
        ACLCache writeACL = getACLCacheInternal(true);
        boolean answer = writeACL.setPermission(principalPK, permissionPK, negative);
        logACLCacheChange(writeACL);
        return answer;
    }


    public boolean setPermissions(Collection<PermissionContainer> permissions) throws EJBSecurityException
    {
        ACLCache readACL = getACLCacheInternal(false);
        if(permissions == null || permissions.isEmpty())
        {
            return false;
        }
        ACLCache writeACL = null;
        boolean resul = true;
        for(Iterator<PermissionContainer> it = permissions.iterator(); it.hasNext(); )
        {
            PermissionContainer permissionContainer = it.next();
            PK principalPK = permissionContainer.getPrincipalPK();
            PK permissionPK = permissionContainer.getRightPK();
            if(!skipSetPermission(readACL, principalPK, permissionPK, permissionContainer.isNegative()))
            {
                if(writeACL == null)
                {
                    writeACL = getACLCacheInternal(true);
                }
                resul = (writeACL.setPermission(principalPK, permissionPK, permissionContainer.isNegative()) && resul);
            }
        }
        if(writeACL != null)
        {
            logACLCacheChange(writeACL);
        }
        return resul;
    }


    protected boolean skipRemovePermission(ACLCache acl, PK principalPK, PK permissionPK)
    {
        int match = acl.findPermission(principalPK, permissionPK);
        return (match == -1);
    }


    public boolean removePermission(PK principalPK, PK permissionPK) throws EJBSecurityException
    {
        ACLCache readACL = getACLCacheInternal(false);
        if(skipRemovePermission(readACL, principalPK, permissionPK))
        {
            return false;
        }
        ACLCache writeACL = getACLCacheInternal(true);
        boolean answer = writeACL.removePermission(principalPK, permissionPK);
        logACLCacheChange(writeACL);
        return answer;
    }


    public boolean removePermissions(Collection<PermissionContainer> permissions) throws EJBSecurityException
    {
        ACLCache readACL = getACLCacheInternal(false);
        if(permissions == null || permissions.isEmpty())
        {
            return false;
        }
        boolean resul = true;
        ACLCache writeACL = null;
        for(Iterator<PermissionContainer> it = permissions.iterator(); it.hasNext(); )
        {
            PermissionContainer permissionContainer = it.next();
            PK principalPK = permissionContainer.getPrincipalPK();
            PK permissionPK = permissionContainer.getRightPK();
            if(!skipRemovePermission(readACL, principalPK, permissionPK))
            {
                if(writeACL == null)
                {
                    writeACL = getACLCacheInternal(true);
                }
                resul = (writeACL.removePermission(principalPK, permissionPK) && resul);
            }
        }
        if(writeACL != null)
        {
            logACLCacheChange(writeACL);
        }
        return resul;
    }


    public int checkItemPermission(PK principalPK, PK permissionPK)
    {
        return getACLCacheInternal(false).findPermission(principalPK, permissionPK);
    }


    public Collection<PK> getPermissionPKs(PK principalPK, boolean negative)
    {
        return getACLCacheInternal(false).getPermissions(principalPK, negative);
    }


    private final ACLCache getACLCacheInternal(boolean forWriting)
    {
        return forWriting ? (ACLCache)getCachedValueForModification((ItemCacheKey)new ItemACLCacheKey()) :
                        (ACLCache)getCachedValueForReading((ItemCacheKey)new ItemACLCacheKey());
    }


    protected void logACLCacheChange(ACLCache aclChache)
    {
    }


    public String ejbHomeGetItemTableName()
    {
        return getItemTableNameImpl();
    }


    public String ejbHomeGetOwnJNDIName()
    {
        return getOwnJNDIName();
    }


    public boolean hasJNDIName(String jndiName)
    {
        return getOwnJNDIName().equals(jndiName);
    }


    public ItemEJB()
    {
        this.needsStoringFlag = false;
    }


    public boolean needsStoring()
    {
        return this.needsStoringFlag;
    }


    public void setNeedsStoring(boolean needsStoring)
    {
        this.needsStoringFlag = needsStoring;
    }


    public void remove() throws YEJBException, YRemoveException
    {
        throw new RuntimeException("never called!");
    }


    public void loadItemData(ResultSet resultSet)
    {
        throw new RuntimeException("never called!");
    }


    private final ACLCache getGlobalACLCacheInternal(boolean forWriting)
    {
        return forWriting ? (ACLCache)getCachedValueForModification((ItemCacheKey)new GlobalACLCacheKey()) :
                        (ACLCache)getCachedValueForReading((ItemCacheKey)new GlobalACLCacheKey());
    }


    public int checkOwnGlobalPermission(PK permissionPK)
    {
        ACLCache acl = getGlobalACLCacheInternal(false);
        return acl.findPermission(getPK(), permissionPK);
    }


    public Collection<PK> getGlobalPermissionPKs(boolean negative)
    {
        return getGlobalACLCacheInternal(false).getPermissions(getPK(), negative);
    }


    public boolean setGlobalPermission(PK permissionPK, boolean negative) throws EJBSecurityException
    {
        ACLCache readACL = getGlobalACLCacheInternal(false);
        PK principalPK = getPK();
        if(skipSetPermission(readACL, principalPK, principalPK, negative))
        {
            return false;
        }
        ACLCache writeACL = getGlobalACLCacheInternal(true);
        boolean answer = writeACL.setPermission(principalPK, permissionPK, negative);
        logACLCacheChange(writeACL);
        return answer;
    }


    public boolean setGlobalPermissions(Collection<PermissionContainer> permissions) throws EJBSecurityException
    {
        ACLCache readACL = getGlobalACLCacheInternal(false);
        if(permissions == null || permissions.isEmpty())
        {
            return false;
        }
        PK principalPK = getPK();
        ACLCache writeACL = null;
        boolean result = true;
        for(Iterator<PermissionContainer> it = permissions.iterator(); it.hasNext(); )
        {
            PermissionContainer permissionContainer = it.next();
            PK permissionPK = permissionContainer.getRightPK();
            if(!skipSetPermission(readACL, principalPK, permissionPK, permissionContainer.isNegative()))
            {
                if(writeACL == null)
                {
                    writeACL = getGlobalACLCacheInternal(true);
                }
                result = (writeACL.setPermission(principalPK, permissionPK, permissionContainer.isNegative()) && result);
            }
        }
        if(writeACL != null)
        {
            logACLCacheChange(writeACL);
        }
        return result;
    }


    public boolean removeGlobalPermission(PK permissionPK) throws EJBSecurityException
    {
        ACLCache readACL = getGlobalACLCacheInternal(false);
        PK principalPK = getPK();
        if(skipRemovePermission(readACL, principalPK, permissionPK))
        {
            return false;
        }
        ACLCache writeACL = getGlobalACLCacheInternal(true);
        boolean answer = writeACL.removePermission(principalPK, permissionPK);
        logACLCacheChange(writeACL);
        return answer;
    }


    public boolean removeGlobalPermissions(Collection<PermissionContainer> permissions) throws EJBSecurityException
    {
        ACLCache readACL = getGlobalACLCacheInternal(false);
        if(permissions == null || permissions.isEmpty())
        {
            return false;
        }
        boolean resul = true;
        ACLCache writeACL = null;
        PK principalPK = getPK();
        for(Iterator<PermissionContainer> it = permissions.iterator(); it.hasNext(); )
        {
            PermissionContainer permissionContainer = it.next();
            PK permissionPK = permissionContainer.getRightPK();
            if(!skipRemovePermission(readACL, principalPK, permissionPK))
            {
                if(writeACL == null)
                {
                    writeACL = getGlobalACLCacheInternal(true);
                }
                resul = (writeACL.removePermission(principalPK, permissionPK) && resul);
            }
        }
        if(writeACL != null)
        {
            logACLCacheChange(writeACL);
        }
        return resul;
    }


    public abstract PK getPkString();


    public abstract void setPkString(PK paramPK);


    public abstract Date getCreationTimestampInternal();


    public abstract void setCreationTimestampInternal(Date paramDate);


    public abstract long getACLTimestampInternal();


    public abstract void setACLTimestampInternal(long paramLong);


    public abstract Date getModifiedTimestampInternal();


    public abstract void setModifiedTimestampInternal(Date paramDate);


    public abstract PK getTypePkString();


    public abstract void setTypePkString(PK paramPK);


    public abstract PK getOwnerPkString();


    public abstract void setOwnerPkString(PK paramPK);


    public abstract long getHJMPTS();


    public abstract boolean isBeforeCreate();


    protected abstract int typeCode();


    protected abstract Map<Object, ItemCacheKey> getCacheKeyMap();


    protected abstract String getItemTableNameImpl();


    public abstract String getOwnJNDIName();
}
