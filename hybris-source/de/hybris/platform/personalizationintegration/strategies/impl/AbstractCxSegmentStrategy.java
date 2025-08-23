package de.hybris.platform.personalizationintegration.strategies.impl;

import de.hybris.platform.personalizationservices.data.BaseSegmentData;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractCxSegmentStrategy
{
    protected ConfigurationService configurationService;


    protected void addProviderPrefixAndSeparatorToSegments(String providerId, List<? extends BaseSegmentData> segments)
    {
        String providerSegmentPrefixPropertyKey = String.format("personalizationintegration.provider.%s.prefix", new Object[] {providerId});
        String providerSegmentSeparatorPropertyKey = String.format("personalizationintegration.provider.%s.separator", new Object[] {providerId});
        String prefix = this.configurationService.getConfiguration().getString(providerSegmentPrefixPropertyKey);
        String separator = this.configurationService.getConfiguration().getString(providerSegmentSeparatorPropertyKey);
        if(StringUtils.isNotBlank(prefix))
        {
            segments.forEach(s -> s.setCode(prefix + prefix + (String)StringUtils.defaultIfEmpty(separator, " ")));
        }
    }


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
