package de.hybris.platform.audit.view.impl;

import de.hybris.platform.audit.internal.config.AuditReportItemNameResolvable;
import de.hybris.platform.audit.view.AuditReportItemNameResolver;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.util.Config;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAuditReportItemNameResolver implements AuditReportItemNameResolver
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAuditReportItemNameResolver.class);
    private static final String SEPARATOR = " / ";
    private CommonI18NService commonI18NService;
    private L10NService l10NService;


    public String getName(Set<String> langIsoCodes, AuditReportItemNameResolvable item)
    {
        String name;
        if(StringUtils.isNotEmpty(item.getDisplayName()))
        {
            name = item.getDisplayName();
        }
        else if(StringUtils.isNotEmpty(item.getDisplayKey()))
        {
            name = getNameFromConfig(item.getDisplayKey()).orElse(getNameFromLocales(langIsoCodes, item.getDisplayKey()));
            if(StringUtils.isEmpty(name))
            {
                LOG.warn("No value found for displayKey: {} in Locales. Using defaultName value ({}) as the name instead.", item
                                .getDisplayKey(), item.getDefaultName());
                return item.getDefaultName();
            }
        }
        else
        {
            name = item.getDefaultName();
        }
        return name;
    }


    private Optional<String> getNameFromConfig(String displayKey)
    {
        return Optional.<String>ofNullable(Config.getParameter(displayKey)).filter(StringUtils::isNotEmpty);
    }


    private String getNameFromLocales(Set<String> langIsoCodes, String displayKey)
    {
        StringBuilder sb = new StringBuilder();
        langIsoCodes
                        .forEach(isoCode -> getLocalizedString(isoCode, displayKey.toLowerCase()).ifPresent(()));
        return (sb.length() == 0) ? "" : sb.substring(0, sb.lastIndexOf(" / ")).trim();
    }


    private Optional<String> getLocalizedString(String langIsoCode, String key)
    {
        try
        {
            return Optional.<Locale>ofNullable(this.commonI18NService.getLocaleForIsoCode(langIsoCode))
                            .map(locale -> this.l10NService.getResourceBundle(new Locale[] {locale})).map(resource -> resource.getString(key))
                            .filter(StringUtils::isNotEmpty);
        }
        catch(MissingResourceException | IllegalArgumentException e)
        {
            LOG.debug(e.getMessage(), e);
            return Optional.empty();
        }
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setL10NService(L10NService l10NService)
    {
        this.l10NService = l10NService;
    }
}
