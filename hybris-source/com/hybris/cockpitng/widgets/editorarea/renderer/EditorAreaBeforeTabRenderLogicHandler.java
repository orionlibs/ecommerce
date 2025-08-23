/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.editorarea.renderer;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;

/**
 * An interface that defines contract for performing the custom logic within DefaultEditorAreaRenderer.
 */
public interface EditorAreaBeforeTabRenderLogicHandler
{
    /**
     * This method is responsible for performing the custom logic before renderer current Tab within DefaultEditorAreaRenderer.
     */
    boolean canRender(final AbstractTab abstractTab, final Object object);


    /**
     * Return true, when it is configured customTab.
     */
    boolean isConfiguredCustomTab(final AbstractTab abstractTab, final Object object);
}
