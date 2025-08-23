/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules.persistence.impl;

import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.persistence.impl.XMLWidgetPersistenceService;
import com.hybris.cockpitng.core.persistence.impl.jaxb.Import;
import com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection;
import com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnectionRemove;
import com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetExtension;
import com.hybris.cockpitng.core.persistence.impl.jaxb.Widgets;
import com.hybris.cockpitng.core.spring.CockpitApplicationContext;
import com.hybris.cockpitng.core.util.ClassLoaderUtils;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.impl.WidgetTreeUtils;
import com.hybris.cockpitng.modules.CockpitModuleConnector;
import com.hybris.cockpitng.modules.persistence.WidgetConnectionsRemover;
import com.hybris.cockpitng.modules.server.ws.jaxb.CockpitModuleInfo;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Extends {@link XMLWidgetPersistenceService} by the feature of adding additional widget.xml snippets to the widget
 * tree.
 */
public class XmlModuleAwarePersistenceService extends XMLWidgetPersistenceService implements ApplicationContextAware
{
    public static final String COCKPITNG_WIDGETS_STORING_ENABLED = "cockpitng.widgets.storingEnabled";
    private static final Logger LOG = LoggerFactory.getLogger(XmlModuleAwarePersistenceService.class);
    private CockpitApplicationContext applicationContext;
    private CockpitModuleConnector cockpitModuleConnector;
    private CockpitProperties cockpitProperties;
    private WidgetConnectionsRemover widgetConnectionsRemover;
    private Boolean storingEnabled;
    private boolean storingEnabledSpring = true;


    @Override
    protected void storeWidgets(final Widgets widgets, final OutputStream outputStream)
    {
        if(isStoringEnabled())
        {
            super.storeWidgets(widgets, outputStream);
        }
    }


    @Override
    public void storeWidgetTree(final Widget widget)
    {
        if(isStoringEnabled())
        {
            if(isLocalWidgetsFileExisting())
            {
                super.storeWidgetTree(widget);
            }
            else
            {
                final Widget rootWidget = WidgetTreeUtils.getRootWidget(widget);
                super.storeWidgetTree(rootWidget);
            }
        }
    }


    @Override
    public void deleteWidgetTree(final Widget node)
    {
        if(isStoringEnabled())
        {
            super.deleteWidgetTree(node);
        }
    }


    @Override
    public void deleteWidgetTree(final Widget node, final File file) throws FileNotFoundException
    {
        if(isStoringEnabled())
        {
            super.deleteWidgetTree(node, file);
        }
    }


    @Override
    protected InputStream getWidgetsFileAsStream() throws FileNotFoundException
    {
        if(isStoringEnabled() && isLocalWidgetsFileExisting())
        {
            return super.getWidgetsFileAsStream();
        }
        else
        {
            return ClassLoaderUtils.getCurrentClassLoader(this.getClass()).getResourceAsStream(getDefaultWidgetConfig());
        }
    }


    protected boolean isLocalWidgetsFileExisting()
    {
        final String fileName = getDefaultWidgetConfig() == null ? "widgets.xml" : getDefaultWidgetConfig();
        final File file = new File(getWidgetLibUtils().getRootDir(), fileName);
        return file.exists();
    }


    @Override
    protected Widgets loadWidgets(final InputStream inputStream)
    {
        final Widgets baseWidgets = super.loadWidgets(inputStream);
        if(isStoringEnabled() && isLocalWidgetsFileExisting())
        {
            return baseWidgets;
        }
        final List<String> modules = cockpitModuleConnector.getCockpitModuleUrls();
        for(final String moduleUrl : modules)
        {
            loadWidgetsForModule(baseWidgets, moduleUrl);
        }
        return baseWidgets;
    }


