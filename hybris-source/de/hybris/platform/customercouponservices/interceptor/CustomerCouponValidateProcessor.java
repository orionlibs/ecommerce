package de.hybris.platform.customercouponservices.interceptor;

import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.servicelayer.interceptor.Interceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.interceptor.impl.InterceptorMapping;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class CustomerCouponValidateProcessor implements BeanPostProcessor
{
    private ValidateInterceptor<AbstractCouponModel> couponValidateInterceptor;


    public Object postProcessAfterInitialization(Object arg0, String arg1)
    {
        if(arg0 instanceof InterceptorMapping)
        {
            InterceptorMapping mapping = (InterceptorMapping)arg0;
            if("AbstractCoupon".equals(mapping.getTypeCode()))
            {
                mapping.setInterceptor((Interceptor)this.couponValidateInterceptor);
            }
        }
        return arg0;
    }


    public Object postProcessBeforeInitialization(Object arg0, String arg1)
    {
        return arg0;
    }


    protected ValidateInterceptor<AbstractCouponModel> getCouponValidateInterceptor()
    {
        return this.couponValidateInterceptor;
    }


    public void setCouponValidateInterceptor(ValidateInterceptor<AbstractCouponModel> couponValidateInterceptor)
    {
        this.couponValidateInterceptor = couponValidateInterceptor;
    }
}
