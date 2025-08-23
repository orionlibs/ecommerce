/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.workflowdetails.renderer;

import com.hybris.cockpitng.config.summaryview.jaxb.Attribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.summaryview.renderer.AbstractSummaryViewItemWithIconRenderer;
import de.hybris.platform.workflow.model.WorkflowModel;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class DefaultWorkflowDetailsSummaryAttachementsRenderer extends AbstractSummaryViewItemWithIconRenderer<WorkflowModel>
{
    public static final String LABEL_WORKFLOW_ATTACHMENTS = "collaboration.workflow.details.summary.status.value";


    @Override
    protected String getIconStatusSClass(final HtmlBasedComponent iconContainer, final Attribute attributeConfiguration,
                    final WorkflowModel data, final DataAttribute dataAttribute, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        return getIconStatusSClass("workflows", "attachments");
    }


    @Override
    protected void renderValue(final Div attributeContainer, final Attribute attributeConfiguration, final WorkflowModel data,
                    final DataAttribute dataAttribute, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final long attachmentsNumber = data.getAttachments().stream().filter(e -> e.getItem() != null).count();
        final Label label = new Label(Labels.getLabel(LABEL_WORKFLOW_ATTACHMENTS, new String[]
                        {String.valueOf(attachmentsNumber)}));
        attributeContainer.appendChild(label);
    }
}
