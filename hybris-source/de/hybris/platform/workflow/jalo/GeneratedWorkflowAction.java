package de.hybris.platform.workflow.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedWorkflowAction extends AbstractWorkflowAction
{
    public static final String INCOMINGLINKS = "incomingLinks";
    public static final String INCOMINGLINKSSTR = "incomingLinksStr";
    public static final String SELECTEDDECISION = "selectedDecision";
    public static final String FIRSTACTIVATED = "firstActivated";
    public static final String ACTIVATED = "activated";
    public static final String COMMENT = "comment";
    public static final String STATUS = "status";
    public static final String TEMPLATE = "template";
    public static final String ATTACHMENTITEMS = "attachmentItems";
    public static final String WORKFLOWPOS = "workflowPOS";
    public static final String WORKFLOW = "workflow";
    public static final String DECISIONS = "decisions";
    public static final String INCOMINGDECISIONS = "incomingDecisions";
    protected static String WORKFLOWACTIONLINKRELATION_SRC_ORDERED = "relation.WorkflowActionLinkRelation.source.ordered";
    protected static String WORKFLOWACTIONLINKRELATION_TGT_ORDERED = "relation.WorkflowActionLinkRelation.target.ordered";
    protected static String WORKFLOWACTIONLINKRELATION_MARKMODIFIED = "relation.WorkflowActionLinkRelation.markmodified";
    public static final String ATTACHMENTS = "attachments";
    protected static String WORKFLOWACTIONITEMATTACHMENTRELATION_SRC_ORDERED = "relation.WorkflowActionItemAttachmentRelation.source.ordered";
    protected static String WORKFLOWACTIONITEMATTACHMENTRELATION_TGT_ORDERED = "relation.WorkflowActionItemAttachmentRelation.target.ordered";
    protected static String WORKFLOWACTIONITEMATTACHMENTRELATION_MARKMODIFIED = "relation.WorkflowActionItemAttachmentRelation.markmodified";
    protected static final BidirectionalOneToManyHandler<GeneratedWorkflowAction> WORKFLOWHANDLER = new BidirectionalOneToManyHandler(GeneratedWorkflowConstants.TC.WORKFLOWACTION, false, "workflow", "workflowPOS", true, true, 2);
    protected static final OneToManyHandler<WorkflowDecision> DECISIONSHANDLER = new OneToManyHandler(GeneratedWorkflowConstants.TC.WORKFLOWDECISION, true, "action", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractWorkflowAction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("selectedDecision", Item.AttributeMode.INITIAL);
        tmp.put("firstActivated", Item.AttributeMode.INITIAL);
        tmp.put("activated", Item.AttributeMode.INITIAL);
        tmp.put("comment", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("template", Item.AttributeMode.INITIAL);
        tmp.put("workflowPOS", Item.AttributeMode.INITIAL);
        tmp.put("workflow", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Date getActivated(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "activated");
    }


    public Date getActivated()
    {
        return getActivated(getSession().getSessionContext());
    }


    public void setActivated(SessionContext ctx, Date value)
    {
        setProperty(ctx, "activated", value);
    }


    public void setActivated(Date value)
    {
        setActivated(getSession().getSessionContext(), value);
    }


    public List<Item> getAttachmentItems()
    {
        return getAttachmentItems(getSession().getSessionContext());
    }


    public List<WorkflowItemAttachment> getAttachments(SessionContext ctx)
    {
        List<WorkflowItemAttachment> items = getLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONITEMATTACHMENTRELATION, "WorkflowItemAttachment", null,
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONITEMATTACHMENTRELATION_SRC_ORDERED, true), false);
        return items;
    }


    public List<WorkflowItemAttachment> getAttachments()
    {
        return getAttachments(getSession().getSessionContext());
    }


    public long getAttachmentsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONITEMATTACHMENTRELATION, "WorkflowItemAttachment", null);
    }


    public long getAttachmentsCount()
    {
        return getAttachmentsCount(getSession().getSessionContext());
    }


    public void setAttachments(SessionContext ctx, List<WorkflowItemAttachment> value)
    {
        setLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONITEMATTACHMENTRELATION, null, value,
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONITEMATTACHMENTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONITEMATTACHMENTRELATION_MARKMODIFIED));
    }


    public void setAttachments(List<WorkflowItemAttachment> value)
    {
        setAttachments(getSession().getSessionContext(), value);
    }


    public void addToAttachments(SessionContext ctx, WorkflowItemAttachment value)
    {
        addLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONITEMATTACHMENTRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONITEMATTACHMENTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONITEMATTACHMENTRELATION_MARKMODIFIED));
    }


    public void addToAttachments(WorkflowItemAttachment value)
    {
        addToAttachments(getSession().getSessionContext(), value);
    }


    public void removeFromAttachments(SessionContext ctx, WorkflowItemAttachment value)
    {
        removeLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONITEMATTACHMENTRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONITEMATTACHMENTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONITEMATTACHMENTRELATION_MARKMODIFIED));
    }


    public void removeFromAttachments(WorkflowItemAttachment value)
    {
        removeFromAttachments(getSession().getSessionContext(), value);
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
        WORKFLOWHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Collection<WorkflowDecision> getDecisions(SessionContext ctx)
    {
        return DECISIONSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<WorkflowDecision> getDecisions()
    {
        return getDecisions(getSession().getSessionContext());
    }


    public void setDecisions(SessionContext ctx, Collection<WorkflowDecision> value)
    {
        DECISIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setDecisions(Collection<WorkflowDecision> value)
    {
        setDecisions(getSession().getSessionContext(), value);
    }


    public void addToDecisions(SessionContext ctx, WorkflowDecision value)
    {
        DECISIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToDecisions(WorkflowDecision value)
    {
        addToDecisions(getSession().getSessionContext(), value);
    }


    public void removeFromDecisions(SessionContext ctx, WorkflowDecision value)
    {
        DECISIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromDecisions(WorkflowDecision value)
    {
        removeFromDecisions(getSession().getSessionContext(), value);
    }


    public Date getFirstActivated(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "firstActivated");
    }


    public Date getFirstActivated()
    {
        return getFirstActivated(getSession().getSessionContext());
    }


    public void setFirstActivated(SessionContext ctx, Date value)
    {
        setProperty(ctx, "firstActivated", value);
    }


    public void setFirstActivated(Date value)
    {
        setFirstActivated(getSession().getSessionContext(), value);
    }


    public Collection<WorkflowDecision> getIncomingDecisions(SessionContext ctx)
    {
        List<WorkflowDecision> items = getLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONLINKRELATION, "WorkflowDecision", null, false, false);
        return items;
    }


    public Collection<WorkflowDecision> getIncomingDecisions()
    {
        return getIncomingDecisions(getSession().getSessionContext());
    }


    public long getIncomingDecisionsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONLINKRELATION, "WorkflowDecision", null);
    }


    public long getIncomingDecisionsCount()
    {
        return getIncomingDecisionsCount(getSession().getSessionContext());
    }


    public void setIncomingDecisions(SessionContext ctx, Collection<WorkflowDecision> value)
    {
        setLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONLINKRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONLINKRELATION_MARKMODIFIED));
    }


    public void setIncomingDecisions(Collection<WorkflowDecision> value)
    {
        setIncomingDecisions(getSession().getSessionContext(), value);
    }


    public void addToIncomingDecisions(SessionContext ctx, WorkflowDecision value)
    {
        addLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONLINKRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONLINKRELATION_MARKMODIFIED));
    }


    public void addToIncomingDecisions(WorkflowDecision value)
    {
        addToIncomingDecisions(getSession().getSessionContext(), value);
    }


    public void removeFromIncomingDecisions(SessionContext ctx, WorkflowDecision value)
    {
        removeLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONLINKRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONLINKRELATION_MARKMODIFIED));
    }


    public void removeFromIncomingDecisions(WorkflowDecision value)
    {
        removeFromIncomingDecisions(getSession().getSessionContext(), value);
    }


    public List<Link> getIncomingLinks()
    {
        return getIncomingLinks(getSession().getSessionContext());
    }


    public String getIncomingLinksStr()
    {
        return getIncomingLinksStr(getSession().getSessionContext());
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("WorkflowDecision");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(WORKFLOWACTIONLINKRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("WorkflowItemAttachment");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(WORKFLOWACTIONITEMATTACHMENTRELATION_MARKMODIFIED);
        }
        return true;
    }


    public WorkflowDecision getSelectedDecision(SessionContext ctx)
    {
        return (WorkflowDecision)getProperty(ctx, "selectedDecision");
    }


    public WorkflowDecision getSelectedDecision()
    {
        return getSelectedDecision(getSession().getSessionContext());
    }


    public void setSelectedDecision(SessionContext ctx, WorkflowDecision value)
    {
        setProperty(ctx, "selectedDecision", value);
    }


    public void setSelectedDecision(WorkflowDecision value)
    {
        setSelectedDecision(getSession().getSessionContext(), value);
    }


    public EnumerationValue getStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "status");
    }


    public EnumerationValue getStatus()
    {
        return getStatus(getSession().getSessionContext());
    }


    public void setStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "status", value);
    }


    public void setStatus(EnumerationValue value)
    {
        setStatus(getSession().getSessionContext(), value);
    }


    public WorkflowActionTemplate getTemplate(SessionContext ctx)
    {
        return (WorkflowActionTemplate)getProperty(ctx, "template");
    }


    public WorkflowActionTemplate getTemplate()
    {
        return getTemplate(getSession().getSessionContext());
    }


    protected void setTemplate(SessionContext ctx, WorkflowActionTemplate value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'template' is not changeable", 0);
        }
        setProperty(ctx, "template", value);
    }


    protected void setTemplate(WorkflowActionTemplate value)
    {
        setTemplate(getSession().getSessionContext(), value);
    }


    public Workflow getWorkflow(SessionContext ctx)
    {
        return (Workflow)getProperty(ctx, "workflow");
    }


    public Workflow getWorkflow()
    {
        return getWorkflow(getSession().getSessionContext());
    }


    protected void setWorkflow(SessionContext ctx, Workflow value)
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


    protected void setWorkflow(Workflow value)
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


    public abstract List<Item> getAttachmentItems(SessionContext paramSessionContext);


    public abstract List<Link> getIncomingLinks(SessionContext paramSessionContext);


    public abstract String getIncomingLinksStr(SessionContext paramSessionContext);
}
