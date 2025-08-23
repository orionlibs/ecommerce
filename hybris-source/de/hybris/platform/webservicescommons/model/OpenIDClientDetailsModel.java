package de.hybris.platform.webservicescommons.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OpenIDClientDetailsModel extends OAuthClientDetailsModel
{
    public static final String _TYPECODE = "OpenIDClientDetails";
    public static final String EXTERNALSCOPECLAIMNAME = "externalScopeClaimName";
    public static final String ISSUER = "issuer";


    public OpenIDClientDetailsModel()
    {
    }


    public OpenIDClientDetailsModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OpenIDClientDetailsModel(String _clientId, String _issuer)
    {
        setClientId(_clientId);
        setIssuer(_issuer);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OpenIDClientDetailsModel(String _clientId, String _issuer, ItemModel _owner)
    {
        setClientId(_clientId);
        setIssuer(_issuer);
        setOwner(_owner);
    }


    @Accessor(qualifier = "externalScopeClaimName", type = Accessor.Type.GETTER)
    public String getExternalScopeClaimName()
    {
        return (String)getPersistenceContext().getPropertyValue("externalScopeClaimName");
    }


    @Accessor(qualifier = "issuer", type = Accessor.Type.GETTER)
    public String getIssuer()
    {
        return (String)getPersistenceContext().getPropertyValue("issuer");
    }


    @Accessor(qualifier = "externalScopeClaimName", type = Accessor.Type.SETTER)
    public void setExternalScopeClaimName(String value)
    {
        getPersistenceContext().setPropertyValue("externalScopeClaimName", value);
    }


    @Accessor(qualifier = "issuer", type = Accessor.Type.SETTER)
    public void setIssuer(String value)
    {
        getPersistenceContext().setPropertyValue("issuer", value);
    }
}
