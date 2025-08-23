package de.hybris.platform.couponfacades.converters.populator;

import de.hybris.platform.commercefacades.voucher.data.VoucherData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class CouponModelToVoucherDataPopulator implements Populator<AbstractCouponModel, VoucherData>
{
    public void populate(AbstractCouponModel source, VoucherData target)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("AbstractCouponModel", source);
        ServicesUtil.validateParameterNotNullStandardMessage("VoucherData", target);
        target.setCode(source.getCouponId());
    }
}
