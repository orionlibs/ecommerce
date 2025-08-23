/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.impl;

import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.core.CockpitComponentInfo;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultCockpitConfigurationService;
import com.hybris.cockpitng.core.config.impl.jaxb.Config;
import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import com.hybris.cockpitng.core.impl.jaxb.Controller;
import com.hybris.cockpitng.core.impl.jaxb.Keywords;
import com.hybris.cockpitng.core.impl.jaxb.View;
import com.hybris.cockpitng.core.persistence.impl.XMLWidgetPersistenceService;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Responsible for creating WidgetDefinition instances based on provided cockpit component info.
 */
public class WidgetDefinitionFactory extends AbstractComponentDefinitionFactory
{
    private static final Logger LOG = LoggerFactory.getLogger(WidgetDefinitionFactory.class);
    private XMLWidgetPersistenceService xmlWidgetPersistenceService;
    private DefaultCockpitConfigurationService cockpitConfigurationService;


    @Override
    public WidgetDefinition createDefinition(final CockpitComponentInfo info)
    {
        Properties properties = info.getProperties();
        final String widgetPath = info.getRootPath();
        final com.hybris.cockpitng.core.impl.jaxb.WidgetDefinition xmlDef = getXMLDefinition(widgetPath, info,
                        com.hybris.cockpitng.core.impl.jaxb.WidgetDefinition.class);
        if(xmlDef != null)
        {
            properties = new Properties(properties);
            info.setProperties(properties);
            loadProperties(properties, xmlDef);
        }
        // ID
        final String id = properties.getProperty("widget-id");
        final WidgetDefinition def = new WidgetDefinition();
        def.setCode(id);
        // parent
        def.setParentCode(properties.getProperty(PROPERTY_WIDGET_PARENT));
        // controller
        String controllerProp = properties.getProperty("widget-controller");
        if(controllerProp != null && controllerProp.trim().endsWith(".groovy"))
        {
            controllerProp = info.getRootPath() + "/" + controllerProp.trim();
            if(controllerProp.charAt(0) == '/')
            {
                controllerProp = controllerProp.substring(1);
            }
        }
        def.setController(controllerProp);
        def.setControllerID(properties.getProperty("widget-controller-id"));
        // view URI
        final String path = StringUtils.appendIfMissing(widgetPath, PATH_SEPARATOR);
        String widgetViewProp = properties.getProperty("widget-view");
        if(widgetViewProp == null && id == null)
        {
            widgetViewProp = DefaultCockpitComponentDefinitionService.NO_VIEW_ZUL;
        }
        else if(widgetViewProp == null)
        {
            final String zulName = getWidgetSimpleID(id) + ".zul";
            if(!checkIfFileIsAbsent(path + zulName, info))
            {
                widgetViewProp = zulName;
            }
            else if(!checkIfFileIsAbsent(path + zulName.toLowerCase(), info))
            {
                widgetViewProp = zulName.toLowerCase();
            }
            else if(!checkIfFileIsAbsent(path + DefaultCockpitComponentDefinitionService.DEFAULT_VIEW_ZUL, info))
            {
                widgetViewProp = DefaultCockpitComponentDefinitionService.DEFAULT_VIEW_ZUL;
            }
            else
            {
                widgetViewProp = DefaultCockpitComponentDefinitionService.NO_VIEW_ZUL;
            }
        }
        if(DefaultCockpitComponentDefinitionService.NO_VIEW_ZUL.equals(widgetViewProp.trim()))
        {
            def.setViewURI(DefaultCockpitComponentDefinitionService.NO_VIEW_ZUL);
        }
        else if(StringUtils.isBlank(widgetPath))
        {
            LOG.warn("Widget root path undefined - unable to determine view url for {}", id);
            def.setViewURI(DefaultCockpitComponentDefinitionService.NO_VIEW_ZUL);
        }
        else
        {
            def.setViewURI(path + widgetViewProp);
        }
        // name, description, default title
        def.setName(properties.getProperty("widget-name"));
        def.setDescription(properties.getProperty("widget-description"));
        def.setDefaultTitle(properties.getProperty("widget-defaultTitle"));
        // category
        def.setCategoryTag(properties.getProperty("widget-category"));
        return def;
    }


    protected void loadProperties(final Properties properties, final com.hybris.cockpitng.core.impl.jaxb.WidgetDefinition xmlDef)
    {
        super.loadProperties(properties, xmlDef);
        // controller
        final Controller controller = xmlDef.getController();
        if(controller != null)
        {
            setProperty(properties, "widget-controller", controller.getClazz());
            setProperty(properties, "widget-controller-id", controller.getId());
        }
        // view URI
        final View view = xmlDef.getView();
        if(view != null)
        {
            setProperty(properties, "widget-view", view.getSrc());
        }
        // name, description, default title
        setProperty(properties, "widget-name", xmlDef.getName());
        setProperty(properties, "widget-description", xmlDef.getDescription());
        setProperty(properties, "widget-defaultTitle", xmlDef.getDefaultTitle());
        // category
        final Keywords keywords = xmlDef.getKeywords();
        if(keywords != null && CollectionUtils.isNotEmpty(keywords.getKeyword()))
        {
            setProperty(properties, "widget-category", keywords.getKeyword().iterator().next());
        }
    }


