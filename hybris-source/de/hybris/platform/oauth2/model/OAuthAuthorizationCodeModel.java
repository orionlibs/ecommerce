package de.hybris.platform.oauth2.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OAuthAuthorizationCodeModel extends ItemModel
{
    public static final String _TYPECODE = "OAuthAuthorizationCode";
    public static final String CODE = "code";
    public static final String AUTHENTICATION = "authentication";


    public OAuthAuthorizationCodeModel()
    {
    }


    public OAuthAuthorizationCodeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OAuthAuthorizationCodeModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OAuthAuthorizationCodeModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "authentication", type = Accessor.Type.GETTER)
    public Object getAuthentication()
    {
        return getPersistenceContext().getPropertyValue("authentication");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "authentication", type = Accessor.Type.SETTER)
    public void setAuthentication(Object value)
    {
        getPersistenceContext().setPropertyValue("authentication", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }
}
