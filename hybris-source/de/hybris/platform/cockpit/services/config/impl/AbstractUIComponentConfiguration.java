package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.services.config.ContextAwareUIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfigurationContext;

public class AbstractUIComponentConfiguration implements ContextAwareUIComponentConfiguration
{
    private UIComponentConfigurationContext context;


    public UIComponentConfigurationContext getContext()
    {
        return this.context;
    }


    public void setContext(UIComponentConfigurationContext context)
    {
        this.context = context;
    }
}
