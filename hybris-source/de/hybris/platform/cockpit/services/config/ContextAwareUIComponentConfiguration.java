package de.hybris.platform.cockpit.services.config;

public interface ContextAwareUIComponentConfiguration extends UIComponentConfiguration
{
    UIComponentConfigurationContext getContext();


    void setContext(UIComponentConfigurationContext paramUIComponentConfigurationContext);
}
