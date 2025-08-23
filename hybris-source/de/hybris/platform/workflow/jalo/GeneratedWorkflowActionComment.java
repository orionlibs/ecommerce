package de.hybris.platform.workflow.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedWorkflowActionComment extends GenericItem
{
    public static final String COMMENT = "comment";
    public static final String USER = "user";
    public static final String WORKFLOWACTION = "workflowAction";
    protected static final BidirectionalOneToManyHandler<GeneratedWorkflowActionComment> WORKFLOWACTIONHANDLER = new BidirectionalOneToManyHandler(GeneratedWorkflowConstants.TC.WORKFLOWACTIONCOMMENT, false, "workflowAction", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("comment", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
        tmp.put("workflowAction", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getComment(SessionContext ctx)
    {
        return (String)getProperty(ctx, "comment");
    }


    public String getComment()
    {
        return getComment(getSession().getSessionContext());
    }


    public void setComment(SessionContext ctx, String value)
    {
        setProperty(ctx, "comment", value);
    }


    public void setComment(String value)
    {
        setComment(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        WORKFLOWACTIONHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public User getUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "user");
    }


    public User getUser()
    {
        return getUser(getSession().getSessionContext());
    }


    public void setUser(SessionContext ctx, User value)
    {
        setProperty(ctx, "user", value);
    }


    public void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }


    public AbstractWorkflowAction getWorkflowAction(SessionContext ctx)
    {
        return (AbstractWorkflowAction)getProperty(ctx, "workflowAction");
    }


    public AbstractWorkflowAction getWorkflowAction()
    {
        return getWorkflowAction(getSession().getSessionContext());
    }


    public void setWorkflowAction(SessionContext ctx, AbstractWorkflowAction value)
    {
        WORKFLOWACTIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setWorkflowAction(AbstractWorkflowAction value)
    {
        setWorkflowAction(getSession().getSessionContext(), value);
    }
}
