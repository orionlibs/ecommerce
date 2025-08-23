package de.hybris.platform.cms2.jalo;

import de.hybris.platform.comments.jalo.Comment;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.workflow.jalo.WorkflowDecision;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCMSWorkflowComment extends Comment
{
    public static final String DECISION = "decision";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Comment.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("decision", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public WorkflowDecision getDecision(SessionContext ctx)
    {
        return (WorkflowDecision)getProperty(ctx, "decision");
    }


    public WorkflowDecision getDecision()
    {
        return getDecision(getSession().getSessionContext());
    }


    public void setDecision(SessionContext ctx, WorkflowDecision value)
    {
        setProperty(ctx, "decision", value);
    }


    public void setDecision(WorkflowDecision value)
    {
        setDecision(getSession().getSessionContext(), value);
    }
}
