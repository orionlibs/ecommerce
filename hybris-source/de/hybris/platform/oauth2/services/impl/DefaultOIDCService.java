package de.hybris.platform.oauth2.services.impl;

import de.hybris.platform.oauth2.jwt.exceptions.KeyStoreProcessingException;
import de.hybris.platform.oauth2.jwt.util.KeyStoreHelper;
import de.hybris.platform.oauth2.services.HybrisOpenIDTokenServices;
import de.hybris.platform.oauth2.services.OIDCService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import de.hybris.platform.webservicescommons.model.OpenIDClientDetailsModel;
import de.hybris.platform.webservicescommons.oauth2.client.ClientDetailsDao;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOIDCService implements OIDCService
{
    private static final Logger LOG = Logger.getLogger(OIDCService.class);
    private HybrisOpenIDTokenServices openidTokenServices;
    private ConfigurationService configurationService;
    private KeyStoreHelper keyStoreHelper;
    private ClientDetailsDao clientDetailsDao;


    public Map<String, Object> getConfiguration(String clientId, HttpServletRequest request)
    {
        Map<String, Object> config = new LinkedHashMap<>();
        String defaultHostNameAndPort = request.getScheme() + "://" + request.getScheme() + ":" + request.getServerName();
        config.put("issuer", defaultHostNameAndPort + "/authorizationserver");
        config.put("authorization_endpoint", defaultHostNameAndPort + "/authorizationserver/oauth/authorize");
        config.put("token_endpoint", defaultHostNameAndPort + "/authorizationserver/oauth/token");
        config.put("jwks_uri", defaultHostNameAndPort + "/authorizationserver/.well-known/jwks.json");
        config.put("token_endpoint_auth_methods_supported", getHybrisOpenIDTokenServices().getTokenEndpointAuthMethods());
        config.put("subject_types_supported", getHybrisOpenIDTokenServices().getSubjectTypes());
        if(clientId != null && !clientId.isEmpty())
        {
            OAuthClientDetailsModel clientById = this.clientDetailsDao.findClientById(clientId);
            if(clientById == null)
            {
                return Collections.emptyMap();
            }
            String str1 = getPropertyValue("algorithm", clientId, true);
            if(str1 == null || str1.isEmpty())
            {
                str1 = "RS256";
            }
            String clientHost = getPropertyValue("public.address", clientId, true);
            if(clientHost != null && !clientHost.isEmpty())
            {
                config.replace("issuer", clientHost + "/authorizationserver");
                config.replace("authorization_endpoint", clientHost + "/authorizationserver/oauth/authorize");
                config.replace("token_endpoint", clientHost + "/authorizationserver/oauth/token");
                config.replace("jwks_uri", clientHost + "/authorizationserver/.well-known/jwks.json");
            }
            config.put("response_types_supported",
                            getPropertyValue("responseTypes", clientId, true).split(","));
            config.put("scopes_supported", clientById.getScope());
            config.put("id_token_signing_alg_values_supported", Arrays.asList(new String[] {str1}));
            return config;
        }
        String OID_TOKEN_SIGNING = "";
        if(clientId != null)
        {
            OID_TOKEN_SIGNING = getPropertyValue("algorithm", clientId, true);
        }
        if(OID_TOKEN_SIGNING == null || OID_TOKEN_SIGNING.isEmpty())
        {
            OID_TOKEN_SIGNING = "RS256";
        }
        config.put("response_types_supported", getHybrisOpenIDTokenServices().getResponseTypes());
        config.put("scopes_supported", getHybrisOpenIDTokenServices().getSupportedScopes());
        config.put("id_token_signing_alg_values_supported", Arrays.asList(new String[] {OID_TOKEN_SIGNING}));
        return config;
    }


    public Map<String, List<Map<String, String>>> getJWKS(String clientId, HttpServletRequest request)
    {
        List<Map<String, String>> jwksList = new ArrayList<>();
        Map<String, List<Map<String, String>>> jwksRoot = new HashMap<>();
        jwksRoot.put("keys", jwksList);
        if(clientId != null && !clientId.isEmpty())
        {
            OAuthClientDetailsModel clientById = this.clientDetailsDao.findClientById(clientId);
            if(clientById == null)
            {
                return jwksRoot;
            }
            Map<String, String> jwks = getJWK((OpenIDClientDetailsModel)clientById);
            if(jwks != null)
            {
                jwksList.add(jwks);
            }
            return jwksRoot;
        }
        for(OAuthClientDetailsModel clientDetail : getHybrisOpenIDTokenServices().getAllOpenIDClientDetails())
        {
            if(clientDetail instanceof OpenIDClientDetailsModel)
            {
                Map<String, String> jwks = getJWK((OpenIDClientDetailsModel)clientDetail);
                if(jwks != null)
                {
                    jwksList.add(jwks);
                }
            }
        }
        return jwksRoot;
    }


    protected Map<String, String> getJWK(OpenIDClientDetailsModel clientDetails)
    {
        RSAPublicKey publicKey;
        String clientId = clientDetails.getClientId();
        String kid = getPropertyValue("kid", clientId, true);
        if(StringUtils.isEmpty(kid))
        {
            return null;
        }
        String algorithm = getPropertyValue("algorithm", clientId, false);
        if(StringUtils.isEmpty(algorithm))
        {
            algorithm = "RS256";
        }
        String securityKeystoreLocation = getPropertyValue("keystore.location", clientId, true);
        String securityKeystorePassword = getPropertyValue("keystore.password", clientId, true);
        try
        {
            InputStream resourceAsStream = HybrisOpenIDTokenServices.class.getResourceAsStream(securityKeystoreLocation);
            try
            {
                KeyStore keystore = getKeyStoreHelper().getKeyStore("JKS", resourceAsStream, securityKeystorePassword);
                publicKey = (RSAPublicKey)getKeyStoreHelper().getPublicKey(keystore, kid);
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
            LOG.warn("OAuth2 error, problems with KeyStore ", (Throwable)e);
            return null;
        }
        catch(IOException e)
        {
            LOG.warn("OAuth2 error, Failed to read security keystore.", e);
            return null;
        }
        Map<String, String> jwks = new LinkedHashMap<>();
        jwks.put("kty", "RSA");
        jwks.put("use", "sig");
        jwks.put("kid", kid);
        jwks.put("alg", algorithm);
        jwks.put("n", this.keyStoreHelper.getModulusAsBase64String(publicKey.getModulus().toByteArray()));
        jwks.put("e", this.keyStoreHelper
                        .getPublicExponentAsBase64String(publicKey.getPublicExponent().toByteArray()));
        return jwks;
    }


    protected static String getPropertyName(String key, String clientID)
    {
        if(StringUtils.isEmpty(clientID))
        {
            return "oauth2" + '.' + key;
        }
        return "oauth2" + '.' + clientID + '.' + key;
    }


    protected String getPropertyValue(String key, String clientID, boolean mandatory)
    {
        String value = getConfigurationService().getConfiguration().getString(getPropertyName(key, clientID));
        if(StringUtils.isEmpty(value) && mandatory)
        {
            LOG.warn("OAuth2 error, missing config value " + getPropertyName(key, clientID));
        }
        else if(StringUtils.isEmpty(value))
        {
            value = getConfigurationService().getConfiguration().getString(getPropertyName(key, null));
        }
        return value;
    }


    protected HybrisOpenIDTokenServices getHybrisOpenIDTokenServices()
    {
        return this.openidTokenServices;
    }


    @Required
    public void setHybrisOpenIDTokenServices(HybrisOpenIDTokenServices openidTokenServices)
    {
        this.openidTokenServices = openidTokenServices;
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


    protected KeyStoreHelper getKeyStoreHelper()
    {
        return this.keyStoreHelper;
    }


    @Required
    public void setKeyStoreHelper(KeyStoreHelper keyStoreHelper)
    {
        this.keyStoreHelper = keyStoreHelper;
    }


    @Resource
    public void setClientDetailsDao(ClientDetailsDao clientDetailsDao)
    {
        this.clientDetailsDao = clientDetailsDao;
    }
}
