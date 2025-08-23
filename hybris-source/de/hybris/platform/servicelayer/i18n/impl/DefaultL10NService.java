package de.hybris.platform.servicelayer.i18n.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.localization.TypeLocalization;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import org.springframework.beans.factory.annotation.Required;

public class DefaultL10NService implements L10NService
{
    private I18NService i18nService;
    private CommonI18NService commonI18NService;
    private ModelService modelService;


    public ResourceBundle getResourceBundle()
    {
        return getResourceBundle(this.i18nService.getAllLocales(this.i18nService.getCurrentLocale()));
    }


    protected Map<Locale, Properties> getLocalizations()
    {
        Map<Locale, Properties> isocodesLocalizations = new HashMap<>();
        Map<Language, Properties> languagesLocalizations = TypeLocalization.getInstance().getLocalizations();
        for(Language language : languagesLocalizations.keySet())
        {
            LanguageModel languageModel = (LanguageModel)this.modelService.get(language);
            Locale locale = this.commonI18NService.getLocaleForLanguage(languageModel);
            isocodesLocalizations.put(this.i18nService.getBestMatchingLocale(locale), languagesLocalizations.get(language));
        }
        return isocodesLocalizations;
    }


    public ResourceBundle getResourceBundle(Locale[] locales)
    {
        Map<Locale, Properties> localizations;
        List<ResourceBundle> bundles = null;
        try
        {
            localizations = getLocalizations();
        }
        catch(JaloObjectNoLongerValidException jonlve)
        {
            TypeLocalization.getInstance().clearLocalizationCache();
            localizations = getLocalizations();
        }
        for(Locale locale : locales)
        {
            Properties properties = localizations.get(this.i18nService.getBestMatchingLocale(locale));
            if(properties != null)
            {
                localizations.remove(locale);
                ResourceBundle bundle = createBundle(properties);
                if(bundles == null)
                {
                    bundles = new ArrayList<>();
                }
                bundles.add(bundle);
            }
        }
        return CompositeResourceBundle.getBundle(bundles);
    }


    private ResourceBundle createBundle(Properties properties)
    {
        ResourceBundle bundle = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            String comments = null;
            properties.store(out, comments);
            bundle = new PropertyResourceBundle(new ByteArrayInputStream(out.toByteArray()));
        }
        catch(IOException ex)
        {
            throw new SystemException("Error when creating resource bundle!", ex);
        }
        return bundle;
    }


    public String getLocalizedString(String resKey, Object[] arguments)
    {
        String value = getLocalizedString(resKey);
        if(value != null && arguments != null && arguments.length != 0)
        {
            Locale locale = JaloSession.getCurrentSession().getSessionContext().getLocale();
            MessageFormat messageFormat = new MessageFormat(value, locale);
            return messageFormat.format(arguments, new StringBuffer(), (FieldPosition)null).toString();
        }
        return value;
    }


    public String getLocalizedString(String resKey)
    {
        String result;
        try
        {
            result = getResourceBundle().getString(resKey);
        }
        catch(RuntimeException re)
        {
            return resKey;
        }
        return result;
    }


    public ResourceBundle getResourceBundle(String baseName)
    {
        return getResourceBundle(baseName, this.i18nService.getAllLocales(this.i18nService.getCurrentLocale()));
    }


    public ResourceBundle getResourceBundle(String baseName, Locale[] locales)
    {
        if(locales == null || locales.length == 0)
        {
            throw new IllegalArgumentException("At least one Locale must be given (was null or empty)");
        }
        return (locales.length == 1) ? ResourceBundle.getBundle(baseName, locales[0]) : CompositeResourceBundle.getBundle(baseName, locales);
    }


    @Required
    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
