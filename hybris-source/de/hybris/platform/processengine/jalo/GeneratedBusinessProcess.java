package de.hybris.platform.processengine.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.processing.constants.GeneratedProcessingConstants;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedBusinessProcess extends GenericItem
{
    public static final String CODE = "code";
    public static final String PROCESSDEFINITIONNAME = "processDefinitionName";
    public static final String PROCESSDEFINITIONVERSION = "processDefinitionVersion";
    public static final String STATE = "state";
    public static final String ENDMESSAGE = "endMessage";
    public static final String USER = "user";
    public static final String CURRENTTASKS = "currentTasks";
    public static final String CONTEXTPARAMETERS = "contextParameters";
    public static final String TASKLOGS = "taskLogs";
    protected static final OneToManyHandler<ProcessTask> CURRENTTASKSHANDLER = new OneToManyHandler(GeneratedProcessingConstants.TC.PROCESSTASK, false, "process", null, false, true, 0);
    protected static final OneToManyHandler<BusinessProcessParameter> CONTEXTPARAMETERSHANDLER = new OneToManyHandler(GeneratedProcessingConstants.TC.BUSINESSPROCESSPARAMETER, true, "process", null, false, true, 0);
    protected static final OneToManyHandler<ProcessTaskLog> TASKLOGSHANDLER = new OneToManyHandler(GeneratedProcessingConstants.TC.PROCESSTASKLOG, true, "process", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("processDefinitionName", Item.AttributeMode.INITIAL);
        tmp.put("processDefinitionVersion", Item.AttributeMode.INITIAL);
        tmp.put("state", Item.AttributeMode.INITIAL);
        tmp.put("endMessage", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
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


    public Collection<BusinessProcessParameter> getContextParameters(SessionContext ctx)
    {
        return CONTEXTPARAMETERSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<BusinessProcessParameter> getContextParameters()
    {
        return getContextParameters(getSession().getSessionContext());
    }


    public void setContextParameters(SessionContext ctx, Collection<BusinessProcessParameter> value)
    {
        CONTEXTPARAMETERSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setContextParameters(Collection<BusinessProcessParameter> value)
    {
        setContextParameters(getSession().getSessionContext(), value);
    }


    public void addToContextParameters(SessionContext ctx, BusinessProcessParameter value)
    {
        CONTEXTPARAMETERSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToContextParameters(BusinessProcessParameter value)
    {
        addToContextParameters(getSession().getSessionContext(), value);
    }


    public void removeFromContextParameters(SessionContext ctx, BusinessProcessParameter value)
    {
        CONTEXTPARAMETERSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromContextParameters(BusinessProcessParameter value)
    {
        removeFromContextParameters(getSession().getSessionContext(), value);
    }


    public Collection<ProcessTask> getCurrentTasks(SessionContext ctx)
    {
        return CURRENTTASKSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<ProcessTask> getCurrentTasks()
    {
        return getCurrentTasks(getSession().getSessionContext());
    }


    public void setCurrentTasks(SessionContext ctx, Collection<ProcessTask> value)
    {
        CURRENTTASKSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setCurrentTasks(Collection<ProcessTask> value)
    {
        setCurrentTasks(getSession().getSessionContext(), value);
    }


    public void addToCurrentTasks(SessionContext ctx, ProcessTask value)
    {
        CURRENTTASKSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToCurrentTasks(ProcessTask value)
    {
        addToCurrentTasks(getSession().getSessionContext(), value);
    }


    public void removeFromCurrentTasks(SessionContext ctx, ProcessTask value)
    {
        CURRENTTASKSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromCurrentTasks(ProcessTask value)
    {
        removeFromCurrentTasks(getSession().getSessionContext(), value);
    }


    public String getEndMessage(SessionContext ctx)
    {
        return (String)getProperty(ctx, "endMessage");
    }


    public String getEndMessage()
    {
        return getEndMessage(getSession().getSessionContext());
    }


    public void setEndMessage(SessionContext ctx, String value)
    {
        setProperty(ctx, "endMessage", value);
    }


    public void setEndMessage(String value)
    {
        setEndMessage(getSession().getSessionContext(), value);
    }


    public String getProcessDefinitionName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "processDefinitionName");
    }


    public String getProcessDefinitionName()
    {
        return getProcessDefinitionName(getSession().getSessionContext());
    }


    protected void setProcessDefinitionName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'processDefinitionName' is not changeable", 0);
        }
        setProperty(ctx, "processDefinitionName", value);
    }


    protected void setProcessDefinitionName(String value)
    {
        setProcessDefinitionName(getSession().getSessionContext(), value);
    }


    public String getProcessDefinitionVersion(SessionContext ctx)
    {
        return (String)getProperty(ctx, "processDefinitionVersion");
    }


    public String getProcessDefinitionVersion()
    {
        return getProcessDefinitionVersion(getSession().getSessionContext());
    }


    public void setProcessDefinitionVersion(SessionContext ctx, String value)
    {
        setProperty(ctx, "processDefinitionVersion", value);
    }


    public void setProcessDefinitionVersion(String value)
    {
        setProcessDefinitionVersion(getSession().getSessionContext(), value);
    }


    public EnumerationValue getState(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "state");
    }


    public EnumerationValue getState()
    {
        return getState(getSession().getSessionContext());
    }


    public void setState(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "state", value);
    }


    public void setState(EnumerationValue value)
    {
        setState(getSession().getSessionContext(), value);
    }


    public Collection<ProcessTaskLog> getTaskLogs(SessionContext ctx)
    {
        return TASKLOGSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<ProcessTaskLog> getTaskLogs()
    {
        return getTaskLogs(getSession().getSessionContext());
    }


    public void setTaskLogs(SessionContext ctx, Collection<ProcessTaskLog> value)
    {
        TASKLOGSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setTaskLogs(Collection<ProcessTaskLog> value)
    {
        setTaskLogs(getSession().getSessionContext(), value);
    }


    public void addToTaskLogs(SessionContext ctx, ProcessTaskLog value)
    {
        TASKLOGSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToTaskLogs(ProcessTaskLog value)
    {
        addToTaskLogs(getSession().getSessionContext(), value);
    }


    public void removeFromTaskLogs(SessionContext ctx, ProcessTaskLog value)
    {
        TASKLOGSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromTaskLogs(ProcessTaskLog value)
    {
        removeFromTaskLogs(getSession().getSessionContext(), value);
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
}
