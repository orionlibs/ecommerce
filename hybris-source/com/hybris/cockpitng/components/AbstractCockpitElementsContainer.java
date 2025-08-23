/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components;

import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;

/**
 *
 */
public abstract class AbstractCockpitElementsContainer extends Div
{
    private static final long serialVersionUID = 7221576379633730482L;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCockpitElementsContainer.class);
    private transient CockpitConfigurationService configurationService;
    private transient WidgetInstanceManager widgetInstanceManager;
    private String config;
    private String group;
    private String prefix;
    private String renderer;
    private boolean initialized;


    public AbstractCockpitElementsContainer()
    {
        AbstractCockpitElementsContainer.this.addEventListener(Events.ON_CREATE, event -> initialize());
    }


    public void reload()
    {
        setInitialized(false);
        this.initialize();
    }


    protected void reloadIfNecessary()
    {
        if(isInitialized())
        {
            reload();
        }
    }


    public final void initialize()
    {
        if(!isInitialized())
        {
            setInitialized(true);
            doInitialize();
        }
    }


    public abstract void doInitialize();


    public abstract Object createDefaultRenderer();


    protected <CONFIG> CONFIG getConfiguration(final String configString, final Class<CONFIG> configurationType)
                    throws CockpitConfigurationException
    {
        if(StringUtils.isBlank(configString))
        {
            return null;
        }
        final ConfigContext context = parseConfigContext(configString);
        final WidgetInstanceManager wim = getWidgetInstanceManager();
        if(wim != null)
        {
            return wim.loadConfiguration(context, configurationType);
        }
        else
        {
            return getCockpitConfigurationService().loadConfiguration(context, configurationType);
        }
    }


    private static ConfigContext parseConfigContext(final String configString)
    {
        if(StringUtils.isBlank(configString))
        {
            return new DefaultConfigContext();
        }
        else if(configString.contains("="))
        {
            final DefaultConfigContext context = new DefaultConfigContext();
            for(final String entry : configString.split(","))
            {
                if(!StringUtils.contains(entry, "="))
                {
                    continue;
                }
                final int delimPos = entry.indexOf('=');
                final String key = entry.substring(0, delimPos);
                final String value = entry.substring(delimPos + 1);
                if(!StringUtils.isBlank(key) && !StringUtils.isBlank(value))
                {
                    context.addAttribute(key.trim(), value.trim());
                }
            }
            return context;
        }
        else
        {
            return new DefaultConfigContext(configString);
        }
    }


    protected String getQualifier(final String property)
    {
        if(StringUtils.isBlank(getPrefix()))
        {
            return property;
        }
        else
        {
            final String prefixString = (getPrefix().endsWith(".") ? getPrefix() : getPrefix() + ".");
            String result = prefixString;
            if(!StringUtils.isBlank(property))
            {
                result += property;
            }
            if(result.endsWith("."))
            {
                result = result.substring(0, result.length() - 1);
            }
            return result;
        }
    }


    protected Object createRenderer()
    {
        if(!StringUtils.isBlank(getRenderer()))
        {
            ClassLoader classLoader = null;
            final WidgetInstanceManager wim = getWidgetInstanceManager();
            if(wim != null)
            {
                final WidgetDefinition def = wim.getWidgetslot()
                                .getWidgetDefinition(wim.getWidgetslot().getWidgetInstance().getWidget());
                if(def != null)
                {
                    classLoader = wim.getWidgetslot().getWidgetDefinitionService().getClassLoader(def);
                }
            }
            try
            {
                Object foundRenderer = SpringUtil.getBean(getRenderer());
                if(foundRenderer == null)
                {
                    final Class<?> clazz = classLoader == null ? Class.forName(getRenderer())
                                    : Class.forName(getRenderer(), true, classLoader);
                    foundRenderer = clazz.newInstance();
                }
                return foundRenderer;
            }
            catch(final ClassNotFoundException | IllegalAccessException | InstantiationException e)
            {
                LOG.error("unable to instantiate renderer for class '" + getRenderer() + "'", e);
            }
        }
        return createDefaultRenderer();
    }


    public String getConfig()
    {
        return config;
    }


    public void setConfig(final String config)
    {
        if(!Objects.equals(this.config, config))
        {
            this.config = config;
            reloadIfNecessary();
        }
    }


    public String getGroup()
    {
        return group;
    }


    public void setGroup(final String group)
    {
        if(!Objects.equals(this.group, group))
        {
            this.group = group;
            reloadIfNecessary();
        }
    }


    public String getPrefix()
    {
        return prefix;
    }


    public void setPrefix(final String prefix)
    {
        if(!Objects.equals(this.prefix, prefix))
        {
            this.prefix = prefix;
            reloadIfNecessary();
        }
    }


    public String getRenderer()
    {
        return renderer;
    }


    public void setRenderer(final String renderer)
    {
        if(!Objects.equals(this.renderer, renderer))
        {
            this.renderer = renderer;
            reloadIfNecessary();
        }
    }


    protected CockpitConfigurationService getCockpitConfigurationService()
    {
        if(this.configurationService == null)
        {
            this.configurationService = (CockpitConfigurationService)SpringUtil.getBean("cockpitConfigurationService");
        }
        return this.configurationService;
    }


    public WidgetInstanceManager getWidgetInstanceManager()
    {
        return widgetInstanceManager;
    }


    public void setWidgetInstanceManager(final WidgetInstanceManager widgetInstanceManager)
    {
        this.widgetInstanceManager = widgetInstanceManager;
    }


    protected boolean isInitialized()
    {
        return initialized;
    }


    protected void setInitialized(final boolean initialized)
    {
        this.initialized = initialized;
    }
}
