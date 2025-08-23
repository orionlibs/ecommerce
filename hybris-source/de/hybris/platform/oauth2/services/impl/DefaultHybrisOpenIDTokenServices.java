package de.hybris.platform.oauth2.services.impl;

import de.hybris.ouath2.openidconnect.dto.IDTokenParameterData;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.oauth2.jwt.exceptions.JwtException;
import de.hybris.platform.oauth2.jwt.exceptions.KeyStoreProcessingException;
import de.hybris.platform.oauth2.jwt.util.IdTokenHelper;
import de.hybris.platform.oauth2.jwt.util.KeyStoreHelper;
import de.hybris.platform.oauth2.services.HybrisOpenIDTokenServices;
import de.hybris.platform.oauth2.util.OIDCUtils;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import de.hybris.platform.webservicescommons.model.OpenIDClientDetailsModel;
import de.hybris.platform.webservicescommons.oauth2.client.ClientDetailsDao;
import de.hybris.platform.webservicescommons.oauth2.client.impl.DefaultClientDetailsDao;
import de.hybris.platform.webservicescommons.oauth2.scope.OpenIDExternalScopesStrategy;
import de.hybris.platform.webservicescommons.oauth2.token.provider.HybrisOAuthTokenServices;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class DefaultHybrisOpenIDTokenServices extends HybrisOAuthTokenServices implements HybrisOpenIDTokenServices
{
    protected static final String AUD_CLAIM = "aud";
    protected static final String NAME_CLAIM = "name";
    protected static final String EMAIL_CLAIM = "email";
    protected static final String GROUPS_CLAIM = "groups";
    protected static final String ID_TOKEN = "id_token";
    protected static final String OPENID_SCOPE_NAME = "openid";
    protected static final String ID_TOKEN_VALIDITY_SECONDS = "idTokenValiditySeconds";
    protected static final String KID = "kid";
    protected static final String KEYSTORE_LOCATION_KEY = "keystore.location";
    protected static final String KEYSTORE_PASS_KEY = "keystore.password";
    protected static final String OAUTH2_ALGORITHM = "algorithm";
    private static final Logger LOG = Logger.getLogger(DefaultHybrisOpenIDTokenServices.class);
    private static final int DEFAULT_TOKEN_VALIDITY_TIME = 3600;
    private static final String ERROR_MESSAGE = "Server error. Can't generate id_token.";
    private static final String PARAM_STATE = "state";
    private static final String PARAM_NONCE = "nonce";
    private ClientDetailsDao clientDetailsDao;
    private KeyStoreHelper keyStoreHelper;
    private ConfigurationService configurationService;
    private UserService userService;
    private OpenIDExternalScopesStrategy externalScopesStrategy;


    public List<String> getTokenEndpointAuthMethods()
    {
        return Arrays.asList(new String[] {"client_secret_post", "client_secret_basic"});
    }


    public List<String> getSubjectTypes()
    {
        return Collections.singletonList("public");
    }


    public List<String> getResponseTypes()
    {
        return Arrays.asList(new String[] {"code", "code id_token", "id_token", "token id_token"});
    }


    public List<String> getSupportedScopes()
    {
        return Arrays.asList(new String[] {"openid", "email", "groups"});
    }


    public List<OAuthClientDetailsModel> getAllOpenIDClientDetails()
    {
        List<OAuthClientDetailsModel> clientDetails = new ArrayList<>();
        for(OAuthClientDetailsModel clientDetail : ((DefaultClientDetailsDao)getClientDetailsDao()).find())
        {
            if(clientDetail instanceof OpenIDClientDetailsModel)
            {
                clientDetails.add(clientDetail);
            }
        }
        return clientDetails;
    }


    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication)
    {
        try
        {
            OAuth2AccessToken accessToken = super.createAccessToken(authentication);
            Set<String> scopes = OAuth2Utils.parseParameterList((String)authentication.getOAuth2Request().getRequestParameters().get("scope"));
            if(scopes.contains("openid"))
            {
                OAuthClientDetailsModel clientDetailsModel = getClientDetailsDao().findClientById(authentication.getOAuth2Request().getClientId());
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
                                    .getUserAuthentication()
                                    .getPrincipal());
                    LOG.debug("externalScopes: " + externalScopes);
                }
                IDTokenParameterData idtokenparam = initializeIdTokenParameters(openIDClientDetailsModel.getClientId());
                DefaultOAuth2AccessToken accessTokenIdToken = new DefaultOAuth2AccessToken(accessToken);
                String requestedScopes = (String)authentication.getOAuth2Request().getRequestParameters().get("scope");
                if(StringUtils.isEmpty(requestedScopes) || !requestedScopes.contains("openid"))
                {
                    LOG.warn("Missing openid scope");
                    throw new InvalidRequestException("Missing openid scope");
                }
                IdTokenHelper idTokenHelper = createIdTokenHelper(authentication, openIDClientDetailsModel, externalScopes, idtokenparam);
                Jwt jwt = idTokenHelper.encodeAndSign(getSigner(idtokenparam));
                Map<String, Object> map = new HashMap<>();
                map.put("id_token", jwt.getEncoded());
                accessTokenIdToken.setAdditionalInformation(map);
                return (OAuth2AccessToken)accessTokenIdToken;
            }
            return accessToken;
        }
        catch(ModelSavingException e)
        {
            LOG.debug("HybrisOAuthTokenServices->createAccessToken : ModelSavingException", (Throwable)e);
            return super.createAccessToken(authentication);
        }
        catch(ModelRemovalException e)
        {
            LOG.debug("HybrisOAuthTokenServices->createAccessToken : ModelRemovalException", (Throwable)e);
            return super.createAccessToken(authentication);
        }
    }


    protected IdTokenHelper createIdTokenHelper(OAuth2Authentication authentication, OpenIDClientDetailsModel openIDClientDetailsModel, List<String> externalScopes, IDTokenParameterData idtokenparam)
    {
        IdTokenHelper idTokenHelper;
        UserModel user = getUserService().getUserForUID((String)authentication.getPrincipal());
        user.getAllGroups();
        String nonce = (String)authentication.getOAuth2Request().getRequestParameters().get("nonce");
        String state = (String)authentication.getOAuth2Request().getRequestParameters().get("state");
        try
        {
            idTokenHelper = (new IdTokenHelper.IdTokenBuilder((new IdTokenHelper.HeaderBuilder()).alg(idtokenparam.getAlgorithm()).kid(idtokenparam.getKid()).getHeaders(),
                            (new IdTokenHelper.ClaimsBuilder()).iss(openIDClientDetailsModel.getIssuer() + "/authorizationserver").nonce(nonce).state(state).sub(authentication.getPrincipal().toString()).iat().exp(idtokenparam.getIdTokenValiditySeconds()).addClaim("aud", idtokenparam.getKid())
                                            .addClaim("name", user.getName()).addClaim("email", user.getUid()).addClaim("groups", this.userService.getAllUserGroupsForUser(user).stream().map(PrincipalModel::getUid).collect(Collectors.toList()))
                                            .setScopes("scope", (List)Stream.<String>of("openid").collect(Collectors.toList())).addScopes(openIDClientDetailsModel.getExternalScopeClaimName(), externalScopes).getClaims())).build();
        }
        catch(JwtException e)
        {
            throw new InvalidRequestException("Server error. Can't generate id_token.", e);
        }
        return idTokenHelper;
    }


    protected IDTokenParameterData initializeIdTokenParameters(String clientId)
    {
        IDTokenParameterData data = new IDTokenParameterData();
        data.setKid(OIDCUtils.getPropertyValue("kid", clientId, true));
        data.setSecurityKeystoreLocation(OIDCUtils.getPropertyValue("keystore.location", clientId, true));
        data.setSecurityKeystorePassword(OIDCUtils.getPropertyValue("keystore.password", clientId, true));
        if(NumberUtils.isDigits(
                        getConfigurationService().getConfiguration().getString("oauth2.idTokenValiditySeconds")))
        {
            data.setIdTokenValiditySeconds(Integer.parseInt(
                            getConfigurationService().getConfiguration().getString("oauth2.idTokenValiditySeconds")));
        }
        else
        {
            data.setIdTokenValiditySeconds(3600);
        }
        if(!StringUtils.isEmpty(getConfigurationService().getConfiguration().getString("oauth2.algorithm")))
        {
            data.setAlgorithm(getConfigurationService().getConfiguration().getString("oauth2.algorithm"));
        }
        else
        {
            data.setAlgorithm("RS256");
        }
        return data;
    }


    protected Signer getSigner(IDTokenParameterData data)
    {
        Signer signer;
        try
        {
            InputStream resourceAsStream = HybrisOpenIDTokenServices.class.getResourceAsStream(data
                            .getSecurityKeystoreLocation());
            try
            {
                signer = getKeyStoreHelper().getSigner("JKS", resourceAsStream, data.getKid(), data
                                .getSecurityKeystorePassword());
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
        return signer;
    }


    public void setExternalScopesStrategy(OpenIDExternalScopesStrategy externalScopesStrategy)
    {
        this.externalScopesStrategy = externalScopesStrategy;
    }


    protected KeyStoreHelper getKeyStoreHelper()
    {
        return this.keyStoreHelper;
    }


    @Required
    public void setKeyStoreHelper(KeyStoreHelper keyStoreHelper)
    {
        this.keyStoreHelper = keyStoreHelper;
    }


    protected ClientDetailsDao getClientDetailsDao()
    {
        return this.clientDetailsDao;
    }


    @Required
    public void setClientDetailsDao(ClientDetailsDao clientDetailsDao)
    {
        this.clientDetailsDao = clientDetailsDao;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
