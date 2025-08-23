/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer.value;

import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.util.UITools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;

/**
 * Label renderer for CompareView to create a string representation of collection value. It creates a container for each
 * element that contains a string representation of it.
 */
public class CompareViewLabelRendererWrapper extends AbstractCockpitEditorRenderer<Object>
{
    protected static final String NO_VALUE_LABEL = "-";
    private static final String SCLASS_EMPTY_VALUE = "yw-compareview-attribute-value-empty";
    private CockpitEditorRenderer<Object> valueRenderer;


    @Override
    public void render(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener)
    {
        final Object initialValue = context.getInitialValue();
        if(initialValue == null || (initialValue instanceof String && StringUtils.isBlank((String)initialValue)))
        {
            final Label noValueLabel = createNoValueLabel(context);
            UITools.addSClass(noValueLabel, SCLASS_EMPTY_VALUE);
            parent.appendChild(noValueLabel);
        }
        else
        {
            getValueRenderer().render(parent, context, listener);
        }
    }


    protected Label createNoValueLabel(final EditorContext<Object> context)
    {
        return new Label(NO_VALUE_LABEL);
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
