/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import com.hybris.cockpitng.engine.WidgetInstanceManager;

/**
 * An interface that defines contract for performing the custom logic within {@link DefaultEditorAreaController}.
 */
public interface EditorAreaBeforeLogicHandler
{
    /**
     * This method is responsible for performing the custom logic before saving/persisting current object within {@link DefaultEditorAreaController}.
     */
    void beforeSave(final WidgetInstanceManager widgetInstanceManager, final Object currentObject);


    /**
     * This method is responsible for performing the custom logic before refreshing/reloading current object within {@link DefaultEditorAreaController}.
     */
    void beforeRefresh(final WidgetInstanceManager widgetInstanceManager, final Object currentObject);


    /**
     * This method is responsible for performing the custom logic when the selected object is added within {@link DefaultEditorAreaController}.
     */
    void onAddSelectedObject(final Object addedObject);
}
