/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.dynamicforms.impl.visitors;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.widgets.common.dynamicforms.ComponentsVisitor;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;

/**
 * Visitor which disables all components which are instance of {@link Editor} or {@link Button}.
 */
public class ComponentsDisablingVisitor implements ComponentsVisitor
{
    @Override
    public void register(final Component component)
    {
        if(component instanceof Editor)
        {
            ((Editor)component).setReadOnly(true);
        }
        else if(component instanceof Button)
        {
            ((Button)component).setDisabled(true);
        }
        component.getChildren().forEach(this::register);
    }


    @Override
    public void unRegister(final Component component)
    {
        // not implemented
    }
}

