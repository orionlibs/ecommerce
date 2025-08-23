package de.hybris.platform.workflow.jalo;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedWorkflow extends CronJob
{
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String ACTIONS = "actions";
    public static final String ATTACHMENTS = "attachments";
    protected static final BidirectionalOneToManyHandler<GeneratedWorkflow> JOBHANDLER = new BidirectionalOneToManyHandler(GeneratedWorkflowConstants.TC.WORKFLOWTEMPLATE, false, "job", null, false, true, 0);
    protected static final OneToManyHandler<WorkflowAction> ACTIONSHANDLER = new OneToManyHandler(GeneratedWorkflowConstants.TC.WORKFLOWACTION, true, "workflow", "workflowPOS", true, true, 2);
    protected static final OneToManyHandler<WorkflowItemAttachment> ATTACHMENTSHANDLER = new OneToManyHandler(GeneratedWorkflowConstants.TC.WORKFLOWITEMATTACHMENT, true, "workflow", "workflowPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<WorkflowAction> getActions(SessionContext ctx)
    {
        return (List<WorkflowAction>)ACTIONSHANDLER.getValues(ctx, (Item)this);
    }


    public List<WorkflowAction> getActions()
    {
        return getActions(getSession().getSessionContext());
    }


    public void setActions(SessionContext ctx, List<WorkflowAction> value)
    {
        ACTIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setActions(List<WorkflowAction> value)
    {
        setActions(getSession().getSessionContext(), value);
    }


    public void addToActions(SessionContext ctx, WorkflowAction value)
    {
        ACTIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToActions(WorkflowAction value)
    {
        addToActions(getSession().getSessionContext(), value);
    }


    public void removeFromActions(SessionContext ctx, WorkflowAction value)
    {
        ACTIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromActions(WorkflowAction value)
    {
        removeFromActions(getSession().getSessionContext(), value);
    }


    public List<WorkflowItemAttachment> getAttachments(SessionContext ctx)
    {
        return (List<WorkflowItemAttachment>)ATTACHMENTSHANDLER.getValues(ctx, (Item)this);
    }


    public List<WorkflowItemAttachment> getAttachments()
    {
        return getAttachments(getSession().getSessionContext());
    }


    public void setAttachments(SessionContext ctx, List<WorkflowItemAttachment> value)
    {
        ATTACHMENTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setAttachments(List<WorkflowItemAttachment> value)
    {
        setAttachments(getSession().getSessionContext(), value);
    }


    public void addToAttachments(SessionContext ctx, WorkflowItemAttachment value)
    {
        ATTACHMENTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToAttachments(WorkflowItemAttachment value)
    {
        addToAttachments(getSession().getSessionContext(), value);
    }


    public void removeFromAttachments(SessionContext ctx, WorkflowItemAttachment value)
    {
        ATTACHMENTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromAttachments(WorkflowItemAttachment value)
    {
        removeFromAttachments(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        JOBHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedWorkflow.getDescription requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedWorkflow.setDescription requires a session language", 0);
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


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedWorkflow.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedWorkflow.setName requires a session language", 0);
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
}
