package de.hybris.platform.apiregistrybackoffice.data;

import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel;
import java.io.Serializable;
import java.util.List;

public class ApiRegistryResetCredentialsForm implements Serializable
{
    private static final long serialVersionUID = 1L;
    private ExposedOAuthCredentialModel credential;
    private List<ExposedDestinationModel> impactedDestinations;
    private List<ExposedOAuthCredentialModel> impactedCredentials;
    private String clientId;
    private String clientSecret;
    private Integer gracePeriod;


    public void setCredential(ExposedOAuthCredentialModel credential)
    {
        this.credential = credential;
    }


    public ExposedOAuthCredentialModel getCredential()
    {
        return this.credential;
    }


    public void setImpactedDestinations(List<ExposedDestinationModel> impactedDestinations)
    {
        this.impactedDestinations = impactedDestinations;
    }


    public List<ExposedDestinationModel> getImpactedDestinations()
    {
        return this.impactedDestinations;
    }


    public void setImpactedCredentials(List<ExposedOAuthCredentialModel> impactedCredentials)
    {
        this.impactedCredentials = impactedCredentials;
    }


    public List<ExposedOAuthCredentialModel> getImpactedCredentials()
    {
        return this.impactedCredentials;
    }


    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }


    public String getClientId()
    {
        return this.clientId;
    }


    public void setClientSecret(String clientSecret)
    {
        this.clientSecret = clientSecret;
    }


    public String getClientSecret()
    {
        return this.clientSecret;
    }


    public void setGracePeriod(Integer gracePeriod)
    {
        this.gracePeriod = gracePeriod;
    }


    public Integer getGracePeriod()
    {
        return this.gracePeriod;
    }
}
