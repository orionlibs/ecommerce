/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.impl;

import com.hybris.cockpitng.actions.ActionDefinition;
import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.core.CockpitComponentInfo;
import com.hybris.cockpitng.core.impl.AbstractComponentDefinitionFactory;
import com.hybris.cockpitng.core.impl.jaxb.Keywords;
import java.util.Properties;
import org.apache.commons.collections.CollectionUtils;

/**
 * Responsible for creating ActionDefinition instances based on provided cockpit component info.
 */
public class ActionDefinitionFactory extends AbstractComponentDefinitionFactory
{
    @Override
    public AbstractCockpitComponentDefinition createDefinition(final CockpitComponentInfo info)
    {
        Properties properties = info.getProperties();
        final String widgetPath = info.getRootPath();
        final com.hybris.cockpitng.core.impl.jaxb.ActionDefinition xmlDef = getXMLDefinition(widgetPath, info,
                        com.hybris.cockpitng.core.impl.jaxb.ActionDefinition.class);
        if(xmlDef != null)
        {
            properties = new Properties(properties);
            info.setProperties(properties);
            loadProperties(properties, xmlDef);
        }
        // ID
        final String id = properties.getProperty("action-id");
        final ActionDefinition def = new ActionDefinition();
        def.setCode(id);
        // action class
        def.setActionClassName(properties.getProperty("action-class"));
        // custom renderer class
        def.setCustomRendererClassName(properties.getProperty("action-custom-renderer"));
        // name, description
        def.setName(properties.getProperty("action-name"));
        def.setDescription(properties.getProperty("action-description"));
        // icon URI
        final String iconUri = properties.getProperty("action-icon");
        def.setIconUri(iconUri);
        final String iconDisabledUri = properties.getProperty("action-icon-disabled");
        def.setIconDisabledUri(iconDisabledUri);
        final String iconHoverUri = properties.getProperty("action-icon-hover");
        def.setIconHoverUri(iconHoverUri);
        // category
        def.setCategoryTag(properties.getProperty("action-category"));
        // input, output
        def.setInputType(properties.getProperty("action-input-type"));
        def.setOutputType(properties.getProperty("action-output-type"));
        return def;
    }


    private Properties loadProperties(final Properties properties,
                    final com.hybris.cockpitng.core.impl.jaxb.ActionDefinition xmlDef)
    {
        super.loadProperties(properties, xmlDef);
        // ID
        setProperty(properties, "action-id", xmlDef.getId());
        // action class
        setProperty(properties, "action-class", xmlDef.getActionClassName());
        // custom renderer class
        setProperty(properties, "action-custom-renderer", xmlDef.getCustomRendererClassName());
        // name, description
        setProperty(properties, "action-name", xmlDef.getName());
        setProperty(properties, "action-description", xmlDef.getDescription());
        // icon URI
        setProperty(properties, "action-icon", xmlDef.getIconUri());
        setProperty(properties, "action-icon-disabled", xmlDef.getIconDisabledUri());
        setProperty(properties, "action-icon-hover", xmlDef.getIconHoverUri());
        // category
        final Keywords keywords = xmlDef.getKeywords();
        if(keywords != null && !CollectionUtils.isEmpty(keywords.getKeyword()))
        {
            setProperty(properties, "action-category", keywords.getKeyword().iterator().next());
        }
        // input, output
        setProperty(properties, "action-input-type", xmlDef.getInputType());
        setProperty(properties, "action-output-type", xmlDef.getOutputType());
        return properties;
    }
}
