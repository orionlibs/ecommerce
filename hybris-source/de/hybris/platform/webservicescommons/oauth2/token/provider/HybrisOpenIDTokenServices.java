package de.hybris.platform.webservicescommons.oauth2.token.provider;

import de.hybris.platform.oauth2.jwt.exceptions.JwtException;
import de.hybris.platform.oauth2.jwt.exceptions.KeyStoreProcessingException;
import de.hybris.platform.oauth2.jwt.util.IdTokenHelper;
import de.hybris.platform.oauth2.jwt.util.KeyStoreHelper;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import de.hybris.platform.webservicescommons.model.OpenIDClientDetailsModel;
import de.hybris.platform.webservicescommons.oauth2.client.ClientDetailsDao;
import de.hybris.platform.webservicescommons.oauth2.scope.OpenIDExternalScopesStrategy;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

@Deprecated(since = "ages", forRemoval = true)
public class HybrisOpenIDTokenServices extends HybrisOAuthTokenServices
{
    protected static final String ID_TOKEN = "id_token";
    protected static final String OPENID_SCOPE_NAME = "openid";
    protected static final String ID_TOKEN_VALIDITY_SECONDS = "idTokenValiditySeconds";
    protected static final String KID = "kid";
    protected static final String KEYSTORE_LOCATION = "keystore.location";
    protected static final String KEYSTORE_PASSWORD = "keystore.password";
    protected static final String ISS = "iss";
    protected static final String OAUTH2_ALGORITHM = "algorithm";
    protected ClientDetailsDao clientDetailsDao;
    protected KeyStoreHelper keyStoreHelper;
    private static final String ERROR_MESSAGE = "Server error. Can't generate id_token.";
    private static final String PARAM_STATE = "state";
    private static final String PARAM_NONCE = "nonce";
    private int idTokenValiditySeconds = 3600;
    private String kid;
    private String algorithm = "RS256";
    private String securityKeystoreLocation;
    private String securityKeystorePassword;
    private Signer signer;
    private String dummyEmail;
    private static final Logger LOG = Logger.getLogger(HybrisOpenIDTokenServices.class);
    private OpenIDExternalScopesStrategy externalScopesStrategy;


    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException
    {
        try
        {
            OAuth2AccessToken accessToken = super.createAccessToken(authentication);
            Set<String> responseTypes = OAuth2Utils.parseParameterList((String)authentication.getOAuth2Request().getRequestParameters().get("response_type"));
            if(responseTypes.contains("id_token"))
            {
                IdTokenHelper idTokenHelper;
                OAuthClientDetailsModel clientDetailsModel = this.clientDetailsDao.findClientById(authentication.getOAuth2Request().getClientId());
                if(!(clientDetailsModel instanceof OpenIDClientDetailsModel))
                {
                    LOG.warn("OAuth2 error, wrong configuration - Client with ID " + clientDetailsModel.getClientId() + " is not instance of " + OpenIDClientDetailsModel.class
                                    .getName());
                    throw new InvalidRequestException("Server error. Can't generate id_token.");
                }
                OpenIDClientDetailsModel openIDClientDetailsModel = (OpenIDClientDetailsModel)clientDetailsModel;
                List<String> externalScopes = null;
                if(openIDClientDetailsModel.getExternalScopeClaimName() != null)
                {
                    externalScopes = this.externalScopesStrategy.getExternalScopes(clientDetailsModel, (String)authentication
                                    .getPrincipal());
                    LOG.debug("externalScopes: " + externalScopes);
                }
                initializeIdTokenParameters(openIDClientDetailsModel.getClientId());
                DefaultOAuth2AccessToken accessTokenIdToken = new DefaultOAuth2AccessToken(accessToken);
                String requestedScopes = (String)authentication.getOAuth2Request().getRequestParameters().get("scope");
                if(StringUtils.isEmpty(requestedScopes) || !requestedScopes.contains("openid"))
                {
                    LOG.warn("Missing openid scope");
                    throw new InvalidRequestException("Missing openid scope");
                }
                String nonce = (String)authentication.getOAuth2Request().getRequestParameters().get("nonce");
                String state = (String)authentication.getOAuth2Request().getRequestParameters().get("state");
                try
                {
                    idTokenHelper = (new IdTokenHelper.IdTokenBuilder((new IdTokenHelper.HeaderBuilder()).alg(this.algorithm).kid(this.kid).getHeaders(),
                                    (new IdTokenHelper.ClaimsBuilder()).iss(openIDClientDetailsModel.getIssuer()).nonce(nonce).state(state).sub(authentication.getPrincipal().toString()).iat().exp(this.idTokenValiditySeconds)
                                                    .setScopes("scope", (List)Stream.<String>of("openid").collect(Collectors.toList())).addScopes(openIDClientDetailsModel.getExternalScopeClaimName(), externalScopes).addClaim("email", this.dummyEmail).getClaims())).build();
                }
                catch(JwtException e)
                {
                    throw new InvalidRequestException("Server error. Can't generate id_token.", e);
                }
                Jwt jwt = idTokenHelper.encodeAndSign(this.signer);
                Map<String, Object> map = new HashMap<>();
                map.put("id_token", jwt.getEncoded());
                accessTokenIdToken.setAdditionalInformation(map);
                return (OAuth2AccessToken)accessTokenIdToken;
            }
            return accessToken;
        }
        catch(ModelSavingException e)
        {
            LOG.debug("HybrisOAuthTokenServices->createAccessToken : ModelSavingException : " + e.getMessage());
            return super.createAccessToken(authentication);
        }
        catch(ModelRemovalException e)
        {
            LOG.debug("HybrisOAuthTokenServices->createAccessToken : ModelRemovalException :" + e.getMessage());
            return super.createAccessToken(authentication);
        }
    }


