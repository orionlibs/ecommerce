package de.hybris.platform.workflow.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedWorkflowManager extends Extension
{
    protected static String WORKFLOWTEMPLATE2PRINCIPALRELATION_SRC_ORDERED = "relation.WorkflowTemplate2PrincipalRelation.source.ordered";
    protected static String WORKFLOWTEMPLATE2PRINCIPALRELATION_TGT_ORDERED = "relation.WorkflowTemplate2PrincipalRelation.target.ordered";
    protected static String WORKFLOWTEMPLATE2PRINCIPALRELATION_MARKMODIFIED = "relation.WorkflowTemplate2PrincipalRelation.markmodified";
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("andConnectionTemplate", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.link.Link", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("active", Item.AttributeMode.INITIAL);
        tmp.put("andConnection", Item.AttributeMode.INITIAL);
        tmp.put("template", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.link.Link", Collections.unmodifiableMap(tmp));
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public Boolean isActive(SessionContext ctx, Link item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedWorkflowConstants.Attributes.WorkflowActionLinkRelation.ACTIVE);
    }


    public Boolean isActive(Link item)
    {
        return isActive(getSession().getSessionContext(), item);
    }


    public boolean isActiveAsPrimitive(SessionContext ctx, Link item)
    {
        Boolean value = isActive(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isActiveAsPrimitive(Link item)
    {
        return isActiveAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setActive(SessionContext ctx, Link item, Boolean value)
    {
        item.setProperty(ctx, GeneratedWorkflowConstants.Attributes.WorkflowActionLinkRelation.ACTIVE, value);
    }


    public void setActive(Link item, Boolean value)
    {
        setActive(getSession().getSessionContext(), item, value);
    }


    public void setActive(SessionContext ctx, Link item, boolean value)
    {
        setActive(ctx, item, Boolean.valueOf(value));
    }


    public void setActive(Link item, boolean value)
    {
        setActive(getSession().getSessionContext(), item, value);
    }


    public Boolean isAndConnection(SessionContext ctx, Link item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedWorkflowConstants.Attributes.WorkflowActionLinkRelation.ANDCONNECTION);
    }


    public Boolean isAndConnection(Link item)
    {
        return isAndConnection(getSession().getSessionContext(), item);
    }


    public boolean isAndConnectionAsPrimitive(SessionContext ctx, Link item)
    {
        Boolean value = isAndConnection(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAndConnectionAsPrimitive(Link item)
    {
        return isAndConnectionAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setAndConnection(SessionContext ctx, Link item, Boolean value)
    {
        item.setProperty(ctx, GeneratedWorkflowConstants.Attributes.WorkflowActionLinkRelation.ANDCONNECTION, value);
    }


    public void setAndConnection(Link item, Boolean value)
    {
        setAndConnection(getSession().getSessionContext(), item, value);
    }


    public void setAndConnection(SessionContext ctx, Link item, boolean value)
    {
        setAndConnection(ctx, item, Boolean.valueOf(value));
    }


    public void setAndConnection(Link item, boolean value)
    {
        setAndConnection(getSession().getSessionContext(), item, value);
    }


    public Boolean isAndConnectionTemplate(SessionContext ctx, Link item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedWorkflowConstants.Attributes.WorkflowActionTemplateLinkTemplateRelation.ANDCONNECTIONTEMPLATE);
    }


    public Boolean isAndConnectionTemplate(Link item)
    {
        return isAndConnectionTemplate(getSession().getSessionContext(), item);
    }


    public boolean isAndConnectionTemplateAsPrimitive(SessionContext ctx, Link item)
    {
        Boolean value = isAndConnectionTemplate(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAndConnectionTemplateAsPrimitive(Link item)
    {
        return isAndConnectionTemplateAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setAndConnectionTemplate(SessionContext ctx, Link item, Boolean value)
    {
        item.setProperty(ctx, GeneratedWorkflowConstants.Attributes.WorkflowActionTemplateLinkTemplateRelation.ANDCONNECTIONTEMPLATE, value);
    }


    public void setAndConnectionTemplate(Link item, Boolean value)
    {
        setAndConnectionTemplate(getSession().getSessionContext(), item, value);
    }


    public void setAndConnectionTemplate(SessionContext ctx, Link item, boolean value)
    {
        setAndConnectionTemplate(ctx, item, Boolean.valueOf(value));
    }


    public void setAndConnectionTemplate(Link item, boolean value)
    {
        setAndConnectionTemplate(getSession().getSessionContext(), item, value);
    }


    public AutomatedWorkflowActionTemplate createAutomatedWorkflowActionTemplate(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWorkflowConstants.TC.AUTOMATEDWORKFLOWACTIONTEMPLATE);
            return (AutomatedWorkflowActionTemplate)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating AutomatedWorkflowActionTemplate : " + e.getMessage(), 0);
        }
    }


    public AutomatedWorkflowActionTemplate createAutomatedWorkflowActionTemplate(Map attributeValues)
    {
        return createAutomatedWorkflowActionTemplate(getSession().getSessionContext(), attributeValues);
    }


    public Workflow createWorkflow(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWorkflowConstants.TC.WORKFLOW);
            return (Workflow)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Workflow : " + e.getMessage(), 0);
        }
    }


    public Workflow createWorkflow(Map attributeValues)
    {
        return createWorkflow(getSession().getSessionContext(), attributeValues);
    }


    public WorkflowAction createWorkflowAction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWorkflowConstants.TC.WORKFLOWACTION);
            return (WorkflowAction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating WorkflowAction : " + e.getMessage(), 0);
        }
    }


    public WorkflowAction createWorkflowAction(Map attributeValues)
    {
        return createWorkflowAction(getSession().getSessionContext(), attributeValues);
    }


    public WorkflowActionComment createWorkflowActionComment(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWorkflowConstants.TC.WORKFLOWACTIONCOMMENT);
            return (WorkflowActionComment)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating WorkflowActionComment : " + e.getMessage(), 0);
        }
    }


    public WorkflowActionComment createWorkflowActionComment(Map attributeValues)
    {
        return createWorkflowActionComment(getSession().getSessionContext(), attributeValues);
    }


    public WorkflowActionTemplate createWorkflowActionTemplate(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWorkflowConstants.TC.WORKFLOWACTIONTEMPLATE);
            return (WorkflowActionTemplate)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating WorkflowActionTemplate : " + e.getMessage(), 0);
        }
    }


    public WorkflowActionTemplate createWorkflowActionTemplate(Map attributeValues)
    {
        return createWorkflowActionTemplate(getSession().getSessionContext(), attributeValues);
    }


    public WorkflowDecision createWorkflowDecision(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWorkflowConstants.TC.WORKFLOWDECISION);
            return (WorkflowDecision)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating WorkflowDecision : " + e.getMessage(), 0);
        }
    }


    public WorkflowDecision createWorkflowDecision(Map attributeValues)
    {
        return createWorkflowDecision(getSession().getSessionContext(), attributeValues);
    }


    public WorkflowDecisionTemplate createWorkflowDecisionTemplate(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWorkflowConstants.TC.WORKFLOWDECISIONTEMPLATE);
            return (WorkflowDecisionTemplate)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating WorkflowDecisionTemplate : " + e.getMessage(), 0);
        }
    }


    public WorkflowDecisionTemplate createWorkflowDecisionTemplate(Map attributeValues)
    {
        return createWorkflowDecisionTemplate(getSession().getSessionContext(), attributeValues);
    }


    public WorkflowItemAttachment createWorkflowItemAttachment(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWorkflowConstants.TC.WORKFLOWITEMATTACHMENT);
            return (WorkflowItemAttachment)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating WorkflowItemAttachment : " + e.getMessage(), 0);
        }
    }


    public WorkflowItemAttachment createWorkflowItemAttachment(Map attributeValues)
    {
        return createWorkflowItemAttachment(getSession().getSessionContext(), attributeValues);
    }


    public WorkflowTemplate createWorkflowTemplate(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWorkflowConstants.TC.WORKFLOWTEMPLATE);
            return (WorkflowTemplate)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating WorkflowTemplate : " + e.getMessage(), 0);
        }
    }


    public WorkflowTemplate createWorkflowTemplate(Map attributeValues)
    {
        return createWorkflowTemplate(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "workflow";
    }


    public String getOwnerName(Link item)
    {
        return getOwnerName(getSession().getSessionContext(), item);
    }


    public Map<Language, String> getAllOwnerName(Link item)
    {
        return getAllOwnerName(getSession().getSessionContext(), item);
    }


    public void setOwnerName(Link item, String value)
    {
        setOwnerName(getSession().getSessionContext(), item, value);
    }


    public void setAllOwnerName(Link item, Map<Language, String> value)
    {
        setAllOwnerName(getSession().getSessionContext(), item, value);
    }


    public Link getTemplate(SessionContext ctx, Link item)
    {
        return (Link)item.getProperty(ctx, GeneratedWorkflowConstants.Attributes.WorkflowActionLinkRelation.TEMPLATE);
    }


    public Link getTemplate(Link item)
    {
        return getTemplate(getSession().getSessionContext(), item);
    }


    protected void setTemplate(SessionContext ctx, Link item, Link value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute '" + GeneratedWorkflowConstants.Attributes.WorkflowActionLinkRelation.TEMPLATE + "' is not changeable", 0);
        }
        item.setProperty(ctx, GeneratedWorkflowConstants.Attributes.WorkflowActionLinkRelation.TEMPLATE, value);
    }


    protected void setTemplate(Link item, Link value)
    {
        setTemplate(getSession().getSessionContext(), item, value);
    }


    public Collection<WorkflowTemplate> getVisibleTemplates(SessionContext ctx, Principal item)
    {
        List<WorkflowTemplate> items = item.getLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWTEMPLATE2PRINCIPALRELATION, "WorkflowTemplate", null, false, false);
        return items;
    }


    public Collection<WorkflowTemplate> getVisibleTemplates(Principal item)
    {
        return getVisibleTemplates(getSession().getSessionContext(), item);
    }


    public long getVisibleTemplatesCount(SessionContext ctx, Principal item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWTEMPLATE2PRINCIPALRELATION, "WorkflowTemplate", null);
    }


    public long getVisibleTemplatesCount(Principal item)
    {
        return getVisibleTemplatesCount(getSession().getSessionContext(), item);
    }


    public void setVisibleTemplates(SessionContext ctx, Principal item, Collection<WorkflowTemplate> value)
    {
        item.setLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWTEMPLATE2PRINCIPALRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWTEMPLATE2PRINCIPALRELATION_MARKMODIFIED));
    }


    public void setVisibleTemplates(Principal item, Collection<WorkflowTemplate> value)
    {
        setVisibleTemplates(getSession().getSessionContext(), item, value);
    }


    public void addToVisibleTemplates(SessionContext ctx, Principal item, WorkflowTemplate value)
    {
        item.addLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWTEMPLATE2PRINCIPALRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWTEMPLATE2PRINCIPALRELATION_MARKMODIFIED));
    }


    public void addToVisibleTemplates(Principal item, WorkflowTemplate value)
    {
        addToVisibleTemplates(getSession().getSessionContext(), item, value);
    }


    public void removeFromVisibleTemplates(SessionContext ctx, Principal item, WorkflowTemplate value)
    {
        item.removeLinkedItems(ctx, false, GeneratedWorkflowConstants.Relations.WORKFLOWTEMPLATE2PRINCIPALRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWTEMPLATE2PRINCIPALRELATION_MARKMODIFIED));
    }


    public void removeFromVisibleTemplates(Principal item, WorkflowTemplate value)
    {
        removeFromVisibleTemplates(getSession().getSessionContext(), item, value);
    }


    public abstract String getOwnerName(SessionContext paramSessionContext, Link paramLink);


    public abstract Map<Language, String> getAllOwnerName(SessionContext paramSessionContext, Link paramLink);


    public abstract void setOwnerName(SessionContext paramSessionContext, Link paramLink, String paramString);


    public abstract void setAllOwnerName(SessionContext paramSessionContext, Link paramLink, Map<Language, String> paramMap);
}
