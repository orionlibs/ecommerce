package de.hybris.platform.ordersplitting.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ConsignmentProcessModel extends BusinessProcessModel
{
    public static final String _TYPECODE = "ConsignmentProcess";
    public static final String _CONSIGNMENT2CONSIGNMENTPROCESS = "Consignment2ConsignmentProcess";
    public static final String _ORDERPROCESS2CONSIGNMENTPROCESS = "OrderProcess2ConsignmentProcess";
    public static final String CONSIGNMENT = "consignment";
    public static final String PARENTPROCESS = "parentProcess";


    public ConsignmentProcessModel()
    {
    }


    public ConsignmentProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsignmentProcessModel(String _code, String _processDefinitionName)
    {
        setCode(_code);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsignmentProcessModel(String _code, ItemModel _owner, String _processDefinitionName)
    {
        setCode(_code);
        setOwner(_owner);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Accessor(qualifier = "consignment", type = Accessor.Type.GETTER)
    public ConsignmentModel getConsignment()
    {
        return (ConsignmentModel)getPersistenceContext().getPropertyValue("consignment");
    }


    @Accessor(qualifier = "parentProcess", type = Accessor.Type.GETTER)
    public OrderProcessModel getParentProcess()
    {
        return (OrderProcessModel)getPersistenceContext().getPropertyValue("parentProcess");
    }


    @Accessor(qualifier = "consignment", type = Accessor.Type.SETTER)
    public void setConsignment(ConsignmentModel value)
    {
        getPersistenceContext().setPropertyValue("consignment", value);
    }


    @Accessor(qualifier = "parentProcess", type = Accessor.Type.SETTER)
    public void setParentProcess(OrderProcessModel value)
    {
        getPersistenceContext().setPropertyValue("parentProcess", value);
    }
}
