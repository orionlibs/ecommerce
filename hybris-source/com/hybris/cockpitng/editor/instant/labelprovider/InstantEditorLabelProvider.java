/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.instant.labelprovider;

import com.hybris.cockpitng.components.Editor;
import org.springframework.core.Ordered;

/**
 * Interface for label providers used by {@link com.hybris.cockpitng.editor.instant.InstantEditorRenderer} to convert
 * underlying editor's value to string representation
 */
public interface InstantEditorLabelProvider extends Ordered
{
    /**
     * Method used to determine if label provider is able to convert the value of underlying editor. When deciding which
     * label provider should be used, the first returning {@code true} from this method is choosen.
     *
     * @param editorType
     *           type of underlying editor as returned by {@link Editor#getType()}
     * @return if this label provider is able to convert underlying editor's value
     */
    boolean canHandle(final String editorType);


    /**
     * Method converting passed value to its string representation
     *
     * @param editorType
     *           type of underlying editor as returned by {@link Editor#getType()}
     * @param value
     *           underlying editor's value to convert
     * @return string representation of {@code value}
     */
    String getLabel(final String editorType, final Object value);
}
