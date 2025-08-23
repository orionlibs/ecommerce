/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer.value;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.dataaccess.facades.type.CollectionDataType;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.EditorUtils;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.util.UITools;
import java.util.Collection;
import java.util.Collections;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

/**
 * Label renderer for CompareView to create a string representation of collection value. It creates a container for each
 * element that contains a string representation of it.
 */
public class CollectionLabelRenderer extends AbstractCockpitEditorRenderer<Object>
{
    private static final String SCLASS_ENTRY_CONTAINER = "yw-attribute-value-entry";
    private static final String SCLASS_EMPTY_ENTRY = "yw-attribute-value-entry-empty";
    private CockpitEditorRenderer<Object> valueRenderer;


    @Override
    public void render(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener)
    {
        final Collection<Object> collection = getCollection(context);
        renderValues(parent, context, listener, collection);
    }


    protected void renderValues(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener,
                    final Collection<Object> collection)
    {
        if(CollectionUtils.isEmpty(collection))
        {
            final Label noElementsLabel = createEmptyCollectionLabel(context);
            UITools.addSClass(noElementsLabel, SCLASS_EMPTY_ENTRY);
            parent.appendChild(noElementsLabel);
        }
        else
        {
            collection.forEach(entry -> {
                final Div entryContainer = new Div();
                renderElement(entryContainer, context, listener, entry);
                UITools.addSClass(entryContainer, SCLASS_ENTRY_CONTAINER);
                parent.appendChild(entryContainer);
            });
        }
    }


    protected Collection<Object> getCollection(final EditorContext<Object> context)
    {
        return (context.getInitialValue() instanceof Collection) ? (Collection)context.getInitialValue() : Collections.emptyList();
    }


    protected Label createEmptyCollectionLabel(final EditorContext<Object> context)
    {
        return new Label(CompareViewLabelRendererWrapper.NO_VALUE_LABEL);
    }


    protected void renderElement(final Component parent, final EditorContext<Object> context,
                    final EditorListener<Object> listener, final Object value)
    {
        final DataType type = context.getParameterAs(Editor.DATA_TYPE);
        final DataType elementType = getElementType(type, context, value);
        renderElement(parent, context, listener, value, elementType);
    }


    protected void renderElement(final Component parent, final EditorContext<Object> context,
                    final EditorListener<Object> listener, final Object value, final DataType elementType)
    {
        final EditorContext<Object> editorContext = EditorContext.clone(context, value);
        editorContext.setValueType(EditorUtils.getEditorType(elementType));
        getValueRenderer().render(parent, editorContext, listener);
    }


    protected DataType getElementType(final DataType collectionType, final EditorContext<Object> context, final Object value)
    {
        return ((CollectionDataType)collectionType).getValueType();
    }


    public CockpitEditorRenderer<Object> getValueRenderer()
    {
        return valueRenderer;
    }


    @Required
    public void setValueRenderer(final CockpitEditorRenderer<Object> valueRenderer)
    {
        this.valueRenderer = valueRenderer;
    }
}
