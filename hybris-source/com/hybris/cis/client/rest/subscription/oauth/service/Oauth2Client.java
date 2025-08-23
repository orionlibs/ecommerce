package com.hybris.cis.client.rest.subscription.oauth.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public interface Oauth2Client
{
    ResponseEntity<OAuth2AccessToken> getToken();
}
