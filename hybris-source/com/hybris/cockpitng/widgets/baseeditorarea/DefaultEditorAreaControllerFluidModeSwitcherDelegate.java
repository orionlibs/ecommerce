/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EditorArea;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zul.Div;
import org.zkoss.zul.Toolbarbutton;

public class DefaultEditorAreaControllerFluidModeSwitcherDelegate
{
    private static final String LABEL_VIEW_MODE_FLUID_PREFIX = "viewModeFluid-";
    private final DefaultEditorAreaController controller;
    private final DefaultEditorAreaControllerConfigurationLoader configurationLoader;
    private final DefaultEditorAreaControllerModelOperationsDelegate modelOperationsDelegate;
    private final DefaultEditorAreaControllerSettingsLoader settingsLoader;
    private final WidgetInstanceManager widgetInstanceManager;


    public DefaultEditorAreaControllerFluidModeSwitcherDelegate(final DefaultEditorAreaController controller)
    {
        this.controller = controller;
        configurationLoader = controller.getConfigurationLoader();
        modelOperationsDelegate = controller.getModelOperationsDelegate();
        settingsLoader = controller.getSettingsLoader();
        widgetInstanceManager = controller.getWidgetInstanceManager();
    }


    public void prepareViewModeButton()
    {
        initFluidViewModeValue();
        controller.getViewModeButton().setVisible(modelOperationsDelegate.isViewModeButtonVisible());
        applyChangesForViewMode();
    }


    public void changeViewMode()
    {
        modelOperationsDelegate.changeViewMode();
        applyChangesForViewMode();
    }


    private void initFluidViewModeValue()
    {
        String initViewMode = settingsLoader.getInitViewMode();
        final EditorArea editorAreaConfiguration = configurationLoader.getEditorAreaConfiguration();
        if(editorAreaConfiguration != null && StringUtils.isNotBlank(editorAreaConfiguration.getViewMode()))
        {
            initViewMode = editorAreaConfiguration.getViewMode();
        }
        modelOperationsDelegate.initFluidViewModeIfNeeded(initViewMode);
    }


    private void setViewModeButtonImages()
    {
        final Toolbarbutton viewModeButton = controller.getViewModeButton();
        if(modelOperationsDelegate.isFluidViewMode())
        {
            viewModeButton.setImage(adjustImageUrl(DefaultEditorAreaController.RESPONSIVE_IMAGE_URL));
            viewModeButton.setHoverImage(adjustImageUrl(DefaultEditorAreaController.RESPONSIVE_IMAGE_URL_HOVER));
        }
        else
        {
            viewModeButton.setImage(adjustImageUrl(DefaultEditorAreaController.STATIC_IMAGE_URL));
            viewModeButton.setHoverImage(adjustImageUrl(DefaultEditorAreaController.STATIC_IMAGE_URL_HOVER));
        }
    }


    private void applyChangesForViewMode()
    {
        final Toolbarbutton viewModeButton = controller.getViewModeButton();
        final boolean fluidMode = modelOperationsDelegate.isFluidViewMode();
        final String labelKey = String.format("%s%s", LABEL_VIEW_MODE_FLUID_PREFIX, Boolean.valueOf(fluidMode));
        if(StringUtils.isNotBlank(labelKey))
        {
            viewModeButton.setTooltiptext(widgetInstanceManager.getLabel(labelKey));
        }
        UITools.modifySClass(viewModeButton, "yw-fluid", fluidMode);
        setViewModeButtonImages();
        if(fluidMode)
        {
            DefaultEditorAreaControllerClientOperationsDelegate.fireAttributesDivWidthRequest(controller.getAttributesDiv().getUuid());
        }
        else
        {
            final Div attributesDiv = controller.getAttributesDiv();
            attributesDiv.setSclass(DefaultEditorAreaController.SCLASS_ATTRIBUTES_DIV_SCLASS);
        }
    }


    private String adjustImageUrl(final String imageUrl)
    {
        return String.format("%s%s", controller.getWidgetRoot(), imageUrl);
    }
}
