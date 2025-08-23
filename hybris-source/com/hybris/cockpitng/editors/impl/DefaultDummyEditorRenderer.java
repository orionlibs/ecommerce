/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editors.impl;

import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.UITools;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Textbox;

public class DefaultDummyEditorRenderer implements CockpitEditorRenderer<Object>
{
    @Autowired
    private LabelService labelService;


    @Override
    public void render(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener)
    {
        final Textbox editorView = new Textbox();
        editorView.setDisabled(true);
        String label = "";
        if(context.getInitialValue() != null)
        {
            if(context.getInitialValue() instanceof String)
            {
                label = (String)context.getInitialValue();
            }
            else
            {
                label = labelService.getObjectLabel(context.getInitialValue());
            }
        }
        editorView.setValue(label);
        editorView.setTooltiptext(label);
        editorView.setParent(parent);
        UITools.modifySClass(editorView, "ye-default-dummy", true);
    }
}
