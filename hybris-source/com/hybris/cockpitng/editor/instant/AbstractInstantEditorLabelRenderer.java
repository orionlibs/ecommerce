/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.instant;

import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.editor.decorated.AbstractEditorRendererWrapper;
import com.hybris.cockpitng.editor.instant.labelprovider.InstantEditorLabelProvider;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.UITools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;

public abstract class AbstractInstantEditorLabelRenderer extends AbstractEditorRendererWrapper
{
    /**
     * editor-parameter which can be used to change label key for no value case
     */
    public static final String PARAM_LABEL_NO_VALUE_KEY = "noValueLabelKey";
    /**
     * editor-parameter which can be used to add css class to no value label
     */
    public static final String PARAM_LABEL_NO_VALUE_STYLE = "noValueLabelStyle";
    /**
     * Key of a label to be displayed when there is no value for given attribute
     */
    public static final String LABEL_KEY_NO_VALUE = "data.no.value";
    protected static final String SCLASS_NO_VALUE = "ye-instant-editor-label-no-value";
    protected static final String SCLASS_ACCESS_DENIED = "ye-instant-editor-label-access-denied";
    private LabelService labelService;
    private PermissionFacade permissionFacade;


    protected void createValueLabel(final Component parent, final EditorContext<Object> context,
                    final InstantEditorLabelProvider labelProvider)
    {
        if(parent != null)
        {
            final Label label = UITools.appendChildIfNeeded(parent, 0, Label.class::isInstance, Label::new);
            updateValueLabel(label, context, labelProvider);
        }
    }


    protected void updateValueLabel(final Label label, final EditorContext<Object> context,
                    final InstantEditorLabelProvider labelProvider)
    {
        final String labelValue = labelProvider.getLabel(context.getValueType(), context.getInitialValue());
        final String noValueLabelKey = context.getParameterAs(PARAM_LABEL_NO_VALUE_KEY);
        final String noValueLabelStyle = context.getParameterAs(PARAM_LABEL_NO_VALUE_STYLE);
        if(context.getInitialValue() != null && !getPermissionFacade().canReadInstance(context.getInitialValue()))
        {
            label.setValue(getLabelService().getAccessDeniedLabel(context.getInitialValue()));
            UITools.addSClass(label, SCLASS_ACCESS_DENIED);
        }
        else if(StringUtils.isEmpty(labelValue) && noValueLabelKey == null)
        {
            label.setValue(Labels.getLabel(LABEL_KEY_NO_VALUE));
            UITools.addSClass(label, SCLASS_NO_VALUE);
        }
        else if(StringUtils.isEmpty(labelValue) && StringUtils.isNotBlank(noValueLabelKey))
        {
            label.setValue(Labels.getLabel(noValueLabelKey));
            UITools.addSClass(label, SCLASS_NO_VALUE);
        }
        else if(StringUtils.isNotEmpty(labelValue))
        {
            label.setValue(labelValue);
            UITools.removeSClass(label, SCLASS_NO_VALUE);
        }
        if(StringUtils.isEmpty(labelValue) && StringUtils.isNotBlank(noValueLabelStyle))
        {
            UITools.addSClass(label, noValueLabelStyle);
        }
    }


    protected LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    protected PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }
}
