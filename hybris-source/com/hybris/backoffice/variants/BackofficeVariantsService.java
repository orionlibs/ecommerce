package com.hybris.backoffice.variants;

import de.hybris.platform.product.VariantsService;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.Locale;
import java.util.Map;

public interface BackofficeVariantsService extends VariantsService
{
    Map<Locale, Object> getLocalizedVariantAttributeValue(VariantProductModel paramVariantProductModel, String paramString);


    void setLocalizedVariantAttributeValue(VariantProductModel paramVariantProductModel, String paramString, Map<Locale, Object> paramMap);
}
