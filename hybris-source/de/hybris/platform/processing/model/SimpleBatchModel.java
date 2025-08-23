package de.hybris.platform.processing.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SimpleBatchModel extends BatchModel
{
    public static final String _TYPECODE = "SimpleBatch";
    public static final String RESULTBATCHID = "resultBatchId";
    public static final String RETRIES = "retries";
    public static final String SCRIPTCODE = "scriptCode";
    public static final String CONTEXT = "context";


    public SimpleBatchModel()
    {
    }


    public SimpleBatchModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SimpleBatchModel(String _executionId, String _id, DistributedProcessModel _process, int _retries, BatchType _type)
    {
        setExecutionId(_executionId);
        setId(_id);
        setProcess(_process);
        setRetries(_retries);
        setType(_type);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SimpleBatchModel(String _executionId, String _id, ItemModel _owner, DistributedProcessModel _process, int _retries, BatchType _type)
    {
        setExecutionId(_executionId);
        setId(_id);
        setOwner(_owner);
        setProcess(_process);
        setRetries(_retries);
        setType(_type);
    }


    @Accessor(qualifier = "context", type = Accessor.Type.GETTER)
    public Object getContext()
    {
        return getPersistenceContext().getPropertyValue("context");
    }


    @Accessor(qualifier = "resultBatchId", type = Accessor.Type.GETTER)
    public String getResultBatchId()
    {
        return (String)getPersistenceContext().getPropertyValue("resultBatchId");
    }


    @Accessor(qualifier = "retries", type = Accessor.Type.GETTER)
    public int getRetries()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("retries"));
    }


    @Accessor(qualifier = "scriptCode", type = Accessor.Type.GETTER)
    public String getScriptCode()
    {
        return (String)getPersistenceContext().getPropertyValue("scriptCode");
    }


    @Accessor(qualifier = "context", type = Accessor.Type.SETTER)
    public void setContext(Object value)
    {
        getPersistenceContext().setPropertyValue("context", value);
    }


    @Accessor(qualifier = "resultBatchId", type = Accessor.Type.SETTER)
    public void setResultBatchId(String value)
    {
        getPersistenceContext().setPropertyValue("resultBatchId", value);
    }


    @Accessor(qualifier = "retries", type = Accessor.Type.SETTER)
    public void setRetries(int value)
    {
        getPersistenceContext().setPropertyValue("retries", toObject(value));
    }


    @Accessor(qualifier = "scriptCode", type = Accessor.Type.SETTER)
    public void setScriptCode(String value)
    {
        getPersistenceContext().setPropertyValue("scriptCode", value);
    }
}
