package com.hybris.cis.client.rest.subscription.oauth.service.impl;

import com.hybris.cis.client.rest.subscription.oauth.service.Oauth2Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service("defaultOauth2Client")
public class Oauth2ClientImpl implements Oauth2Client
{
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String GRANT_TYPE = "grant_type";
    @Autowired
    private ResourceOwnerPasswordResourceDetails resourceDetails;
    @Autowired
    @Qualifier("defaultRestTemplateBuilder")
    private RestTemplateBuilder restTemplateBuilder;


    public ResponseEntity<OAuth2AccessToken> getToken()
    {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.resourceDetails.getAccessTokenUri()).queryParam("client_id", new Object[] {this.resourceDetails.getClientId()}).queryParam("client_secret", new Object[] {this.resourceDetails.getClientSecret()})
                        .queryParam("username", new Object[] {this.resourceDetails.getUsername()}).queryParam("password", new Object[] {this.resourceDetails.getPassword()}).queryParam("grant_type", new Object[] {this.resourceDetails.getGrantType()});
        ResponseEntity<OAuth2AccessToken> response = getRestTemplateBuilder().build().postForEntity(builder.toUriString(), null, OAuth2AccessToken.class, new Object[0]);
        return response;
    }


    public RestTemplateBuilder getRestTemplateBuilder()
    {
        return this.restTemplateBuilder;
    }


    public void setRestTemplateBuilder(RestTemplateBuilder restTemplateBuilder)
    {
        this.restTemplateBuilder = restTemplateBuilder;
    }
}
