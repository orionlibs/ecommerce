package de.hybris.platform.webservicescommons.oauth2.token;

import java.util.Collection;

public interface OAuthRevokeTokenService
{
    boolean revokeAccessToken(String paramString);


    void revokeUserAccessTokens(String paramString, Collection<String> paramCollection);


    boolean revokeRefreshToken(String paramString);
}
