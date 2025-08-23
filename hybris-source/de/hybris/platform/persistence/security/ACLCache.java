package de.hybris.platform.persistence.security;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInternalException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ACLCache implements Serializable, Cloneable
{
    private static final boolean DEFAULT_PERMISSION = false;
    private final PK itemPK;


    public static ACLCache create(PK itemPK, long version)
    {
        return new ACLCache(itemPK, version);
    }


    public static ACLCache load(PK itemPK, long version, Collection<ACLEntryCache> allACLs)
    {
        return new ACLCache(itemPK, version, allACLs);
    }


    public static boolean translatePermissionToBoolean(int result)
    {
        switch(result)
        {
            case 0:
                return true;
            case 1:
                return false;
            case -1:
            case 2:
                return false;
        }
        throw new EJBInternalException(null, "invalid result for AccessManagerEJB.checkACL() " + result, 0);
    }


    private Map<Object, ACLEntryCache> cacheMap = null;
    private long version = -1L;
    private boolean needsUpdateFlag = false;


    private ACLCache(PK itemPK, long version)
    {
        this(itemPK, version, Collections.EMPTY_LIST);
    }


    private ACLCache(PK itemPK, long version, Collection<ACLEntryCache> allACLs)
    {
        this.version = version;
        this.itemPK = itemPK;
        for(Iterator<ACLEntryCache> it = allACLs.iterator(); it.hasNext(); )
        {
            ACLEntryCache acl = it.next();
            getCacheMap().put(acl.getKey(), acl);
        }
    }


    public boolean needsUpdate()
    {
        return this.needsUpdateFlag;
    }


    public Collection<ACLEntryCache> getUpdateableACLs()
    {
        if(!this.needsUpdateFlag)
        {
            return Collections.EMPTY_LIST;
        }
        Collection<ACLEntryCache> updateableACLs = new ArrayList<>();
        for(Iterator<ACLEntryCache> iter = getCacheMap().values().iterator(); iter.hasNext(); )
        {
            ACLEntryCache next = iter.next();
            if(next.hasChanged())
            {
                updateableACLs.add(next);
            }
        }
        return updateableACLs;
    }


    private boolean isEmpty()
    {
        return (this.cacheMap == null || this.cacheMap.isEmpty());
    }


    private Map<Object, ACLEntryCache> getCacheMap()
    {
        if(this.cacheMap == null)
        {
            this.cacheMap = new HashMap<>();
        }
        return this.cacheMap;
    }


    private void markModified()
    {
        markModified(false);
    }


    private void markModified(boolean ownersChanged)
    {
        if(ownersChanged)
        {
            ownersChanged = true;
        }
        else
        {
            this.needsUpdateFlag = true;
            this.version++;
        }
    }


    public void wroteChanges()
    {
        for(Iterator<ACLEntryCache> it = getUpdateableACLs().iterator(); it.hasNext(); )
        {
            ACLEntryCache ec = it.next();
            if(ec.getNegative() == null)
            {
                this.cacheMap.remove(ec.getKey());
                continue;
            }
            ec.wroteChanges();
        }
        this.needsUpdateFlag = false;
    }


    public PK getItemPK()
    {
        return this.itemPK;
    }


    public long getVersion()
    {
        return this.version;
    }


    public void setVersion(long newVersion)
    {
        this.version = newVersion;
    }


    protected Object clone() throws CloneNotSupportedException
    {
        if(this.needsUpdateFlag)
        {
            throw new EJBInternalException(null, "you cannot clone a modified EJBPropertyCache object", 0);
        }
        boolean cloneACL = (this.cacheMap != null);
        Collection<ACLEntryCache> newACL = cloneACL ? new ArrayList<>() : Collections.EMPTY_LIST;
        if(cloneACL)
        {
            for(Iterator<Map.Entry<Object, ACLEntryCache>> it = this.cacheMap.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry<Object, ACLEntryCache> e = it.next();
                ACLEntryCache acl = e.getValue();
                newACL.add((ACLEntryCache)acl.clone());
            }
        }
        Object ret = new ACLCache(this.itemPK, getVersion(), newACL);
        return ret;
    }


    public Collection<PK> getRestrictedPrincipals()
    {
        if(isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        Collection<PK> res = new ArrayList<>(getCacheMap().size());
        for(Iterator<Map.Entry<Object, ACLEntryCache>> it = getCacheMap().entrySet().iterator(); it.hasNext(); )
        {
            ACLEntryCache entry = (ACLEntryCache)((Map.Entry)it.next()).getValue();
            res.add(entry.getPrincipal());
        }
        return res;
    }


    public Map<PK, List<Boolean>> getPermissionMap(List<PK> rightPKs)
    {
        if(isEmpty() || rightPKs == null || rightPKs.isEmpty())
        {
            return Collections.EMPTY_MAP;
        }
        Map<PK, List<Boolean>> ret = new HashMap<>();
        List<Boolean> emptyRow = Arrays.asList(new Boolean[rightPKs.size()]);
        for(Iterator<Map.Entry<Object, ACLEntryCache>> it = getCacheMap().entrySet().iterator(); it.hasNext(); )
        {
            ACLEntryCache entry = (ACLEntryCache)((Map.Entry)it.next()).getValue();
            int index = rightPKs.indexOf(entry.getPermission());
            if(index >= 0)
            {
                List<Boolean> row = ret.get(entry.getPrincipal());
                if(row == null)
                {
                    ret.put(entry.getPrincipal(), row = new ArrayList<>(emptyRow));
                }
                row.set(index, entry.isNegative() ? Boolean.TRUE : Boolean.FALSE);
            }
        }
        return ret;
    }


    public void setPermissionMap(List<PK> rightPKs, Map<PK, List<Boolean>> principalToBooleanMap) throws EJBSecurityException
    {
        if(rightPKs == null || rightPKs.isEmpty())
        {
            throw new EJBSecurityException(null, "user right list was null or empty", 0);
        }
        Set<PK> rightSet = new HashSet<>(rightPKs);
        Set<PK> principalSet = new HashSet<>(principalToBooleanMap.keySet());
        for(Iterator<Map.Entry<Object, ACLEntryCache>> iterator = getCacheMap().entrySet().iterator(); iterator.hasNext(); )
        {
            ACLEntryCache entry = (ACLEntryCache)((Map.Entry)iterator.next()).getValue();
            if(rightSet.contains(entry.getPermission()) && !principalSet.contains(entry.getPrincipal()))
            {
                removePermission(entry.getPrincipal(), entry.getPermission());
            }
        }
        for(Iterator<Map.Entry<PK, List<Boolean>>> it = principalToBooleanMap.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry<PK, List<Boolean>> e = it.next();
            PK pcplPK = e.getKey();
            List<Boolean> settings = e.getValue();
            if(settings == null || settings.size() != rightPKs.size())
            {
                throw new EJBSecurityException(null, "permission list was null or size did not match user right list size", 0);
            }
            for(int i = 0, s = rightPKs.size(); i < s; i++)
            {
                PK rightPK = rightPKs.get(i);
                if(rightPK == null)
                {
                    throw new EJBSecurityException(null, "user right at " + i + " was null", 0);
                }
                Boolean set = settings.get(i);
                if(set == null)
                {
                    removePermission(pcplPK, rightPK);
                }
                else
                {
                    setPermission(pcplPK, rightPK, set.booleanValue());
                }
            }
        }
    }


    public Collection<PK> getPermissions(PK principalPK, boolean negative)
    {
        if(principalPK == null)
        {
            return Collections.EMPTY_LIST;
        }
        if(isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        Collection<PK> resul = new ArrayList<>();
        for(Iterator<Map.Entry<Object, ACLEntryCache>> it = getCacheMap().entrySet().iterator(); it.hasNext(); )
        {
            ACLEntryCache entry = (ACLEntryCache)((Map.Entry)it.next()).getValue();
            if(principalPK.equals(entry.getPrincipal()) && negative == entry.isNegative())
            {
                resul.add(entry.getPermission());
            }
        }
        return resul;
    }


    public int findPermission(PK principalPK, PK permissionPK)
    {
        ACLEntryCache entry = getCacheMap().get(ACLEntryCache.constructKey(principalPK, permissionPK));
        return (entry != null) ? (entry.isNegative() ? 1 : 0) : -1;
    }


    public boolean isPermissionRemoved(PK principalPK, PK permissionPK)
    {
        ACLEntryCache entry = getCacheMap().get(ACLEntryCache.constructKey(principalPK, permissionPK));
        return (entry != null && entry.getNegative() == null);
    }


    public boolean setPermission(PK principalPK, PK permissionPK, boolean negative) throws EJBSecurityException
    {
        ACLEntryCache entry = getCacheMap().get(ACLEntryCache.constructKey(principalPK, permissionPK));
        boolean modified = false;
        if(entry != null)
        {
            boolean old = entry.isNegative();
            entry.setNegative(negative);
            modified = (old != negative);
        }
        else
        {
            entry = ACLEntryCache.create(principalPK, permissionPK, negative);
            getCacheMap().put(entry.getKey(), entry);
            modified = true;
        }
        if(modified)
        {
            markModified();
        }
        return modified;
    }


    public boolean removePermission(PK principalPK, PK permissionPK) throws EJBSecurityException
    {
        ACLEntryCache entry = getCacheMap().get(ACLEntryCache.constructKey(principalPK, permissionPK));
        if(entry != null)
        {
            entry.remove();
            markModified();
            return true;
        }
        return false;
    }


    public void deletePermission()
    {
    }
}
