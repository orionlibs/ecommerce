package de.hybris.platform.servicelayer.security.spring;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.security.auth.AuthenticationService;
import de.hybris.platform.servicelayer.security.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Deprecated(since = "ages", forRemoval = true)
public class HybrisAuthenticationProvider implements AuthenticationProvider
{
    private AuthenticationService authenticationService;
    private UserToAuthenticationConverter userToAuthenticationConverter;


    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        UserModel user;
        String username = null;
        String password = null;
        try
        {
            username = (String)authentication.getPrincipal();
            password = (String)authentication.getCredentials();
        }
        catch(ClassCastException e)
        {
            throw new AuthenticationServiceException(e.getLocalizedMessage(), e);
        }
        try
        {
            user = this.authenticationService.login(username, password);
        }
        catch(InvalidCredentialsException e)
        {
            throw new BadCredentialsException("Username/password combination not found");
        }
        return this.userToAuthenticationConverter.convert(user);
    }


    public boolean supports(Class<?> authentication)
    {
        return Authentication.class.isAssignableFrom(authentication);
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
