package com.hybris.backoffice.variants;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.product.VariantsService;
import de.hybris.platform.product.impl.DefaultVariantsService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.variants.jalo.VariantProduct;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultBackofficeVariantsService extends DefaultVariantsService implements BackofficeVariantsService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBackofficeVariantsService.class);
    private transient VariantsService variantService;
    private transient UserService userService;
    private transient CommonI18NService commonI18NService;


    public Map<Locale, Object> getLocalizedVariantAttributeValue(VariantProductModel variant, String qualifier)
    {
        VariantProduct variantProduct = (VariantProduct)getModelService().getSource(variant);
        Object attributeValue = getLocalizedValuesForAllLanguages(qualifier, variantProduct);
        if(attributeValue != null)
        {
            return convertToLocaleMap((Map<Language, Object>)attributeValue);
        }
        return null;
    }


    private Object getLocalizedValuesForAllLanguages(String qualifier, VariantProduct variantProduct)
    {
        return getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, variantProduct, qualifier), (UserModel)this.userService
                        .getAdminUser());
    }


    public void setLocalizedVariantAttributeValue(VariantProductModel variantProductModel, String qualifier, Map<Locale, Object> localizedValues)
    {
        getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, variantProductModel, qualifier, localizedValues), (UserModel)this.userService
                        .getAdminUser());
    }


    private Map<Locale, Object> convertToLocaleMap(Map<Language, Object> languageMap)
    {
        Map<Locale, Object> localeMap = new HashMap<>();
        languageMap.forEach((language, value) -> localeMap.put(language.getLocale(), value));
        return localeMap;
    }


    private Map<LanguageModel, Object> convertToLanguageMap(Map<Locale, Object> localeMap)
    {
        Map<LanguageModel, Object> languageMap = new HashMap<>();
        localeMap.forEach((locale, value) -> {
            LanguageModel language = this.commonI18NService.getLanguage(locale.toString());
            languageMap.put(language, value);
        });
        return languageMap;
    }


    public VariantsService getVariantService()
    {
        return this.variantService;
    }


    @Required
    public void setVariantService(VariantsService variantService)
    {
        this.variantService = variantService;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }
}
