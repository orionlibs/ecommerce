package de.hybris.platform.servicelayer.user.interceptors;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import org.springframework.beans.factory.annotation.Required;

public class UserPasswordEncodingValidator implements ValidateInterceptor
{
    private PasswordEncoderService passwordEncoderService;


    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof UserModel)
        {
            UserModel user = (UserModel)model;
            if(!this.passwordEncoderService.isSupportedEncoding(user.getPasswordEncoding()))
            {
                throw new InterceptorException("The given password encoding '" + user.getPasswordEncoding() + "' for the user '" + user
                                .getUid() + "' is unknown. Available encodings are: " + this.passwordEncoderService
                                .getSupportedEncodings());
            }
        }
    }


    @Required
    public void setPasswordEncoderService(PasswordEncoderService passwordEncoderService)
    {
        this.passwordEncoderService = passwordEncoderService;
    }
}
