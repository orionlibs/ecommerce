package de.hybris.platform.webservicescommons.oauth2.token.impl;

import de.hybris.platform.webservicescommons.oauth2.token.OAuthRevokeTokenService;
import java.util.Collection;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;

public class DefaultOAuthRevokeTokenService implements OAuthRevokeTokenService
{
    private final TokenStore tokenStore;


    public DefaultOAuthRevokeTokenService(TokenStore tokenStore)
    {
        this.tokenStore = tokenStore;
    }


    public boolean revokeAccessToken(String tokenId)
    {
        OAuth2AccessToken accessToken = this.tokenStore.readAccessToken(tokenId);
        return revokeAccessToken(accessToken);
    }


    public void revokeUserAccessTokens(String userId, Collection<String> tokensToPreserve)
    {
        Collection<OAuth2AccessToken> allUserTokens = this.tokenStore.findTokensByClientIdAndUserName(null, userId);
        for(OAuth2AccessToken token : allUserTokens)
        {
            if(token != null && tokensToPreserve != null)
            {
                String tokenId = token.getValue();
                if(tokenId != null && tokensToPreserve.contains(tokenId))
                {
                    continue;
                }
            }
            revokeAccessToken(token);
        }
    }


    public boolean revokeRefreshToken(String tokenId)
    {
        OAuth2RefreshToken refreshToken = this.tokenStore.readRefreshToken(tokenId);
        return (refreshToken != null && removeUsingRefreshToken(refreshToken));
    }


    private boolean revokeAccessToken(OAuth2AccessToken accessToken)
    {
        if(accessToken == null)
        {
            return false;
        }
        if(accessToken.getRefreshToken() != null)
        {
            return removeUsingRefreshToken(accessToken.getRefreshToken());
        }
        this.tokenStore.removeAccessToken(accessToken);
        return true;
    }


    private boolean removeUsingRefreshToken(OAuth2RefreshToken refreshToken)
    {
        this.tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);
        this.tokenStore.removeRefreshToken(refreshToken);
        return true;
    }
}
