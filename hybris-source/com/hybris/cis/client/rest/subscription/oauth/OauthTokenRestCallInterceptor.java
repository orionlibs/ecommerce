package com.hybris.cis.client.rest.subscription.oauth;

import com.hybris.cis.client.rest.subscription.oauth.service.Oauth2Client;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public class OauthTokenRestCallInterceptor implements ClientHttpRequestInterceptor
{
    public static final String TOKEN_HEADER = "access-token";
    private OAuth2AccessToken oAuth2AccessToken = null;
    @Autowired
    private Oauth2Client oauth2Client;
    @Value("${hybris.sbg.web.platform.oauth.enabled:true}")
    private boolean oauthEnabled;


    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException
    {
        if(isOauthEnabled() && !request.getHeaders().containsKey("access-token"))
        {
            if(getoAuth2AccessToken() == null || getoAuth2AccessToken().isExpired())
            {
                setoAuth2AccessToken((OAuth2AccessToken)getOauth2Client().getToken().getBody());
            }
            request.getHeaders().add("access-token", getoAuth2AccessToken().getValue());
        }
        return execution.execute(request, body);
    }


    public OAuth2AccessToken getoAuth2AccessToken()
    {
        return this.oAuth2AccessToken;
    }


    public void setoAuth2AccessToken(OAuth2AccessToken oAuth2AccessToken)
    {
        this.oAuth2AccessToken = oAuth2AccessToken;
    }


    public boolean isOauthEnabled()
    {
        return this.oauthEnabled;
    }


    public void setOauthEnabled(boolean oauthEnabled)
    {
        this.oauthEnabled = oauthEnabled;
    }


    public Oauth2Client getOauth2Client()
    {
        return this.oauth2Client;
    }


    public void setOauth2Client(Oauth2Client oauth2Client)
    {
        this.oauth2Client = oauth2Client;
    }
}
