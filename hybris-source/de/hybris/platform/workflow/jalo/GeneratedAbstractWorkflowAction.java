package de.hybris.platform.workflow.jalo;

import de.hybris.platform.commons.jalo.renderer.RendererTemplate;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedAbstractWorkflowAction extends GenericItem
{
    public static final String ACTIONTYPE = "actionType";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PRINCIPALASSIGNED = "principalAssigned";
    public static final String SENDEMAIL = "sendEmail";
    public static final String EMAILADDRESS = "emailAddress";
    public static final String PREDECESSORSSTR = "predecessorsStr";
    public static final String RENDERERTEMPLATE = "rendererTemplate";
    public static final String PREDECESSORS = "predecessors";
    protected static String WORKFLOWACTIONORDERINGRELATION_SRC_ORDERED = "relation.WorkflowActionOrderingRelation.source.ordered";
    protected static String WORKFLOWACTIONORDERINGRELATION_TGT_ORDERED = "relation.WorkflowActionOrderingRelation.target.ordered";
    protected static String WORKFLOWACTIONORDERINGRELATION_MARKMODIFIED = "relation.WorkflowActionOrderingRelation.markmodified";
    public static final String SUCCESSORS = "successors";
    public static final String WORKFLOWACTIONCOMMENTS = "workflowActionComments";
    protected static final OneToManyHandler<WorkflowActionComment> WORKFLOWACTIONCOMMENTSHANDLER = new OneToManyHandler(GeneratedWorkflowConstants.TC.WORKFLOWACTIONCOMMENT, true, "workflowAction", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("actionType", Item.AttributeMode.INITIAL);
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("principalAssigned", Item.AttributeMode.INITIAL);
        tmp.put("sendEmail", Item.AttributeMode.INITIAL);
        tmp.put("emailAddress", Item.AttributeMode.INITIAL);
        tmp.put("rendererTemplate", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getActionType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "actionType");
    }


    public EnumerationValue getActionType()
    {
        return getActionType(getSession().getSessionContext());
    }


    public void setActionType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "actionType", value);
    }


    public void setActionType(EnumerationValue value)
    {
        setActionType(getSession().getSessionContext(), value);
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


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractWorkflowAction.getDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "description", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllDescription()
    {
        return getAllDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractWorkflowAction.setDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public void setAllDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "description", value);
    }


    public void setAllDescription(Map<Language, String> value)
    {
        setAllDescription(getSession().getSessionContext(), value);
    }


    public String getEmailAddress(SessionContext ctx)
    {
        return (String)getProperty(ctx, "emailAddress");
    }


    public String getEmailAddress()
    {
        return getEmailAddress(getSession().getSessionContext());
    }


    public void setEmailAddress(SessionContext ctx, String value)
    {
        setProperty(ctx, "emailAddress", value);
    }


    public void setEmailAddress(String value)
    {
        setEmailAddress(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("AbstractWorkflowAction");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(WORKFLOWACTIONORDERINGRELATION_MARKMODIFIED);
        }
        return true;
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractWorkflowAction.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedAbstractWorkflowAction.setName requires a session language", 0);
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


    public List<AbstractWorkflowAction> getPredecessors(SessionContext ctx)
    {
        List<AbstractWorkflowAction> items = getLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONORDERINGRELATION, "AbstractWorkflowAction", null,
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONORDERINGRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONORDERINGRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<AbstractWorkflowAction> getPredecessors()
    {
        return getPredecessors(getSession().getSessionContext());
    }


    public long getPredecessorsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONORDERINGRELATION, "AbstractWorkflowAction", null);
    }


    public long getPredecessorsCount()
    {
        return getPredecessorsCount(getSession().getSessionContext());
    }


    public void setPredecessors(SessionContext ctx, List<AbstractWorkflowAction> value)
    {
        setLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONORDERINGRELATION, null, value,
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONORDERINGRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONORDERINGRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONORDERINGRELATION_MARKMODIFIED));
    }


    public void setPredecessors(List<AbstractWorkflowAction> value)
    {
        setPredecessors(getSession().getSessionContext(), value);
    }


    public void addToPredecessors(SessionContext ctx, AbstractWorkflowAction value)
    {
        addLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONORDERINGRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONORDERINGRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONORDERINGRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONORDERINGRELATION_MARKMODIFIED));
    }


    public void addToPredecessors(AbstractWorkflowAction value)
    {
        addToPredecessors(getSession().getSessionContext(), value);
    }


    public void removeFromPredecessors(SessionContext ctx, AbstractWorkflowAction value)
    {
        removeLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONORDERINGRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONORDERINGRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONORDERINGRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONORDERINGRELATION_MARKMODIFIED));
    }


    public void removeFromPredecessors(AbstractWorkflowAction value)
    {
        removeFromPredecessors(getSession().getSessionContext(), value);
    }


    public String getPredecessorsStr()
    {
        return getPredecessorsStr(getSession().getSessionContext());
    }


    public Principal getPrincipalAssigned(SessionContext ctx)
    {
        return (Principal)getProperty(ctx, "principalAssigned");
    }


    public Principal getPrincipalAssigned()
    {
        return getPrincipalAssigned(getSession().getSessionContext());
    }


    public void setPrincipalAssigned(SessionContext ctx, Principal value)
    {
        setProperty(ctx, "principalAssigned", value);
    }


    public void setPrincipalAssigned(Principal value)
    {
        setPrincipalAssigned(getSession().getSessionContext(), value);
    }


    public RendererTemplate getRendererTemplate(SessionContext ctx)
    {
        return (RendererTemplate)getProperty(ctx, "rendererTemplate");
    }


    public RendererTemplate getRendererTemplate()
    {
        return getRendererTemplate(getSession().getSessionContext());
    }


    public void setRendererTemplate(SessionContext ctx, RendererTemplate value)
    {
        setProperty(ctx, "rendererTemplate", value);
    }


    public void setRendererTemplate(RendererTemplate value)
    {
        setRendererTemplate(getSession().getSessionContext(), value);
    }


    public Boolean isSendEmail(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "sendEmail");
    }


    public Boolean isSendEmail()
    {
        return isSendEmail(getSession().getSessionContext());
    }


    public boolean isSendEmailAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSendEmail(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSendEmailAsPrimitive()
    {
        return isSendEmailAsPrimitive(getSession().getSessionContext());
    }


    public void setSendEmail(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "sendEmail", value);
    }


    public void setSendEmail(Boolean value)
    {
        setSendEmail(getSession().getSessionContext(), value);
    }


    public void setSendEmail(SessionContext ctx, boolean value)
    {
        setSendEmail(ctx, Boolean.valueOf(value));
    }


    public void setSendEmail(boolean value)
    {
        setSendEmail(getSession().getSessionContext(), value);
    }


    public List<AbstractWorkflowAction> getSuccessors(SessionContext ctx)
    {
        List<AbstractWorkflowAction> items = getLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONORDERINGRELATION, "AbstractWorkflowAction", null,
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONORDERINGRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONORDERINGRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<AbstractWorkflowAction> getSuccessors()
    {
        return getSuccessors(getSession().getSessionContext());
    }


    public long getSuccessorsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONORDERINGRELATION, "AbstractWorkflowAction", null);
    }


    public long getSuccessorsCount()
    {
        return getSuccessorsCount(getSession().getSessionContext());
    }


    public void setSuccessors(SessionContext ctx, List<AbstractWorkflowAction> value)
    {
        setLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONORDERINGRELATION, null, value,
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONORDERINGRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONORDERINGRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONORDERINGRELATION_MARKMODIFIED));
    }


    public void setSuccessors(List<AbstractWorkflowAction> value)
    {
        setSuccessors(getSession().getSessionContext(), value);
    }


    public void addToSuccessors(SessionContext ctx, AbstractWorkflowAction value)
    {
        addLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONORDERINGRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONORDERINGRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONORDERINGRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONORDERINGRELATION_MARKMODIFIED));
    }


    public void addToSuccessors(AbstractWorkflowAction value)
    {
        addToSuccessors(getSession().getSessionContext(), value);
    }


    public void removeFromSuccessors(SessionContext ctx, AbstractWorkflowAction value)
    {
        removeLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWACTIONORDERINGRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONORDERINGRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(WORKFLOWACTIONORDERINGRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(WORKFLOWACTIONORDERINGRELATION_MARKMODIFIED));
    }


    public void removeFromSuccessors(AbstractWorkflowAction value)
    {
        removeFromSuccessors(getSession().getSessionContext(), value);
    }


    public Collection<WorkflowActionComment> getWorkflowActionComments(SessionContext ctx)
    {
        return WORKFLOWACTIONCOMMENTSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<WorkflowActionComment> getWorkflowActionComments()
    {
        return getWorkflowActionComments(getSession().getSessionContext());
    }


    public void setWorkflowActionComments(SessionContext ctx, Collection<WorkflowActionComment> value)
    {
        WORKFLOWACTIONCOMMENTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setWorkflowActionComments(Collection<WorkflowActionComment> value)
    {
        setWorkflowActionComments(getSession().getSessionContext(), value);
    }


    public void addToWorkflowActionComments(SessionContext ctx, WorkflowActionComment value)
    {
        WORKFLOWACTIONCOMMENTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToWorkflowActionComments(WorkflowActionComment value)
    {
        addToWorkflowActionComments(getSession().getSessionContext(), value);
    }


    public void removeFromWorkflowActionComments(SessionContext ctx, WorkflowActionComment value)
    {
        WORKFLOWACTIONCOMMENTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromWorkflowActionComments(WorkflowActionComment value)
    {
        removeFromWorkflowActionComments(getSession().getSessionContext(), value);
    }


    public abstract String getPredecessorsStr(SessionContext paramSessionContext);
}
