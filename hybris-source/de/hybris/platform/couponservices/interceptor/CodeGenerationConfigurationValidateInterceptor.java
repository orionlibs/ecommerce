package de.hybris.platform.couponservices.interceptor;

import de.hybris.platform.couponservices.dao.CouponDao;
import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.couponservices.services.CouponCodeGenerationService;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class CodeGenerationConfigurationValidateInterceptor implements ValidateInterceptor<CodeGenerationConfigurationModel>
{
    private static final String NAME_NULL_ERROR_MSG = "exception.codegenerationconfigurationvalidateinterceptor.name.null";
    private static final String COUPONS_IN_USE_ERROR_MSG = "exception.codegenerationconfigurationvalidateinterceptor.in.use";
    private static final String CODE_SEPARATOR_NULL_ERROR_MSG = "exception.codegenerationconfigurationvalidateinterceptor.separator.null";
    private static final String INVALID_CODE_SEPARATOR_ERROR_MSG = "exception.codegenerationconfigurationvalidateinterceptor.invalid.separator";
    private static final String COUPON_PART_COUNT_ERROR_MSG = "exception.codegenerationconfigurationvalidateinterceptor.part.count";
    private static final String TOTAL_LENGTH1_ERROR_MSG = "exception.codegenerationconfigurationvalidateinterceptor.total.length1";
    private static final String TOTAL_LENGTH2_ERROR_MSG = "exception.codegenerationconfigurationvalidateinterceptor.total.length2";
    private static final String TOTAL_LENGTH3_ERROR_MSG = "exception.codegenerationconfigurationvalidateinterceptor.total.length3";
    private CouponCodeGenerationService couponCodeGenerationService;
    private CouponDao couponDao;
    private L10NService l10NService;


    public void onValidate(CodeGenerationConfigurationModel model, InterceptorContext ctx) throws InterceptorException
    {
        if(StringUtils.isEmpty(model.getName()))
        {
            throw new InterceptorException(getL10NService().getLocalizedString("exception.codegenerationconfigurationvalidateinterceptor.name.null"));
        }
        validateCodeSeperator(model);
        validateCoupon(model);
        if(ctx.isModified(model) && !ctx.isNew(model) && checkModelAttributeValuesChanged(model, ctx))
        {
            List<MultiCodeCouponModel> coupons = getCouponDao().findMultiCodeCouponsByCodeConfiguration(model);
            if(CollectionUtils.isNotEmpty(coupons) && coupons
                            .stream().anyMatch(coupon -> (coupon.getCouponCodeNumber().intValue() > 0)))
            {
                throw new InterceptorException(getL10NService().getLocalizedString("exception.codegenerationconfigurationvalidateinterceptor.in.use", new Object[] {Integer.valueOf(coupons.size())}));
            }
        }
    }


    protected void validateCodeSeperator(CodeGenerationConfigurationModel model) throws InterceptorException
    {
        String codeSeparator = model.getCodeSeparator();
        if(StringUtils.isEmpty(codeSeparator))
        {
            throw new InterceptorException(getL10NService().getLocalizedString("exception.codegenerationconfigurationvalidateinterceptor.separator.null"));
        }
        if(!getCouponCodeGenerationService().isValidCodeSeparator(codeSeparator))
        {
            throw new InterceptorException(getL10NService().getLocalizedString("exception.codegenerationconfigurationvalidateinterceptor.invalid.separator"));
        }
    }


    protected void validateCoupon(CodeGenerationConfigurationModel model) throws InterceptorException
    {
        int partCount = model.getCouponPartCount();
        if(partCount <= 0)
        {
            throw new InterceptorException(getL10NService().getLocalizedString("exception.codegenerationconfigurationvalidateinterceptor.part.count"));
        }
        int partLength = model.getCouponPartLength();
        if(partLength <= 0)
        {
            throw new InterceptorException(getL10NService().getLocalizedString("exception.codegenerationconfigurationvalidateinterceptor.part.count"));
        }
        validateTotalLength(partCount, partLength);
    }


    protected void validateTotalLength(int partCount, int partLength) throws InterceptorException
    {
        int totalLength = partCount * partLength;
        if(totalLength < 4)
        {
            throw new InterceptorException(getL10NService().getLocalizedString("exception.codegenerationconfigurationvalidateinterceptor.total.length1"));
        }
        if(totalLength % 4 != 0)
        {
            throw new InterceptorException(getL10NService().getLocalizedString("exception.codegenerationconfigurationvalidateinterceptor.total.length2"));
        }
        if(totalLength > 40)
        {
            throw new InterceptorException(getL10NService().getLocalizedString("exception.codegenerationconfigurationvalidateinterceptor.total.length3"));
        }
    }


    protected boolean checkModelAttributeValuesChanged(CodeGenerationConfigurationModel model, InterceptorContext ctx)
    {
        return (isCodeSeparattorChanged(model, ctx) || isCouponPartCountChanged(model, ctx) || isCouponPartLengthChanged(model, ctx));
    }


    protected boolean isCodeSeparattorChanged(CodeGenerationConfigurationModel model, InterceptorContext ctx)
    {
        return (ctx.isModified(model, "codeSeparator") &&
                        !model.getCodeSeparator().equals(getPreviousValue(model, "codeSeparator")));
    }


    protected boolean isCouponPartCountChanged(CodeGenerationConfigurationModel model, InterceptorContext ctx)
    {
        Integer previousValue = getPreviousValue(model, "couponPartCount");
        return (ctx.isModified(model, "couponPartCount") && model
                        .getCouponPartCount() != previousValue.intValue());
    }


    protected boolean isCouponPartLengthChanged(CodeGenerationConfigurationModel model, InterceptorContext ctx)
    {
        Integer previousValue = getPreviousValue(model, "couponPartLength");
        return (ctx.isModified(model, "couponPartLength") && model
                        .getCouponPartLength() != previousValue.intValue());
    }


    protected <T> T getPreviousValue(CodeGenerationConfigurationModel model, String attributeName)
    {
        return (T)ModelContextUtils.getItemModelContext((AbstractItemModel)model).getOriginalValue(attributeName);
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


    public void setL10NService(L10NService l10NService)
    {
        this.l10NService = l10NService;
    }
}
