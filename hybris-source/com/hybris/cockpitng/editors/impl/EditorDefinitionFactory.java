/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editors.impl;

import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.core.CockpitComponentInfo;
import com.hybris.cockpitng.core.impl.AbstractComponentDefinitionFactory;
import com.hybris.cockpitng.core.impl.DefaultCockpitComponentDefinitionService;
import com.hybris.cockpitng.core.impl.jaxb.Keywords;
import com.hybris.cockpitng.core.impl.jaxb.View;
import com.hybris.cockpitng.editors.EditorDefinition;
import java.util.Properties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Responsible for creating EditorDefinition instances based on provided cockpit component info.
 */
public class EditorDefinitionFactory extends AbstractComponentDefinitionFactory
{
    /**
     * @deprecated since 1808, use {@link DefaultCockpitComponentDefinitionService#DEFAULT_VIEW_ZUL} instead
     */
    @Deprecated(since = "1808", forRemoval = true)
    public static final String DEFAULT_VIEW_ZUL_FILENAME = DefaultCockpitComponentDefinitionService.DEFAULT_VIEW_ZUL;
    public static final String PROPERTY_EDITOR_ID = "editor-id";
    public static final String PROPERTY_EDITOR_CLASS = "editor-class";
    public static final String PROPERTY_EDITOR_NAME = "editor-name";
    public static final String PROPERTY_EDITOR_DESCRIPTION = "editor-description";
    public static final String PROPERTY_EDITOR_CATEGORY = "editor-category";
    public static final String PROPERTY_EDITOR_TYPE = "editor-type";
    public static final String PROPERTY_VIEW_ZUL = "view-zul";
    private static final String ZUL_EXTENSION = ".zul";


    @Override
    public AbstractCockpitComponentDefinition createDefinition(final CockpitComponentInfo info)
    {
        Properties properties = info.getProperties();
        final String widgetPath = info.getRootPath();
        final com.hybris.cockpitng.core.impl.jaxb.EditorDefinition xmlDef = getXMLDefinition(widgetPath, info,
                        com.hybris.cockpitng.core.impl.jaxb.EditorDefinition.class);
        if(xmlDef != null)
        {
            properties = new Properties(properties);
            info.setProperties(properties);
            loadProperties(info, xmlDef);
        }
        // ID
        final String id = properties.getProperty(PROPERTY_EDITOR_ID);
        final EditorDefinition def = new EditorDefinition();
        def.setCode(id);
        // view
        final String viewZul = properties.getProperty(PROPERTY_VIEW_ZUL);
        def.setViewSrc(viewZul);
        // editor class
        String editorClass = properties.getProperty(PROPERTY_EDITOR_CLASS);
        if(StringUtils.isBlank(editorClass) && StringUtils.isNotBlank(viewZul))
        {
            editorClass = DefaultZulCockpitEditorRenderer.class.getName();
        }
        def.setEditorClassName(editorClass);
        // name, description
        def.setName(properties.getProperty(PROPERTY_EDITOR_NAME));
        def.setDescription(properties.getProperty(PROPERTY_EDITOR_DESCRIPTION));
        // category
        def.setCategoryTag(properties.getProperty(PROPERTY_EDITOR_CATEGORY));
        // type
        def.setType(properties.getProperty(PROPERTY_EDITOR_TYPE));
        //parent
        def.setParentCode(properties.getProperty(PROPERTY_WIDGET_PARENT));
        def.setHandlesLocalization(Boolean.valueOf(properties.getProperty("handles-localization")));
        return def;
    }


    private Properties loadProperties(final CockpitComponentInfo info,
                    final com.hybris.cockpitng.core.impl.jaxb.EditorDefinition xmlDef)
    {
        final Properties properties = info.getProperties();
        super.loadProperties(properties, xmlDef);
        // ID
        setProperty(properties, PROPERTY_EDITOR_ID, xmlDef.getId());
        // action class
        setProperty(properties, PROPERTY_EDITOR_CLASS, xmlDef.getEditorClassName());
        // name, description
        setProperty(properties, PROPERTY_EDITOR_NAME, xmlDef.getName());
        setProperty(properties, PROPERTY_EDITOR_DESCRIPTION, xmlDef.getDescription());
        // category
        final Keywords keywords = xmlDef.getKeywords();
        if(keywords != null && CollectionUtils.isNotEmpty(keywords.getKeyword()))
        {
            setProperty(properties, PROPERTY_EDITOR_CATEGORY, keywords.getKeyword().iterator().next());
        }
        // type
        setProperty(properties, PROPERTY_EDITOR_TYPE, xmlDef.getType());
        // view
        final String viewZul = getViewZul(info, xmlDef);
        setProperty(properties, PROPERTY_VIEW_ZUL, viewZul);
        setProperty(properties, "handles-localization", String.valueOf(xmlDef.isHandlesLocalization()));
        return properties;
    }


    private String getViewZul(final CockpitComponentInfo info, final com.hybris.cockpitng.core.impl.jaxb.EditorDefinition xmlDef)
    {
        final View view = xmlDef.getView();
        String viewZul = view == null ? null : view.getSrc();
        if(viewZul == null)
        {
            final String path = getEditorDirectoryPath(info);
            final String widgetID = getWidgetSimpleID(xmlDef.getId());
            final String zulFileName = widgetID + ZUL_EXTENSION;
            if(!checkIfFileIsAbsent(path + zulFileName, info))
            {
                viewZul = zulFileName;
            }
            else if(!checkIfFileIsAbsent(path + zulFileName.toLowerCase(), info))
            {
                viewZul = zulFileName.toLowerCase();
            }
            else
            {
                viewZul = DefaultCockpitComponentDefinitionService.DEFAULT_VIEW_ZUL;
            }
        }
        return viewZul;
    }


    private String getEditorDirectoryPath(final CockpitComponentInfo info)
    {
        String path = info.getRootPath();
        if(!path.endsWith(PATH_SEPARATOR))
        {
            path += PATH_SEPARATOR;
        }
        return path;
    }
}
