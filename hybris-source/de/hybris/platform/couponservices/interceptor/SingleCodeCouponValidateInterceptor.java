package de.hybris.platform.couponservices.interceptor;

import com.google.common.base.Preconditions;
import de.hybris.platform.couponservices.model.SingleCodeCouponModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Objects;
import org.apache.commons.lang.BooleanUtils;

public class SingleCodeCouponValidateInterceptor implements ValidateInterceptor<SingleCodeCouponModel>
{
    private static final String MODEL_NULL_ERROR_MSG = "exception.abstractcouponvalidateinterceptor.model.cannot.null";
    private static final String ID_NULL_ERROR_MSG = "exception.multicodecouponvalidateinterceptor.id.null";
    private static final String ID_MODIFY_ERROR_MSG = "exception.singlecodecouponvalidateinterceptor.id.modify";
    private static final String REDEMPTIONS1_ERROR_MSG = "exception.singlecodecouponvalidateinterceptor.redemptions1";
    private static final String REDEMPTIONS2_ERROR_MSG = "exception.singlecodecouponvalidateinterceptor.redemptions2";
    private L10NService l10NService;


    public void onValidate(SingleCodeCouponModel coupon, InterceptorContext ctx) throws InterceptorException
    {
        Preconditions.checkArgument(Objects.nonNull(coupon), getL10NService().getLocalizedString("exception.abstractcouponvalidateinterceptor.model.cannot.null"));
        Preconditions.checkArgument(Objects.nonNull(coupon.getCouponId()), getL10NService().getLocalizedString("exception.multicodecouponvalidateinterceptor.id.null"));
        if(!ctx.isNew(coupon) && BooleanUtils.isTrue(coupon.getActive()) && ctx.isModified(coupon, "couponId"))
        {
            throw new CouponInterceptorException(getL10NService().getLocalizedString("exception.singlecodecouponvalidateinterceptor.id.modify"));
        }
        validateMaxRedemptionsPerCustomer(coupon);
    }


    protected void validateMaxRedemptionsPerCustomer(SingleCodeCouponModel coupon) throws CouponInterceptorException
    {
        int maxRedemptionsPerCustomer = Objects.isNull(coupon.getMaxRedemptionsPerCustomer()) ? Integer.MAX_VALUE : coupon.getMaxRedemptionsPerCustomer().intValue();
        int maxTotalRedemptions = Objects.isNull(coupon.getMaxTotalRedemptions()) ? Integer.MAX_VALUE : coupon.getMaxTotalRedemptions().intValue();
        if(maxRedemptionsPerCustomer < 1)
        {
            throw new CouponInterceptorException(getL10NService().getLocalizedString("exception.singlecodecouponvalidateinterceptor.redemptions1"));
        }
        if(Objects.nonNull(coupon.getMaxRedemptionsPerCustomer()) && Objects.nonNull(coupon.getMaxTotalRedemptions()) && maxRedemptionsPerCustomer > maxTotalRedemptions)
        {
            throw new CouponInterceptorException(getL10NService().getLocalizedString("exception.singlecodecouponvalidateinterceptor.redemptions2"));
        }
    }


    protected L10NService getL10NService()
    {
        return this.l10NService;
    }


    public void setL10NService(L10NService l10NService)
    {
        this.l10NService = l10NService;
    }
}
