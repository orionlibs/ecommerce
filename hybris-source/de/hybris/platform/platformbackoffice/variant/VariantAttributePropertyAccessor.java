package de.hybris.platform.platformbackoffice.variant;

import com.hybris.backoffice.variants.BackofficeVariantsService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.VariantsService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.Ordered;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

public class VariantAttributePropertyAccessor implements PropertyAccessor, Ordered
{
    public static final Logger LOG = LoggerFactory.getLogger(VariantAttributePropertyAccessor.class);
    private static final int DEFAULT_ORDER = 1200;
    private TypeFacade typeFacade;
    private CockpitLocaleService cockpitLocaleService;
    private CockpitUserService cockpitUserService;
    private BackofficeVariantsService backofficeVariantsService;


    public Class<?>[] getSpecificTargetClasses()
    {
        return new Class[] {VariantProductModel.class};
    }


    public boolean canRead(EvaluationContext evaluationContext, Object target, String qualifier)
    {
        return canHandle(target, qualifier);
    }


    public boolean canWrite(EvaluationContext evaluationContext, Object target, String qualifier)
    {
        return canHandle(target, qualifier);
    }


    protected boolean canHandle(Object target, String qualifier)
    {
        VariantProductModel variant = (VariantProductModel)target;
        ProductModel baseProduct = variant.getBaseProduct();
        if(null == baseProduct)
        {
            return false;
        }
        VariantTypeModel variantType = baseProduct.getVariantType();
        if(variantType == null)
        {
            return false;
        }
        String code = variantType.getCode();
        return getBackofficeVariantsService().getVariantAttributes(code).contains(qualifier);
    }


    private boolean isLocalized(Object target, String qualifier)
    {
        DataAttribute attribute = this.typeFacade.getAttribute(target, qualifier);
        return attribute.isLocalized();
    }


    public TypedValue read(EvaluationContext evaluationContext, Object target, String qualifier)
    {
        VariantProductModel variant = (VariantProductModel)target;
        if(isLocalized(variant, qualifier))
        {
            Map<Locale, Object> localizedValues = getBackofficeVariantsService().getLocalizedVariantAttributeValue(variant, qualifier);
            Map<Locale, Object> allowedLocalizedValues = getEnabledLocalization(localizedValues);
            return new TypedValue(allowedLocalizedValues);
        }
        return new TypedValue(getBackofficeVariantsService().getVariantAttributeValue(variant, qualifier));
    }


    public void write(EvaluationContext evaluationContext, Object target, String qualifier, Object value)
    {
        VariantProductModel variant = (VariantProductModel)target;
        if(isLocalized(target, qualifier))
        {
            Map<Locale, Object> enabledLocalization = getEnabledLocalization(value);
            getBackofficeVariantsService().setLocalizedVariantAttributeValue(variant, qualifier, enabledLocalization);
        }
        else
        {
            getBackofficeVariantsService().setVariantAttributeValue(variant, qualifier, value);
        }
    }


    protected Map<Locale, Object> getEnabledLocalization(Object locales)
    {
        Validate.isInstanceOf(Map.class, locales);
        Map<Locale, Object> localesMap = (Map<Locale, Object>)locales;
        List<Locale> enabledDataLocales = this.cockpitLocaleService.getEnabledDataLocales(this.cockpitUserService.getCurrentUser());
        return (Map<Locale, Object>)localesMap.entrySet().stream().filter(e -> enabledDataLocales.contains(e.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    @Deprecated(since = "1808", forRemoval = true)
    public VariantsService getVariantsService()
    {
        return (VariantsService)this.backofficeVariantsService;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public void setVariantsService(VariantsService variantsService)
    {
    }


    @Required
    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public void setVariantService(VariantsService variantService)
    {
    }


    public void setCockpitLocaleService(CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public void setModelService(ModelService modelService)
    {
    }


    public void setCockpitUserService(CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }


    public int getOrder()
    {
        return 1200;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public void setSessionService(SessionService sessionService)
    {
    }


    @Deprecated(since = "1808", forRemoval = true)
    public void setUserService(UserService userService)
    {
    }


    @Deprecated(since = "1808", forRemoval = true)
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
    }


    @Required
    public void setBackofficeVariantsService(BackofficeVariantsService backofficeVariantsService)
    {
        this.backofficeVariantsService = backofficeVariantsService;
    }


    public BackofficeVariantsService getBackofficeVariantsService()
    {
        return this.backofficeVariantsService;
    }
}
