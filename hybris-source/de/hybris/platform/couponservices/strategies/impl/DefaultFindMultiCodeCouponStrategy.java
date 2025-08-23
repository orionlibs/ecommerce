package de.hybris.platform.couponservices.strategies.impl;

import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.services.CouponCodeGenerationService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFindMultiCodeCouponStrategy extends AbstractFindCouponStrategy
{
    private CouponCodeGenerationService couponCodeGenerationService;


    protected String getCouponId(String couponCode)
    {
        return getCouponCodeGenerationService().extractCouponPrefix(couponCode);
    }


    protected CouponCodeGenerationService getCouponCodeGenerationService()
    {
        return this.couponCodeGenerationService;
    }


    protected Optional<AbstractCouponModel> couponValidation(AbstractCouponModel coupon)
    {
        return (coupon instanceof de.hybris.platform.couponservices.model.MultiCodeCouponModel) ? super.couponValidation(coupon) : Optional.<AbstractCouponModel>empty();
    }


    @Required
    public void setCouponCodeGenerationService(CouponCodeGenerationService couponCodeGenerationService)
    {
        this.couponCodeGenerationService = couponCodeGenerationService;
    }
}
