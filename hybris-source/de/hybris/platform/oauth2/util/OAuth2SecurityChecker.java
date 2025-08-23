package de.hybris.platform.oauth2.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.expression.OAuth2ExpressionUtils;

public class OAuth2SecurityChecker
{
    public boolean clientHasRole(Authentication authentication, String role)
    {
        return clientHasAnyRole(authentication, new String[] {role});
    }


    public boolean clientHasAnyRole(Authentication authentication, String... roles)
    {
        return OAuth2ExpressionUtils.clientHasAnyRole(authentication, roles);
    }


    public boolean hasScope(Authentication authentication, String scope)
    {
        return hasAnyScope(authentication, new String[] {scope});
    }


    public boolean hasAnyScope(Authentication authentication, String... scopes)
    {
        boolean result = OAuth2ExpressionUtils.hasAnyScope(authentication, scopes);
        return result;
    }


    public boolean isUser(Authentication authentication)
    {
        return OAuth2ExpressionUtils.isOAuthUserAuth(authentication);
    }


    public boolean isClient(Authentication authentication)
    {
        return OAuth2ExpressionUtils.isOAuthClientAuth(authentication);
    }
}
