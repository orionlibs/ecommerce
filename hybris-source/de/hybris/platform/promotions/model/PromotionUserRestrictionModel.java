package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class PromotionUserRestrictionModel extends AbstractPromotionRestrictionModel
{
    public static final String _TYPECODE = "PromotionUserRestriction";
    public static final String POSITIVE = "positive";
    public static final String USERS = "users";


    public PromotionUserRestrictionModel()
    {
    }


    public PromotionUserRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionUserRestrictionModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "positive", type = Accessor.Type.GETTER)
    public Boolean getPositive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("positive");
    }


    @Accessor(qualifier = "users", type = Accessor.Type.GETTER)
    public Collection<PrincipalModel> getUsers()
    {
        return (Collection<PrincipalModel>)getPersistenceContext().getPropertyValue("users");
    }


    @Accessor(qualifier = "positive", type = Accessor.Type.SETTER)
    public void setPositive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("positive", value);
    }


    @Accessor(qualifier = "users", type = Accessor.Type.SETTER)
    public void setUsers(Collection<PrincipalModel> value)
    {
        getPersistenceContext().setPropertyValue("users", value);
    }
}
