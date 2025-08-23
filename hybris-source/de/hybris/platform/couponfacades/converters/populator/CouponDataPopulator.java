package de.hybris.platform.couponfacades.converters.populator;

import de.hybris.platform.commercefacades.coupon.data.CouponData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class CouponDataPopulator implements Populator<AbstractCouponModel, CouponData>
{
    public void populate(AbstractCouponModel source, CouponData target)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("AbstractCouponModel", source);
        ServicesUtil.validateParameterNotNullStandardMessage("CouponData", target);
        target.setCouponId(source.getCouponId());
        target.setName(source.getName());
        target.setActive(source.getActive().booleanValue());
        target.setStartDate(source.getStartDate());
        target.setEndDate(source.getEndDate());
    }
}
