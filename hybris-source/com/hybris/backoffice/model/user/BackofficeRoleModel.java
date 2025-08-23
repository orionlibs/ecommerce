package com.hybris.backoffice.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class BackofficeRoleModel extends UserGroupModel
{
    public static final String _TYPECODE = "BackofficeRole";
    public static final String AUTHORITIES = "authorities";


    public BackofficeRoleModel()
    {
    }


    public BackofficeRoleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BackofficeRoleModel(String _uid)
    {
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BackofficeRoleModel(ItemModel _owner, String _uid)
    {
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "authorities", type = Accessor.Type.GETTER)
    public Collection<String> getAuthorities()
    {
        return (Collection<String>)getPersistenceContext().getPropertyValue("authorities");
    }


    @Accessor(qualifier = "authorities", type = Accessor.Type.SETTER)
    public void setAuthorities(Collection<String> value)
    {
        getPersistenceContext().setPropertyValue("authorities", value);
    }
}
