package de.hybris.platform.hac.custom;

import java.util.Collection;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomTabProvidersRegistry
{
    @Autowired
    private Map<String, CustomTabProvider> providers;


    public Collection<CustomTabProvider> getProviders()
    {
        return this.providers.values();
    }
}
