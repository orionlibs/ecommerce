package de.hybris.platform.webservicescommons.oauth2.token.provider;

import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.webservicescommons.model.OAuthAccessTokenModel;
import de.hybris.platform.webservicescommons.model.OAuthRefreshTokenModel;
import de.hybris.platform.webservicescommons.oauth2.token.OAuthTokenService;
import de.hybris.platform.webservicescommons.util.YSanitizer;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

public class HybrisOAuthTokenStore implements TokenStore
{
    private static final Logger LOG = Logger.getLogger(HybrisOAuthTokenStore.class);
    private final AuthenticationKeyGenerator authenticationKeyGenerator = (AuthenticationKeyGenerator)new DefaultAuthenticationKeyGenerator();
    private OAuthTokenService oauthTokenService;


    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication)
    {
        OAuth2AccessToken accessToken = null;
        OAuthAccessTokenModel accessTokenModel = null;
        String authenticationId = this.authenticationKeyGenerator.extractKey(authentication);
        try
        {
            accessTokenModel = this.oauthTokenService.getAccessTokenForAuthentication(authenticationId);
            accessToken = deserializeAccessToken((byte[])accessTokenModel.getToken());
        }
        catch(IllegalArgumentException | ClassCastException e)
        {
            LOG.warn("Could not extract access token for authentication " + authentication);
            this.oauthTokenService.removeAccessTokenForAuthentication(authenticationId);
        }
        catch(UnknownIdentifierException e)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.debug("Failed to find access token for authentication " + authentication);
            }
        }
        try
        {
            if(accessToken != null && accessTokenModel != null &&
                            !StringUtils.equals(authenticationId, this.authenticationKeyGenerator
                                            .extractKey(deserializeAuthentication((byte[])accessTokenModel.getAuthentication()))))
            {
                replaceToken(authentication, accessToken);
            }
        }
        catch(IllegalArgumentException | ClassCastException ex)
        {
            replaceToken(authentication, accessToken);
        }
        return accessToken;
    }


    protected void replaceToken(OAuth2Authentication authentication, OAuth2AccessToken accessToken)
    {
        removeAccessToken(accessToken.getValue());
        storeAccessToken(accessToken, authentication);
    }


    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication)
    {
        OAuthRefreshTokenModel refreshTokenModel = null;
        if(token.getRefreshToken() != null)
        {
            String refreshTokenKey = extractTokenKey(token.getRefreshToken().getValue());
            try
            {
                refreshTokenModel = this.oauthTokenService.getRefreshToken(refreshTokenKey);
            }
            catch(UnknownIdentifierException e)
            {
                refreshTokenModel = this.oauthTokenService.saveRefreshToken(refreshTokenKey,
                                serializeRefreshToken(token.getRefreshToken()),
                                serializeAuthentication(authentication));
            }
        }
        if(Config.getBoolean("oauth2.accesstoken.save.retry", true))
        {
            tryToSaveAccessTokenWithRetryOnModelSavingException(token, authentication, refreshTokenModel);
        }
        else
        {
            saveAccessToken(token, authentication, refreshTokenModel);
        }
    }


    private void tryToSaveAccessTokenWithRetryOnModelSavingException(OAuth2AccessToken token, OAuth2Authentication authentication, OAuthRefreshTokenModel refreshTokenModel)
    {
        try
        {
            saveAccessToken(token, authentication, refreshTokenModel);
        }
        catch(ModelSavingException e)
        {
            if(isSpringDuplicateKeyException((Exception)e))
            {
                saveAccessToken(token, authentication, refreshTokenModel);
            }
        }
    }


    private void saveAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication, OAuthRefreshTokenModel refreshTokenModel)
    {
        this.oauthTokenService.saveAccessToken(extractTokenKey(token.getValue()), serializeAccessToken(token), this.authenticationKeyGenerator
                                        .extractKey(authentication),
                        serializeAuthentication(authentication),
                        authentication.isClientOnly() ? null : authentication.getName(), authentication
                                        .getOAuth2Request().getClientId(), refreshTokenModel);
    }


    protected boolean isSpringDuplicateKeyException(Exception e)
    {
        return (Utilities.getRootCauseOfType(e, DuplicateKeyException.class) != null);
    }


    public OAuth2AccessToken readAccessToken(String tokenValue)
    {
        OAuth2AccessToken accessToken = null;
        try
        {
            OAuthAccessTokenModel accessTokenModel = this.oauthTokenService.getAccessToken(extractTokenKey(tokenValue));
            accessToken = deserializeAccessToken((byte[])accessTokenModel.getToken());
        }
        catch(IllegalArgumentException | ClassCastException e)
        {
            LOG.warn("Failed to deserialize access token for  " + YSanitizer.sanitize(tokenValue));
            removeAccessToken(tokenValue);
        }
        catch(UnknownIdentifierException e)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("Failed to find access token for token " + YSanitizer.sanitize(tokenValue));
            }
        }
        return accessToken;
    }


    public void removeAccessToken(OAuth2AccessToken token)
    {
        removeAccessToken(token.getValue());
    }


    public void removeAccessToken(String tokenValue)
    {
        this.oauthTokenService.removeAccessToken(extractTokenKey(tokenValue));
    }


    public OAuth2Authentication readAuthentication(OAuth2AccessToken token)
    {
        return readAuthentication(token.getValue());
    }


    public OAuth2Authentication readAuthentication(String token)
    {
        OAuth2Authentication authentication = null;
        try
        {
            OAuthAccessTokenModel accessTokenModel = this.oauthTokenService.getAccessToken(extractTokenKey(token));
            authentication = deserializeAuthentication((byte[])accessTokenModel.getAuthentication());
        }
        catch(IllegalArgumentException | ClassCastException e)
        {
            LOG.warn("Failed to deserialize authentication for " + YSanitizer.sanitize(token));
            removeAccessToken(token);
        }
        catch(UnknownIdentifierException e)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("Failed to find authentication for token " + YSanitizer.sanitize(token));
            }
        }
        return authentication;
    }


    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication)
    {
        this.oauthTokenService.saveRefreshToken(extractTokenKey(refreshToken.getValue()), serializeRefreshToken(refreshToken),
                        serializeAuthentication(authentication));
    }


    public OAuth2RefreshToken readRefreshToken(String token)
    {
        OAuth2RefreshToken refreshToken = null;
        try
        {
            OAuthRefreshTokenModel refreshTokenModel = this.oauthTokenService.getRefreshToken(extractTokenKey(token));
            refreshToken = deserializeRefreshToken((byte[])refreshTokenModel.getToken());
        }
        catch(IllegalArgumentException | ClassCastException e)
        {
            LOG.warn("Failed to deserialize refresh token for token " + YSanitizer.sanitize(token));
            removeRefreshToken(token);
        }
        catch(UnknownIdentifierException e)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("Failed to find refresh token for token " + YSanitizer.sanitize(token));
            }
        }
        return refreshToken;
    }


    public void removeRefreshToken(OAuth2RefreshToken token)
    {
        removeRefreshToken(token.getValue());
    }


    public void removeRefreshToken(String token)
    {
        this.oauthTokenService.removeRefreshToken(extractTokenKey(token));
    }


    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token)
    {
        return readAuthenticationForRefreshToken(token.getValue());
    }


    public OAuth2Authentication readAuthenticationForRefreshToken(String value)
    {
        OAuth2Authentication authentication = null;
        try
        {
            OAuthRefreshTokenModel refreshTokenModel = this.oauthTokenService.getRefreshToken(extractTokenKey(value));
            authentication = deserializeAuthentication((byte[])refreshTokenModel.getAuthentication());
        }
        catch(IllegalArgumentException | ClassCastException e)
        {
            LOG.warn("Failed to deserialize authentication for refresh token " + YSanitizer.sanitize(value));
            List<OAuthAccessTokenModel> accessTokenModelList = this.oauthTokenService.getAccessTokenListForRefreshToken(extractTokenKey(value));
            for(OAuthAccessTokenModel accessTokenModel : accessTokenModelList)
            {
                LOG.warn("Trying to deserialize authentication from parent access token " + accessTokenModel);
                try
                {
                    authentication = deserializeAuthentication((byte[])accessTokenModel.getAuthentication());
                    break;
                }
                catch(IllegalArgumentException | ClassCastException ignored)
                {
                    LOG.warn("Failed to deserialize authentication for access token " + accessTokenModel);
                }
            }
            if(authentication == null)
            {
                removeAccessTokenUsingRefreshToken(value);
                removeRefreshToken(value);
                LOG.warn("Failed to deserialize authentication for access token");
            }
        }
        catch(UnknownIdentifierException e)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("Failed to find refresh token for " + YSanitizer.sanitize(value));
            }
        }
        return authentication;
    }


    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken)
    {
        removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }


    public void removeAccessTokenUsingRefreshToken(String refreshToken)
    {
        this.oauthTokenService.removeAccessTokenUsingRefreshToken(extractTokenKey(refreshToken));
    }


    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId)
    {
        List<OAuth2AccessToken> accessTokenList = new ArrayList<>();
        List<OAuthAccessTokenModel> accessTokenModelList = this.oauthTokenService.getAccessTokensForClient(clientId);
        for(OAuthAccessTokenModel accessTokenModel : accessTokenModelList)
        {
            try
            {
                OAuth2AccessToken accessToken = deserializeAccessToken((byte[])accessTokenModel.getToken());
                accessTokenList.add(accessToken);
            }
            catch(IllegalArgumentException | ClassCastException e)
            {
                LOG.warn("Failed to deserialize access token for client : " + YSanitizer.sanitize(clientId));
                this.oauthTokenService.removeAccessToken(accessTokenModel.getTokenId());
            }
        }
        return accessTokenList;
    }


    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName)
    {
        if(clientId == null)
        {
            return findTokensByUserName(userName);
        }
        List<OAuth2AccessToken> accessTokenList = new ArrayList<>();
        List<OAuthAccessTokenModel> accessTokenModelList = this.oauthTokenService.getAccessTokensForClientAndUser(clientId, userName);
        for(OAuthAccessTokenModel accessTokenModel : accessTokenModelList)
        {
            try
            {
                OAuth2AccessToken accessToken = deserializeAccessToken((byte[])accessTokenModel.getToken());
                accessTokenList.add(accessToken);
            }
            catch(IllegalArgumentException | ClassCastException e)
            {
                LOG.warn("Failed to deserialize access token for client : " + YSanitizer.sanitize(clientId));
                this.oauthTokenService.removeAccessToken(accessTokenModel.getTokenId());
            }
        }
        return accessTokenList;
    }


    public Collection<OAuth2AccessToken> findTokensByUserName(String userName)
    {
        List<OAuth2AccessToken> accessTokenList = new ArrayList<>();
        List<OAuthAccessTokenModel> accessTokenModelList = this.oauthTokenService.getAccessTokensForUser(userName);
        for(OAuthAccessTokenModel accessTokenModel : accessTokenModelList)
        {
            try
            {
                OAuth2AccessToken accessToken = deserializeAccessToken((byte[])accessTokenModel.getToken());
                accessTokenList.add(accessToken);
            }
            catch(IllegalArgumentException | ClassCastException e)
            {
                LOG.warn("Failed to deserialize access token for user : " + YSanitizer.sanitize(userName));
                this.oauthTokenService.removeAccessToken(accessTokenModel.getTokenId());
            }
        }
        return accessTokenList;
    }


    protected String extractTokenKey(String value)
    {
        MessageDigest digest;
        if(value == null)
        {
            return null;
        }
        try
        {
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new IllegalStateException("SHA-256 algorithm not available.  Fatal (should be in the JDK).");
        }
        try
        {
            byte[] bytes = digest.digest(value.getBytes("UTF-8"));
            return String.format("%032x", new Object[] {new BigInteger(1, bytes)});
        }
        catch(UnsupportedEncodingException e)
        {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
        }
    }


    protected byte[] serializeAccessToken(OAuth2AccessToken token)
    {
        return SerializationUtils.serialize(token);
    }


    protected byte[] serializeRefreshToken(OAuth2RefreshToken token)
    {
        return SerializationUtils.serialize(token);
    }


    protected byte[] serializeAuthentication(OAuth2Authentication authentication)
    {
        return SerializationUtils.serialize(authentication);
    }


    protected OAuth2AccessToken deserializeAccessToken(byte[] token)
    {
        return (OAuth2AccessToken)SerializationUtils.deserialize(token);
    }


    protected OAuth2RefreshToken deserializeRefreshToken(byte[] token)
    {
        return (OAuth2RefreshToken)SerializationUtils.deserialize(token);
    }


    protected OAuth2Authentication deserializeAuthentication(byte[] authentication)
    {
        return (OAuth2Authentication)SerializationUtils.deserialize(authentication);
    }


    public OAuthTokenService getOauthTokenService()
    {
        return this.oauthTokenService;
    }


    @Required
    public void setOauthTokenService(OAuthTokenService oauthTokenService)
    {
        this.oauthTokenService = oauthTokenService;
    }
}
