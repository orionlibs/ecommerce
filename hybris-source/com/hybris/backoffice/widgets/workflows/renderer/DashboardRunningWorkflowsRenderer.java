/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.workflows.renderer;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.workflow.model.WorkflowModel;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listitem;

/**
 * Renders workflows which are already started=running.
 */
public class DashboardRunningWorkflowsRenderer extends DefaultRunningWorkflowsRenderer
{
    @Override
    protected HtmlBasedComponent createTitle(final Listitem parent, final Object config, final WorkflowModel data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Div topContent = new Div();
        topContent.setSclass(SCLASS_WORKFLOWS_LIST_CONTENT_TOP);
        final Div threeDots = new Div();
        renderThreeDots(parent, config, data, dataType, widgetInstanceManager, threeDots);
        topContent.appendChild(threeDots);
        final Button button = createTitleButton(data, widgetInstanceManager);
        topContent.appendChild(button);
        final Label dateLabel = createDateLabel(data);
        topContent.appendChild(dateLabel);
        final Label noOfAttachmentsLabel = createNoOfAttachmentsLabel(widgetInstanceManager, data);
        topContent.appendChild(noOfAttachmentsLabel);
        return topContent;
    }


    @Override
    protected Div createContent(final WorkflowModel workflowModel, final DataType dataType, final WidgetInstanceManager wim)
    {
        final Div content = new Div();
        final Div middleContent = createMiddleContent(wim, workflowModel);
        middleContent.setSclass(SCLASS_WORKFLOWS_LIST_MIDDLE);
        content.appendChild(middleContent);
        return content;
    }
}
