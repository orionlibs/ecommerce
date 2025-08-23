package de.hybris.platform.servicelayer.i18n.util;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.List;
import java.util.stream.Collectors;

public final class LanguageUtils
{
    public static boolean isLanguagePresent(String isoCode)
    {
        CommonI18NService commonI18NService = (CommonI18NService)Registry.getApplicationContext().getBean("commonI18NService", CommonI18NService.class);
        List<LanguageModel> language = (List<LanguageModel>)commonI18NService.getAllLanguages().stream().filter(lang -> lang.getIsocode().equals(isoCode)).collect(Collectors.toList());
        return !language.isEmpty();
    }
}
