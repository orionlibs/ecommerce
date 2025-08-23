package de.hybris.platform.workflow.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedWorkflowActionTemplate extends AbstractWorkflowAction
{
    public static final String INCOMINGLINKTEMPLATES = "incomingLinkTemplates";
    public static final String INCOMINGLINKTEMPLATESSTR = "incomingLinkTemplatesStr";
    public static final String CREATIONTYPE = "creationType";
    public static final String WORKFLOWPOS = "workflowPOS";
    public static final String WORKFLOW = "workflow";
    public static final String DECISIONTEMPLATES = "decisionTemplates";
    public static final String INCOMINGTEMPLATEDECISIONS = "incomingTemplateDecisions";
    protected static String WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION_SRC_ORDERED = "relation.WorkflowActionTemplateLinkTemplateRelation.source.ordered";
    protected static String WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION_TGT_ORDERED = "relation.WorkflowActionTemplateLinkTemplateRelation.target.ordered";
    protected static String WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION_MARKMODIFIED = "relation.WorkflowActionTemplateLinkTemplateRelation.markmodified";
    protected static final BidirectionalOneToManyHandler<GeneratedWorkflowActionTemplate> WORKFLOWHANDLER = new BidirectionalOneToManyHandler(GeneratedWorkflowConstants.TC.WORKFLOWACTIONTEMPLATE, false, "workflow", "workflowPOS", true, true, 2);
    protected static final OneToManyHandler<WorkflowDecisionTemplate> DECISIONTEMPLATESHANDLER = new OneToManyHandler(GeneratedWorkflowConstants.TC.WORKFLOWDECISIONTEMPLATE, true, "actionTemplate", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractWorkflowAction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("workflowPOS", Item.AttributeMode.INITIAL);
        tmp.put("workflow", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        WORKFLOWHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public ComposedType getCreationType()
    {
        return getCreationType(getSession().getSessionContext());
    }


    public void setCreationType(ComposedType value)
    {
        setCreationType(getSession().getSessionContext(), value);
    }


    public Collection<WorkflowDecisionTemplate> getDecisionTemplates(SessionContext ctx)
    {
        return DECISIONTEMPLATESHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<WorkflowDecisionTemplate> getDecisionTemplates()
    {
        return getDecisionTemplates(getSession().getSessionContext());
    }


    public void setDecisionTemplates(SessionContext ctx, Collection<WorkflowDecisionTemplate> value)
    {
        DECISIONTEMPLATESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setDecisionTemplates(Collection<WorkflowDecisionTemplate> value)
    {
        setDecisionTemplates(getSession().getSessionContext(), value);
    }


    public void addToDecisionTemplates(SessionContext ctx, WorkflowDecisionTemplate value)
    {
        DECISIONTEMPLATESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToDecisionTemplates(WorkflowDecisionTemplate value)
    {
        addToDecisionTemplates(getSession().getSessionContext(), value);
    }


    public void removeFromDecisionTemplates(SessionContext ctx, WorkflowDecisionTemplate value)
    {
        DECISIONTEMPLATESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromDecisionTemplates(WorkflowDecisionTemplate value)
    {
        removeFromDecisionTemplates(getSession().getSessionContext(), value);
    }


    public List<Link> getIncomingLinkTemplates()
    {
        return getIncomingLinkTemplates(getSession().getSessionContext());
    }


    public String getIncomingLinkTemplatesStr()
    {
        return getIncomingLinkTemplatesStr(getSession().getSessionContext());
    }


    public Collection<WorkflowDecisionTemplate> getIncomingTemplateDecisions(SessionContext ctx)
    {
        List<WorkflowDecisionTemplate> items = getLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION, "WorkflowDecisionTemplate", null, false, false);
        return items;
    }


    public Collection<WorkflowDecisionTemplate> getIncomingTemplateDecisions()
    {
        return getIncomingTemplateDecisions(getSession().getSessionContext());
    }


    public long getIncomingTemplateDecisionsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION, "WorkflowDecisionTemplate", null);
    }


    public long getIncomingTemplateDecisionsCount()
    {
        return getIncomingTemplateDecisionsCount(getSession().getSessionContext());
    }


    public void setIncomingTemplateDecisions(SessionContext ctx, Collection<WorkflowDecisionTemplate> value)
    {
        setLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION_MARKMODIFIED));
    }


    public void setIncomingTemplateDecisions(Collection<WorkflowDecisionTemplate> value)
    {
        setIncomingTemplateDecisions(getSession().getSessionContext(), value);
    }


    public void addToIncomingTemplateDecisions(SessionContext ctx, WorkflowDecisionTemplate value)
    {
        addLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION_MARKMODIFIED));
    }


    public void addToIncomingTemplateDecisions(WorkflowDecisionTemplate value)
    {
        addToIncomingTemplateDecisions(getSession().getSessionContext(), value);
    }


    public void removeFromIncomingTemplateDecisions(SessionContext ctx, WorkflowDecisionTemplate value)
    {
        removeLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION_MARKMODIFIED));
    }


    public void removeFromIncomingTemplateDecisions(WorkflowDecisionTemplate value)
    {
        removeFromIncomingTemplateDecisions(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("WorkflowDecisionTemplate");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION_MARKMODIFIED);
        }
        return true;
    }


    public WorkflowTemplate getWorkflow(SessionContext ctx)
    {
        return (WorkflowTemplate)getProperty(ctx, "workflow");
    }


    public WorkflowTemplate getWorkflow()
    {
        return getWorkflow(getSession().getSessionContext());
    }


    protected void setWorkflow(SessionContext ctx, WorkflowTemplate value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'workflow' is not changeable", 0);
        }
        WORKFLOWHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setWorkflow(WorkflowTemplate value)
    {
        setWorkflow(getSession().getSessionContext(), value);
    }


    Integer getWorkflowPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "workflowPOS");
    }


    Integer getWorkflowPOS()
    {
        return getWorkflowPOS(getSession().getSessionContext());
    }


    int getWorkflowPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getWorkflowPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getWorkflowPOSAsPrimitive()
    {
        return getWorkflowPOSAsPrimitive(getSession().getSessionContext());
    }


    void setWorkflowPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "workflowPOS", value);
    }


    void setWorkflowPOS(Integer value)
    {
        setWorkflowPOS(getSession().getSessionContext(), value);
    }


    void setWorkflowPOS(SessionContext ctx, int value)
    {
        setWorkflowPOS(ctx, Integer.valueOf(value));
    }


    void setWorkflowPOS(int value)
    {
        setWorkflowPOS(getSession().getSessionContext(), value);
    }


    public abstract ComposedType getCreationType(SessionContext paramSessionContext);


    public abstract void setCreationType(SessionContext paramSessionContext, ComposedType paramComposedType);


    public abstract List<Link> getIncomingLinkTemplates(SessionContext paramSessionContext);


    public abstract String getIncomingLinkTemplatesStr(SessionContext paramSessionContext);
}
