package de.hybris.platform.test;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.ItemEJB;
import de.hybris.platform.persistence.ItemPermissionFacade;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.security.EJBSecurityException;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.util.ItemPropertyValue;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ItemDummy implements ItemRemote, ItemPermissionFacade
{
    private final PK pk;


    public ItemDummy(PK pk)
    {
        this.pk = pk;
    }


    public String getItemTableName()
    {
        throw new RuntimeException();
    }


    public boolean hasJNDIName(String jndiName)
    {
        throw new RuntimeException();
    }


    public String getItemJNDIName()
    {
        throw new RuntimeException();
    }


    public long getHJMPTS()
    {
        return 0L;
    }


    public PK getPkString()
    {
        return this.pk;
    }


    public String getPrimaryKey()
    {
        return (this.pk == null) ? null : this.pk.toString();
    }


    public PK getPK()
    {
        return this.pk;
    }


    public void setStagedCopy(ItemRemote rem)
    {
        throw new RuntimeException();
    }


    public void setRemoved(boolean bool)
    {
        throw new RuntimeException();
    }


    public void checkValid()
    {
        throw new RuntimeException();
    }


    public boolean isCreated()
    {
        throw new RuntimeException();
    }


    public void deepCopyFrom(ItemRemote remote)
    {
        throw new RuntimeException();
    }


    public boolean isCopy()
    {
        throw new RuntimeException();
    }


    public boolean isRemoved()
    {
        throw new RuntimeException();
    }


    public boolean stagingActivated()
    {
        throw new RuntimeException();
    }


    public ItemRemote getStagedCopy()
    {
        throw new RuntimeException();
    }


    public boolean hasModified()
    {
        throw new RuntimeException();
    }


    public void setCreated(boolean bool)
    {
        throw new RuntimeException();
    }


    public void setCopy(boolean bool)
    {
        throw new RuntimeException();
    }


    public void clearEntityCaches()
    {
        throw new RuntimeException();
    }


    public void remove()
    {
        throw new RuntimeException();
    }


    public Date getCreationTime()
    {
        throw new RuntimeException();
    }


    public void setCreationTime(Date creationTime)
    {
    }


    public Date getModifiedTime()
    {
        throw new RuntimeException();
    }


    public void setModifiedTime(Date timestamp)
    {
        throw new RuntimeException();
    }


    public long getLastModifiedTime(int stage)
    {
        throw new RuntimeException();
    }


    public boolean wasModifiedSince(Date time)
    {
        throw new RuntimeException();
    }


    public ComposedTypeRemote getComposedType()
    {
        throw new RuntimeException();
    }


    public void setComposedType(ComposedTypeRemote type)
    {
        throw new RuntimeException();
    }


    public ItemRemote getOwner()
    {
        throw new RuntimeException();
    }


    public void setOwner(ItemRemote item)
    {
        throw new RuntimeException();
    }


    public void setOwnerRef(ItemPropertyValue ownerRef)
    {
        throw new RuntimeException();
    }


    public ItemRemote copy()
    {
        throw new RuntimeException();
    }


    public int checkItemPermission(PK principalPK, PK permissionPK)
    {
        throw new RuntimeException();
    }


    public Collection getACLOwners()
    {
        throw new RuntimeException();
    }


    public Collection getPermissionPKs(PK principalPK, boolean negative)
    {
        throw new RuntimeException();
    }


    public Collection getRestrictedPrincipalPKs()
    {
        throw new RuntimeException();
    }


    public boolean removePermission(PK principalPK, PK permissionPK) throws EJBSecurityException
    {
        throw new RuntimeException();
    }


    public boolean removePermissions(Collection permissions) throws EJBSecurityException
    {
        throw new RuntimeException();
    }


    public boolean setPermission(PK principalPK, PK permissionPK, boolean negative) throws EJBSecurityException
    {
        throw new RuntimeException();
    }


    public boolean setPermissions(Collection permissions) throws EJBSecurityException
    {
        throw new RuntimeException();
    }


    public String getACLOwnerPKsInternal()
    {
        throw new RuntimeException();
    }


    public void setACLOwnerPKsInternal(String pk)
    {
        throw new RuntimeException();
    }


    public PK getTypeKey()
    {
        throw new RuntimeException();
    }


    public Map getPrincipalToBooleanListMap(List rightPKs)
    {
        throw new RuntimeException();
    }


    public void setPrincipalToBooleanListMap(List rightPKs, Map principalPKToBooleanListMap) throws EJBSecurityException
    {
        throw new RuntimeException();
    }


    public ItemEJB getUnderlayingEntity()
    {
        return null;
    }


    public boolean setGlobalPermission(PK permissionPK, boolean negative) throws EJBSecurityException
    {
        throw new RuntimeException();
    }


    public boolean setGlobalPermissions(Collection permissions) throws EJBSecurityException
    {
        throw new RuntimeException();
    }


    public boolean removeGlobalPermission(PK permissionPK) throws EJBSecurityException
    {
        throw new RuntimeException();
    }


    public boolean removeGlobalPermissions(Collection permissions) throws EJBSecurityException
    {
        throw new RuntimeException();
    }


    public Collection getGlobalPermissionPKs(boolean negative)
    {
        throw new RuntimeException();
    }


    public int checkOwnGlobalPermission(PK permissionPK)
    {
        throw new RuntimeException();
    }
}
