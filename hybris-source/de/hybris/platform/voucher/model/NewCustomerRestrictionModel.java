package de.hybris.platform.voucher.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class NewCustomerRestrictionModel extends RestrictionModel
{
    public static final String _TYPECODE = "NewCustomerRestriction";


    public NewCustomerRestrictionModel()
    {
    }


    public NewCustomerRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public NewCustomerRestrictionModel(VoucherModel _voucher)
    {
        setVoucher(_voucher);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public NewCustomerRestrictionModel(ItemModel _owner, VoucherModel _voucher)
    {
        setOwner(_owner);
        setVoucher(_voucher);
    }
}
