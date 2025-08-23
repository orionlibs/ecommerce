package de.hybris.platform.servicelayer.security.auth.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.security.auth.AuthenticationService;
import de.hybris.platform.servicelayer.security.auth.InvalidCredentialsException;
import de.hybris.platform.servicelayer.security.spring.UserToAuthenticationConverter;
import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.ws.security.WSPasswordCallback;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class HybrisPasswordCallback implements CallbackHandler
{
    private AuthenticationService authenticationService;
    private UserToAuthenticationConverter userToAuthenticationConverter;


    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
    {
        if(callbacks.length != 1)
        {
            throw new IllegalArgumentException("Unexpected number of callbacks: " + callbacks.length);
        }
        WSPasswordCallback callback = (WSPasswordCallback)callbacks[0];
        try
        {
            UserModel user = this.authenticationService.login(callback.getIdentifier(), callback.getPassword());
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = this.userToAuthenticationConverter.convert(user);
            securityContext.setAuthentication(authentication);
        }
        catch(InvalidCredentialsException e)
        {
            throw new SecurityException("Invalid credentials");
        }
    }


    @Required
    public void setAuthenticationService(AuthenticationService authenticationService)
    {
        this.authenticationService = authenticationService;
    }


    @Required
    public void setUserToAuthenticationConverter(UserToAuthenticationConverter userToAuthenticationConverter)
    {
        this.userToAuthenticationConverter = userToAuthenticationConverter;
    }
}
