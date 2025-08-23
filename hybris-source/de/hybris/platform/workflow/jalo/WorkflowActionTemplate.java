package de.hybris.platform.workflow.jalo;

import de.hybris.bootstrap.util.RequirementHolder;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class WorkflowActionTemplate extends GeneratedWorkflowActionTemplate implements RequirementHolder
{
    private static final Logger LOG = Logger.getLogger(WorkflowActionTemplate.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        ComposedType workflowActionType = TypeManager.getInstance().getComposedType(WorkflowAction.class);
        allAttributes.put("creationType", workflowActionType);
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public WorkflowAction createWorkflowAction(Workflow workflow)
    {
        JaloSession.getCurrentSession().createLocalSessionContext();
        JaloSession.getCurrentSession().getSessionContext().setLanguage(null);
        Map<String, Object> params = new HashMap<>();
        params.put("name", getAllName());
        params.put("description", getAllDescription());
        params.put("principalAssigned", getPrincipalAssigned());
        params.put("workflow", workflow);
        params.put("status", WorkflowAction.getIdleStatus());
        params.put("sendEmail", isSendEmail());
        params.put("emailAddress", getEmailAddress());
        params.put("template", this);
        params.put("rendererTemplate", getRendererTemplate());
        params.put("actionType", getActionType());
        WorkflowAction action = null;
        if(getCreationType() == null)
        {
            action = WorkflowManager.getInstance().createWorkflowAction(params);
        }
        else
        {
            action = WorkflowManager.getInstance().createWorkflowAction(params, getCreationType());
        }
        JaloSession.getCurrentSession().removeLocalSessionContext();
        return action;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addToPredecessors(SessionContext ctx, AbstractWorkflowAction value)
    {
        super.addToPredecessors(ctx, value);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addToSuccessors(SessionContext ctx, AbstractWorkflowAction value)
    {
        super.addToSuccessors(ctx, value);
    }


    public Set<? extends RequirementHolder> getRequirements()
    {
        return new HashSet<>(getPredecessors());
    }


    protected void perform(WorkflowAction action)
    {
    }


    @ForceJALO(reason = "something else")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        Map<String, Item> params = new HashMap<>();
        params.put("template", this);
        List<WorkflowAction> result = FlexibleSearch.getInstance().search("SELECT {PK} FROM {WorkflowAction} WHERE {template}=?template", params, WorkflowAction.class).getResult();
        if(!result.isEmpty())
        {
            throw new ConsistencyCheckException(Localization.getLocalizedString("error.workflowactiontemplate.remove"), 0);
        }
        super.remove(ctx);
    }


    @ForceJALO(reason = "something else")
    public List<Link> getIncomingLinkTemplates(SessionContext ctx)
    {
        return (List<Link>)WorkflowManager.getInstance().getLinkTemplates(null, (AbstractWorkflowAction)this);
    }


    @ForceJALO(reason = "something else")
    public String getIncomingLinkTemplatesStr(SessionContext ctx)
    {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        List<Link> incomingLinkList = getIncomingLinkTemplates(ctx);
        for(Link link : incomingLinkList)
        {
            if(first)
            {
                first = false;
            }
            else
            {
                builder.append(", ");
            }
            if(((WorkflowDecisionTemplate)link.getSource()).getActionTemplate().getName() == null)
            {
                builder.append(((WorkflowDecisionTemplate)link.getSource()).getActionTemplate().getCode());
                continue;
            }
            builder.append(((WorkflowDecisionTemplate)link.getSource()).getActionTemplate().getName());
        }
        return builder.toString();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public long getDecisionsCount()
    {
        String query = "SELECT COUNT({" + WorkflowDecisionTemplate.PK + "}) FROM {" + GeneratedWorkflowConstants.TC.WORKFLOWDECISIONTEMPLATE + "} WHERE {actionTemplate}=?action";
        try
        {
            List<Long> result = FlexibleSearch.getInstance().search(query, Collections.singletonMap("action", this), Long.class).getResult();
            return ((Long)result.iterator().next()).longValue();
        }
        catch(FlexibleSearchException e)
        {
            throw new JaloInternalException(e, "flexible search error for search query '" + query + "'", 0);
        }
    }


    @ForceJALO(reason = "something else")
    public ComposedType getCreationType(SessionContext ctx)
    {
        if(getProperty(ctx, "creationType") == null)
        {
            setCreationType(ctx, TypeManager.getInstance().getComposedType(WorkflowAction.class));
        }
        return (ComposedType)getProperty(ctx, "creationType");
    }


    @ForceJALO(reason = "something else")
    public void setCreationType(SessionContext ctx, ComposedType value)
    {
        try
        {
            if(WorkflowAction.class.isInstance(value.getJaloClass().newInstance()))
            {
                setProperty(ctx, "creationType", value);
            }
            else
            {
                throw new JaloInvalidParameterException(Localization.getLocalizedString("error.workflowactiontemplate.creationtype.notvalid", new String[] {value
                                .getName()}), 0);
            }
        }
        catch(InstantiationException e)
        {
            throw new JaloInvalidParameterException(Localization.getLocalizedString("error.workflowactiontemplate.creationtype.noinstance", new String[] {value
                            .getName()}), 0);
        }
        catch(IllegalAccessException e)
        {
            throw new JaloInvalidParameterException(e.getLocalizedMessage(), 0);
        }
    }
}
