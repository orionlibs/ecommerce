package de.hybris.platform.processing.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SimpleDistributedProcessModel extends DistributedProcessModel
{
    public static final String _TYPECODE = "SimpleDistributedProcess";
    public static final String BATCHSIZE = "batchSize";


    public SimpleDistributedProcessModel()
    {
    }


    public SimpleDistributedProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SimpleDistributedProcessModel(String _code, String _currentExecutionId, DistributedProcessState _state)
    {
        setCode(_code);
        setCurrentExecutionId(_currentExecutionId);
        setState(_state);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SimpleDistributedProcessModel(int _batchSize, String _code, String _currentExecutionId, String _handlerBeanId, String _nodeGroup, ItemModel _owner, DistributedProcessState _state)
    {
        setBatchSize(_batchSize);
        setCode(_code);
        setCurrentExecutionId(_currentExecutionId);
        setHandlerBeanId(_handlerBeanId);
        setNodeGroup(_nodeGroup);
        setOwner(_owner);
        setState(_state);
    }


    @Accessor(qualifier = "batchSize", type = Accessor.Type.GETTER)
    public int getBatchSize()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("batchSize"));
    }


    @Accessor(qualifier = "batchSize", type = Accessor.Type.SETTER)
    public void setBatchSize(int value)
    {
        getPersistenceContext().setPropertyValue("batchSize", toObject(value));
    }
}
