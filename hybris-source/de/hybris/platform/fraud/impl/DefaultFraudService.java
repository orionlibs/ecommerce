package de.hybris.platform.fraud.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.fraud.FraudService;
import de.hybris.platform.fraud.FraudServiceProvider;
import java.util.Collection;
import java.util.Collections;

public class DefaultFraudService implements FraudService
{
    private Collection<FraudServiceProvider> providers;


    public Collection<FraudServiceProvider> getProviders()
    {
        return (this.providers == null) ? Collections.<FraudServiceProvider>emptyList() : this.providers;
    }


    public void setProviders(Collection<FraudServiceProvider> providers)
    {
        this.providers = providers;
    }


    public FraudServiceProvider getProvider(String name)
    {
        for(FraudServiceProvider p : getProviders())
        {
            if(name.equalsIgnoreCase(p.getProviderName()))
            {
                return p;
            }
        }
        throw new IllegalArgumentException("got no configured provider " + name + " within " + getProviders());
    }


    public FraudServiceResponse recognizeActivitySymptoms(String providerName, UserModel user)
    {
        return getProvider(providerName).recognizeUserActivitySymptoms(user);
    }


    public FraudServiceResponse recognizeOrderSymptoms(String providerName, AbstractOrderModel order)
    {
        return getProvider(providerName).recognizeOrderFraudSymptoms(order);
    }
}
