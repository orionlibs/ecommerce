package de.hybris.platform.ruleengineservices.configuration.impl;

import de.hybris.platform.ruleengineservices.configuration.Switch;
import de.hybris.platform.ruleengineservices.configuration.SwitchService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSwitchService implements SwitchService
{
    private Map<Switch, Boolean> statuses = new EnumMap<>(Switch.class);
    private ConfigurationService configurationService;


    public boolean isEnabled(Switch switchOption)
    {
        return ((Boolean)getStatuses().getOrDefault(switchOption, Boolean.FALSE)).booleanValue();
    }


    @PostConstruct
    protected void populateSwitchStatuses()
    {
        Stream.<Switch>of(Switch.values()).forEach(s -> getStatuses().put(s, getConfigurationService().getConfiguration().getBoolean(s.getValue(), Boolean.FALSE)));
    }


    protected Map<Switch, Boolean> getStatuses()
    {
        return this.statuses;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
