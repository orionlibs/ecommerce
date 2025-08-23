/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.labeledtoggle;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionListener;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.actions.toggle.ToggleActionRenderer;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;

public class LabeledToggleActionRenderer extends ToggleActionRenderer
{
    protected static final String SCLASS_YW_LABELED_TOGGLE_LABEL = "yw-labeled-toggle-label";
    protected static final String SETTING_LABEL_EXPRESSION = "labelExpression";


    @Override
    public void render(final Component parent, final CockpitAction<Object, Object> action, final ActionContext<Object> context,
                    final boolean updateMode, final ActionListener<Object> listener)
    {
        super.render(parent, action, context, updateMode, listener);
        renderLabel(parent, context);
    }


    protected void renderLabel(final Component parent, final ActionContext<Object> context)
    {
        final Object value = hasLabelExpression(context) ? evaluateExpression(context) : context.getData();
        if(value != null)
        {
            final Label label = new Label();
            label.setSclass(SCLASS_YW_LABELED_TOGGLE_LABEL);
            label.setValue(value.toString());
            parent.appendChild(label);
        }
    }


    protected boolean hasLabelExpression(final ActionContext<Object> context)
    {
        final Object settingValue = context.getParameter(SETTING_LABEL_EXPRESSION);
        return settingValue != null && StringUtils.isNotEmpty(settingValue.toString());
    }


    protected Object evaluateExpression(final ActionContext<Object> context)
    {
        return getExpressionResolverFactory().createResolver().getValue(context.getData(),
                        (String)context.getParameter(SETTING_LABEL_EXPRESSION));
    }
}
