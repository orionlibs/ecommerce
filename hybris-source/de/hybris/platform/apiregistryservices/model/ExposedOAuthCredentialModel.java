package de.hybris.platform.apiregistryservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;

public class ExposedOAuthCredentialModel extends AbstractCredentialModel
{
    public static final String _TYPECODE = "ExposedOAuthCredential";
    public static final String OAUTHCLIENTDETAILS = "oAuthClientDetails";
    public static final String PASSWORD = "password";


    public ExposedOAuthCredentialModel()
    {
    }


    public ExposedOAuthCredentialModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExposedOAuthCredentialModel(String _id, OAuthClientDetailsModel _oAuthClientDetails)
    {
        setId(_id);
        setOAuthClientDetails(_oAuthClientDetails);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExposedOAuthCredentialModel(String _id, OAuthClientDetailsModel _oAuthClientDetails, ItemModel _owner)
    {
        setId(_id);
        setOAuthClientDetails(_oAuthClientDetails);
        setOwner(_owner);
    }


    @Accessor(qualifier = "oAuthClientDetails", type = Accessor.Type.GETTER)
    public OAuthClientDetailsModel getOAuthClientDetails()
    {
        return (OAuthClientDetailsModel)getPersistenceContext().getPropertyValue("oAuthClientDetails");
    }


    @Accessor(qualifier = "password", type = Accessor.Type.GETTER)
    public String getPassword()
    {
        return (String)getPersistenceContext().getPropertyValue("password");
    }


    @Accessor(qualifier = "oAuthClientDetails", type = Accessor.Type.SETTER)
    public void setOAuthClientDetails(OAuthClientDetailsModel value)
    {
        getPersistenceContext().setPropertyValue("oAuthClientDetails", value);
    }


    @Accessor(qualifier = "password", type = Accessor.Type.SETTER)
    public void setPassword(String value)
    {
        getPersistenceContext().setPropertyValue("password", value);
    }
}