    protected void loadWidgetsForModule(final Widgets baseWidgets, final String moduleUrl)
    {
        final String widgetTreeContent = cockpitModuleConnector.getWidgetTreeContent(moduleUrl);
        if(widgetTreeContent != null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Processing widget extensions from module '{}'...", moduleUrl);
            }
            final InputStream resourceAsStream = new ByteArrayInputStream(widgetTreeContent.getBytes(Charset.defaultCharset()));
            final Widgets additionalWidgets = super.loadWidgets(resourceAsStream);
            if(additionalWidgets != null)
            {
                setContextId(additionalWidgets, moduleUrl);
                addAdditionalWidgets(baseWidgets, additionalWidgets);
            }
        }
    }


    protected void addAdditionalWidgets(final Widgets baseWidgets, final Widgets additionalWidgets)
    {
        if(additionalWidgets != null)
        {
            final List<WidgetExtension> widgetExtensions = additionalWidgets.getWidgetExtension();
            final List<Import> widgetImports = additionalWidgets.getImports();
            final List<WidgetConnection> widgetConnections = baseWidgets.getWidgetConnection();
            final List<WidgetConnectionRemove> widgetConnectionRemove = baseWidgets.getWidgetConnectionRemove();
            baseWidgets.getWidgetExtension().addAll(widgetExtensions);
            baseWidgets.getImports().addAll(widgetImports);
            widgetConnections.addAll(additionalWidgets.getWidgetConnection());
            widgetConnectionRemove.addAll(additionalWidgets.getWidgetConnectionRemove());
            updateWidgetConnections(baseWidgets, additionalWidgets);
        }
    }


    /**
     *
     * @deprecated since 1811, not used anymore
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected void updateWidgetConnections(final Widgets baseWidgets, final Widgets additionalWidgets)
    {
    }


    @Override
    protected void applyExtensions(final Widgets widgets)
    {
        super.applyExtensions(widgets);
        final List<WidgetConnectionRemove> widgetConnectionsToRemove = widgets.getWidgetConnectionRemove();
        widgetConnectionsRemover.filterConnections(widgets.getWidgetConnection(), widgetConnectionsToRemove);
    }


    public boolean isStoringEnabled()
    {
        if(storingEnabled != null)
        {
            return Boolean.TRUE.equals(storingEnabled);
        }
        if(getCockpitProperties() == null)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("Could not find cockpitProperty bean, using spring default value={}", storingEnabledSpring);
            }
            storingEnabled = Boolean.valueOf(storingEnabledSpring);
        }
        else
        {
            final String property = getCockpitProperties().getProperty(COCKPITNG_WIDGETS_STORING_ENABLED);
            if(property == null)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Could not find cockpitProperty '{}', using spring default value={}", COCKPITNG_WIDGETS_STORING_ENABLED,
                                    storingEnabledSpring);
                }
                storingEnabled = Boolean.valueOf(storingEnabledSpring);
            }
            else
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Found cockpitProperty '{}', value={}", COCKPITNG_WIDGETS_STORING_ENABLED, property);
                }
                storingEnabled = BooleanUtils.toBooleanObject(property);
            }
        }
        return Boolean.TRUE.equals(storingEnabled);
    }


    public void setStoringEnabled(final boolean storingEnabled)
    {
        this.storingEnabledSpring = storingEnabled;
    }


    @Override
    public CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    @Override
    @Autowired
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    @Override
    public void resetToDefaults()
    {
        getApplicationContext().getClassLoader().reset();
        final String libDirAbsolutePath = getWidgetLibUtils().libDirAbsolutePath();
        final List<String> modules = cockpitModuleConnector.getCockpitModuleUrls();
        for(final String moduleUrl : modules)
        {
            final CockpitModuleInfo cockpitModuleInfo = cockpitModuleConnector.getModuleInfo(moduleUrl);
            if(cockpitModuleInfo != null)
            {
                cockpitModuleConnector.getLibraryHandler(moduleUrl).afterDeploy(cockpitModuleInfo, libDirAbsolutePath);
            }
        }
    }


    public CockpitModuleConnector getCockpitModuleConnector()
    {
        return cockpitModuleConnector;
    }


    @Required
    public void setCockpitModuleConnector(final CockpitModuleConnector cockpitModuleConnector)
    {
        this.cockpitModuleConnector = cockpitModuleConnector;
    }


    @Required
    public void setWidgetConnectionsRemover(final WidgetConnectionsRemover widgetConnectionsRemover)
    {
        this.widgetConnectionsRemover = widgetConnectionsRemover;
    }


    public WidgetConnectionsRemover getWidgetConnectionsRemover()
    {
        return widgetConnectionsRemover;
    }


    protected CockpitApplicationContext getApplicationContext()
    {
        return applicationContext;
    }


    @Override
    public void setApplicationContext(final ApplicationContext applicationContext)
    {
        this.applicationContext = CockpitApplicationContext.getCockpitApplicationContext(applicationContext);
    }
}
