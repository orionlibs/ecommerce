/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.instant.labelprovider.function;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editor.instant.labelprovider.InstantEditorLabelProvider;
import org.zkoss.zul.Label;

/**
 *
 * @deprecated since 6.6
 * @see com.hybris.cockpitng.core.Executable
 */
@FunctionalInterface
@Deprecated(since = "6.6", forRemoval = true)
public interface LabelUpdateFunction
{
    /**
     * Updates label component basing on editor value.
     * <p>
     * Base functionality should be to set label's value, but it can do more, e.g. set proper CSS class basing on the
     * value.
     * </p>
     *
     * @param label
     *           component to update
     * @param editor
     *           editor which value should be reflected in the label
     * @param labelProvider
     *           label provider used to get string representation of editor's value
     */
    void update(final Label label, final Editor editor, final InstantEditorLabelProvider labelProvider);
}
