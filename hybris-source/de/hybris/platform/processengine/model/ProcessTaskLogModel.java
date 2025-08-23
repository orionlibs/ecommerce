package de.hybris.platform.processengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class ProcessTaskLogModel extends ItemModel
{
    public static final String _TYPECODE = "ProcessTaskLog";
    public static final String _PROCESS2TASKLOGRELATION = "Process2TaskLogRelation";
    public static final String RETURNCODE = "returnCode";
    public static final String STARTDATE = "startDate";
    public static final String ENDDATE = "endDate";
    public static final String ACTIONID = "actionId";
    public static final String CLUSTERID = "clusterId";
    public static final String LOGMESSAGES = "logMessages";
    public static final String PROCESS = "process";


    public ProcessTaskLogModel()
    {
    }


    public ProcessTaskLogModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProcessTaskLogModel(String _actionId, Integer _clusterId, BusinessProcessModel _process)
    {
        setActionId(_actionId);
        setClusterId(_clusterId);
        setProcess(_process);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProcessTaskLogModel(String _actionId, Integer _clusterId, ItemModel _owner, BusinessProcessModel _process)
    {
        setActionId(_actionId);
        setClusterId(_clusterId);
        setOwner(_owner);
        setProcess(_process);
    }


    @Accessor(qualifier = "actionId", type = Accessor.Type.GETTER)
    public String getActionId()
    {
        return (String)getPersistenceContext().getPropertyValue("actionId");
    }


    @Accessor(qualifier = "clusterId", type = Accessor.Type.GETTER)
    public Integer getClusterId()
    {
        return (Integer)getPersistenceContext().getPropertyValue("clusterId");
    }


    @Accessor(qualifier = "endDate", type = Accessor.Type.GETTER)
    public Date getEndDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("endDate");
    }


    @Accessor(qualifier = "logMessages", type = Accessor.Type.GETTER)
    public String getLogMessages()
    {
        return (String)getPersistenceContext().getPropertyValue("logMessages");
    }


    @Accessor(qualifier = "process", type = Accessor.Type.GETTER)
    public BusinessProcessModel getProcess()
    {
        return (BusinessProcessModel)getPersistenceContext().getPropertyValue("process");
    }


    @Accessor(qualifier = "returnCode", type = Accessor.Type.GETTER)
    public String getReturnCode()
    {
        return (String)getPersistenceContext().getPropertyValue("returnCode");
    }


    @Accessor(qualifier = "startDate", type = Accessor.Type.GETTER)
    public Date getStartDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("startDate");
    }


    @Accessor(qualifier = "actionId", type = Accessor.Type.SETTER)
    public void setActionId(String value)
    {
        getPersistenceContext().setPropertyValue("actionId", value);
    }


    @Accessor(qualifier = "clusterId", type = Accessor.Type.SETTER)
    public void setClusterId(Integer value)
    {
        getPersistenceContext().setPropertyValue("clusterId", value);
    }


    @Accessor(qualifier = "endDate", type = Accessor.Type.SETTER)
    public void setEndDate(Date value)
    {
        getPersistenceContext().setPropertyValue("endDate", value);
    }


    @Accessor(qualifier = "logMessages", type = Accessor.Type.SETTER)
    public void setLogMessages(String value)
    {
        getPersistenceContext().setPropertyValue("logMessages", value);
    }


    @Accessor(qualifier = "process", type = Accessor.Type.SETTER)
    public void setProcess(BusinessProcessModel value)
    {
        getPersistenceContext().setPropertyValue("process", value);
    }


    @Accessor(qualifier = "returnCode", type = Accessor.Type.SETTER)
    public void setReturnCode(String value)
    {
        getPersistenceContext().setPropertyValue("returnCode", value);
    }


    @Accessor(qualifier = "startDate", type = Accessor.Type.SETTER)
    public void setStartDate(Date value)
    {
        getPersistenceContext().setPropertyValue("startDate", value);
    }
}
