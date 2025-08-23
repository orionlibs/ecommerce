package de.hybris.platform.processing.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class BatchModel extends ItemModel
{
    public static final String _TYPECODE = "Batch";
    public static final String _DISTRIBUTEDPROCESS2BATCHRELATION = "DistributedProcess2BatchRelation";
    public static final String ID = "id";
    public static final String EXECUTIONID = "executionId";
    public static final String TYPE = "type";
    public static final String REMAININGWORKLOAD = "remainingWorkLoad";
    public static final String PROCESS = "process";


    public BatchModel()
    {
    }


    public BatchModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BatchModel(String _executionId, String _id, DistributedProcessModel _process, BatchType _type)
    {
        setExecutionId(_executionId);
        setId(_id);
        setProcess(_process);
        setType(_type);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BatchModel(String _executionId, String _id, ItemModel _owner, DistributedProcessModel _process, BatchType _type)
    {
        setExecutionId(_executionId);
        setId(_id);
        setOwner(_owner);
        setProcess(_process);
        setType(_type);
    }


    @Accessor(qualifier = "executionId", type = Accessor.Type.GETTER)
    public String getExecutionId()
    {
        return (String)getPersistenceContext().getPropertyValue("executionId");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "process", type = Accessor.Type.GETTER)
    public DistributedProcessModel getProcess()
    {
        return (DistributedProcessModel)getPersistenceContext().getPropertyValue("process");
    }


    @Accessor(qualifier = "remainingWorkLoad", type = Accessor.Type.GETTER)
    public long getRemainingWorkLoad()
    {
        return toPrimitive((Long)getPersistenceContext().getPropertyValue("remainingWorkLoad"));
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public BatchType getType()
    {
        return (BatchType)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "executionId", type = Accessor.Type.SETTER)
    public void setExecutionId(String value)
    {
        getPersistenceContext().setPropertyValue("executionId", value);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "process", type = Accessor.Type.SETTER)
    public void setProcess(DistributedProcessModel value)
    {
        getPersistenceContext().setPropertyValue("process", value);
    }


    @Accessor(qualifier = "remainingWorkLoad", type = Accessor.Type.SETTER)
    public void setRemainingWorkLoad(long value)
    {
        getPersistenceContext().setPropertyValue("remainingWorkLoad", toObject(value));
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(BatchType value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }
}
