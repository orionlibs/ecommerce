/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EditorArea;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultEditorAreaControllerConfigurationLoader
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultEditorAreaControllerConfigurationLoader.class);
    private final DefaultEditorAreaControllerModelOperationsDelegate modelOperationsDelegate;
    private final DefaultEditorAreaControllerSettingsLoader settingsLoader;
    private final WidgetInstanceManager widgetInstanceManager;


    public DefaultEditorAreaControllerConfigurationLoader(final DefaultEditorAreaController controller)
    {
        modelOperationsDelegate = controller.getModelOperationsDelegate();
        settingsLoader = controller.getSettingsLoader();
        widgetInstanceManager = controller.getWidgetInstanceManager();
    }


    public EditorArea getEditorAreaConfiguration()
    {
        if(modelOperationsDelegate.getCurrentType() != null)
        {
            return getEditorAreaConfiguration(modelOperationsDelegate.getCurrentType().getCode(),
                            modelOperationsDelegate.getCurrentContext());
        }
        return null;
    }


    public EditorArea getEditorAreaConfiguration(final String typeCode, final String component)
    {
        final DefaultConfigContext configContext = new DefaultConfigContext(component);
        configContext.setType(typeCode);
        EditorArea config = null;
        try
        {
            config = widgetInstanceManager.loadConfiguration(configContext, EditorArea.class);
            if(config == null)
            {
                LOG.warn("Loaded UI configuration is null. Ignoring.");
            }
        }
        catch(final CockpitConfigurationException cce)
        {
            LOG.error("Could not load cockpit config for the given context '" + configContext + "'.", cce);
        }
        return settingsLoader.isEssentialsInOverviewTab() ? decorateConfigWithOverviewTab(config) : config;
    }


    private static EditorArea decorateConfigWithOverviewTab(final EditorArea config)
    {
        return config != null && config.getEssentials() != null ? new EditorAreaConfigWithOverviewTab(config) : config;
    }
}