    @Override
    public void initialize(final CockpitComponentInfo info, final AbstractCockpitComponentDefinition definition)
    {
        if(definition instanceof WidgetDefinition)
        {
            loadComposedWidgetRoot((WidgetDefinition)definition, info.getRootPath(), info);
            loadDefaultWidgetConfig(info.getRootPath(), info);
        }
    }


    private void loadComposedWidgetRoot(final WidgetDefinition definition, final String path, final CockpitComponentInfo info)
    {
        String widgetsXml = path.trim();
        if(!widgetsXml.endsWith("/"))
        {
            widgetsXml += "/";
        }
        widgetsXml += "widgets.xml";
        if(widgetsXml.charAt(0) == '/')
        {
            widgetsXml = widgetsXml.substring(1);
        }
        final InputStream stream = getInfoResourceAsStream(info, widgetsXml);
        if(stream != null)
        {
            final Widget widget = this.xmlWidgetPersistenceService.loadWidgetTree(definition.getCode(), stream);
            if(widget != null)
            {
                setPartOfGroupRecursive(widget);
                definition.setComposedWidgetRoot(widget);
            }
        }
    }


    private void setPartOfGroupRecursive(final Widget widget)
    {
        widget.setPartOfGroup(true);
        final List<Widget> children = widget.getChildren();
        if(CollectionUtils.isNotEmpty(children))
        {
            for(final Widget child : children)
            {
                setPartOfGroupRecursive(child);
            }
        }
    }


    private void loadDefaultWidgetConfig(final String path, final CockpitComponentInfo info)
    {
        String configXml = path.trim();
        if(!configXml.endsWith("/"))
        {
            configXml += "/";
        }
        configXml += "cockpit-config.xml";
        if(configXml.charAt(0) == '/')
        {
            configXml = configXml.substring(1);
        }
        final InputStream stream = getInfoResourceAsStream(info, configXml);
        if(stream != null)
        {
            final Config rootConfig;
            try
            {
                rootConfig = this.cockpitConfigurationService.loadRootConfiguration(stream);
                if(rootConfig == null)
                {
                    return;
                }
                final Config mainRootConfig = this.cockpitConfigurationService.loadRootConfiguration();
                final boolean updated = updateMainConfig(mainRootConfig, rootConfig);
                if(updated)
                {
                    this.cockpitConfigurationService.storeRootConfig(mainRootConfig);
                }
            }
            catch(final CockpitConfigurationException e)
            {
                // ok
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Exception thrown: ", e);
                }
            }
        }
    }


    private boolean updateMainConfig(final Config mainRootConfig, final Config rootConfig)
    {
        boolean updated = false;
        for(final Context context : rootConfig.getContext())
        {
            updated |= updateMainConfig(mainRootConfig, context);
        }
        return updated;
    }


    private boolean updateMainConfig(final Config mainRootConfig, final Context context)
    {
        boolean result = false;
        final Object element = context.getAny();
        if(element != null)
        {
            final Map<String, String> ctx = this.cockpitConfigurationService.getContext(context);
            Context mainContext = null;
            final List<Context> mainContextList = findContext(mainRootConfig, ctx);
            if(mainContextList == null || mainContextList.isEmpty())
            {
                mainContext = new Context();
                mainContext.setMergeBy(context.getMergeBy());
                mainContext.setParent("auto".equals(context.getParent()) ? null : context.getParent());
                this.cockpitConfigurationService.setContext(mainContext, ctx);
                mainRootConfig.getContext().add(mainContext);
                result = true;
            }
            else
            {
                final Context lastOne = mainContextList.get(mainContextList.size() - 1);
                if(lastOne.getAny() == null)
                {
                    mainContext = lastOne;
                }
            }
            if(mainContext != null)
            {
                mainContext.setAny(element);
                result = true;
            }
        }
        for(final Context child : context.getContext())
        {
            result |= updateMainConfig(mainRootConfig, child);
        }
        return result;
    }


    private List<Context> findContext(final Config mainRootConfig, final Map<String, String> ctx)
    {
        final List<Context> mainContextList = this.cockpitConfigurationService.findContext(mainRootConfig, ctx, false, true);
        if(CollectionUtils.isNotEmpty(mainContextList))
        {
            final List<Context> result = new ArrayList<>(mainContextList);
            for(final Context context : mainContextList)
            {
                final Map<String, String> ctx2 = this.cockpitConfigurationService.getContext(context);
                if(!ctx.equals(ctx2))
                {
                    result.remove(context);
                }
            }
            return result;
        }
        return Collections.emptyList();
    }


    @Required
    public void setXmlWidgetPersistenceService(final XMLWidgetPersistenceService xmlWidgetPersistenceService)
    {
        this.xmlWidgetPersistenceService = xmlWidgetPersistenceService;
    }


    @Required
    public void setCockpitConfigurationService(final DefaultCockpitConfigurationService cockpitConfigurationService)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
    }
}
