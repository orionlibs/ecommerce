package de.hybris.platform.workflow.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedWorkflowDecision extends AbstractWorkflowDecision
{
    public static final String ACTION = "action";
    public static final String TOACTIONS = "toActions";
    protected static String WORKFLOWACTIONLINKRELATION_SRC_ORDERED = "relation.WorkflowActionLinkRelation.source.ordered";
    protected static String WORKFLOWACTIONLINKRELATION_TGT_ORDERED = "relation.WorkflowActionLinkRelation.target.ordered";
    protected static String WORKFLOWACTIONLINKRELATION_MARKMODIFIED = "relation.WorkflowActionLinkRelation.markmodified";
    protected static final BidirectionalOneToManyHandler<GeneratedWorkflowDecision> ACTIONHANDLER = new BidirectionalOneToManyHandler(GeneratedWorkflowConstants.TC.WORKFLOWDECISION, false, "action", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractWorkflowDecision.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("action", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public WorkflowAction getAction(SessionContext ctx)
    {
        return (WorkflowAction)getProperty(ctx, "action");
    }


    public WorkflowAction getAction()
    {
        return getAction(getSession().getSessionContext());
    }


    public void setAction(SessionContext ctx, WorkflowAction value)
    {
        ACTIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setAction(WorkflowAction value)
    {
        setAction(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        ACTIONHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("WorkflowAction");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(WORKFLOWACTIONLINKRELATION_MARKMODIFIED);
        }
        return true;
    }


    public Collection<WorkflowAction> getToActions(SessionContext ctx)
    {
        List<WorkflowAction> items = getLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONLINKRELATION, "WorkflowAction", null, false, false);
        return items;
    }


    public Collection<WorkflowAction> getToActions()
    {
        return getToActions(getSession().getSessionContext());
    }


    public long getToActionsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONLINKRELATION, "WorkflowAction", null);
    }


    public long getToActionsCount()
    {
        return getToActionsCount(getSession().getSessionContext());
    }


    public void setToActions(SessionContext ctx, Collection<WorkflowAction> value)
    {
        setLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONLINKRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONLINKRELATION_MARKMODIFIED));
    }


    public void setToActions(Collection<WorkflowAction> value)
    {
        setToActions(getSession().getSessionContext(), value);
    }


    public void addToToActions(SessionContext ctx, WorkflowAction value)
    {
        addLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONLINKRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONLINKRELATION_MARKMODIFIED));
    }


    public void addToToActions(WorkflowAction value)
    {
        addToToActions(getSession().getSessionContext(), value);
    }


    public void removeFromToActions(SessionContext ctx, WorkflowAction value)
    {
        removeLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONLINKRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONLINKRELATION_MARKMODIFIED));
    }


    public void removeFromToActions(WorkflowAction value)
    {
        removeFromToActions(getSession().getSessionContext(), value);
    }
}
