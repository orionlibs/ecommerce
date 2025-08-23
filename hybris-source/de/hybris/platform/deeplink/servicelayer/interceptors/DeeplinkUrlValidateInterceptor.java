package de.hybris.platform.deeplink.servicelayer.interceptors;

import de.hybris.platform.deeplink.dao.DeeplinkUrlDao;
import de.hybris.platform.deeplink.model.rules.DeeplinkUrlModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.util.localization.Localization;

public class DeeplinkUrlValidateInterceptor implements ValidateInterceptor
{
    private static final String INVALID_CODE_CHAR = "-";
    private DeeplinkUrlDao deeplinkUrlDao;


    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof DeeplinkUrlModel && ((DeeplinkUrlModel)model).getCode().contains("-"))
        {
            throw new InterceptorException(
                            getLocalizedString("validation.cant_contains_char", new Object[] {"code", "-"}));
        }
    }


    public DeeplinkUrlDao getDeeplinkUrlDao()
    {
        return this.deeplinkUrlDao;
    }


    public void setDeeplinkUrlDao(DeeplinkUrlDao deeplinkUrlDao)
    {
        this.deeplinkUrlDao = deeplinkUrlDao;
    }


    protected String getLocalizedString(String key, Object... arguments)
    {
        return Localization.getLocalizedString(key, arguments);
    }
}
