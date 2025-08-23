package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.menu;

import de.hybris.platform.cockpit.components.ComponentsHelper;
import de.hybris.platform.cockpit.components.contentbrowser.AdvanceMenupopup;
import de.hybris.platform.cockpit.components.menu.ManageMenuitem;
import de.hybris.platform.cockpit.components.menu.impl.UsersAssignedListWorkflowRenderer;
import de.hybris.platform.cockpit.components.navigationarea.renderer.WorkflowSectionRenderer;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.MyWorkflowsTreeRenderer;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.WorkflowFacade;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.WorkflowViewOptions;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Menuseparator;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

public class WorkflowPopupMenu extends Menupopup
{
    private final Tree workflowTree;
    private final Object rootDummy;
    private final WorkflowSectionRenderer parentSectionRenderer;


    public WorkflowPopupMenu(Treeitem treeitem, Treecell cell, WorkflowSectionRenderer parentSectionRenderer, MyWorkflowsTreeRenderer myWorkflowsTreeRenderer, Tree workflowTree, Object rootDummy)
    {
        this.parentSectionRenderer = parentSectionRenderer;
        this.workflowTree = workflowTree;
        this.rootDummy = rootDummy;
        addEventListener("onOpen", (EventListener)new Object(this, treeitem, cell, parentSectionRenderer, myWorkflowsTreeRenderer));
    }


    private void createViewOptionsMenuEntry(Menupopup ctxMenu, Treeitem treeitem)
    {
        Menu menuViewOptions = new Menu(Labels.getLabel("workflow.view.options"));
        Menupopup viewOptions = new Menupopup();
        Menu menuWorkflowOptions = new Menu(Labels.getLabel("workflow.view.options.workflow"));
        Menupopup workflowOptions = new Menupopup();
        Menu menuAdhocWorkflowOptions = new Menu(Labels.getLabel("workflow.view.options.adhocworkflow"));
        Menupopup adhocWorkflowOptions = new Menupopup();
        if(treeitem.getValue() != null)
        {
            Menuitem menuViewAsGraph = new Menuitem(Labels.getLabel("workflow.view.options.viewasgraph"));
            WorkflowModel workflowModel = (WorkflowModel)((TypedObject)treeitem.getValue()).getObject();
            menuViewAsGraph.addEventListener("onClick", (EventListener)new Object(this, workflowModel));
            menuViewAsGraph.setParent((Component)viewOptions);
        }
        Object object = new Object(this, workflowOptions, adhocWorkflowOptions);
        createMenuItem(workflowOptions, (EventListener)object, "workflow.planned", WorkflowViewOptions.Options.PLANNED);
        createMenuItem(workflowOptions, (EventListener)object, "workflow.running", WorkflowViewOptions.Options.RUNNING);
        createMenuItem(workflowOptions, (EventListener)object, "workflow.finished", WorkflowViewOptions.Options.FINISHED);
        createMenuItem(workflowOptions, (EventListener)object, "workflow.range", WorkflowViewOptions.Options.FROM_TO);
        createMenuItem(workflowOptions, (EventListener)object, "workflow.terminated", WorkflowViewOptions.Options.TERMINATED);
        workflowOptions.setParent((Component)menuWorkflowOptions);
        menuWorkflowOptions.setParent((Component)viewOptions);
        createAdhocMenuItem(adhocWorkflowOptions, (EventListener)object, "workflow.planned", WorkflowViewOptions.Options.PLANNED);
        createAdhocMenuItem(adhocWorkflowOptions, (EventListener)object, "workflow.running", WorkflowViewOptions.Options.RUNNING);
        createAdhocMenuItem(adhocWorkflowOptions, (EventListener)object, "workflow.finished", WorkflowViewOptions.Options.FINISHED);
        createAdhocMenuItem(adhocWorkflowOptions, (EventListener)object, "workflow.range", WorkflowViewOptions.Options.FROM_TO);
        createAdhocMenuItem(adhocWorkflowOptions, (EventListener)object, "workflow.terminated", WorkflowViewOptions.Options.TERMINATED);
        adhocWorkflowOptions.setParent((Component)menuAdhocWorkflowOptions);
        menuAdhocWorkflowOptions.setParent((Component)viewOptions);
        viewOptions.setParent((Component)menuViewOptions);
        menuViewOptions.setParent((Component)ctxMenu);
    }


