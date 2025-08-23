/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer.value;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.dataaccess.facades.type.MapDataType;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.util.UITools;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Span;

/**
 * Label renderer for CompareView to create a string representation of map value. It creates a container for each map
 * entry that contains a string representation of key and value.
 */
public class MapLabelRenderer extends CollectionLabelRenderer
{
    private static final String KEY_VALUE_SEPARATOR_IMG_CLASS = "yw-compareview-map-arrow";


    @Override
    protected Collection<Object> getCollection(final EditorContext<Object> context)
    {
        final Object initialValue = context.getInitialValue();
        if(initialValue instanceof Map)
        {
            return ((Map)context.getInitialValue()).entrySet();
        }
        else if(initialValue instanceof Map.Entry)
        {
            return Collections.singleton(initialValue);
        }
        else
        {
            return Collections.emptySet();
        }
    }


    @Override
    protected void renderElement(final Component parent, final EditorContext<Object> context,
                    final EditorListener<Object> listener, final Object value)
    {
        final Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>)value;
        final MapDataType type = context.getParameterAs(Editor.DATA_TYPE);
        final Span keyContainer = new Span();
        super.renderElement(keyContainer, context, listener, entry.getKey(), type.getKeyType());
        final Span arrowImage = new Span();
        UITools.addSClass(arrowImage, KEY_VALUE_SEPARATOR_IMG_CLASS);
        final Span valueContainer = new Span();
        super.renderElement(valueContainer, context, listener, entry.getValue(), type.getValueType());
        final Div mapEntryContainer = new Div();
        mapEntryContainer.appendChild(keyContainer);
        mapEntryContainer.appendChild(arrowImage);
        mapEntryContainer.appendChild(valueContainer);
        parent.appendChild(mapEntryContainer);
    }
}
