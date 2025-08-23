/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.renderers.attributedescription;

import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.util.UITools;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;

public class DefaultAttributeDescriptionIconRenderer implements AttributeDescriptionIconRenderer
{
    public static final String YW_DESC_TOOLTIP = "yw-desc-tooltip";
    protected static final String COCKPIT_PROPERTY_DISPLAY_ATTRIBUTE_DESCRIPTION = "cockpitng.displayAttributeDescriptions";
    private static final String SCLASS_CELL_DESCRIPTION = "attribute-label-description";
    private CockpitProperties cockpitProperties;


    @Override
    public void renderDescriptionIcon(final String desc, final Div
                    labelContainer)
    {
        if(BooleanUtils.isTrue(isDisplayingAttributeDescriptionEnabled()) && StringUtils.isNotBlank(desc))
        {
            final Div description = new Div();
            final Popup popup = new Popup();
            popup.appendChild(new Label(desc));
            UITools.modifySClass(popup, YW_DESC_TOOLTIP, true);
            description.setTooltiptext(StringUtils.abbreviate(desc, 20));
            description.appendChild(popup);
            description.addEventListener(Events.ON_CLICK, event -> popup.open(description));
            labelContainer.appendChild(description);
            UITools.modifySClass(description, SCLASS_CELL_DESCRIPTION, true);
        }
    }


    protected boolean isDisplayingAttributeDescriptionEnabled()
    {
        final String displayAttributeDescriptionPropertyValue = cockpitProperties.getProperty
                        (COCKPIT_PROPERTY_DISPLAY_ATTRIBUTE_DESCRIPTION);
        return BooleanUtils.toBoolean(displayAttributeDescriptionPropertyValue);
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }
}
