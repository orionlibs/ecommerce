package de.hybris.platform.cockpit.events.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkflowCockpitEvent extends InfoboxCockpitEvent
{
    private final List<TypedObject> wfActions = new ArrayList<>();


    public WorkflowCockpitEvent(Object source, List<TypedObject> wfActions)
    {
        super(source);
        if(wfActions != null)
        {
            this.wfActions.addAll(wfActions);
        }
    }


    public List<TypedObject> getWorkflowActions()
    {
        return Collections.unmodifiableList(this.wfActions);
    }


    public List<TypedObject> getRelatedItems()
    {
        return Collections.unmodifiableList(this.wfActions);
    }
}
