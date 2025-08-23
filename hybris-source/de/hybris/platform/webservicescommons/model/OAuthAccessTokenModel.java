package de.hybris.platform.webservicescommons.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OAuthAccessTokenModel extends ItemModel
{
    public static final String _TYPECODE = "OAuthAccessToken";
    public static final String _USER2TOKENRELATION = "User2TokenRelation";
    public static final String TOKENID = "tokenId";
    public static final String TOKEN = "token";
    public static final String AUTHENTICATIONID = "authenticationId";
    public static final String CLIENT = "client";
    public static final String AUTHENTICATION = "authentication";
    public static final String REFRESHTOKEN = "refreshToken";
    public static final String USER = "user";


    public OAuthAccessTokenModel()
    {
    }


    public OAuthAccessTokenModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OAuthAccessTokenModel(String _authenticationId, OAuthClientDetailsModel _client, String _tokenId)
    {
        setAuthenticationId(_authenticationId);
        setClient(_client);
        setTokenId(_tokenId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OAuthAccessTokenModel(String _authenticationId, OAuthClientDetailsModel _client, ItemModel _owner, OAuthRefreshTokenModel _refreshToken, Object _token, String _tokenId)
    {
        setAuthenticationId(_authenticationId);
        setClient(_client);
        setOwner(_owner);
        setRefreshToken(_refreshToken);
        setToken(_token);
        setTokenId(_tokenId);
    }


    @Accessor(qualifier = "authentication", type = Accessor.Type.GETTER)
    public Object getAuthentication()
    {
        return getPersistenceContext().getPropertyValue("authentication");
    }


    @Accessor(qualifier = "authenticationId", type = Accessor.Type.GETTER)
    public String getAuthenticationId()
    {
        return (String)getPersistenceContext().getPropertyValue("authenticationId");
    }


    @Accessor(qualifier = "client", type = Accessor.Type.GETTER)
    public OAuthClientDetailsModel getClient()
    {
        return (OAuthClientDetailsModel)getPersistenceContext().getPropertyValue("client");
    }


    @Accessor(qualifier = "refreshToken", type = Accessor.Type.GETTER)
    public OAuthRefreshTokenModel getRefreshToken()
    {
        return (OAuthRefreshTokenModel)getPersistenceContext().getPropertyValue("refreshToken");
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


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "authentication", type = Accessor.Type.SETTER)
    public void setAuthentication(Object value)
    {
        getPersistenceContext().setPropertyValue("authentication", value);
    }


    @Accessor(qualifier = "authenticationId", type = Accessor.Type.SETTER)
    public void setAuthenticationId(String value)
    {
        getPersistenceContext().setPropertyValue("authenticationId", value);
    }


    @Accessor(qualifier = "client", type = Accessor.Type.SETTER)
    public void setClient(OAuthClientDetailsModel value)
    {
        getPersistenceContext().setPropertyValue("client", value);
    }


    @Accessor(qualifier = "refreshToken", type = Accessor.Type.SETTER)
    public void setRefreshToken(OAuthRefreshTokenModel value)
    {
        getPersistenceContext().setPropertyValue("refreshToken", value);
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


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }
}
