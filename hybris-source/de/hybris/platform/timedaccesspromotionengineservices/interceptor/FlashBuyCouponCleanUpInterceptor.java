package de.hybris.platform.timedaccesspromotionengineservices.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.timedaccesspromotionengineservices.FlashBuyService;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCouponModel;

public class FlashBuyCouponCleanUpInterceptor implements RemoveInterceptor<FlashBuyCouponModel>
{
    private FlashBuyService flashBuyService;


    public void onRemove(FlashBuyCouponModel coupon, InterceptorContext ctx) throws InterceptorException
    {
        getFlashBuyService().deleteCronJobAndTrigger(coupon);
    }


    protected FlashBuyService getFlashBuyService()
    {
        return this.flashBuyService;
    }


    public void setFlashBuyService(FlashBuyService flashBuyService)
    {
        this.flashBuyService = flashBuyService;
    }
}
