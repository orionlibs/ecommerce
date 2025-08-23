package de.hybris.platform.couponwebservices.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.couponservices.model.SingleCodeCouponModel;
import de.hybris.platform.couponwebservices.dto.SingleCodeCouponWsDTO;
import de.hybris.platform.couponwebservices.util.CouponWsUtils;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class SingleCodeCouponWsPopulator implements Populator<SingleCodeCouponModel, SingleCodeCouponWsDTO>
{
    private CouponWsUtils couponWsUtils;


    public void populate(SingleCodeCouponModel source, SingleCodeCouponWsDTO target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        Objects.requireNonNull(target);
        Optional.<Date>ofNullable(source.getStartDate()).map(getCouponWsUtils().getDateToStringMapper()).ifPresent(target::setStartDate);
        Objects.requireNonNull(target);
        Optional.<Date>ofNullable(source.getEndDate()).map(getCouponWsUtils().getDateToStringMapper()).ifPresent(target::setEndDate);
        convertSingleCodeCoupon(source, target);
    }


    protected void convertSingleCodeCoupon(SingleCodeCouponModel source, SingleCodeCouponWsDTO target)
    {
        target.setCouponId(source.getCouponId());
        target.setName(source.getName());
        target.setActive(source.getActive());
        target.setMaxRedemptionsPerCustomer(source.getMaxRedemptionsPerCustomer());
        target.setMaxTotalRedemptions(source.getMaxTotalRedemptions());
    }


    protected CouponWsUtils getCouponWsUtils()
    {
        return this.couponWsUtils;
    }


    @Required
    public void setCouponWsUtils(CouponWsUtils couponWsUtils)
    {
        this.couponWsUtils = couponWsUtils;
    }
}
