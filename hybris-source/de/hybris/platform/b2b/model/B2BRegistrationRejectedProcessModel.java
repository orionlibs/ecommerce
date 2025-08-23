package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class B2BRegistrationRejectedProcessModel extends B2BRegistrationProcessModel
{
    public static final String _TYPECODE = "B2BRegistrationRejectedProcess";
    public static final String REJECTREASON = "rejectReason";


    public B2BRegistrationRejectedProcessModel()
    {
    }


    public B2BRegistrationRejectedProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BRegistrationRejectedProcessModel(String _code, String _processDefinitionName)
    {
        setCode(_code);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BRegistrationRejectedProcessModel(String _code, ItemModel _owner, String _processDefinitionName)
    {
        setCode(_code);
        setOwner(_owner);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Accessor(qualifier = "rejectReason", type = Accessor.Type.GETTER)
    public String getRejectReason()
    {
        return (String)getPersistenceContext().getPropertyValue("rejectReason");
    }


    @Accessor(qualifier = "rejectReason", type = Accessor.Type.SETTER)
    public void setRejectReason(String value)
    {
        getPersistenceContext().setPropertyValue("rejectReason", value);
    }
}
