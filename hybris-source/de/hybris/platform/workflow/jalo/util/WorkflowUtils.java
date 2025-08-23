package de.hybris.platform.workflow.jalo.util;

import de.hybris.bootstrap.util.RequirementSolver;
import de.hybris.bootstrap.util.RequirementSolverException;
import de.hybris.platform.workflow.jalo.WorkflowActionTemplate;
import de.hybris.platform.workflow.jalo.WorkflowTemplate;
import java.util.ArrayList;
import java.util.Collection;

public final class WorkflowUtils
{
    public static boolean hasCycle(WorkflowTemplate template)
    {
        if(template != null)
        {
            return hasCycle(template.getActions());
        }
        return false;
    }


    public static boolean hasCycle(Collection<WorkflowActionTemplate> actions, WorkflowActionTemplate action)
    {
        Collection<WorkflowActionTemplate> temp = new ArrayList<>(actions);
        temp.add(action);
        return hasCycle(temp);
    }


    public static boolean hasCycle(Collection<WorkflowActionTemplate> actions)
    {
        try
        {
            RequirementSolver.solve(actions);
        }
        catch(RequirementSolverException e)
        {
            return true;
        }
        return false;
    }
}
