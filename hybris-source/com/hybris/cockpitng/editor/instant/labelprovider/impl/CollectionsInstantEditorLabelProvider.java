/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.instant.labelprovider.impl;

import com.hybris.cockpitng.editors.EditorUtils;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.zkoss.util.resource.Labels;

public class CollectionsInstantEditorLabelProvider extends AbstractInstantEditorLabelProvider
{
    protected static final String LABEL_KEY_COLLECTION_SIZE = "collection.size";


    @Override
    public boolean canHandle(final String editorType)
    {
        return EditorUtils.getMultiReferenceEditorPattern().matcher(editorType).matches()
                        || EditorUtils.getListEditorPattern().matcher(editorType).matches()
                        || EditorUtils.getMapEditorPattern().matcher(editorType).matches();
    }


    @Override
    public String getLabel(final String editorType, final Object value)
    {
        final Integer size = getElementsCountFromValue(value);
        if(size != null && size.intValue() > 0)
        {
            return Labels.getLabel(LABEL_KEY_COLLECTION_SIZE, ArrayUtils.toArray(size));
        }
        return "";
    }


    protected Integer getElementsCountFromValue(final Object value)
    {
        if(value instanceof Collection)
        {
            return Integer.valueOf(((Collection)value).size());
        }
        if(value instanceof Map)
        {
            return Integer.valueOf(((Map)value).size());
        }
        return null;
    }
}
