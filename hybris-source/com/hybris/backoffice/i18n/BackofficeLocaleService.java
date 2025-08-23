/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.i18n;

import com.google.common.base.Splitter;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.i18n.impl.DefaultCockpitLocaleService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Manages the UI locale. Uses the ZK locale and the on-premise i18nService locale as well.
 */
public class BackofficeLocaleService extends DefaultCockpitLocaleService
{
    public static final String LANG_PACKS_PROPERTY = "lang.packs";
    private List<Locale> uiLocales;
    private I18NService i18nService;
    private CockpitProperties globalProperties;


    @Override
    public List<Locale> getAllLocales()
    {
        return new ArrayList<Locale>(i18nService.getSupportedLocales());
    }


    @Override
    public void setCurrentLocale(final Locale locale)
    {
        super.setCurrentLocale(locale);
        this.i18nService.setCurrentLocale(locale);
    }


    @Override
    public List<Locale> getAllUILocales()
    {
        if(uiLocales == null)
        {
            initializeUILocales();
        }
        return uiLocales;
    }


    private synchronized void initializeUILocales()
    {
        if(uiLocales == null)
        {
            final String isoCodes = globalProperties.getProperty(LANG_PACKS_PROPERTY);
            if(StringUtils.isNotBlank(isoCodes))
            {
                final List<String> uiLocaleCodes = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(isoCodes);
                if(!uiLocaleCodes.isEmpty())
                {
                    final List<Locale> tmpLocales = new ArrayList<>(uiLocaleCodes.size());
                    final List<Locale> allLocales = getAllLocales();
                    for(final Locale locale : allLocales)
                    {
                        if(uiLocaleCodes.contains(locale.toString()))
                        {
                            tmpLocales.add(locale);
                        }
                    }
                    uiLocales = Collections.unmodifiableList(tmpLocales);
                }
                else
                {
                    uiLocales = Collections.emptyList();
                }
            }
            else
            {
                uiLocales = Collections.emptyList();
            }
        }
    }


    @Required
    public void setI18nService(final I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    @Required
    public void setGlobalProperties(final CockpitProperties globalProperties)
    {
        this.globalProperties = globalProperties;
    }


    @Override
    public void reset()
    {
        super.reset();
        uiLocales = null;
    }
}
