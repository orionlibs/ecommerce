/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.actionbar.impl;

import com.hybris.backoffice.actionbar.ActionComponent;
import com.hybris.backoffice.actionbar.ActionDefinition;
import com.hybris.backoffice.actionbar.ActionbarContext;
import com.hybris.cockpitng.util.ClickTrackingTools;
import com.hybris.cockpitng.util.YTestTools;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.HtmlBasedComponent;

/**
 * Utilities methods to help rendering actions representations
 */
public class ActionComponentUtils
{
    private static final String ATTRIBUTE_DEFINITION = "actionDefinition";


    protected ActionComponentUtils()
    {
        // blocks the possibility of create a new instance
    }


    /**
     * Returns label for provided localized key
     *
     * @param context
     *           bar context
     * @param locKey
     *           key to be found
     * @param fallback
     *           fallback label to be returned if no label for key found
     * @return label
     */
    public static String getLabel(final ActionbarContext context, final String locKey, final String fallback)
    {
        if(StringUtils.isBlank(locKey))
        {
            return fallback;
        }
        else
        {
            final String label = context.getWidgetInstanceManager().getLabel(locKey);
            if(StringUtils.isEmpty(label))
            {
                return (StringUtils.isEmpty(fallback) ? locKey : fallback);
            }
            else
            {
                return label;
            }
        }
    }


    /**
     * Initialises component representation of action - sets its labels, images, etc.
     *
     * @param nodeElement
     *           component representation
     * @param context
     *           bar context
     * @param definition
     *           action definition
     */
    public static void formatNodeElement(final ActionComponent nodeElement, final ActionbarContext context,
                    final ActionDefinition definition)
    {
        nodeElement.setLabel(getLabel(context, definition.getNameLocKey(), definition.getName()));
        nodeElement.setImage(getLabel(context, definition.getIconUriLocKey(), definition.getIconUri()));
        if(definition.getDescription() == null && definition.getDescriptionLocKey() == null)
        {
            nodeElement.setTooltiptext(nodeElement.getLabel());
        }
        else
        {
            nodeElement.setLabel(getLabel(context, definition.getDescriptionLocKey(), definition.getDescription()));
        }
        initComponent(nodeElement, context, definition);
    }


    /**
     * Initialises component representation of action - stores all needed meta data, sets test ids, etc.
     *
     * @param nodeElement
     *           component representation
     * @param context
     *           bar context
     * @param definition
     *           action definition
     */
    public static void initComponent(final ActionComponent nodeElement, final ActionbarContext context,
                    final ActionDefinition definition)
    {
        ClickTrackingTools.modifyClickTrackingId(nodeElement.getComponent(), definition.getNameLocKey());
        YTestTools.modifyYTestId(nodeElement.getComponent(), definition.getId());
        nodeElement.setAttribute(ATTRIBUTE_DEFINITION, definition);
    }


    /**
     * Gets a definition bound to provided component representation
     *
     * @param nodeComponent
     *           component representation
     * @param <D>
     *           type of definition expected
     * @return definition bound to component or <code>null</code> if a component does not represent any action
     */
    public static <D extends ActionDefinition> D getDefinition(final HtmlBasedComponent nodeComponent)
    {
        return (D)nodeComponent.getAttribute(ATTRIBUTE_DEFINITION);
    }
}
