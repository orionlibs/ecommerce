package de.hybris.platform.webservicescommons.oauth2.client.impl;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

public class OpenIDClientDetailsService extends DefaultClientDetailsService
{
    protected static final String OPENID_SCOPE_NAME = "openid";


    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException
    {
        try
        {
            ClientDetails clientDetails = super.loadClientByClientId(clientId);
            if(clientDetails instanceof BaseClientDetails)
            {
                BaseClientDetails baseClientDetails = (BaseClientDetails)clientDetails;
                baseClientDetails.getScope().add("openid");
                return (ClientDetails)baseClientDetails;
            }
            throw new ClientRegistrationException("loadClientByClientId failed, expected instance of BaseClientDetails got " + clientDetails);
        }
        catch(ClientRegistrationException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new ClientRegistrationException("loadClientByClientId failed", e);
        }
    }
}
