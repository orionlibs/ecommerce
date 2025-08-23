package de.hybris.platform.cockpit.wizards.workflow;

import de.hybris.platform.cockpit.components.ComponentsHelper;
import de.hybris.platform.cockpit.components.duallistbox.impl.CockpitUsersDualListboxEditor;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.WorkflowFacade;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Collections;
import java.util.List;

public class AssignWorkflowWizardController extends DefaultPageController
{
    public void done(Wizard wizard, WizardPage page)
    {
        AssignWorkflowPage assignWorkflowPage = null;
        if(page instanceof AssignWorkflowPage)
        {
            assignWorkflowPage = (AssignWorkflowPage)page;
        }
        CockpitUsersDualListboxEditor editor = assignWorkflowPage.getEditor();
        List<TypedObject> assignedValuesList = editor.getAssignedValuesList();
        TypedObject typedWorkflow = (TypedObject)wizard.getWizardContext().getAttribute("workflow");
        WorkflowModel workflowModel = (WorkflowModel)typedWorkflow.getObject();
        if(!assignedValuesList.isEmpty())
        {
            PrincipalModel userToAssign = (PrincipalModel)((TypedObject)assignedValuesList.iterator().next()).getObject();
            getWorkflowFacade().assignUser(userToAssign, workflowModel);
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, typedWorkflow, Collections.EMPTY_LIST));
            if(userToAssign.getName() == null || userToAssign.getName().trim().isEmpty())
            {
                ComponentsHelper.displayNotification("workflow.user.assigned", "workflow.assigned.successfully.nousername", new Object[0]);
            }
            else
            {
                ComponentsHelper.displayNotification("workflow.user.assigned", "workflow.assigned.successfully.user", new Object[] {userToAssign
                                .getName()});
            }
        }
        else if(assignedValuesList.isEmpty())
        {
            getWorkflowFacade().unassignUser(workflowModel);
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, typedWorkflow, Collections.EMPTY_LIST));
            ComponentsHelper.displayNotification("workflow.user.unassigned", "workflow.unassigned.successfully.nousername", new Object[0]);
        }
    }


    public WorkflowFacade getWorkflowFacade()
    {
        return (WorkflowFacade)Registry.getApplicationContext().getBean("workflowFacade", WorkflowFacade.class);
    }
}
