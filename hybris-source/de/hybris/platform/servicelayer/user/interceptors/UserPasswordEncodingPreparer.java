package de.hybris.platform.servicelayer.user.interceptors;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import org.springframework.beans.factory.annotation.Required;

public class UserPasswordEncodingPreparer implements PrepareInterceptor
{
    private PasswordEncoderService passwordEncoderService;


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof UserModel)
        {
            UserModel user = (UserModel)model;
            if(user.getPasswordEncoding() == null || user.getPasswordEncoding().isEmpty())
            {
                if(!this.passwordEncoderService.isSupportedEncoding("*"))
                {
                    throw new InterceptorException("Attribute User.passwordEncoding is empty and a default password encoder for the key '*' was not found! ");
                }
                user.setPasswordEncoding("*");
            }
        }
    }


    @Required
    public void setPasswordEncoderService(PasswordEncoderService passwordEncoderService)
    {
        this.passwordEncoderService = passwordEncoderService;
    }
}
