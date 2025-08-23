/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.workflowactions.renderer;

import com.hybris.backoffice.renderer.utils.UIDateRendererProvider;
import com.hybris.backoffice.widgets.workflowactions.WorkflowActionsController;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.HyperlinkFallbackLabelProvider;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

/**
 * Renderer which is responsible for rendering workflow action on the list.
 */
public class DefaultWorkflowActionsRenderer implements WidgetComponentRenderer<Listitem, Object, WorkflowActionModel>
{
    protected static final String SCLASS_WORKFLOW_ACTIONS_LIST_CONTENT = "yw-workflow-actions-list-content";
    protected static final String SCLASS_WORKFLOW_ACTIONS_LIST_CONTENT_BOTTOM = "yw-workflow-actions-list-content-bottom yw-workflow-actions-list-info-container";
    protected static final String SCLASS_WORKFLOW_ACTIONS_LIST_CONTENT_MIDDLE = "yw-workflow-actions-list-content-middle";
    protected static final String SCLASS_WORKFLOW_ACTIONS_LIST_CONTENT_TOP = "yw-workflow-actions-list-content-top";
    protected static final String SCLASS_WORKFLOW_ACTIONS_LIST_INFO = "yw-workflow-actions-list-info";
    protected static final String SCLASS_WORKFLOW_ACTIONS_LIST_ITEM = "yw-workflow-actions-list-item";
    protected static final String SCLASS_WORKFLOW_ACTIONS_LIST_TITLE = "yw-workflow-actions-list-title ye-text-button";
    protected static final String LABEL_WORKFLOW_ACTIONS_ATTACHMENT = "workflowactions.attachment";
    protected static final String LABEL_WORKFLOW_ACTIONS_ATTACHMENTS = "workflowactions.attachments";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWorkflowActionsRenderer.class);
    private TimeService timeService;
    private UIDateRendererProvider uiDateRendererProvider;
    private LabelService labelService;
    private SessionService sessionService;
    private I18NService i18NService;
    private PermissionFacade permissionFacade;
    private HyperlinkFallbackLabelProvider hyperlinkFallbackLabelProvider;


    @Override
    public void render(final Listitem listitem, final Object configuration, final WorkflowActionModel data,
                    final DataType dataType, final WidgetInstanceManager wim)
    {
        final Div mainDiv = new Div();
        mainDiv.setSclass(SCLASS_WORKFLOW_ACTIONS_LIST_CONTENT);
        mainDiv.appendChild(createTopContent(createTopContentTitle(data), data, wim));
        final Div middleContent = createMiddleContent(wim, data);
        middleContent.setSclass(SCLASS_WORKFLOW_ACTIONS_LIST_CONTENT_MIDDLE);
        mainDiv.appendChild(middleContent);
        final Div bottomContent = createBottomContent(wim, data);
        bottomContent.setSclass(SCLASS_WORKFLOW_ACTIONS_LIST_CONTENT_BOTTOM);
        mainDiv.appendChild(bottomContent);
        final Listcell listcell = new Listcell();
        listcell.appendChild(mainDiv);
        listitem.setSclass(SCLASS_WORKFLOW_ACTIONS_LIST_ITEM);
        listitem.appendChild(listcell);
    }


    protected String createTopContentTitle(final WorkflowActionModel data)
    {
        return labelService.getShortObjectLabel(data);
    }


    protected Div createTopContent(final String title, final WorkflowActionModel data, final WidgetInstanceManager wim)
    {
        final Div topContent = new Div();
        topContent.setSclass(SCLASS_WORKFLOW_ACTIONS_LIST_CONTENT_TOP);
        topContent.appendChild(createTitleComponent(title, data, wim));
        return topContent;
    }


    protected Component createTitleComponent(final String title, final WorkflowActionModel data, final WidgetInstanceManager wim)
    {
        if(getPermissionFacade().canReadInstance(data))
        {
            return createTitleButton(title, data, wim);
        }
        return new Label(title);
    }


    protected Button createTitleButton(final String title, final WorkflowActionModel data, final WidgetInstanceManager wim)
    {
        final Button button = new Button();
        button.setSclass(SCLASS_WORKFLOW_ACTIONS_LIST_TITLE);
        button.setLabel(hyperlinkFallbackLabelProvider.getFallback(title));
        button.addEventListener(Events.ON_CLICK, event -> onTitleClick(data, wim));
        return button;
    }


    protected Div createMiddleContent(final WidgetInstanceManager wim, final WorkflowActionModel data)
    {
        final Div middleContent = new Div();
        final Label workflowNameLabel = new Label(createMiddleContentLabel(data));
        middleContent.appendChild(workflowNameLabel);
        return middleContent;
    }


    protected String createMiddleContentLabel(final WorkflowActionModel data)
    {
        final WorkflowModel workflow = data.getWorkflow();
        if(workflow == null)
        {
            LOG.warn("Could not find workflow. Using fallback label.");
            return getLabelService().getObjectLabel(data);
        }
        return getLabelService().getObjectLabel(workflow);
    }


    protected Div createBottomContent(final WidgetInstanceManager wim, final WorkflowActionModel data)
    {
        final Div bottomContent = new Div();
        final Label dateLabel = createDateLabel(data);
        dateLabel.setSclass(SCLASS_WORKFLOW_ACTIONS_LIST_INFO);
        bottomContent.appendChild(dateLabel);
        final Label noOfAttachmentsLabel = createNoOfAttachmentsLabel(wim, data);
        noOfAttachmentsLabel.setSclass(SCLASS_WORKFLOW_ACTIONS_LIST_INFO);
        bottomContent.appendChild(noOfAttachmentsLabel);
        return bottomContent;
    }


    protected Label createDateLabel(final WorkflowActionModel data)
    {
        if(permissionFacade.canReadInstance(data))
        {
            final UIDateRendererProvider rendererProvider = getUiDateRendererProvider();
            final String dateLabelValue = rendererProvider.getFormattedDateLabel(getTimeService().getCurrentTime(),
                            data.getActivated());
            return new Label(dateLabelValue);
        }
        return createNoReadAccessLabel(data);
    }


    @InextensibleMethod
    private Label createNoReadAccessLabel(final WorkflowActionModel workflowAction)
    {
        return new Label(getLabelService().getAccessDeniedLabel(workflowAction));
    }


    protected Label createNoOfAttachmentsLabel(final WidgetInstanceManager wim, final WorkflowActionModel data)
    {
        if(permissionFacade.canReadInstance(data))
        {
            final long attachmentsSize = data.getWorkflow().getAttachments().stream().filter(e -> e.getItem() != null).count();
            final String labelKey = attachmentsSize == 1 ? LABEL_WORKFLOW_ACTIONS_ATTACHMENT : LABEL_WORKFLOW_ACTIONS_ATTACHMENTS;
            final String noOfAttachmentsLabelValue = wim.getLabel(labelKey, new String[]
                            {String.valueOf(attachmentsSize)});
            return new Label(noOfAttachmentsLabelValue);
        }
        return createNoReadAccessLabel(data);
    }


    public UIDateRendererProvider getUiDateRendererProvider()
    {
        return uiDateRendererProvider;
    }


    @Required
    public void setUiDateRendererProvider(final UIDateRendererProvider uiDateRendererProvider)
    {
        this.uiDateRendererProvider = uiDateRendererProvider;
    }


    protected void onTitleClick(final WorkflowActionModel data, final WidgetInstanceManager wim)
    {
        wim.sendOutput(WorkflowActionsController.SOCKET_OUT_WORKFLOW_ACTION_SELECTED, data);
    }


    public TimeService getTimeService()
    {
        return timeService;
    }


    @Required
    public void setTimeService(final TimeService timeService)
    {
        this.timeService = timeService;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public SessionService getSessionService()
    {
        return sessionService;
    }


    @Required
    public void setSessionService(final SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public I18NService getI18NService()
    {
        return i18NService;
    }


    @Required
    public void setI18NService(final I18NService i18NService)
    {
        this.i18NService = i18NService;
    }


    public PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    public HyperlinkFallbackLabelProvider getHyperlinkFallbackLabelProvider()
    {
        return hyperlinkFallbackLabelProvider;
    }


    @Required
    public void setHyperlinkFallbackLabelProvider(final HyperlinkFallbackLabelProvider hyperlinkFallbackLabelProvider)
    {
        this.hyperlinkFallbackLabelProvider = hyperlinkFallbackLabelProvider;
    }
}
