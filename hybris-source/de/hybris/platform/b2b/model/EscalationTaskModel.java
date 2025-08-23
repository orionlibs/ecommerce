package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.task.TaskModel;

public class EscalationTaskModel extends TaskModel
{
    public static final String _TYPECODE = "EscalationTask";
    public static final String ORDER = "order";


    public EscalationTaskModel()
    {
    }


    public EscalationTaskModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EscalationTaskModel(OrderModel _order, String _runnerBean)
    {
        setOrder(_order);
        setRunnerBean(_runnerBean);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EscalationTaskModel(OrderModel _order, ItemModel _owner, String _runnerBean)
    {
        setOrder(_order);
        setOwner(_owner);
        setRunnerBean(_runnerBean);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public OrderModel getOrder()
    {
        return (OrderModel)getPersistenceContext().getPropertyValue("order");
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(OrderModel value)
    {
        getPersistenceContext().setPropertyValue("order", value);
    }
}
