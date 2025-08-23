package de.hybris.platform.b2b.process.approval.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class B2BApprovalProcessModel extends OrderProcessModel
{
    public static final String _TYPECODE = "B2BApprovalProcess";


    public B2BApprovalProcessModel()
    {
    }


    public B2BApprovalProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BApprovalProcessModel(String _code, String _processDefinitionName)
    {
        setCode(_code);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BApprovalProcessModel(String _code, ItemModel _owner, String _processDefinitionName)
    {
        setCode(_code);
        setOwner(_owner);
        setProcessDefinitionName(_processDefinitionName);
    }
}
