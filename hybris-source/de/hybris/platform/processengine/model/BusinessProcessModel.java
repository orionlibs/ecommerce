package de.hybris.platform.processengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;

public class BusinessProcessModel extends ItemModel
{
    public static final String _TYPECODE = "BusinessProcess";
    public static final String CODE = "code";
    public static final String PROCESSDEFINITIONNAME = "processDefinitionName";
    public static final String PROCESSDEFINITIONVERSION = "processDefinitionVersion";
    public static final String STATE = "state";
    public static final String PROCESSSTATE = "processState";
    public static final String ENDMESSAGE = "endMessage";
    public static final String USER = "user";
    public static final String CURRENTTASKS = "currentTasks";
    public static final String CONTEXTPARAMETERS = "contextParameters";
    public static final String TASKLOGS = "taskLogs";
    public static final String EMAILS = "emails";


    public BusinessProcessModel()
    {
    }


    public BusinessProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BusinessProcessModel(String _code, String _processDefinitionName)
    {
        setCode(_code);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BusinessProcessModel(String _code, ItemModel _owner, String _processDefinitionName)
    {
        setCode(_code);
        setOwner(_owner);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "contextParameters", type = Accessor.Type.GETTER)
    public Collection<BusinessProcessParameterModel> getContextParameters()
    {
        return (Collection<BusinessProcessParameterModel>)getPersistenceContext().getPropertyValue("contextParameters");
    }


    @Accessor(qualifier = "currentTasks", type = Accessor.Type.GETTER)
    public Collection<ProcessTaskModel> getCurrentTasks()
    {
        return (Collection<ProcessTaskModel>)getPersistenceContext().getPropertyValue("currentTasks");
    }


    @Accessor(qualifier = "emails", type = Accessor.Type.GETTER)
    public List<EmailMessageModel> getEmails()
    {
        return (List<EmailMessageModel>)getPersistenceContext().getPropertyValue("emails");
    }


    @Accessor(qualifier = "endMessage", type = Accessor.Type.GETTER)
    public String getEndMessage()
    {
        return (String)getPersistenceContext().getPropertyValue("endMessage");
    }


    @Accessor(qualifier = "processDefinitionName", type = Accessor.Type.GETTER)
    public String getProcessDefinitionName()
    {
        return (String)getPersistenceContext().getPropertyValue("processDefinitionName");
    }


    @Accessor(qualifier = "processDefinitionVersion", type = Accessor.Type.GETTER)
    public String getProcessDefinitionVersion()
    {
        return (String)getPersistenceContext().getPropertyValue("processDefinitionVersion");
    }


    @Accessor(qualifier = "processState", type = Accessor.Type.GETTER)
    public ProcessState getProcessState()
    {
        return (ProcessState)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "processState");
    }


    @Accessor(qualifier = "state", type = Accessor.Type.GETTER)
    public ProcessState getState()
    {
        return (ProcessState)getPersistenceContext().getPropertyValue("state");
    }


    @Accessor(qualifier = "taskLogs", type = Accessor.Type.GETTER)
    public Collection<ProcessTaskLogModel> getTaskLogs()
    {
        return (Collection<ProcessTaskLogModel>)getPersistenceContext().getPropertyValue("taskLogs");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "contextParameters", type = Accessor.Type.SETTER)
    public void setContextParameters(Collection<BusinessProcessParameterModel> value)
    {
        getPersistenceContext().setPropertyValue("contextParameters", value);
    }


    @Accessor(qualifier = "currentTasks", type = Accessor.Type.SETTER)
    public void setCurrentTasks(Collection<ProcessTaskModel> value)
    {
        getPersistenceContext().setPropertyValue("currentTasks", value);
    }


    @Accessor(qualifier = "emails", type = Accessor.Type.SETTER)
    public void setEmails(List<EmailMessageModel> value)
    {
        getPersistenceContext().setPropertyValue("emails", value);
    }


    @Accessor(qualifier = "endMessage", type = Accessor.Type.SETTER)
    public void setEndMessage(String value)
    {
        getPersistenceContext().setPropertyValue("endMessage", value);
    }


    @Accessor(qualifier = "processDefinitionName", type = Accessor.Type.SETTER)
    public void setProcessDefinitionName(String value)
    {
        getPersistenceContext().setPropertyValue("processDefinitionName", value);
    }


    @Accessor(qualifier = "processDefinitionVersion", type = Accessor.Type.SETTER)
    public void setProcessDefinitionVersion(String value)
    {
        getPersistenceContext().setPropertyValue("processDefinitionVersion", value);
    }


    @Accessor(qualifier = "state", type = Accessor.Type.SETTER)
    public void setState(ProcessState value)
    {
        getPersistenceContext().setPropertyValue("state", value);
    }


    @Accessor(qualifier = "taskLogs", type = Accessor.Type.SETTER)
    public void setTaskLogs(Collection<ProcessTaskLogModel> value)
    {
        getPersistenceContext().setPropertyValue("taskLogs", value);
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }
}
