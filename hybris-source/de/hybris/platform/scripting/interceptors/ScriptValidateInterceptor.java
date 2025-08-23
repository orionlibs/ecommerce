package de.hybris.platform.scripting.interceptors;

import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

public class ScriptValidateInterceptor implements ValidateInterceptor<ScriptModel>
{
    public void onValidate(ScriptModel scriptModel, InterceptorContext ctx) throws InterceptorException
    {
        if(scriptModel.getCode().contains("/"))
        {
            throw new InterceptorException("Illegal character('/') in script code");
        }
    }
}
