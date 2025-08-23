package de.hybris.platform.couponservices.interceptor;

import com.google.common.base.Preconditions;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Calendar;
import java.util.Objects;

public class AbstractCouponValidateInterceptor implements ValidateInterceptor<AbstractCouponModel>
{
    private static final String EXCEPTION_MESSAGE_KEY_NOT_NULL = "exception.abstractcouponvalidateinterceptor.model.cannot.null";
    private static final String EXCEPTION_MESSAGE_KEY_END_DATE = "exception.abstractcouponvalidateinterceptor.enddate";
    private static final String EXCEPTION_MESSAGE_KEY_START_END_DATE = "exception.abstractcouponvalidateinterceptor.startdate.enddate";
    private L10NService l10NService;


    public void onValidate(AbstractCouponModel coupon, InterceptorContext ctx) throws InterceptorException
    {
        Preconditions.checkArgument(Objects.nonNull(coupon), getL10NService().getLocalizedString("exception.abstractcouponvalidateinterceptor.model.cannot.null"));
        if(Objects.nonNull(coupon.getEndDate()) && coupon.getEndDate().before(Calendar.getInstance().getTime()))
        {
            throw new CouponInterceptorException(getL10NService().getLocalizedString("exception.abstractcouponvalidateinterceptor.enddate"));
        }
        if(Objects.nonNull(coupon.getStartDate()) && Objects.nonNull(coupon.getEndDate()) && coupon.getStartDate().after(coupon.getEndDate()))
        {
            throw new CouponInterceptorException(getL10NService().getLocalizedString("exception.abstractcouponvalidateinterceptor.startdate.enddate"));
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
