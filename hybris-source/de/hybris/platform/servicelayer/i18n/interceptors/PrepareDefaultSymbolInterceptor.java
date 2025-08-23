package de.hybris.platform.servicelayer.i18n.interceptors;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import java.util.Currency;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class PrepareDefaultSymbolInterceptor implements PrepareInterceptor
{
    private I18NService i18NService;
    private static final Logger LOG = Logger.getLogger(PrepareDefaultSymbolInterceptor.class);


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CurrencyModel)
        {
            CurrencyModel currModel = (CurrencyModel)model;
            String isoCode = currModel.getIsocode();
            if(isoCode != null && (currModel.getSymbol() == null || "".equals(currModel.getSymbol())))
            {
                try
                {
                    Currency cur = this.i18NService.getBestMatchingJavaCurrency(isoCode);
                    currModel.setSymbol(cur.getSymbol(LocaleHelper.getPersistenceLocale()));
                }
                catch(IllegalArgumentException e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("No currency found for isocode = " + isoCode);
                    }
                }
            }
        }
    }


    @Required
    public void setI18NService(I18NService i18nService)
    {
        this.i18NService = i18nService;
    }
}
