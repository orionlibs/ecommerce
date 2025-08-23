package de.hybris.platform.webservicescommons.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Set;

public class OpenIDExternalScopesModel extends ItemModel
{
    public static final String _TYPECODE = "OpenIDExternalScopes";
    public static final String CODE = "code";
    public static final String CLIENTDETAILSID = "clientDetailsId";
    public static final String PERMITTEDPRINCIPALS = "permittedPrincipals";
    public static final String SCOPE = "scope";


    public OpenIDExternalScopesModel()
    {
    }


    public OpenIDExternalScopesModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OpenIDExternalScopesModel(OpenIDClientDetailsModel _clientDetailsId, String _code)
    {
        setClientDetailsId(_clientDetailsId);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OpenIDExternalScopesModel(OpenIDClientDetailsModel _clientDetailsId, String _code, ItemModel _owner)
    {
        setClientDetailsId(_clientDetailsId);
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "clientDetailsId", type = Accessor.Type.GETTER)
    public OpenIDClientDetailsModel getClientDetailsId()
    {
        return (OpenIDClientDetailsModel)getPersistenceContext().getPropertyValue("clientDetailsId");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "permittedPrincipals", type = Accessor.Type.GETTER)
    public Collection<PrincipalModel> getPermittedPrincipals()
    {
        return (Collection<PrincipalModel>)getPersistenceContext().getPropertyValue("permittedPrincipals");
    }


    @Accessor(qualifier = "scope", type = Accessor.Type.GETTER)
    public Set<String> getScope()
    {
        return (Set<String>)getPersistenceContext().getPropertyValue("scope");
    }


    @Accessor(qualifier = "clientDetailsId", type = Accessor.Type.SETTER)
    public void setClientDetailsId(OpenIDClientDetailsModel value)
    {
        getPersistenceContext().setPropertyValue("clientDetailsId", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "permittedPrincipals", type = Accessor.Type.SETTER)
    public void setPermittedPrincipals(Collection<PrincipalModel> value)
    {
        getPersistenceContext().setPropertyValue("permittedPrincipals", value);
    }


    @Accessor(qualifier = "scope", type = Accessor.Type.SETTER)
    public void setScope(Set<String> value)
    {
        getPersistenceContext().setPropertyValue("scope", value);
    }
}
