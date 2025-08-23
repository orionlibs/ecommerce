/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine;

import com.hybris.cockpitng.core.ui.WidgetInstance;

/**
 * Provides z-index for modal windows to preserve theirs order after refresh.
 */
public interface ModalWindowStack
{
    /**
     * Provides z-index for given widget instance. If widget has already z-index assigned the same will be returned.
     *
     * @param widgetInstance widget instance.
     * @return z-index which should be used to show window.
     */
    int getModalWindowZIndex(WidgetInstance widgetInstance);


    /**
     * Removes widget's z-index from stack and it's model.
     *
     * @param widgetInstance widget instance.
     */
    void onWindowClosed(WidgetInstance widgetInstance);


    /**
     * Resets z-index stack
     */
    void resetModalWindowStack();
}
