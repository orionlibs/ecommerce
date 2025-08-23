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

public abstract class GeneratedWorkflowDecisionTemplate extends AbstractWorkflowDecision
{
    public static final String PARENTWORKFLOWTEMPLATE = "parentWorkflowTemplate";
    public static final String ACTIONTEMPLATE = "actionTemplate";
    public static final String TOTEMPLATEACTIONS = "toTemplateActions";
    protected static String WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION_SRC_ORDERED = "relation.WorkflowActionTemplateLinkTemplateRelation.source.ordered";
    protected static String WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION_TGT_ORDERED = "relation.WorkflowActionTemplateLinkTemplateRelation.target.ordered";
    protected static String WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION_MARKMODIFIED = "relation.WorkflowActionTemplateLinkTemplateRelation.markmodified";
    protected static final BidirectionalOneToManyHandler<GeneratedWorkflowDecisionTemplate> ACTIONTEMPLATEHANDLER = new BidirectionalOneToManyHandler(GeneratedWorkflowConstants.TC.WORKFLOWDECISIONTEMPLATE, false, "actionTemplate", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractWorkflowDecision.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("actionTemplate", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public WorkflowActionTemplate getActionTemplate(SessionContext ctx)
    {
        return (WorkflowActionTemplate)getProperty(ctx, "actionTemplate");
    }


    public WorkflowActionTemplate getActionTemplate()
    {
        return getActionTemplate(getSession().getSessionContext());
    }


    public void setActionTemplate(SessionContext ctx, WorkflowActionTemplate value)
    {
        ACTIONTEMPLATEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setActionTemplate(WorkflowActionTemplate value)
    {
        setActionTemplate(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        ACTIONTEMPLATEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("WorkflowActionTemplate");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION_MARKMODIFIED);
        }
        return true;
    }


    public WorkflowTemplate getParentWorkflowTemplate()
    {
        return getParentWorkflowTemplate(getSession().getSessionContext());
    }


    public Collection<WorkflowActionTemplate> getToTemplateActions(SessionContext ctx)
    {
        List<WorkflowActionTemplate> items = getLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION, "WorkflowActionTemplate", null, false, false);
        return items;
    }


    public Collection<WorkflowActionTemplate> getToTemplateActions()
    {
        return getToTemplateActions(getSession().getSessionContext());
    }


    public long getToTemplateActionsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION, "WorkflowActionTemplate", null);
    }


    public long getToTemplateActionsCount()
    {
        return getToTemplateActionsCount(getSession().getSessionContext());
    }


    public void setToTemplateActions(SessionContext ctx, Collection<WorkflowActionTemplate> value)
    {
        setLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION_MARKMODIFIED));
    }


    public void setToTemplateActions(Collection<WorkflowActionTemplate> value)
    {
        setToTemplateActions(getSession().getSessionContext(), value);
    }


    public void addToToTemplateActions(SessionContext ctx, WorkflowActionTemplate value)
    {
        addLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION_MARKMODIFIED));
    }


    public void addToToTemplateActions(WorkflowActionTemplate value)
    {
        addToToTemplateActions(getSession().getSessionContext(), value);
    }


    public void removeFromToTemplateActions(SessionContext ctx, WorkflowActionTemplate value)
    {
        removeLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION_MARKMODIFIED));
    }


    public void removeFromToTemplateActions(WorkflowActionTemplate value)
    {
        removeFromToTemplateActions(getSession().getSessionContext(), value);
    }


    public abstract WorkflowTemplate getParentWorkflowTemplate(SessionContext paramSessionContext);
}
