package de.hybris.platform.webservicescommons.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OAuthRefreshTokenModel extends ItemModel
{
    public static final String _TYPECODE = "OAuthRefreshToken";
    public static final String TOKENID = "tokenId";
    public static final String TOKEN = "token";
    public static final String AUTHENTICATION = "authentication";


    public OAuthRefreshTokenModel()
    {
    }


    public OAuthRefreshTokenModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OAuthRefreshTokenModel(String _tokenId)
    {
        setTokenId(_tokenId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OAuthRefreshTokenModel(ItemModel _owner, String _tokenId)
    {
        setOwner(_owner);
        setTokenId(_tokenId);
    }


    @Accessor(qualifier = "authentication", type = Accessor.Type.GETTER)
    public Object getAuthentication()
    {
        return getPersistenceContext().getPropertyValue("authentication");
    }


    @Accessor(qualifier = "token", type = Accessor.Type.GETTER)
    public Object getToken()
    {
        return getPersistenceContext().getPropertyValue("token");
    }


    @Accessor(qualifier = "tokenId", type = Accessor.Type.GETTER)
    public String getTokenId()
    {
        return (String)getPersistenceContext().getPropertyValue("tokenId");
    }


    @Accessor(qualifier = "authentication", type = Accessor.Type.SETTER)
    public void setAuthentication(Object value)
    {
        getPersistenceContext().setPropertyValue("authentication", value);
    }


    @Accessor(qualifier = "token", type = Accessor.Type.SETTER)
    public void setToken(Object value)
    {
        getPersistenceContext().setPropertyValue("token", value);
    }


    @Accessor(qualifier = "tokenId", type = Accessor.Type.SETTER)
    public void setTokenId(String value)
    {
        getPersistenceContext().setPropertyValue("tokenId", value);
    }
}
