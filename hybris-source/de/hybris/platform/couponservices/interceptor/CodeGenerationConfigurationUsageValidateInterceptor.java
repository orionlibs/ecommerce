package de.hybris.platform.couponservices.interceptor;

import de.hybris.platform.couponservices.dao.CouponDao;
import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class CodeGenerationConfigurationUsageValidateInterceptor implements RemoveInterceptor<CodeGenerationConfigurationModel>
{
    private static final String EXCEPTION_MESSAGE_KEY = "exception.codegenerationconfigurationusagevalidateinterceptor.cannot.delete";
    private L10NService l10NService;
    private CouponDao couponDao;


    public void onRemove(CodeGenerationConfigurationModel configuration, InterceptorContext ctx) throws InterceptorException
    {
        List<MultiCodeCouponModel> result = getCouponDao().findMultiCodeCouponsByCodeConfiguration(configuration);
        if(!result.isEmpty())
        {
            String names = result.stream().map(coupon -> (String)Optional.<String>ofNullable(coupon.getName()).orElse(coupon.getCouponId())).collect(Collectors.joining(","));
            throw new InterceptorException(getL10NService().getLocalizedString("exception.codegenerationconfigurationusagevalidateinterceptor.cannot.delete", new Object[] {names}));
        }
    }


    protected CouponDao getCouponDao()
    {
        return this.couponDao;
    }


    @Required
    public void setCouponDao(CouponDao couponDao)
    {
        this.couponDao = couponDao;
    }


    protected L10NService getL10NService()
    {
        return this.l10NService;
    }


    @Required
    public void setL10NService(L10NService l10NService)
    {
        this.l10NService = l10NService;
    }
}
