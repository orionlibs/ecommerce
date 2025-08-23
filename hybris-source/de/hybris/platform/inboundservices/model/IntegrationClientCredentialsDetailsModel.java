package de.hybris.platform.inboundservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;

public class IntegrationClientCredentialsDetailsModel extends OAuthClientDetailsModel
{
    public static final String _TYPECODE = "IntegrationClientCredentialsDetails";
    public static final String USER = "user";


    public IntegrationClientCredentialsDetailsModel()
    {
    }


    public IntegrationClientCredentialsDetailsModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IntegrationClientCredentialsDetailsModel(String _clientId, EmployeeModel _user)
    {
        setClientId(_clientId);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IntegrationClientCredentialsDetailsModel(String _clientId, ItemModel _owner, EmployeeModel _user)
    {
        setClientId(_clientId);
        setOwner(_owner);
        setUser(_user);
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public EmployeeModel getUser()
    {
        return (EmployeeModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(EmployeeModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }
}
