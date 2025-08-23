package de.hybris.platform.workflow.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
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

public abstract class GeneratedWorkflowItemAttachment extends GenericItem
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String COMMENT = "comment";
    public static final String ITEM = "item";
    public static final String TYPEOFITEM = "typeOfItem";
    public static final String ACTIONSTR = "actionStr";
    public static final String WORKFLOWPOS = "workflowPOS";
    public static final String WORKFLOW = "workflow";
    public static final String ACTIONS = "actions";
    protected static String WORKFLOWACTIONITEMATTACHMENTRELATION_SRC_ORDERED = "relation.WorkflowActionItemAttachmentRelation.source.ordered";
    protected static String WORKFLOWACTIONITEMATTACHMENTRELATION_TGT_ORDERED = "relation.WorkflowActionItemAttachmentRelation.target.ordered";
    protected static String WORKFLOWACTIONITEMATTACHMENTRELATION_MARKMODIFIED = "relation.WorkflowActionItemAttachmentRelation.markmodified";
    protected static final BidirectionalOneToManyHandler<GeneratedWorkflowItemAttachment> WORKFLOWHANDLER = new BidirectionalOneToManyHandler(GeneratedWorkflowConstants.TC.WORKFLOWITEMATTACHMENT, false, "workflow", "workflowPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("comment", Item.AttributeMode.INITIAL);
        tmp.put("item", Item.AttributeMode.INITIAL);
        tmp.put("typeOfItem", Item.AttributeMode.INITIAL);
        tmp.put("workflowPOS", Item.AttributeMode.INITIAL);
        tmp.put("workflow", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<WorkflowAction> getActions(SessionContext ctx)
    {
        List<WorkflowAction> items = getLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONITEMATTACHMENTRELATION, "WorkflowAction", null,
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONITEMATTACHMENTRELATION_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<WorkflowAction> getActions()
    {
        return getActions(getSession().getSessionContext());
    }


    public long getActionsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONITEMATTACHMENTRELATION, "WorkflowAction", null);
    }


    public long getActionsCount()
    {
        return getActionsCount(getSession().getSessionContext());
    }


    public void setActions(SessionContext ctx, Collection<WorkflowAction> value)
    {
        setLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONITEMATTACHMENTRELATION, null, value,
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONITEMATTACHMENTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONITEMATTACHMENTRELATION_MARKMODIFIED));
    }


    public void setActions(Collection<WorkflowAction> value)
    {
        setActions(getSession().getSessionContext(), value);
    }


    public void addToActions(SessionContext ctx, WorkflowAction value)
    {
        addLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONITEMATTACHMENTRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONITEMATTACHMENTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONITEMATTACHMENTRELATION_MARKMODIFIED));
    }


    public void addToActions(WorkflowAction value)
    {
        addToActions(getSession().getSessionContext(), value);
    }


    public void removeFromActions(SessionContext ctx, WorkflowAction value)
    {
        removeLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONITEMATTACHMENTRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONITEMATTACHMENTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONITEMATTACHMENTRELATION_MARKMODIFIED));
    }


    public void removeFromActions(WorkflowAction value)
    {
        removeFromActions(getSession().getSessionContext(), value);
    }


    public String getActionStr()
    {
        return getActionStr(getSession().getSessionContext());
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    protected void setCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'code' is not changeable", 0);
        }
        setProperty(ctx, "code", value);
    }


    protected void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
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


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("WorkflowAction");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(WORKFLOWACTIONITEMATTACHMENTRELATION_MARKMODIFIED);
        }
        return true;
    }


    public Item getItem(SessionContext ctx)
    {
        return (Item)getProperty(ctx, "item");
    }


    public Item getItem()
    {
        return getItem(getSession().getSessionContext());
    }


    public void setItem(SessionContext ctx, Item value)
    {
        setProperty(ctx, "item", value);
    }


    public void setItem(Item value)
    {
        setItem(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedWorkflowItemAttachment.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedWorkflowItemAttachment.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }


    public ComposedType getTypeOfItem(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "typeOfItem");
    }


    public ComposedType getTypeOfItem()
    {
        return getTypeOfItem(getSession().getSessionContext());
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


    public abstract String getActionStr(SessionContext paramSessionContext);
}
