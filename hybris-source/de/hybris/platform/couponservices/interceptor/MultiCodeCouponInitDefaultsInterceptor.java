package de.hybris.platform.couponservices.interceptor;

import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.couponservices.services.CouponCodeGenerationService;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import org.springframework.beans.factory.annotation.Required;

public class MultiCodeCouponInitDefaultsInterceptor implements InitDefaultsInterceptor<MultiCodeCouponModel>
{
    private CouponCodeGenerationService couponCodeGenerationService;


    public void onInitDefaults(MultiCodeCouponModel model, InterceptorContext ctx) throws InterceptorException
    {
        model.setSignature(getCouponCodeGenerationService().generateCouponSignature());
        model.setAlphabet(getCouponCodeGenerationService().generateCouponAlphabet());
    }


    protected CouponCodeGenerationService getCouponCodeGenerationService()
    {
        return this.couponCodeGenerationService;
    }


    @Required
    public void setCouponCodeGenerationService(CouponCodeGenerationService couponCodeGenerationService)
    {
        this.couponCodeGenerationService = couponCodeGenerationService;
    }
}
