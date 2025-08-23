/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EditorArea;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;

public class DefaultEditorAreaControllerWidgetTitleDelegate
{
    private static final String LABEL_NEW_ITEM_EDITOR_AREA = "editorArea.newItem";
    private static final String LABEL_EMPTY_EDITOR_AREA = "editorArea.empty";
    private final DefaultEditorAreaController controller;
    private final DefaultEditorAreaControllerConfigurationLoader configurationLoader;
    private final DefaultEditorAreaControllerModelOperationsDelegate modelOperationsDelegate;
    private final DefaultEditorAreaControllerSettingsLoader settingsLoader;


    public DefaultEditorAreaControllerWidgetTitleDelegate(final DefaultEditorAreaController controller)
    {
        this.controller = controller;
        configurationLoader = controller.getConfigurationLoader();
        modelOperationsDelegate = controller.getModelOperationsDelegate();
        settingsLoader = controller.getSettingsLoader();
    }


    protected void updateWidgetTitle()
    {
        final Object value = modelOperationsDelegate.getCurrentObject();
        String title = getTitle(value);
        if(settingsLoader.isEnableEmbeddedTitle())
        {
            final String embeddedTitle = settingsLoader.getEmbeddedTitle();
            final String embeddedTitlePrefix = settingsLoader.getEmbeddedTitlePrefix();
            title = getLabelByKey(embeddedTitle, title);
            if(StringUtils.isBlank(title))
            {
                title = embeddedTitle;
            }
            if(StringUtils.isNotBlank(embeddedTitlePrefix))
            {
                title = String.format("%s %s", getLabelByKey(embeddedTitlePrefix, embeddedTitlePrefix), title);
            }
            controller.getEditorAreaTitle().setValue(title);
        }
        else
        {
            controller.setWidgetTitle(title);
        }
        //TODO: widget title can be set below, but this is not propagate by sending to output socket
        if(settingsLoader.isTitleFromContext())
        {
            setWidgetTitleByCtxName();
        }
        controller.sendOutput(DefaultEditorAreaController.SOCKET_OUTPUT_WIDGET_TITLE, title);
    }


    private String getTitle(final Object value)
    {
        final String title;
        if(value != null && controller.getObjectFacade().isNew(value))
        {
            final String objectLabel = controller.getLabelService()
                            .getObjectLabel(modelOperationsDelegate.getCurrentType().getCode());
            title = controller.getLabel(LABEL_NEW_ITEM_EDITOR_AREA, new Object[]
                            {objectLabel});
        }
        else
        {
            title = value == null ? controller.getLabel(LABEL_EMPTY_EDITOR_AREA)
                            : controller.getLabelService().getObjectLabel(value);
        }
        return title;
    }


    private String getLabelByKey(final String key, final String defaultLabel)
    {
        String label = controller.getLabel(key);
        if(StringUtils.isBlank(label))
        {
            label = StringUtils.defaultString(Labels.getLabel(key), defaultLabel);
        }
        return label;
    }


    void setWidgetTitleByCtxName()
    {
        final EditorArea context = configurationLoader.getEditorAreaConfiguration();
        if(context != null && StringUtils.isNotEmpty(context.getName()))
        {
            final String label = getLabelByKey(context.getName(), context.getName());
            controller.setWidgetTitle(label);
        }
    }
}
