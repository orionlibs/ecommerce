package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.PushComponent;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.WorkflowCockpitEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.workflow.WorkflowActionService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkflowPushController extends AbstractPushController
{
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowPushController.class);
    private final List<TypedObject> lastWfActions = new ArrayList<>();
    private final List<String> attClassNames = new LinkedList<>();
    private TypeService typeService;
    private WorkflowActionService workflowActionService;
    public static final String PARAM_ATT_CLASS_NAMES = "attClassNames";


    public WorkflowPushController()
    {
        this(null);
    }


    public WorkflowPushController(int updateInterval)
    {
        this(null, updateInterval);
    }


    public WorkflowPushController(PushComponent component)
    {
        this(component, 60000);
    }


    public WorkflowPushController(PushComponent component, int updateInterval)
    {
        super(component, updateInterval);
    }


    protected void after()
    {
    }


    protected void before()
    {
    }


    public boolean isUpdateNeeded()
    {
        boolean updateNeeded = false;
        List<String> attachmentClassNames = getAttachmentClassNames();
        if(attachmentClassNames == null || attachmentClassNames.isEmpty())
        {
            LOG.warn("Can not update using server push. Reason: No attachment class name specified.");
        }
        else
        {
            WorkflowActionService workflowActionService = getWorkflowActionService();
            if(workflowActionService == null)
            {
                LOG.warn("Could not retrieve workflow action service.");
            }
            else
            {
                List<TypedObject> wfActions = getTypeService().wrapItems(workflowActionService.getAllUserWorkflowActionsWithAttachments(attachmentClassNames));
                if(!this.lastWfActions.equals(wfActions))
                {
                    this.lastWfActions.clear();
                    this.lastWfActions.addAll(wfActions);
                    updateNeeded = true;
                }
            }
        }
        return updateNeeded;
    }


    public void update()
    {
        addEvent((CockpitEvent)new WorkflowCockpitEvent(getComponent(), this.lastWfActions));
    }


    protected List<String> getAttachmentClassNames()
    {
        return this.attClassNames;
    }


    protected void loadParameters()
    {
        super.loadParameters();
        Object types = this.params.get("attClassNames");
        if(types instanceof Collection)
        {
            this.attClassNames.addAll((Collection<? extends String>)types);
        }
    }


    private TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = (TypeService)getWebApplicationContext().getBean("cockpitTypeService");
        }
        return this.typeService;
    }


    private WorkflowActionService getWorkflowActionService()
    {
        if(this.workflowActionService == null)
        {
            this.workflowActionService = (WorkflowActionService)getWebApplicationContext().getBean("workflowActionService");
        }
        return this.workflowActionService;
    }
}
