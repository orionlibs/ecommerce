package de.hybris.platform.b2bacceleratorservices.model.process;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ReplenishmentProcessModel extends StoreFrontCustomerProcessModel
{
    public static final String _TYPECODE = "ReplenishmentProcess";
    public static final String CARTTOORDERCRONJOB = "cartToOrderCronJob";


    public ReplenishmentProcessModel()
    {
    }


    public ReplenishmentProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReplenishmentProcessModel(String _code, String _processDefinitionName)
    {
        setCode(_code);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReplenishmentProcessModel(String _code, ItemModel _owner, String _processDefinitionName)
    {
        setCode(_code);
        setOwner(_owner);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Accessor(qualifier = "cartToOrderCronJob", type = Accessor.Type.GETTER)
    public CartToOrderCronJobModel getCartToOrderCronJob()
    {
        return (CartToOrderCronJobModel)getPersistenceContext().getPropertyValue("cartToOrderCronJob");
    }


    @Accessor(qualifier = "cartToOrderCronJob", type = Accessor.Type.SETTER)
    public void setCartToOrderCronJob(CartToOrderCronJobModel value)
    {
        getPersistenceContext().setPropertyValue("cartToOrderCronJob", value);
    }
}
