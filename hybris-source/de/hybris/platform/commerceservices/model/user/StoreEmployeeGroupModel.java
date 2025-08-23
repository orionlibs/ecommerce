package de.hybris.platform.commerceservices.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

public class StoreEmployeeGroupModel extends UserGroupModel
{
    public static final String _TYPECODE = "StoreEmployeeGroup";
    public static final String STORE = "store";


    public StoreEmployeeGroupModel()
    {
    }


    public StoreEmployeeGroupModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StoreEmployeeGroupModel(String _uid)
    {
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StoreEmployeeGroupModel(ItemModel _owner, String _uid)
    {
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "store", type = Accessor.Type.GETTER)
    public PointOfServiceModel getStore()
    {
        return (PointOfServiceModel)getPersistenceContext().getPropertyValue("store");
    }


    @Accessor(qualifier = "store", type = Accessor.Type.SETTER)
    public void setStore(PointOfServiceModel value)
    {
        getPersistenceContext().setPropertyValue("store", value);
    }
}
