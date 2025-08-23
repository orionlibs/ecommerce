package de.hybris.platform.couponservices.interceptor;

import com.google.common.base.Equivalence;
import com.google.common.base.Preconditions;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class MultiCodeCouponValidateInterceptor implements ValidateInterceptor<MultiCodeCouponModel>, InitializingBean
{
    private static final String MODEL_NULL_ERROR_MSG = "exception.abstractcouponvalidateinterceptor.model.cannot.null";
    private static final String ID_NULL_ERROR_MSG = "exception.multicodecouponvalidateinterceptor.id.null";
    private static final String PATTERN_ERROR_MSG = "exception.multicodecouponvalidateinterceptor.pattern.convention";
    private static final String ID_MODIFY_ERROR_MSG = "exception.multicodecouponvalidateinterceptor.id.modify";
    private static final String CONFIG_MODIFY_ERROR_MSG = "exception.multicodecouponvalidateinterceptor.configuration.modify";
    private static final String CODE_REMOVE_ERROR_MSG = "exception.multicodecouponvalidateinterceptor.code.remove";
    private static final String DECREMENTED_ERROR_MSG = "exception.multicodecouponvalidateinterceptor.number.decremented";
    private L10NService l10NService;
    private ConfigurationService configurationService;
    private Pattern couponIdPattern;


    public void onValidate(@Nonnull MultiCodeCouponModel coupon, InterceptorContext ctx) throws InterceptorException
    {
        Preconditions.checkArgument(Objects.nonNull(coupon), getL10NService().getLocalizedString("exception.abstractcouponvalidateinterceptor.model.cannot.null"));
        Preconditions.checkArgument(Objects.nonNull(coupon.getCouponId()), getL10NService().getLocalizedString("exception.multicodecouponvalidateinterceptor.id.null"));
        String couponId = coupon.getCouponId();
        Matcher matcher = this.couponIdPattern.matcher(couponId);
        if(!matcher.matches())
        {
            throw new CouponInterceptorException(getL10NService().getLocalizedString("exception.multicodecouponvalidateinterceptor.pattern.convention", new Object[] {this.couponIdPattern.pattern()}));
        }
        if(!ctx.isNew(coupon))
        {
            if(checkActiveCoupon(coupon, ctx))
            {
                if(ctx.isModified(coupon, "couponId"))
                {
                    throw new CouponInterceptorException(getL10NService().getLocalizedString("exception.multicodecouponvalidateinterceptor.id.modify"));
                }
                if(isCodeGenerationConfigurationChanged(coupon, ctx))
                {
                    throw new CouponInterceptorException(getL10NService().getLocalizedString("exception.multicodecouponvalidateinterceptor.configuration.modify"));
                }
            }
            if(ctx.isModified(coupon, "generatedCodes"))
            {
                checkRemovalOfGeneratedCodes(coupon, ctx);
            }
            if(ctx.isModified(coupon, "couponCodeNumber"))
            {
                checkSeedNumberIsNotDecremented(coupon, ctx);
            }
        }
    }


    protected void checkRemovalOfGeneratedCodes(MultiCodeCouponModel coupon, InterceptorContext ctx) throws CouponInterceptorException
    {
        Collection<MediaModel> generatedCodes = coupon.getGeneratedCodes();
        Collection<MediaModel> originalGeneratedCodes = getOriginal(coupon, ctx, "generatedCodes");
        if(CollectionUtils.isNotEmpty(originalGeneratedCodes))
        {
            MediaModelEquivalence mediaModelEquivalence = new MediaModelEquivalence();
            Set<Equivalence.Wrapper<MediaModel>> originalGeneratedCodesSet = getEquivalenceWrappedSet(originalGeneratedCodes, (Equivalence<MediaModel>)mediaModelEquivalence);
            Set<Equivalence.Wrapper<MediaModel>> generatedCodesSet = getEquivalenceWrappedSet(generatedCodes, (Equivalence<MediaModel>)mediaModelEquivalence);
            if(!generatedCodesSet.containsAll(originalGeneratedCodesSet))
            {
                throw new CouponInterceptorException(getL10NService().getLocalizedString("exception.multicodecouponvalidateinterceptor.code.remove"));
            }
        }
    }


    protected void checkSeedNumberIsNotDecremented(MultiCodeCouponModel coupon, InterceptorContext ctx) throws CouponInterceptorException
    {
        Long couponCodeNumber = getOriginal(coupon, ctx, "couponCodeNumber");
        if(Objects.nonNull(couponCodeNumber) && (
                        Objects.isNull(coupon.getCouponCodeNumber()) || coupon.getCouponCodeNumber().longValue() < couponCodeNumber.longValue()))
        {
            throw new CouponInterceptorException(getL10NService().getLocalizedString("exception.multicodecouponvalidateinterceptor.number.decremented"));
        }
    }


    public void afterPropertiesSet()
    {
        String couponIdRegexp = getConfigurationService().getConfiguration().getString("couponservices.code.generation.prefix.regexp", "[A-Za-z0-9]+");
        this.couponIdPattern = Pattern.compile(couponIdRegexp);
    }


    protected Set<Equivalence.Wrapper<MediaModel>> getEquivalenceWrappedSet(Collection<MediaModel> seedCollection, Equivalence<MediaModel> eq)
    {
        Set<Equivalence.Wrapper<MediaModel>> equivalenceWrappedSet = new HashSet<>();
        if(CollectionUtils.isNotEmpty(seedCollection))
        {
            seedCollection.forEach(s -> equivalenceWrappedSet.add(eq.wrap(s)));
        }
        return equivalenceWrappedSet;
    }


    protected boolean checkActiveCoupon(MultiCodeCouponModel coupon, InterceptorContext ctx)
    {
        return CollectionUtils.isEmpty(coupon.getGeneratedCodes()) ? (
                        (BooleanUtils.isTrue(coupon.getActive()) && BooleanUtils.isTrue(getOriginal(coupon, ctx, "active")))) : true;
    }


    protected boolean isCodeGenerationConfigurationChanged(MultiCodeCouponModel model, InterceptorContext ctx)
    {
        CodeGenerationConfigurationModel originalConfiguration = getOriginal(model, ctx, "codeGenerationConfiguration");
        return
                        !model.getCodeGenerationConfiguration().getName().equals(originalConfiguration.getName());
    }


    protected <T> T getOriginal(MultiCodeCouponModel coupon, InterceptorContext ctx, String attributeQualifier)
    {
        if(ctx.isModified(coupon, attributeQualifier))
        {
            ItemModelContext modelContext = ModelContextUtils.getItemModelContext((AbstractItemModel)coupon);
            return (T)modelContext.getOriginalValue(attributeQualifier);
        }
        ModelService modelService = Objects.<ModelService>requireNonNull(ctx.getModelService());
        return (T)modelService.getAttributeValue(coupon, attributeQualifier);
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
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
