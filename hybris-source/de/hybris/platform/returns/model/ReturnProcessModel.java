package de.hybris.platform.returns.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ReturnProcessModel extends BusinessProcessModel
{
    public static final String _TYPECODE = "ReturnProcess";
    public static final String _RETURN2RETURNPROCESS = "Return2ReturnProcess";
    public static final String RETURNREQUEST = "returnRequest";


    public ReturnProcessModel()
    {
    }


    public ReturnProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReturnProcessModel(String _code, String _processDefinitionName)
    {
        setCode(_code);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReturnProcessModel(String _code, ItemModel _owner, String _processDefinitionName)
    {
        setCode(_code);
        setOwner(_owner);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Accessor(qualifier = "returnRequest", type = Accessor.Type.GETTER)
    public ReturnRequestModel getReturnRequest()
    {
        return (ReturnRequestModel)getPersistenceContext().getPropertyValue("returnRequest");
    }


    @Accessor(qualifier = "returnRequest", type = Accessor.Type.SETTER)
    public void setReturnRequest(ReturnRequestModel value)
    {
        getPersistenceContext().setPropertyValue("returnRequest", value);
    }
}
