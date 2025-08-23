package de.hybris.platform.sap.sapcpiadapter.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPCpiOutboundB2BContactModel extends SAPCpiOutboundB2BCustomerModel
{
    public static final String _TYPECODE = "SAPCpiOutboundB2BContact";
    public static final String _SAPCPIOUTBOUNDB2BCUSTOMER2SAPCPIOUTBOUNDB2BCONTACT = "SAPCpiOutboundB2BCustomer2SAPCpiOutboundB2BContact";
    public static final String SAPCPIOUTBOUNDB2BCUSTOMER = "sapCpiOutboundB2BCustomer";


    public SAPCpiOutboundB2BContactModel()
    {
    }


    public SAPCpiOutboundB2BContactModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPCpiOutboundB2BContactModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "sapCpiOutboundB2BCustomer", type = Accessor.Type.GETTER)
    public SAPCpiOutboundB2BCustomerModel getSapCpiOutboundB2BCustomer()
    {
        return (SAPCpiOutboundB2BCustomerModel)getPersistenceContext().getPropertyValue("sapCpiOutboundB2BCustomer");
    }


    @Accessor(qualifier = "sapCpiOutboundB2BCustomer", type = Accessor.Type.SETTER)
    public void setSapCpiOutboundB2BCustomer(SAPCpiOutboundB2BCustomerModel value)
    {
        getPersistenceContext().setPropertyValue("sapCpiOutboundB2BCustomer", value);
    }
}
