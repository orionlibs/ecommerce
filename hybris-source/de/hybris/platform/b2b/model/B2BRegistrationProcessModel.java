package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class B2BRegistrationProcessModel extends StoreFrontCustomerProcessModel
{
    public static final String _TYPECODE = "B2BRegistrationProcess";
    public static final String REGISTRATION = "registration";


    public B2BRegistrationProcessModel()
    {
    }


    public B2BRegistrationProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BRegistrationProcessModel(String _code, String _processDefinitionName)
    {
        setCode(_code);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BRegistrationProcessModel(String _code, ItemModel _owner, String _processDefinitionName)
    {
        setCode(_code);
        setOwner(_owner);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Accessor(qualifier = "registration", type = Accessor.Type.GETTER)
    public B2BRegistrationModel getRegistration()
    {
        return (B2BRegistrationModel)getPersistenceContext().getPropertyValue("registration");
    }


    @Accessor(qualifier = "registration", type = Accessor.Type.SETTER)
    public void setRegistration(B2BRegistrationModel value)
    {
        getPersistenceContext().setPropertyValue("registration", value);
    }
}
