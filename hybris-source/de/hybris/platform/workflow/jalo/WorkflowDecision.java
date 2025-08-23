package de.hybris.platform.workflow.jalo;

import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.security.JaloSecurityException;
import java.util.Collection;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class WorkflowDecision extends GeneratedWorkflowDecision
{
    private static final Logger LOG = Logger.getLogger(WorkflowDecision.class.getName());


    public void chosen() throws JaloSecurityException, JaloBusinessException
    {
        Collection<Link> links = WorkflowManager.getInstance().getLinks(this, null);
        for(Link link : links)
        {
            link.setAttribute("active", Boolean.TRUE);
        }
        Collection<WorkflowAction> actions = getToActions();
        for(WorkflowAction action : actions)
        {
            action.checkIncomingLinks();
        }
    }
}
