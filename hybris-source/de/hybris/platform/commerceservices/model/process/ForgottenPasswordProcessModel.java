package de.hybris.platform.commerceservices.model.process;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ForgottenPasswordProcessModel extends StoreFrontCustomerProcessModel
{
    public static final String _TYPECODE = "ForgottenPasswordProcess";
    public static final String TOKEN = "token";


    public ForgottenPasswordProcessModel()
    {
    }


    public ForgottenPasswordProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ForgottenPasswordProcessModel(String _code, String _processDefinitionName)
    {
        setCode(_code);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ForgottenPasswordProcessModel(String _code, ItemModel _owner, String _processDefinitionName)
    {
        setCode(_code);
        setOwner(_owner);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Accessor(qualifier = "token", type = Accessor.Type.GETTER)
    public String getToken()
    {
        return (String)getPersistenceContext().getPropertyValue("token");
    }


    @Accessor(qualifier = "token", type = Accessor.Type.SETTER)
    public void setToken(String value)
    {
        getPersistenceContext().setPropertyValue("token", value);
    }
}
