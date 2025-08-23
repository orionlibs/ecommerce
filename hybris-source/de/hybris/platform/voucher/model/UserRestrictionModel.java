package de.hybris.platform.voucher.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class UserRestrictionModel extends RestrictionModel
{
    public static final String _TYPECODE = "UserRestriction";
    public static final String USERS = "users";


    public UserRestrictionModel()
    {
    }


    public UserRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public UserRestrictionModel(Collection<PrincipalModel> _users, VoucherModel _voucher)
    {
        setUsers(_users);
        setVoucher(_voucher);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public UserRestrictionModel(ItemModel _owner, Collection<PrincipalModel> _users, VoucherModel _voucher)
    {
        setOwner(_owner);
        setUsers(_users);
        setVoucher(_voucher);
    }


    @Accessor(qualifier = "users", type = Accessor.Type.GETTER)
    public Collection<PrincipalModel> getUsers()
    {
        return (Collection<PrincipalModel>)getPersistenceContext().getPropertyValue("users");
    }


    @Accessor(qualifier = "users", type = Accessor.Type.SETTER)
    public void setUsers(Collection<PrincipalModel> value)
    {
        getPersistenceContext().setPropertyValue("users", value);
    }
}
