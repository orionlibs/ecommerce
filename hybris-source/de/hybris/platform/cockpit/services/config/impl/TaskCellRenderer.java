package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.components.AdvancedGroupbox;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.workflow.WorkflowActionCommentService;
import de.hybris.platform.workflow.WorkflowAttachmentService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.exceptions.ActivationWorkflowActionException;
import de.hybris.platform.workflow.exceptions.WorkflowActionDecideException;
import de.hybris.platform.workflow.exceptions.WorkflowTerminatedException;
import de.hybris.platform.workflow.model.WorkflowActionCommentModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import java.util.Collection;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

public class TaskCellRenderer implements CellRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(TaskCellRenderer.class);
    private WorkflowService serviceLayerWorkflowService;
    private TypeService typeService;
    private WorkflowProcessingService workflowProcessingService;
    private WorkflowActionCommentService workflowActionCommentService;
    private WorkflowAttachmentService workflowAttachmentService;
    private final DefaultColumnDescriptor colDescr;
    protected static final String ACTIONROW_SCLASS = "actionRow";
    protected static final String ADD_COMMENT_SCLASS = "commentAdd";
    protected static final String COMMENT_SCLASS = "comment";
    protected static final String COMMENT_AUTOMATED_SCLASS = "comment automated";
    protected static final String COMMENT_HEADER_SCLASS = "header";
    private static final String SECTION_CONTAINER_SCLASS = "section_component_container";


    public TaskCellRenderer(ColumnConfiguration colConf)
    {
        this.colDescr = colConf.getColumnDescriptor();
    }


    public void render(TableModel model, int colIndex, int rowIndex, Component parent)
    {
        TypedObject task = (TypedObject)model.getListComponentModel().getListModel().elementAt(rowIndex);
        if(task.getObject() instanceof WorkflowActionModel)
        {
            WorkflowActionModel action = (WorkflowActionModel)task.getObject();
            AdvancedGroupbox groupbox = new AdvancedGroupbox();
            groupbox.setSclass("taskGroupbox");
            parent.appendChild((Component)groupbox);
            groupbox.setLabel(action.getName());
            groupbox.getCaptionContainer().appendChild((Component)new Label("| " + action
                            .getWorkflow().getName() + " | " + UITools.getLocalDateTime(action.getCreationtime())));
            groupbox.addEventListener("onOpen", (EventListener)new Object(this, parent, groupbox, model, task));
            Div container = new Div();
            container.setSclass("section_component_container");
            groupbox.appendChild((Component)container);
            container.setHeight("100%");
            groupbox.setOpen(model.getListComponentModel().isSelected(rowIndex));
            Div commentsDiv = new Div();
            renderActionRow(task, model, container, parent, commentsDiv);
            container.appendChild((Component)commentsDiv);
            renderComments(task, commentsDiv);
            container.addEventListener("onClick", (EventListener)new Object(this));
        }
    }


    protected void renderActionRow(TypedObject task, TableModel model, Div container, Component parent, Div commentsDiv)
    {
        Div actionRowDiv = new Div();
        container.appendChild((Component)actionRowDiv);
        actionRowDiv.setSclass("actionRow");
        WorkflowActionModel action = (WorkflowActionModel)task.getObject();
        Collection<WorkflowDecisionModel> decisions = action.getDecisions();
        renderDecisionDropDown(actionRowDiv, parent, model, decisions, task);
        renderGotoItem(actionRowDiv, model, task);
    }


    protected void renderGotoItem(Div actionRowDiv, TableModel model, TypedObject task)
    {
        Button gotoItemImage = new Button(Labels.getLabel("task.gotoitem"));
        actionRowDiv.appendChild((Component)gotoItemImage);
        gotoItemImage.setSclass("btnblue");
        gotoItemImage.setTooltiptext(Labels.getLabel("task.gotoitem"));
        gotoItemImage.setImage("/cockpit/images/arrow_onblue.gif");
        gotoItemImage.setDir("reverse");
        gotoItemImage.addEventListener("onClick", (EventListener)new Object(this, model, task));
    }


    protected void renderDecisionDropDown(Div actionRowDiv, Component parent, TableModel model, Collection<WorkflowDecisionModel> decisions, TypedObject task)
    {
        Listbox dropdown = new Listbox();
        actionRowDiv.appendChild((Component)dropdown);
        Listitem firstListItem = new Listitem();
        dropdown.appendChild((Component)firstListItem);
        firstListItem.setLabel(Labels.getLabel("task.setstatus"));
        firstListItem.setValue(null);
        dropdown.setSelectedIndex(0);
        dropdown.setMultiple(false);
        dropdown.setRows(1);
        dropdown.setMold("select");
        if(getServiceLayerWorkflowService().isTerminated(((WorkflowActionModel)task.getObject()).getWorkflow()))
        {
            dropdown.setDisabled(true);
        }
        dropdown.addEventListener("onSelect", (EventListener)new Object(this, dropdown, task, model, parent));
        for(WorkflowDecisionModel decision : decisions)
        {
            Listitem listItem = new Listitem();
            dropdown.appendChild((Component)listItem);
            String label = decision.getName();
            if(label == null || label.length() == 0)
            {
                label = decision.getCode();
            }
            listItem.setLabel(label);
            listItem.setValue(getTypeService().wrapItem(decision));
        }
    }


    protected void doDecideAction(WorkflowActionModel action, WorkflowDecisionModel decision, TableModel model, Component parent)
    {
        try
        {
            getWorkflowProcessingService().decideAction(action, decision);
            model.fireEvent("hidecontextarea", null);
            ((BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective()).collapseEditorArea();
            ((BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective()).setActiveItem(null);
            UISessionUtils.getCurrentSession().getCurrentPerspective().getEditorArea().reset();
            UISessionUtils.getCurrentSession().getCurrentPerspective().getEditorArea().getEditorAreaController()
                            .resetSectionPanelModel();
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(parent, null, Collections.EMPTY_LIST));
        }
        catch(WorkflowActionDecideException e)
        {
            String localizedString = "Error while setting decision";
            if(e.getMessage() != null)
            {
                if(e.getMessage().startsWith("[01]"))
                {
                    localizedString = Localization.getLocalizedString("error.workflowaction.decide.notprincipalassigned");
                }
                else if(e.getMessage().startsWith("[02]"))
                {
                    localizedString = Labels.getLabel("workflow.error.workflowaction.decide.alreadycompleted");
                }
            }
            Notification notification = new Notification(localizedString);
            model.fireEvent("shownotification", notification);
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(parent, null, Collections.EMPTY_LIST));
        }
        catch(WorkflowTerminatedException e)
        {
            Notification notification = new Notification(Labels.getLabel("workflow.error.workflowaction.decide.terminated"));
            model.fireEvent("shownotification", notification);
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(parent, null, Collections.EMPTY_LIST));
        }
        catch(ActivationWorkflowActionException e)
        {
            Notification notification = new Notification(Labels.getLabel("workflow.error.workflowaction.activation"), e.getMessage());
            model.fireEvent("shownotification", notification);
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(parent, null, Collections.EMPTY_LIST));
        }
    }


    protected void renderComments(TypedObject task, Div commentsDiv)
    {
        Collection<WorkflowActionCommentModel> comments = ((WorkflowActionModel)task.getObject()).getWorkflowActionComments();
        Div addCommentDiv = new Div();
        commentsDiv.appendChild((Component)addCommentDiv);
        addCommentDiv.setSclass("commentAdd");
        for(WorkflowActionCommentModel comment : comments)
        {
            Div commentDiv = new Div();
            commentsDiv.appendChild((Component)commentDiv);
            if(getWorkflowActionCommentService().isAutomatedComment(comment))
            {
                commentDiv.setSclass("comment automated");
            }
            else
            {
                commentDiv.setSclass("comment");
            }
            Div commentHeaderDiv = new Div();
            commentDiv.appendChild((Component)commentHeaderDiv);
            commentHeaderDiv.setSclass("header");
            StringBuffer commentHeader = new StringBuffer(UITools.getLocalDateTime(comment.getCreationtime()));
            UserModel commentUser = comment.getUser();
            if(commentUser != null)
            {
                commentHeader.append(" | ").append(commentUser.getDisplayName());
            }
            commentHeaderDiv.appendChild((Component)new Label(commentHeader.toString()));
            commentDiv.appendChild((Component)new Label(comment.getComment()));
        }
    }


    private WorkflowService getServiceLayerWorkflowService()
    {
        if(this.serviceLayerWorkflowService == null)
        {
            this
                            .serviceLayerWorkflowService = (WorkflowService)Registry.getApplicationContext().getBean("newestWorkflowService");
        }
        return this.serviceLayerWorkflowService;
    }


    private WorkflowProcessingService getWorkflowProcessingService()
    {
        if(this.workflowProcessingService == null)
        {
            this
                            .workflowProcessingService = (WorkflowProcessingService)Registry.getApplicationContext().getBean("workflowProcessingService");
        }
        return this.workflowProcessingService;
    }


    private WorkflowActionCommentService getWorkflowActionCommentService()
    {
        if(this.workflowActionCommentService == null)
        {
            this
                            .workflowActionCommentService = (WorkflowActionCommentService)Registry.getApplicationContext().getBean("workflowActionCommentService");
        }
        return this.workflowActionCommentService;
    }


    private WorkflowAttachmentService getWorkflowAttachmentService()
    {
        if(this.workflowAttachmentService == null)
        {
            this
                            .workflowAttachmentService = (WorkflowAttachmentService)Registry.getApplicationContext().getBean("workflowAttachmentService");
        }
        return this.workflowAttachmentService;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }
}
