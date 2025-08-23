package de.hybris.platform.webservicescommons.testsupport.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.core.Registry;
import java.io.IOException;
import java.util.Map;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

public class WsSecuredRequestBuilder extends WsAbstractRequestBuilder<WsSecuredRequestBuilder>
{
    public static final String WS_TEST_OAUTH2_TOKEN_ENDPOINT_PATH_KEY = "webservices.test.oauth2.endpoint";
    protected static final String HEADER_AUTH_KEY = "Authorization";
    protected static final String HEADER_AUTH_VALUE_PREFIX = "Bearer";
    private static final Logger LOG = Logger.getLogger(WsSecuredRequestBuilder.class);
    private static final String GRANT_TYPE_CLIENT_CRIDENTIALS = "client_credentials";
    private static final String GRANT_TYPE_PASSWORD = "password";
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private static final String OAUTH_EXTENSION_NAME = "oauth2";
    private final String oAuthEndpointPath = getDefaultOAuthEndpoint();
    private String oAuthClientId;
    private String oAuthClientSecret;
    private String oAuthResourceOwnerName;
    private String oAuthResourceOwnerPassword;
    private String oAuthScope;
    private OAuthGrantType oAuthGrantType;


    public WsSecuredRequestBuilder client(String clientId, String clientSecret)
    {
        this.oAuthClientId = clientId;
        this.oAuthClientSecret = clientSecret;
        return getThis();
    }


    public WsSecuredRequestBuilder scope(String... scope)
    {
        if(scope == null)
        {
            throw new IllegalArgumentException("scope has to have not null value");
        }
        this.oAuthScope = String.join(",", (CharSequence[])scope);
        return getThis();
    }


    public WsSecuredRequestBuilder resourceOwner(String oAuthResourceOwnerName, String oAuthResourceOwnerPassword)
    {
        this.oAuthResourceOwnerName = oAuthResourceOwnerName;
        this.oAuthResourceOwnerPassword = oAuthResourceOwnerPassword;
        return getThis();
    }


    public WsSecuredRequestBuilder grantClientCredentials()
    {
        this.oAuthGrantType = OAuthGrantType.CLIENT_CREDENTIALS;
        return getThis();
    }


    public WsSecuredRequestBuilder grantResourceOwnerPasswordCredentials()
    {
        this.oAuthGrantType = OAuthGrantType.RESOURCE_OWNER_PASSWORD_CREDENTIALS;
        return getThis();
    }


    protected String getDefaultOAuthEndpoint()
    {
        return Registry.getCurrentTenant().getConfig().getString("webservices.test.oauth2.endpoint", "/oauth/token");
    }


    protected String getOAuth2TokenUsingClientCredentials()
    {
        return getOAuth2TokenUsingClientCredentials(buildOAuthWebTarget(), this.oAuthClientId, this.oAuthClientSecret, this.oAuthScope);
    }


    protected String getOAuth2TokenUsingClientCredentials(WebTarget oAuthWebTarget, String clientID, String clientSecret, String scope)
    {
        try
        {
            Response result = oAuthWebTarget.queryParam("grant_type", new Object[] {"client_credentials"}).queryParam("client_id", new Object[] {clientID}).queryParam("client_secret", new Object[] {clientSecret}).queryParam("scope", new Object[] {scope}).request()
                            .accept(new String[] {"application/json"}).post(Entity.entity(null, "application/json"));
            result.bufferEntity();
            if(result.hasEntity())
            {
                return getTokenFromJsonStr((String)result.readEntity(String.class));
            }
            LOG.error("Empty response body!!");
            return null;
        }
        catch(IOException ex)
        {
            LOG.error("Error during authorizing REST client client credentials!!", ex);
            return null;
        }
    }


    protected String getOAuth2Token()
    {
        if(this.oAuthGrantType == null)
        {
            throw new WsAbstractRequestBuilder.WsRequestBuilderException("OAuth grant type not set!");
        }
        switch(null.$SwitchMap$de$hybris$platform$webservicescommons$testsupport$client$WsSecuredRequestBuilder$OAuthGrantType[this.oAuthGrantType.ordinal()])
        {
            case 1:
                return getOAuth2TokenUsingClientCredentials();
            case 2:
                return getOAuth2TokenUsingResourceOwnerPassword();
        }
        return null;
    }


    protected String getOAuth2TokenUsingResourceOwnerPassword()
    {
        return getOAuth2TokenUsingResourceOwnerPassword(buildOAuthWebTarget(), this.oAuthClientId, this.oAuthClientSecret, this.oAuthResourceOwnerName, this.oAuthResourceOwnerPassword, this.oAuthScope);
    }


    protected String getOAuth2TokenUsingResourceOwnerPassword(WebTarget oAuthWebTarget, String clientID, String clientSecret, String resourceOwnerName, String resourceOwnerPassword, String scope)
    {
        try
        {
            Response result = oAuthWebTarget.queryParam("grant_type", new Object[] {"password"}).queryParam("username", new Object[] {resourceOwnerName}).queryParam("password", new Object[] {resourceOwnerPassword}).queryParam("client_id", new Object[] {clientID})
                            .queryParam("client_secret", new Object[] {clientSecret}).queryParam("scope", new Object[] {scope}).request().accept(new String[] {"application/json"}).post(Entity.entity(null, "application/json"));
            result.bufferEntity();
            if(result.hasEntity())
            {
                return getTokenFromJsonStr((String)result.readEntity(String.class));
            }
            LOG.error("Empty response body!!");
            return null;
        }
        catch(IOException ex)
        {
            LOG.error("Error during authorizing REST client using Resource owner password!!", ex);
            return null;
        }
    }


    protected WebTarget buildOAuthWebTarget()
    {
        return createWebTarget(getHost(), getPort(), isUseHttps(), "oauth2", this.oAuthEndpointPath);
    }


    public Invocation.Builder build()
    {
        String token = getOAuth2Token();
        String authHeaderValue = "Bearer " + token;
        Invocation.Builder builder = super.build();
        builder.header("Authorization", authHeaderValue);
        return builder;
    }


    public String getTokenFromJsonStr(String jsonStr) throws IOException
    {
        Map<String, String> map = (Map<String, String>)this.jsonMapper.readValue(jsonStr, (TypeReference)new Object(this));
        return map.get("access_token");
    }


    protected WsSecuredRequestBuilder getThis()
    {
        return this;
    }
}
