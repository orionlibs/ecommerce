package de.hybris.platform.voucher;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;
import de.hybris.platform.voucher.model.RestrictionModel;

public interface VoucherRestrictionService
{
    boolean isFulfilled(RestrictionModel paramRestrictionModel, AbstractOrderModel paramAbstractOrderModel);


    VoucherEntrySet getApplicableEntries(RestrictionModel paramRestrictionModel, AbstractOrderModel paramAbstractOrderModel);


    boolean isFulfilled(RestrictionModel paramRestrictionModel, ProductModel paramProductModel);
}
