package de.hybris.platform.fraud.impl;

import de.hybris.platform.fraud.FraudServiceProvider;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractFraudServiceProvider implements FraudServiceProvider
{
    private String providerName;


    @Required
    public void setProviderName(String providerName)
    {
        this.providerName = providerName;
    }


    public String getProviderName()
    {
        return this.providerName;
    }
}
