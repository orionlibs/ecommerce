package de.hybris.platform.processing.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class DistributedProcessModel extends ItemModel
{
    public static final String _TYPECODE = "DistributedProcess";
    public static final String CODE = "code";
    public static final String HANDLERBEANID = "handlerBeanId";
    public static final String CURRENTEXECUTIONID = "currentExecutionId";
    public static final String STATE = "state";
    public static final String STOPREQUESTED = "stopRequested";
    public static final String NODEGROUP = "nodeGroup";
    public static final String STATUS = "status";
    public static final String EXTENDEDSTATUS = "extendedStatus";
    public static final String PROGRESS = "progress";
    public static final String BATCHES = "batches";


    public DistributedProcessModel()
    {
    }


    public DistributedProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DistributedProcessModel(String _code, String _currentExecutionId, DistributedProcessState _state)
    {
        setCode(_code);
        setCurrentExecutionId(_currentExecutionId);
        setState(_state);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DistributedProcessModel(String _code, String _currentExecutionId, String _handlerBeanId, String _nodeGroup, ItemModel _owner, DistributedProcessState _state)
    {
        setCode(_code);
        setCurrentExecutionId(_currentExecutionId);
        setHandlerBeanId(_handlerBeanId);
        setNodeGroup(_nodeGroup);
        setOwner(_owner);
        setState(_state);
    }


    @Accessor(qualifier = "batches", type = Accessor.Type.GETTER)
    public Collection<BatchModel> getBatches()
    {
        return (Collection<BatchModel>)getPersistenceContext().getPropertyValue("batches");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "currentExecutionId", type = Accessor.Type.GETTER)
    public String getCurrentExecutionId()
    {
        return (String)getPersistenceContext().getPropertyValue("currentExecutionId");
    }


    @Accessor(qualifier = "extendedStatus", type = Accessor.Type.GETTER)
    public String getExtendedStatus()
    {
        return (String)getPersistenceContext().getPropertyValue("extendedStatus");
    }


    @Accessor(qualifier = "handlerBeanId", type = Accessor.Type.GETTER)
    public String getHandlerBeanId()
    {
        return (String)getPersistenceContext().getPropertyValue("handlerBeanId");
    }


    @Accessor(qualifier = "nodeGroup", type = Accessor.Type.GETTER)
    public String getNodeGroup()
    {
        return (String)getPersistenceContext().getPropertyValue("nodeGroup");
    }


    @Accessor(qualifier = "progress", type = Accessor.Type.GETTER)
    public Double getProgress()
    {
        return (Double)getPersistenceContext().getPropertyValue("progress");
    }


    @Accessor(qualifier = "state", type = Accessor.Type.GETTER)
    public DistributedProcessState getState()
    {
        return (DistributedProcessState)getPersistenceContext().getPropertyValue("state");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public String getStatus()
    {
        return (String)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "stopRequested", type = Accessor.Type.GETTER)
    public boolean isStopRequested()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("stopRequested"));
    }


    @Accessor(qualifier = "batches", type = Accessor.Type.SETTER)
    public void setBatches(Collection<BatchModel> value)
    {
        getPersistenceContext().setPropertyValue("batches", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "currentExecutionId", type = Accessor.Type.SETTER)
    public void setCurrentExecutionId(String value)
    {
        getPersistenceContext().setPropertyValue("currentExecutionId", value);
    }


    @Accessor(qualifier = "extendedStatus", type = Accessor.Type.SETTER)
    public void setExtendedStatus(String value)
    {
        getPersistenceContext().setPropertyValue("extendedStatus", value);
    }


    @Accessor(qualifier = "handlerBeanId", type = Accessor.Type.SETTER)
    public void setHandlerBeanId(String value)
    {
        getPersistenceContext().setPropertyValue("handlerBeanId", value);
    }


    @Accessor(qualifier = "nodeGroup", type = Accessor.Type.SETTER)
    public void setNodeGroup(String value)
    {
        getPersistenceContext().setPropertyValue("nodeGroup", value);
    }


    @Accessor(qualifier = "progress", type = Accessor.Type.SETTER)
    public void setProgress(Double value)
    {
        getPersistenceContext().setPropertyValue("progress", value);
    }


    @Accessor(qualifier = "state", type = Accessor.Type.SETTER)
    public void setState(DistributedProcessState value)
    {
        getPersistenceContext().setPropertyValue("state", value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(String value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }


    @Accessor(qualifier = "stopRequested", type = Accessor.Type.SETTER)
    public void setStopRequested(boolean value)
    {
        getPersistenceContext().setPropertyValue("stopRequested", toObject(value));
    }
}
