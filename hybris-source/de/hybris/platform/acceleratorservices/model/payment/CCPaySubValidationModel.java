package de.hybris.platform.acceleratorservices.model.payment;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CCPaySubValidationModel extends ItemModel
{
    public static final String _TYPECODE = "CCPaySubValidation";
    public static final String SUBSCRIPTIONID = "subscriptionId";


    public CCPaySubValidationModel()
    {
    }


    public CCPaySubValidationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CCPaySubValidationModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "subscriptionId", type = Accessor.Type.GETTER)
    public String getSubscriptionId()
    {
        return (String)getPersistenceContext().getPropertyValue("subscriptionId");
    }


    @Accessor(qualifier = "subscriptionId", type = Accessor.Type.SETTER)
    public void setSubscriptionId(String value)
    {
        getPersistenceContext().setPropertyValue("subscriptionId", value);
    }
}
