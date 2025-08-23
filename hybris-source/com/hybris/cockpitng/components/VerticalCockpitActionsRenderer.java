/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.ActionGroup;
import com.hybris.cockpitng.util.UITools;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Vbox;

/**
 * Renders actions vertically using {@link Vbox} as opposed to extended {@link DefaultCockpitActionsRenderer} which uses
 * {@link org.zkoss.zul.Hbox}
 */
public class VerticalCockpitActionsRenderer extends DefaultCockpitActionsRenderer
{
    @Override
    protected HtmlBasedComponent getOrCreateGroupContainer(final AbstractCockpitElementsContainer parent,
                    final ActionGroup groupConfig)
    {
        final Optional<Component> existingGroupContainer = parent.getChildren().stream()
                        .filter(child -> (child instanceof Vbox) && groupQualifierEquals(groupConfig.getQualifier(),
                                        ObjectUtils.toString(child.getAttribute(ACTION_GROUP_QUALIFIER), null)))//
                        .findAny();
        if(existingGroupContainer.isPresent())
        {
            return (Vbox)existingGroupContainer.get();
        }
        else
        {
            final Vbox container = new Vbox();
            container.setAttribute(ACTION_GROUP_QUALIFIER, computeGroupQualifier(groupConfig));
            container.setSclass("cng-action-group");
            UITools.modifySClass(container, "cng-action-separator", groupConfig.isShowSeparator());
            container.setParent(parent);
            return container;
        }
    }
}
