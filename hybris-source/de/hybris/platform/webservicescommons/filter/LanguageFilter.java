package de.hybris.platform.webservicescommons.filter;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

public class LanguageFilter extends OncePerRequestFilter
{
    private static final Logger LOG = LoggerFactory.getLogger(LanguageFilter.class);
    private CommonI18NService commonI18nService;
    private Optional<I18NService> i18NService;


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        try
        {
            Enumeration<Locale> locales = request.getLocales();
            while(locales.hasMoreElements())
            {
                Locale locale = locales.nextElement();
                LanguageModel languageModel = getLanguageModel(locale);
                if(languageModel != null)
                {
                    this.commonI18nService.setCurrentLanguage(languageModel);
                    this.i18NService.ifPresent(i18n -> i18n.setCurrentLocale(locale));
                    break;
                }
            }
        }
        catch(RuntimeException e)
        {
            LOG.debug("Failed to set locale for request.", e);
        }
        filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
    }


    protected LanguageModel getLanguageModel(Locale locale)
    {
        try
        {
            return this.commonI18nService.getLanguage(locale.getLanguage());
        }
        catch(UnknownIdentifierException e)
        {
            return null;
        }
    }


    public void setCommonI18nService(CommonI18NService commonI18nService)
    {
        this.commonI18nService = commonI18nService;
    }


    @Autowired(required = false)
    public void setI18NService(I18NService i18nService)
    {
        this.i18NService = Optional.ofNullable(i18nService);
    }
}