    private void initializeIdTokenParameters(String clientId)
    {
        this.kid = getPropertyValue("kid", clientId, true);
        this.securityKeystoreLocation = getPropertyValue("keystore.location", clientId, true);
        this.securityKeystorePassword = getPropertyValue("keystore.password", clientId, true);
        if(NumberUtils.isDigits(Config.getParameter("oauth2.idTokenValiditySeconds")))
        {
            this
                            .idTokenValiditySeconds = Integer.valueOf(Config.getParameter("oauth2.idTokenValiditySeconds")).intValue();
        }
        if(!StringUtils.isEmpty(Config.getParameter("oauth2.algorithm")))
        {
            this.algorithm = Config.getParameter("oauth2.algorithm");
        }
        try
        {
            InputStream resourceAsStream = HybrisOpenIDTokenServices.class.getResourceAsStream(this.securityKeystoreLocation);
            try
            {
                this.signer = this.keyStoreHelper.getSigner("JKS", resourceAsStream, this.kid, this.securityKeystorePassword);
                if(resourceAsStream != null)
                {
                    resourceAsStream.close();
                }
            }
            catch(Throwable throwable)
            {
                if(resourceAsStream != null)
                {
                    try
                    {
                        resourceAsStream.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(KeyStoreProcessingException e)
        {
            LOG.warn("OAuth2 error, problems with KeyStore " + e.getMessage());
            throw new InvalidRequestException("Server error. Can't generate id_token.", e);
        }
        catch(IOException e)
        {
            throw new SystemException("Failed to read security keystore.", e);
        }
        this.dummyEmail = Config.getParameter(getPropertyName("dummyemail", clientId));
    }


    private String getPropertyName(String key, String clientID)
    {
        if(StringUtils.isEmpty(clientID))
        {
            return "oauth2" + '.' + key;
        }
        return "oauth2" + '.' + clientID + '.' + key;
    }


    private String getPropertyValue(String key, String clientID, boolean mandatory)
    {
        String value = Config.getParameter(getPropertyName(key, clientID));
        if(StringUtils.isEmpty(value) && mandatory)
        {
            LOG.warn("OAuth2 error, missing config value " + getPropertyName(key, clientID));
            throw new InvalidRequestException("Server error. Can't generate id_token.");
        }
        if(StringUtils.isEmpty(value))
        {
            value = Config.getParameter(getPropertyName(key, null));
        }
        return value;
    }


    public void setExternalScopesStrategy(OpenIDExternalScopesStrategy externalScopesStrategy)
    {
        this.externalScopesStrategy = externalScopesStrategy;
    }


    @Required
    public void setKeyStoreHelper(KeyStoreHelper keyStoreHelper)
    {
        this.keyStoreHelper = keyStoreHelper;
    }


    @Required
    public void setClientDetailsDao(ClientDetailsDao clientDetailsDao)
    {
        this.clientDetailsDao = clientDetailsDao;
    }
}
