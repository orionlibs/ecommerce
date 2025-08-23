package de.hybris.platform.cockpit.util;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.zkoss.util.resource.LabelLocator;
import org.zkoss.zk.ui.Executions;

public class CockpitLocator implements LabelLocator
{
    private static final Logger LOG = LoggerFactory.getLogger(CockpitLocator.class);
    private final String resourceFolder = "/localization/";


    public URL locate(Locale locale) throws Exception
    {
        URL url = locateInternal(locale);
        if("true".equalsIgnoreCase(UITools.getCockpitParameter("fallbackLanguageLocalization.enabled", Executions.getCurrent())) && url == null)
        {
            url = getFallbackLanguageUrl(locale);
        }
        if(url == null)
        {
            url = (new ClassPathResource(getResourceFolder() + "i3-label_en.properties")).getURL();
        }
        return url;
    }


    private final URL getFallbackLanguageUrl(Locale locale) throws IOException
    {
        I18NService i18nService = (I18NService)Registry.getApplicationContext().getBean("i18nService", I18NService.class);
        List<Locale> fallbackLocales = Arrays.asList(i18nService.getFallbackLocales(locale));
        if(CollectionUtils.isNotEmpty(fallbackLocales))
        {
            for(Locale fallbackLocale : fallbackLocales)
            {
                URL url = locateInternal(fallbackLocale);
                if(url != null)
                {
                    return url;
                }
            }
        }
        return null;
    }


    protected URL locateInternal(Locale locale) throws IOException
    {
        ClassPathResource ressource = new ClassPathResource(getResourceFolder() + getResourceFolder());
        URL url = null;
        try
        {
            url = ressource.getURL();
        }
        catch(FileNotFoundException fnfe)
        {
            LOG.debug("Can't find i3 property file for locale [" + locale.toString() + "] in classpath");
        }
        return url;
    }


    private final String getI3LabelName(Locale locale)
    {
        return (locale == null) ? "i3-label_en.properties" : ("i3-label_" + locale.getLanguage() + ".properties");
    }


    protected String getResourceFolder()
    {
        return "/localization/";
    }
}
