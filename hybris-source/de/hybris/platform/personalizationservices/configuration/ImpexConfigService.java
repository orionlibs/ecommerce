package de.hybris.platform.personalizationservices.configuration;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.Map;

public class ImpexConfigService
{
    public static void ignoreIfLanguageIsMissing(Map<Integer, String> line, String lang)
    {
        CommonI18NService commonI18NService = (CommonI18NService)Registry.getApplicationContext().getBean("commonI18NService", CommonI18NService.class);
        try
        {
            commonI18NService.getLanguage(lang);
        }
        catch(RuntimeException e)
        {
            line.clear();
        }
    }
}
