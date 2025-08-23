package de.hybris.platform.jalo.user;

import de.hybris.platform.core.Constants;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class UserGroup extends GeneratedUserGroup
{
    @SLDSafe(portingClass = "MandatoryAttributesValidator, UniqueAttributesInterceptor", portingMethod = "onValidate(final Object model, final InterceptorContext ctx)")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(allAttributes.get("uid") == null)
        {
            throw new JaloInvalidParameterException("missing uidto create a " + type.getCode(), 0);
        }
        String uid = (String)allAttributes.get("uid");
        checkConsistencyUid(uid, "Duplicate userGroup for uid '" + uid + "'", type.getCode());
        allAttributes.setAttributeMode("uid", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void checkSystemPrincipal() throws ConsistencyCheckException
    {
        if(Constants.USER.ADMIN_USERGROUP.equals(getUid()))
        {
            throw new ConsistencyCheckException(null, "cannot change uid for admin group", 0);
        }
    }


    @SLDSafe(portingClass = "ModifySystemUsersInterceptor", portingMethod = "onValidate(final Object model, final InterceptorContext ctx)")
    public void setUid(SessionContext ctx, String value) throws ConsistencyCheckException
    {
        checkSystemPrincipal();
        String oldUid = getUid();
        if(oldUid != value && (oldUid == null || !oldUid.equals(value)))
        {
            checkConsistencyUid(value, "Duplicate userGroup for uid '" + value + "'", getComposedType().getCode());
            super.setUid(ctx, value);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getAllAddresses()
    {
        return getSession().getUserManager().getAllAddresses((Item)this);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Address createAddress()
    {
        try
        {
            return (Address)ComposedType.newInstance(getSession().getSessionContext(), Address.class, new Object[] {Item.OWNER, this});
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Address createAddress(Map fields)
    {
        Item.ItemAttributeMap params = new Item.ItemAttributeMap();
        if(fields != null)
        {
            params.putAll(fields);
        }
        params.put(Item.OWNER, this);
        try
        {
            return (Address)ComposedType.newInstance(getSession().getSessionContext(), Address.class, (Map)params);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    @SLDSafe(portingClass = "UserService", portingMethod = "isAdminGroup(UserGroupModel)")
    @Deprecated(since = "ages", forRemoval = false)
    public boolean isAdmin()
    {
        if(Constants.USER.ADMIN_USERGROUP.equals(getUID()))
        {
            return true;
        }
        for(Iterator<UserGroup> it = getGroups().iterator(); it.hasNext(); )
        {
            if(((UserGroup)it.next()).isAdmin())
            {
                return true;
            }
        }
        return false;
    }
}
