package de.hybris.platform.webservicescommons.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.crypto.password.PasswordEncoder;

public class OAuthClientInterceptor implements PrepareInterceptor<OAuthClientDetailsModel>
{
    private PasswordEncoder clientSecretEncoder;


    public void onPrepare(OAuthClientDetailsModel model, InterceptorContext interceptorContext) throws InterceptorException
    {
        if(interceptorContext.isModified(model, "clientSecret") && model.getClientSecret() != null)
        {
            model.setClientSecret(this.clientSecretEncoder.encode(model.getClientSecret()));
        }
    }


    public PasswordEncoder getClientSecretEncoder()
    {
        return this.clientSecretEncoder;
    }


    @Required
    public void setClientSecretEncoder(PasswordEncoder clientSecretEncoder)
    {
        this.clientSecretEncoder = clientSecretEncoder;
    }
}