    private void createAssignMenuitem(Menupopup ctxMenu, TypedObject typedWorkflow)
    {
        WorkflowModel adhocWorkflowModel = (WorkflowModel)typedWorkflow.getObject();
        Menu menuAssignTo = new Menu(Labels.getLabel("workflow.assignto"));
        AdvanceMenupopup assignTo = new AdvanceMenupopup();
        UserGroupModel groupModel = getWorkflowFacade().getUserGroupForUID("cockpitgroup");
        List<WorkflowActionModel> actions = adhocWorkflowModel.getActions();
        if(actions.isEmpty())
        {
            ComponentsHelper.displayNotification("workflow.caption", "workflow.nosteps.nousers", new Object[0]);
            return;
        }
        List<PrincipalModel> assignedUsers = new ArrayList<>();
        List<WorkflowActionModel> startActions = getWorkflowFacade().getStartWorkflowActions(adhocWorkflowModel);
        for(WorkflowActionModel action : startActions)
        {
            assignedUsers.add(action.getPrincipalAssigned());
        }
        UsersAssignedListWorkflowRenderer editor = new UsersAssignedListWorkflowRenderer(UISessionUtils.getCurrentSession().getTypeService().wrapItems(assignedUsers), typedWorkflow);
        assignTo.appendChild((Component)editor.createMenuListViewComponent());
        assignTo.appendChild((Component)new Menuseparator());
        Object object = new Object(this, assignedUsers, groupModel, typedWorkflow);
        ManageMenuitem manageMenuitem = new ManageMenuitem(Labels.getLabel("workflow.users.manage"), (EventListener)object);
        assignTo.appendChild((Component)manageMenuitem);
        assignTo.appendChild((Component)new Menuseparator());
        ComponentsHelper.createMenuWithUsers((Menupopup)assignTo, (PrincipalModel)groupModel, assignedUsers, (EventListener)new CheckboxClickListener(this, typedWorkflow));
        menuAssignTo.appendChild((Component)assignTo);
        ctxMenu.appendChild((Component)menuAssignTo);
    }


    private Window getDateRangeModalWindow(Menuitem menuitem, boolean workflow)
    {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("title", Labels.getLabel("datePicker.title"));
        arguments.put("message", Labels.getLabel("datePicker.message"));
        arguments.put("messageFrom", Labels.getLabel("general.from"));
        arguments.put("messageTo", Labels.getLabel("general.to"));
        Datebox dateFromBox = workflow ? new Datebox(this.parentSectionRenderer.getCurrentWorkflowViewOptions().getFilterFrom()) : new Datebox(this.parentSectionRenderer.getCurrentAdhocWorkflowViewOptions().getFilterFrom());
        Datebox dateToBox = workflow ? new Datebox(this.parentSectionRenderer.getCurrentWorkflowViewOptions().getFilterTo()) : new Datebox(this.parentSectionRenderer.getCurrentAdhocWorkflowViewOptions().getFilterTo());
        arguments.put("dateFrom", dateFromBox);
        arguments.put("dateTo", dateToBox);
        Button buttonOK = new Button(Labels.getLabel("general.ok"));
        buttonOK.addEventListener("onClick", (EventListener)new Object(this, menuitem, workflow, dateFromBox, dateToBox, buttonOK));
        arguments.put("buttonOK", buttonOK);
        Button buttonCancel = new Button(Labels.getLabel("general.cancel"));
        buttonCancel.addEventListener("onClick", (EventListener)new Object(this, buttonCancel, menuitem));
        arguments.put("buttonCancel", buttonCancel);
        Component window = Executions.createComponents("cockpit/customcomponents/dateRangePicker.zul", null, arguments);
        return (window instanceof Window) ? (Window)window : null;
    }


    private void createAdhocMenuItem(Menupopup adhocWorkflowOptions, EventListener viewOptionsEventListener, String labelKey, WorkflowViewOptions.Options option)
    {
        Menuitem adhocWorkflowPlanned = new Menuitem(Labels.getLabel(labelKey));
        adhocWorkflowPlanned.setCheckmark(true);
        adhocWorkflowPlanned.setChecked(this.parentSectionRenderer.getCurrentAdhocWorkflowViewOptions().isSelected(option));
        adhocWorkflowPlanned.addEventListener("onClick", viewOptionsEventListener);
        adhocWorkflowPlanned.setParent((Component)adhocWorkflowOptions);
    }


    private void createMenuItem(Menupopup workflowOptions, EventListener viewOptionsEventListener, String labelKey, WorkflowViewOptions.Options option)
    {
        Menuitem workflowPlanned = new Menuitem(Labels.getLabel(labelKey));
        workflowPlanned.setCheckmark(true);
        workflowPlanned.setChecked(this.parentSectionRenderer.getCurrentWorkflowViewOptions().isSelected(option));
        workflowPlanned.addEventListener("onClick", viewOptionsEventListener);
        workflowPlanned.setParent((Component)workflowOptions);
    }


    public WorkflowFacade getWorkflowFacade()
    {
        return (WorkflowFacade)Registry.getApplicationContext().getBean("workflowFacade", WorkflowFacade.class);
    }
}
