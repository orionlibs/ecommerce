package de.hybris.platform.servicelayer.security.spring;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.security.auth.AuthenticationService;
import de.hybris.platform.servicelayer.security.auth.InvalidCredentialsException;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Deprecated(since = "ages", forRemoval = true)
public class HybrisGroupBasedAuthenticationProvider implements AuthenticationProvider
{
    private AuthenticationService authenticationService;
    private UserToAuthenticationConverter userToAuthenticationConverter;
    private Set<String> authorizedGroups;


    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
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
            UserModel user = this.authenticationService.login(username, password);
            for(PrincipalGroupModel group : user.getGroups())
            {
                if(this.authorizedGroups.contains(group.getUid()))
                {
                    return this.userToAuthenticationConverter.convert(user);
                }
            }
            this.authenticationService.logout();
            throw new InvalidCredentialsException("User does not belong to a group authorized for login.");
        }
        catch(InvalidCredentialsException e)
        {
            throw new BadCredentialsException("Username/password combination not found", e);
        }
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


    @Required
    public void setAuthorizedGroups(Set<String> authorizedGroups)
    {
        this.authorizedGroups = authorizedGroups;
    }
}
