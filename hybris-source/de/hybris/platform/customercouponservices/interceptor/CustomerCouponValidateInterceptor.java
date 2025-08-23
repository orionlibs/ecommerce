package de.hybris.platform.customercouponservices.interceptor;

import com.google.common.base.Preconditions;
import de.hybris.platform.couponservices.interceptor.CouponInterceptorException;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import org.joda.time.DateTime;

public class CustomerCouponValidateInterceptor implements ValidateInterceptor<AbstractCouponModel>
{
    private static final String UTC = "UTC";
    private static final int COUPON_START_YEAR_DEFAULT = 1970;
    private static final int COUPON_END_YEAR_DEFAULT = 9999;
    private static final int COUPON_DATE_MONTH_DEFAULT = 11;
    private static final int COUPON_DATE_DATE_DEFAULT = 30;
    private static final int COUPON_DATE_HOUR_DEFAULT = 23;
    private static final int COUPON_DATE_MINUTE_DEFAULT = 59;
    private static final int COUPON_DATE_SECOND_DEFAULT = 59;


    public void onValidate(AbstractCouponModel coupon, InterceptorContext ctx) throws InterceptorException
    {
        Preconditions.checkArgument(Objects.nonNull(coupon), "Coupon model cannot be NULL here.");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date startDate = coupon.getStartDate();
        Date endDate = coupon.getEndDate();
        if(coupon instanceof de.hybris.platform.customercouponservices.model.CustomerCouponModel)
        {
            if(Objects.isNull(startDate))
            {
                calendar.set(1970, 0, 1, 0, 0, 0);
                coupon.setStartDate(calendar.getTime());
            }
            if(Objects.isNull(endDate))
            {
                calendar.set(9999, 11, 30, 23, 59, 59);
                coupon.setEndDate(calendar.getTime());
            }
        }
        else if(Objects.nonNull(endDate) && (new DateTime(endDate)).isBeforeNow())
        {
            throw new CouponInterceptorException("End date cannot be in the past.");
        }
        if(Objects.nonNull(startDate) && Objects.nonNull(endDate) && startDate.after(endDate))
        {
            throw new CouponInterceptorException("Illegal value of startDate or endDate: endDate should be after startDate.");
        }
    }
}
