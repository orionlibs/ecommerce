/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules.config.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hybris.cockpitng.core.config.CockpitConfigurationContextStrategy;
import com.hybris.cockpitng.modules.CockpitModuleConnector;
import com.hybris.cockpitng.modules.server.ws.jaxb.CockpitModuleInfo;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * Backoffice module context strategy. Provides backoffice module names of dependent backoffice modules as parent
 * contexts.
 */
public class CockpitModuleConfigurationContextStrategy implements CockpitConfigurationContextStrategy
{
    private CockpitModuleConnector cockpitModuleConnector;
    private List<String> initialModules = Lists.newArrayList();


    @Override
    public boolean isResettable()
    {
        return true;
    }


    @Override
    public List<String> getParentContexts(final String context)
    {
        if(StringUtils.isBlank(context))
        {
            return Collections.<String>emptyList();
        }
        final List<String> allModules = getModuleNames();
        if("_root_".equals(context))
        {
            if(allModules.isEmpty())
            {
                // fallback to no-module config
                return Collections.singletonList("");
            }
            else
            {
                return Collections.singletonList(allModules.get(allModules.size() - 1));
            }
        }
        final int index = allModules.indexOf(context);
        if(index < 1)
        {
            // fallback to no-module config
            return Collections.singletonList("");
        }
        else
        {
            return Collections.singletonList(allModules.get(index - 1));
        }
    }


    protected List<String> getModuleNames()
    {
        if(this.cockpitModuleConnector == null)
        {
            return Collections.<String>emptyList();
        }
        final List<String> allModules = this.cockpitModuleConnector.getCockpitModuleUrls();
        final Set<String> names = Sets.newLinkedHashSet(getInitialModules());
        for(final String moduleUrl : allModules)
        {
            final CockpitModuleInfo info = this.cockpitModuleConnector.getModuleInfo(moduleUrl);
            if(info != null)
            {
                names.add(info.getId());
            }
        }
        return Lists.newArrayList(names);
    }


    @Override
    public boolean valueMatches(final String contextValue, final String value)
    {
        return StringUtils.equals(contextValue, value);
    }


    public void setCockpitModuleConnector(final CockpitModuleConnector cockpitModuleConnector)
    {
        this.cockpitModuleConnector = cockpitModuleConnector;
    }


    public List<String> getInitialModules()
    {
        return initialModules;
    }


    public void setInitialModules(final List<String> initialModules)
    {
        this.initialModules = initialModules;
    }
}
