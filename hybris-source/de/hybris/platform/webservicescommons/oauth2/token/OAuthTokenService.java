package de.hybris.platform.webservicescommons.oauth2.token;

import de.hybris.platform.webservicescommons.model.OAuthAccessTokenModel;
import de.hybris.platform.webservicescommons.model.OAuthRefreshTokenModel;
import java.util.List;

public interface OAuthTokenService
{
    OAuthAccessTokenModel getAccessToken(String paramString);


    void saveAccessToken(OAuthAccessTokenModel paramOAuthAccessTokenModel);


    OAuthAccessTokenModel saveAccessToken(String paramString1, Object paramObject1, String paramString2, Object paramObject2, String paramString3, String paramString4, OAuthRefreshTokenModel paramOAuthRefreshTokenModel);


    void removeAccessToken(String paramString);


    OAuthRefreshTokenModel getRefreshToken(String paramString);


    void saveRefreshToken(OAuthRefreshTokenModel paramOAuthRefreshTokenModel);


    OAuthRefreshTokenModel saveRefreshToken(String paramString, Object paramObject1, Object paramObject2);


    void removeRefreshToken(String paramString);


    void removeAccessTokenUsingRefreshToken(String paramString);


    List<OAuthAccessTokenModel> getAccessTokenListForRefreshToken(String paramString);


    List<OAuthAccessTokenModel> getAccessTokensForClient(String paramString);


    List<OAuthAccessTokenModel> getAccessTokensForUser(String paramString);


    OAuthAccessTokenModel getAccessTokenForAuthentication(String paramString);


    void removeAccessTokenForAuthentication(String paramString);


    List<OAuthAccessTokenModel> getAccessTokensForClientAndUser(String paramString1, String paramString2);
}
