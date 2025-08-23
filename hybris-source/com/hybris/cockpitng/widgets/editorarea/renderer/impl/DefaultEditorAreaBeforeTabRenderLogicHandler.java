/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.editorarea.renderer.impl;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaBeforeTabRenderLogicHandler;

public class DefaultEditorAreaBeforeTabRenderLogicHandler implements EditorAreaBeforeTabRenderLogicHandler
{
    @Override
    public boolean canRender(final AbstractTab abstractTab, final Object object)
    {
        return true;
    }


    @Override
    public boolean isConfiguredCustomTab(final AbstractTab abstractTab, final Object object)
    {
        return false;
    }
}
