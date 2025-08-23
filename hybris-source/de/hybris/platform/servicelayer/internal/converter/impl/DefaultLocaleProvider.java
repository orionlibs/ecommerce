package de.hybris.platform.servicelayer.internal.converter.impl;

import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.internal.service.ServicelayerUtils;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DefaultLocaleProvider implements LocaleProvider
{
    private transient I18NService i18NService;


    public DefaultLocaleProvider(I18NService i18NService)
    {
        ServicesUtil.validateParameterNotNull(i18NService, "given LocalizationService is null!");
        this.i18NService = i18NService;
    }


    protected I18NService service()
    {
        if(this.i18NService == null)
        {
            this.i18NService = (I18NService)ServicelayerUtils.getApplicationContext().getBean("i18nService");
        }
        return this.i18NService;
    }


    public Locale getCurrentDataLocale()
    {
        return service().getCurrentLocale();
    }


    public Locale toDataLocale(Locale external)
    {
        return service().getBestMatchingLocale(external);
    }


    public List<Locale> getFallbacks(Locale requested)
    {
        if(isFallbackEnabled())
        {
            return Arrays.asList(service().getFallbackLocales(requested));
        }
        return null;
    }


    public boolean isFallbackEnabled()
    {
        return service().isLocalizationFallbackEnabled();
    }
}
