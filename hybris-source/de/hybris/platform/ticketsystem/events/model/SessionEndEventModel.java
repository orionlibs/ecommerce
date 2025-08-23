package de.hybris.platform.ticketsystem.events.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SessionEndEventModel extends SessionEventModel
{
    public static final String _TYPECODE = "SessionEndEvent";
    public static final String CUSTOMER = "customer";


    public SessionEndEventModel()
    {
    }


    public SessionEndEventModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SessionEndEventModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "customer", type = Accessor.Type.GETTER)
    public UserModel getCustomer()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("customer");
    }


    @Accessor(qualifier = "customer", type = Accessor.Type.SETTER)
    public void setCustomer(UserModel value)
    {
        getPersistenceContext().setPropertyValue("customer", value);
    }
}
