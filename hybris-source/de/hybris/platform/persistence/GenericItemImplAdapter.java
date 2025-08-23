package de.hybris.platform.persistence;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.JaloPropertyContainer;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

class GenericItemImplAdapter implements GenericItem.GenericItemImpl
{
    private static <T> T notSupported()
    {
        throw new UnsupportedOperationException("This operation is not supported by the item backed by the polyglot persistence.");
    }


    public Map getAllLocalizedProperties(SessionContext ctx)
    {
        return notSupported();
    }


    public Set getLocalizedPropertyNames(SessionContext ctx)
    {
        return notSupported();
    }


    public Object getLocalizedProperty(SessionContext ctx, String name)
    {
        return notSupported();
    }


    public Object setLocalizedProperty(SessionContext ctx, String name, Object value)
    {
        return notSupported();
    }


    public Object removeLocalizedProperty(SessionContext ctx, String name)
    {
        return notSupported();
    }


    public Map<Language, ?> getAllLocalizedProperties(SessionContext ctx, String name, Set<Language> languages)
    {
        return notSupported();
    }


    public Map<Language, ?> setAllLocalizableProperties(SessionContext ctx, String name, Map<Language, ?> props)
    {
        return notSupported();
    }


    public Map getAllProperties(SessionContext ctx)
    {
        return notSupported();
    }


    public Set getPropertyNames(SessionContext ctx)
    {
        return notSupported();
    }


    public Object setProperty(SessionContext ctx, String name, Object value)
    {
        return notSupported();
    }


    public void setAllProperties(SessionContext ctx, JaloPropertyContainer propertyContainer) throws ConsistencyCheckException
    {
        notSupported();
    }


    public Object getProperty(SessionContext ctx, String name)
    {
        return notSupported();
    }


    public Object removeProperty(SessionContext ctx, String name)
    {
        return notSupported();
    }


    public PK getPK()
    {
        return notSupported();
    }


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        notSupported();
    }


    public ComposedType getComposedType()
    {
        return notSupported();
    }


    public void setComposedType(ComposedType type)
    {
        notSupported();
    }


    public Item getOwner(SessionContext ctx)
    {
        return notSupported();
    }


    public void setOwner(SessionContext ctx, Item item)
    {
        notSupported();
    }


    public Date getCreationTime()
    {
        return notSupported();
    }


    public void setCreationTime(Date time)
    {
        notSupported();
    }


    public Date getModificationTime()
    {
        return notSupported();
    }


    public void setModificationTime(Date date)
    {
        notSupported();
    }


    public int checkItemPermission(PK princpalPK, PK rightPK)
    {
        return ((Integer)notSupported()).intValue();
    }


    public Collection getRestrictedPrincipals()
    {
        return notSupported();
    }


    public Collection getPrincipalPermissions(PK principalPK, boolean negative)
    {
        return notSupported();
    }


    public void addPermission(PK principalPK, PK right, boolean negative)
    {
        notSupported();
    }


    public void clearPermission(PK principalPK, PK rightPK)
    {
        notSupported();
    }


    public Map getPermissionMap(List userRights)
    {
        return notSupported();
    }


    public void setPermissionMap(List<UserRight> userRights, Map<Principal, List<Boolean>> permissionMap) throws JaloSecurityException
    {
        notSupported();
    }


    public long getHJMPTS()
    {
        return ((Long)notSupported()).longValue();
    }


    public PK getTypeKey()
    {
        return notSupported();
    }


    public void invalidateLocalCaches()
    {
        notSupported();
    }


    public Class getJaloObjectClass()
    {
        return notSupported();
    }


    public ItemPermissionFacade getPermissionFacade()
    {
        return notSupported();
    }


    public <T extends Item> Collection<T> getRelatedItems(String relationQualifier)
    {
        return notSupported();
    }


    public <T extends Item> boolean setRelatedItems(String relationQualifier, Collection<T> values)
    {
        return ((Boolean)notSupported()).booleanValue();
    }


    public boolean isTransactionSupported()
    {
        return false;
    }
}
