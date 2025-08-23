package de.hybris.platform.sap.sapcpiadapter.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPCpiOutboundOrderCancellationModel extends SAPCpiOutboundOrderModel
{
    public static final String _TYPECODE = "SAPCpiOutboundOrderCancellation";
    public static final String REJECTIONREASON = "rejectionReason";


    public SAPCpiOutboundOrderCancellationModel()
    {
    }


    public SAPCpiOutboundOrderCancellationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPCpiOutboundOrderCancellationModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "rejectionReason", type = Accessor.Type.GETTER)
    public String getRejectionReason()
    {
        return (String)getPersistenceContext().getPropertyValue("rejectionReason");
    }


    @Accessor(qualifier = "rejectionReason", type = Accessor.Type.SETTER)
    public void setRejectionReason(String value)
    {
        getPersistenceContext().setPropertyValue("rejectionReason", value);
    }
}
