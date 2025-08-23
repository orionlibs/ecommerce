package de.hybris.platform.apiregistryservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ConsumedOAuthCredentialModel extends AbstractCredentialModel
{
    public static final String _TYPECODE = "ConsumedOAuthCredential";
    public static final String CLIENTID = "clientId";
    public static final String OAUTHURL = "oAuthUrl";
    public static final String CLIENTSECRET = "clientSecret";


    public ConsumedOAuthCredentialModel()
    {
    }


    public ConsumedOAuthCredentialModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsumedOAuthCredentialModel(String _clientId, String _id)
    {
        setClientId(_clientId);
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsumedOAuthCredentialModel(String _clientId, String _id, ItemModel _owner)
    {
        setClientId(_clientId);
        setId(_id);
        setOwner(_owner);
    }


    @Accessor(qualifier = "clientId", type = Accessor.Type.GETTER)
    public String getClientId()
    {
        return (String)getPersistenceContext().getPropertyValue("clientId");
    }


    @Accessor(qualifier = "clientSecret", type = Accessor.Type.GETTER)
    public String getClientSecret()
    {
        return (String)getPersistenceContext().getPropertyValue("clientSecret");
    }


    @Accessor(qualifier = "oAuthUrl", type = Accessor.Type.GETTER)
    public String getOAuthUrl()
    {
        return (String)getPersistenceContext().getPropertyValue("oAuthUrl");
    }


    @Accessor(qualifier = "clientId", type = Accessor.Type.SETTER)
    public void setClientId(String value)
    {
        getPersistenceContext().setPropertyValue("clientId", value);
    }


    @Accessor(qualifier = "clientSecret", type = Accessor.Type.SETTER)
    public void setClientSecret(String value)
    {
        getPersistenceContext().setPropertyValue("clientSecret", value);
    }


    @Accessor(qualifier = "oAuthUrl", type = Accessor.Type.SETTER)
    public void setOAuthUrl(String value)
    {
        getPersistenceContext().setPropertyValue("oAuthUrl", value);
    }
}
