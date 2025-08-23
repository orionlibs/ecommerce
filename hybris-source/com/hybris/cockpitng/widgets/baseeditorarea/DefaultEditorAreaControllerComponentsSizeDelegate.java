/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.HtmlBasedComponent;

public class DefaultEditorAreaControllerComponentsSizeDelegate
{
    private final DefaultEditorAreaController controller;
    private final DefaultEditorAreaControllerModelOperationsDelegate modelOperationsDelegate;
    private final DefaultEditorAreaControllerSettingsLoader settingsLoader;


    public DefaultEditorAreaControllerComponentsSizeDelegate(final DefaultEditorAreaController controller)
    {
        this.controller = controller;
        modelOperationsDelegate = controller.getModelOperationsDelegate();
        settingsLoader = controller.getSettingsLoader();
    }


    public void applySclassAccordingToWidth(final int width)
    {
        final HtmlBasedComponent targetComponent = controller.getAttributesDiv();
        if(targetComponent != null)
        {
            if(modelOperationsDelegate.isFluidViewMode())
            {
                final String attributesDivSclass = calculateScssClass(width);
                targetComponent.setSclass(attributesDivSclass);
            }
            else
            {
                targetComponent.setSclass(DefaultEditorAreaController.SCLASS_ATTRIBUTES_DIV_SCLASS);
            }
        }
    }


    private String calculateScssClass(final int width)
    {
        String attributesDivSclass = StringUtils.EMPTY;
        if(width <= settingsLoader.getMinWidthThreshold())
        {
            attributesDivSclass = String.format("%s%s", DefaultEditorAreaController.SCLASS_ATTRIBUTES_DIV_SCLASS, "-min");
        }
        else if(width > settingsLoader.getMinWidthThreshold() && width < settingsLoader.getMaxWidthThreshold())
        {
            attributesDivSclass = String.format("%s%s", DefaultEditorAreaController.SCLASS_ATTRIBUTES_DIV_SCLASS, "-avg");
        }
        else if(width >= settingsLoader.getMaxWidthThreshold())
        {
            attributesDivSclass = String.format("%s%s", DefaultEditorAreaController.SCLASS_ATTRIBUTES_DIV_SCLASS, "-max");
        }
        return attributesDivSclass;
    }
}
