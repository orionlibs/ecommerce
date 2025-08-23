package de.hybris.platform.servicelayer.i18n.interceptors;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.util.Utilities;

public class ValidateCurrencyDataInterceptor implements ValidateInterceptor
{
    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CurrencyModel)
        {
            CurrencyModel currency = (CurrencyModel)model;
            if(currency.getDigits() == null || currency.getDigits().intValue() < 0)
            {
                throw new InterceptorException("Number of digits for CurrencyModel must be greater that 0.");
            }
            if(Utilities.fuzzyEquals(0.0D, currency.getConversion().doubleValue()))
            {
                throw new InterceptorException("Conversion factory for CurrencyModel cannot equals to 0 or null");
            }
        }
    }
}
