package de.hybris.platform.webservicescommons.oauth2.client.impl;

import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import de.hybris.platform.webservicescommons.oauth2.client.ClientDetailsDao;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

public class DefaultClientDetailsService implements ClientDetailsService
{
    protected ClientDetailsDao dao;


    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException
    {
        try
        {
            return loadClient(clientId);
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


    private ClientDetails loadClient(String clientId)
    {
        OAuthClientDetailsModel model = this.dao.findClientById(clientId);
        if(model == null)
        {
            throw new NoSuchClientException("Unknown client " + clientId);
        }
        if(model.getDisabled() != null && model.getDisabled().booleanValue())
        {
            throw new ClientRegistrationException(String.format("Authentication for clientId:'%s' is disabled", new Object[] {clientId}));
        }
        return convertClient(model);
    }


    protected ClientDetails convertClient(OAuthClientDetailsModel model)
    {
        BaseClientDetails result = new BaseClientDetails();
        result.setAccessTokenValiditySeconds(model.getAccessTokenValiditySeconds());
        result.setAdditionalInformation(Collections.emptyMap());
        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>)emptyIfNull(model.getAuthorities()).stream().map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
        result.setAuthorities(authorities);
        result.setAuthorizedGrantTypes(emptyIfNull(model.getAuthorizedGrantTypes()));
        result.setAutoApproveScopes(emptyIfNull(model.getAutoApprove()));
        result.setClientId(model.getClientId());
        result.setClientSecret(model.getClientSecret());
        result.setRefreshTokenValiditySeconds(model.getRefreshTokenValiditySeconds());
        result.setRegisteredRedirectUri(emptyIfNull(model.getRegisteredRedirectUri()));
        result.setResourceIds(emptyIfNull(model.getResourceIds()));
        result.setScope(emptyIfNull(model.getScope()));
        return (ClientDetails)result;
    }


    private <T> Set<T> emptyIfNull(Set<T> collection)
    {
        if(collection == null)
        {
            return Collections.emptySet();
        }
        return collection;
    }


    public void setClientDetailsDao(ClientDetailsDao dao)
    {
        this.dao = dao;
    }
}
