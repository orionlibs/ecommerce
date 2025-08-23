/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.common.configuration;

import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListView;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditorConfigurationUtil
{
    private static final Logger LOG = LoggerFactory.getLogger(EditorConfigurationUtil.class);


    private EditorConfigurationUtil()
    {
        throw new AssertionError("Using constructor of this class is prohibited.");
    }


    public static List<ListColumn> getColumns(final EditorContext context, final WidgetInstanceManager widgetInstanceManager,
                    final String typeCode)
    {
        final String configCtxKey = StringUtils.defaultIfBlank((String)context.getParameter("listConfigContext"), "listview");
        final ListView listConfiguration = loadConfiguration(typeCode, widgetInstanceManager, configCtxKey, ListView.class);
        Validate.notNull("Could not load " + ListView.class.getSimpleName() + " configuration for type " + typeCode,
                        listConfiguration);
        return listConfiguration != null ? listConfiguration.getColumn() : Collections.emptyList();
    }


    public static Base getBaseConfiguration(final WidgetInstanceManager widgetInstanceManager, final String typeCode)
    {
        return loadConfiguration(typeCode, widgetInstanceManager, "base", Base.class, false);
    }


    public static <K> K loadConfiguration(final String typeCode, final WidgetInstanceManager wim, final String contextParam,
                    final Class<K> clazz)
    {
        return loadConfiguration(typeCode, wim, contextParam, clazz, true);
    }


    public static <K> K loadConfiguration(final String typeCode, final WidgetInstanceManager wim, final String contextParam,
                    final Class<K> clazz, final boolean logErrorIfNotExist)
    {
        K config = null;
        final DefaultConfigContext configContext = new DefaultConfigContext(contextParam);
        configContext.setType(typeCode);
        try
        {
            config = wim.loadConfiguration(configContext, clazz);
            if(config == null)
            {
                LOG.warn("Loaded UI configuration is null. Ignoring.");
            }
        }
        catch(final CockpitConfigurationException cce)
        {
            if(logErrorIfNotExist)
            {
                LOG.error("Could not load cockpit config for the given context '" + configContext + "'.", cce);
            }
            else
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Could not load cockpit config for the given context '" + configContext + "'.", cce);
                }
            }
        }
        return config;
    }
}