/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.ResetConfigurationStrategy;
import com.hybris.cockpitng.core.util.CockpitProperties;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Strategy based on local.properties/cockpit.properties file
 */
public class DefaultResetConfigurationStrategy implements ResetConfigurationStrategy
{
    private static final String PROPERTY_RESET_TRIGGERS = "cockpitng.reset.triggers";
    private static final String PROPERTY_RESET_SCOPE_GENERAL = "cockpitng.reset.scope";
    private static final String PROPERTY_RESET_SCOPE_PATTERN = "cockpitng.reset.%s.scope";
    protected CockpitProperties cockpitProperties;


    public boolean isTriggerConfigured(final String trigger)
    {
        final String resetTriggers = cockpitProperties.getProperty(PROPERTY_RESET_TRIGGERS);
        return resetTriggers != null && Arrays.asList(resetTriggers.split(",")).contains(trigger);
    }


    public Set<String> getResetScopes(final String trigger)
    {
        final Set<String> scopes = new HashSet<>();
        String config = cockpitProperties.getProperty(PROPERTY_RESET_SCOPE_GENERAL);
        if(StringUtils.isNotEmpty(config))
        {
            scopes.addAll(Arrays.asList(config.split(",")));
        }
        config = cockpitProperties.getProperty(String.format(PROPERTY_RESET_SCOPE_PATTERN, trigger));
        if(StringUtils.isNotEmpty(config))
        {
            scopes.addAll(Arrays.asList(config.split(",")));
        }
        return scopes;
    }


    protected CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }
}
