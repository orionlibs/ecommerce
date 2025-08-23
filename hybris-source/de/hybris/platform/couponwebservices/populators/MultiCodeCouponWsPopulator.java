package de.hybris.platform.couponwebservices.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.couponwebservices.dto.MultiCodeCouponWsDTO;
import de.hybris.platform.couponwebservices.util.CouponWsUtils;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class MultiCodeCouponWsPopulator implements Populator<MultiCodeCouponModel, MultiCodeCouponWsDTO>
{
    private CouponWsUtils couponWsUtils;


    public void populate(MultiCodeCouponModel source, MultiCodeCouponWsDTO target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        Objects.requireNonNull(target);
        Optional.<Date>ofNullable(source.getStartDate()).map(getCouponWsUtils().getDateToStringMapper()).ifPresent(target::setStartDate);
        Objects.requireNonNull(target);
        Optional.<Date>ofNullable(source.getEndDate()).map(getCouponWsUtils().getDateToStringMapper()).ifPresent(target::setEndDate);
        convertMultiCodeCoupon(source, target);
    }


    protected void convertMultiCodeCoupon(MultiCodeCouponModel source, MultiCodeCouponWsDTO target)
    {
        target.setCouponId(source.getCouponId());
        target.setName(source.getName());
        target.setActive(source.getActive());
        target.setCouponCodeNumber(source.getCouponCodeNumber());
        Objects.requireNonNull(target);
        Optional.<CodeGenerationConfigurationModel>ofNullable(source.getCodeGenerationConfiguration()).map(c -> c.getName()).ifPresent(target::setCodeGenerationConfiguration);
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
