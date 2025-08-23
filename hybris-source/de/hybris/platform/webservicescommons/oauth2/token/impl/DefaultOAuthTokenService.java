package de.hybris.platform.webservicescommons.oauth2.token.impl;

import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.user.daos.UserDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.model.OAuthAccessTokenModel;
import de.hybris.platform.webservicescommons.model.OAuthRefreshTokenModel;
import de.hybris.platform.webservicescommons.oauth2.client.ClientDetailsDao;
import de.hybris.platform.webservicescommons.oauth2.token.OAuthTokenService;
import de.hybris.platform.webservicescommons.oauth2.token.dao.OAuthTokenDao;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class DefaultOAuthTokenService extends AbstractBusinessService implements OAuthTokenService
{
    private static final Logger log = LoggerFactory.getLogger(DefaultOAuthTokenService.class);
    private OAuthTokenDao oauthTokenDao;
    private SearchRestrictionService searchRestrictionService;
    private UserDao userDao;
    private ClientDetailsDao clientDetailsDao;


    public OAuthAccessTokenModel getAccessToken(String id)
    {
        ServicesUtil.validateParameterNotNull(id, "Parameter 'id' must not be null!");
        return (OAuthAccessTokenModel)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, id));
    }


    public void saveAccessToken(OAuthAccessTokenModel token)
    {
        ServicesUtil.validateParameterNotNull(token, "Parameter 'token' must not be null!");
        getModelService().save(token);
    }


    public OAuthRefreshTokenModel getRefreshToken(String id)
    {
        ServicesUtil.validateParameterNotNull(id, "Parameter 'id' must not be null!");
        return (OAuthRefreshTokenModel)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, id));
    }


    public void saveRefreshToken(OAuthRefreshTokenModel token)
    {
        ServicesUtil.validateParameterNotNull(token, "Parameter 'token' must not be null!");
        getModelService().save(token);
    }


    public void removeAccessToken(String id)
    {
        ServicesUtil.validateParameterNotNull(id, "Parameter 'id' must not be null!");
        getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, id));
    }


    public void removeRefreshToken(String id)
    {
        ServicesUtil.validateParameterNotNull(id, "Parameter 'id' must not be null!");
        getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, id));
    }


    public void removeAccessTokenUsingRefreshToken(String refreshTokenId)
    {
        ServicesUtil.validateParameterNotNull(refreshTokenId, "Parameter 'refreshTokenId' must not be null!");
        getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, refreshTokenId));
    }


    public List<OAuthAccessTokenModel> getAccessTokenListForRefreshToken(String refreshTokenId)
    {
        ServicesUtil.validateParameterNotNull(refreshTokenId, "Parameter 'refreshTokenId' must not be null!");
        return (List<OAuthAccessTokenModel>)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, refreshTokenId));
    }


    public OAuthAccessTokenModel getAccessTokenForAuthentication(String authenticationId)
    {
        ServicesUtil.validateParameterNotNull(authenticationId, "Parameter 'authenticationId' must not be null!");
        return (OAuthAccessTokenModel)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, authenticationId));
    }


    public List<OAuthAccessTokenModel> getAccessTokensForClient(String clientId)
    {
        ServicesUtil.validateParameterNotNull(clientId, "Parameter 'clientId' must not be null!");
        return (List<OAuthAccessTokenModel>)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, clientId));
    }


    public List<OAuthAccessTokenModel> getAccessTokensForUser(String userName)
    {
        ServicesUtil.validateParameterNotNull(userName, "Parameter 'userName' must not be null!");
        return (List<OAuthAccessTokenModel>)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, userName));
    }


    public List<OAuthAccessTokenModel> getAccessTokensForClientAndUser(String clientId, String userName)
    {
        ServicesUtil.validateParameterNotNull(userName, "Parameter 'userName' must not be null!");
        ServicesUtil.validateParameterNotNull(clientId, "Parameter 'clientId' must not be null!");
        return (List<OAuthAccessTokenModel>)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, clientId, userName));
    }


    public void removeAccessTokenForAuthentication(String authenticationId)
    {
        ServicesUtil.validateParameterNotNull(authenticationId, "Parameter 'authenticationId' must not be null!");
        getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, authenticationId));
    }


    public OAuthRefreshTokenModel saveRefreshToken(String refreshTokenId, Object refreshToken, Object authentication)
    {
        ServicesUtil.validateParameterNotNull(refreshTokenId, "Parameter 'refreshTokenId' must not be null!");
        return (OAuthRefreshTokenModel)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, refreshTokenId, refreshToken, authentication));
    }


    public OAuthAccessTokenModel saveAccessToken(String accessTokenId, Object accessToken, String authenticationId, Object authentication, String userName, String clientId, OAuthRefreshTokenModel refreshTokenModel)
    {
        ServicesUtil.validateParameterNotNull(accessTokenId, "Parameter 'accessTokenId' must not be null!");
        return (OAuthAccessTokenModel)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, accessTokenId, clientId, userName, refreshTokenModel, accessToken, authenticationId, authentication));
    }


    private void saveOrSkipSavingAccessToken(OAuthAccessTokenModel accessTokenModel, Object authentication, Object savedAuthentication)
    {
        if(!canSkipSaveForAccessTokenModel(authentication, savedAuthentication))
        {
            getModelService().save(accessTokenModel);
        }
    }


    private boolean isOptimizeAccessTokenSaveEnabled()
    {
        return Config.getBoolean("oauth2.optimize.accesstoken.save.enabled", true);
    }


    private boolean canSkipSaveForAccessTokenModel(Object authentication, Object authenticationFromDB)
    {
        if(!isOptimizeAccessTokenSaveEnabled())
        {
            return false;
        }
        OAuth2Authentication auth2Authentication = deserializeToOAuth2Authentication(authentication);
        if(auth2Authentication == null)
        {
            return false;
        }
        OAuth2Authentication auth2AuthenticationFromDB = deserializeToOAuth2Authentication(authenticationFromDB);
        if(auth2AuthenticationFromDB == null)
        {
            return false;
        }
        return auth2Authentication.equals(auth2AuthenticationFromDB);
    }


    private OAuth2Authentication deserializeToOAuth2Authentication(Object authentication)
    {
        Object authenticationObject;
        if(!(authentication instanceof byte[]))
        {
            return null;
        }
        try
        {
            authenticationObject = SerializationUtils.deserialize((byte[])authentication);
        }
        catch(IllegalArgumentException iea)
        {
            log.debug("Failed to deserialize authentication object", iea);
            return null;
        }
        if(authenticationObject instanceof OAuth2Authentication)
        {
            return (OAuth2Authentication)authenticationObject;
        }
        return null;
    }


    public OAuthTokenDao getOauthTokenDao()
    {
        return this.oauthTokenDao;
    }


    @Required
    public void setOauthTokenDao(OAuthTokenDao oauthTokenDao)
    {
        this.oauthTokenDao = oauthTokenDao;
    }


    public SearchRestrictionService getSearchRestrictionService()
    {
        return this.searchRestrictionService;
    }


    @Required
    public void setSearchRestrictionService(SearchRestrictionService searchRestrictionService)
    {
        this.searchRestrictionService = searchRestrictionService;
    }


    @Required
    public void setUserDao(UserDao userDao)
    {
        this.userDao = userDao;
    }


    @Required
    public void setClientDetailsDao(ClientDetailsDao clientDetailsDao)
    {
        this.clientDetailsDao = clientDetailsDao;
    }
}
