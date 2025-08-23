package de.hybris.platform.oauth2.provider.custom;

import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

public class DefaultCustomTokenGranter extends AbstractTokenGranter
{
    private static final String GRANT_TYPE = "custom";


    protected DefaultCustomTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory)
    {
        super(tokenServices, clientDetailsService, requestFactory, "custom");
    }


    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest)
    {
        throw new UnsupportedGrantTypeException("This is a custom grant, which does not have any default implementation. This needs to be implemented.");
    }
}
