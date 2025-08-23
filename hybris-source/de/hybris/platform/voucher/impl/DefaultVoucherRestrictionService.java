package de.hybris.platform.voucher.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.voucher.VoucherRestrictionService;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;
import de.hybris.platform.voucher.model.RestrictionModel;

public class DefaultVoucherRestrictionService extends AbstractVoucherService implements VoucherRestrictionService
{
    public VoucherEntrySet getApplicableEntries(RestrictionModel restriction, AbstractOrderModel order)
    {
        return getRestriction(restriction).getApplicableEntries(getAbstractOrder(order));
    }


    public boolean isFulfilled(RestrictionModel restriction, AbstractOrderModel order)
    {
        return getRestriction(restriction).isFulfilled(getAbstractOrder(order));
    }


    public boolean isFulfilled(RestrictionModel restriction, ProductModel product)
    {
        return getRestriction(restriction).isFulfilled(getProduct(product));
    }
}
