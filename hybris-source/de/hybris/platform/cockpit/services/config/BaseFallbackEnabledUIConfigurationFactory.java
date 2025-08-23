package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;

public interface BaseFallbackEnabledUIConfigurationFactory<T extends UIComponentConfiguration> extends UIComponentConfigurationFactory
{
    T createDefault(ObjectTemplate paramObjectTemplate, BaseConfiguration paramBaseConfiguration);
}
