package de.hybris.platform.webservicescommons.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class OAuthClientDetailsModel extends ItemModel
{
    public static final String _TYPECODE = "OAuthClientDetails";
    public static final String CLIENTID = "clientId";
    public static final String RESOURCEIDS = "resourceIds";
    public static final String CLIENTSECRET = "clientSecret";
    public static final String SCOPE = "scope";
    public static final String AUTHORIZEDGRANTTYPES = "authorizedGrantTypes";
    public static final String REGISTEREDREDIRECTURI = "registeredRedirectUri";
    public static final String AUTHORITIES = "authorities";
    public static final String ACCESSTOKENVALIDITYSECONDS = "accessTokenValiditySeconds";
    public static final String REFRESHTOKENVALIDITYSECONDS = "refreshTokenValiditySeconds";
    public static final String AUTOAPPROVE = "autoApprove";
    public static final String DISABLED = "disabled";
    public static final String OAUTHURL = "oAuthUrl";


    public OAuthClientDetailsModel()
    {
    }


    public OAuthClientDetailsModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OAuthClientDetailsModel(String _clientId)
    {
        setClientId(_clientId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OAuthClientDetailsModel(String _clientId, ItemModel _owner)
    {
        setClientId(_clientId);
        setOwner(_owner);
    }


    @Accessor(qualifier = "accessTokenValiditySeconds", type = Accessor.Type.GETTER)
    public Integer getAccessTokenValiditySeconds()
    {
        return (Integer)getPersistenceContext().getPropertyValue("accessTokenValiditySeconds");
    }


    @Accessor(qualifier = "authorities", type = Accessor.Type.GETTER)
    public Set<String> getAuthorities()
    {
        return (Set<String>)getPersistenceContext().getPropertyValue("authorities");
    }


    @Accessor(qualifier = "authorizedGrantTypes", type = Accessor.Type.GETTER)
    public Set<String> getAuthorizedGrantTypes()
    {
        return (Set<String>)getPersistenceContext().getPropertyValue("authorizedGrantTypes");
    }


    @Accessor(qualifier = "autoApprove", type = Accessor.Type.GETTER)
    public Set<String> getAutoApprove()
    {
        return (Set<String>)getPersistenceContext().getPropertyValue("autoApprove");
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


    @Accessor(qualifier = "disabled", type = Accessor.Type.GETTER)
    public Boolean getDisabled()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("disabled");
    }


    @Accessor(qualifier = "oAuthUrl", type = Accessor.Type.GETTER)
    public String getOAuthUrl()
    {
        return (String)getPersistenceContext().getPropertyValue("oAuthUrl");
    }


    @Accessor(qualifier = "refreshTokenValiditySeconds", type = Accessor.Type.GETTER)
    public Integer getRefreshTokenValiditySeconds()
    {
        return (Integer)getPersistenceContext().getPropertyValue("refreshTokenValiditySeconds");
    }


    @Accessor(qualifier = "registeredRedirectUri", type = Accessor.Type.GETTER)
    public Set<String> getRegisteredRedirectUri()
    {
        return (Set<String>)getPersistenceContext().getPropertyValue("registeredRedirectUri");
    }


    @Accessor(qualifier = "resourceIds", type = Accessor.Type.GETTER)
    public Set<String> getResourceIds()
    {
        return (Set<String>)getPersistenceContext().getPropertyValue("resourceIds");
    }


    @Accessor(qualifier = "scope", type = Accessor.Type.GETTER)
    public Set<String> getScope()
    {
        return (Set<String>)getPersistenceContext().getPropertyValue("scope");
    }


    @Accessor(qualifier = "accessTokenValiditySeconds", type = Accessor.Type.SETTER)
    public void setAccessTokenValiditySeconds(Integer value)
    {
        getPersistenceContext().setPropertyValue("accessTokenValiditySeconds", value);
    }


    @Accessor(qualifier = "authorities", type = Accessor.Type.SETTER)
    public void setAuthorities(Set<String> value)
    {
        getPersistenceContext().setPropertyValue("authorities", value);
    }


    @Accessor(qualifier = "authorizedGrantTypes", type = Accessor.Type.SETTER)
    public void setAuthorizedGrantTypes(Set<String> value)
    {
        getPersistenceContext().setPropertyValue("authorizedGrantTypes", value);
    }


    @Accessor(qualifier = "autoApprove", type = Accessor.Type.SETTER)
    public void setAutoApprove(Set<String> value)
    {
        getPersistenceContext().setPropertyValue("autoApprove", value);
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


    @Accessor(qualifier = "disabled", type = Accessor.Type.SETTER)
    public void setDisabled(Boolean value)
    {
        getPersistenceContext().setPropertyValue("disabled", value);
    }


    @Accessor(qualifier = "oAuthUrl", type = Accessor.Type.SETTER)
    public void setOAuthUrl(String value)
    {
        getPersistenceContext().setPropertyValue("oAuthUrl", value);
    }


    @Accessor(qualifier = "refreshTokenValiditySeconds", type = Accessor.Type.SETTER)
    public void setRefreshTokenValiditySeconds(Integer value)
    {
        getPersistenceContext().setPropertyValue("refreshTokenValiditySeconds", value);
    }


    @Accessor(qualifier = "registeredRedirectUri", type = Accessor.Type.SETTER)
    public void setRegisteredRedirectUri(Set<String> value)
    {
        getPersistenceContext().setPropertyValue("registeredRedirectUri", value);
    }


    @Accessor(qualifier = "resourceIds", type = Accessor.Type.SETTER)
    public void setResourceIds(Set<String> value)
    {
        getPersistenceContext().setPropertyValue("resourceIds", value);
    }


    @Accessor(qualifier = "scope", type = Accessor.Type.SETTER)
    public void setScope(Set<String> value)
    {
        getPersistenceContext().setPropertyValue("scope", value);
    }
}
