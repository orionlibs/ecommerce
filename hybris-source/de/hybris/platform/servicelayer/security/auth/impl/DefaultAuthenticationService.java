package de.hybris.platform.servicelayer.security.auth.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.security.CannotDecodePasswordException;
import de.hybris.platform.jalo.security.PasswordEncoderNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.security.auth.AuthenticationService;
import de.hybris.platform.servicelayer.security.auth.InvalidCredentialsException;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAuthenticationService extends AbstractBusinessService implements AuthenticationService
{
    public static final String LOGIN_ANONYMOUS_ALWAYS_DISABLED = "login.anonymous.always.disabled";
    private transient UserService userService;
    private transient PasswordEncoderService passwordEncoderService;


    public UserModel login(String login, String password) throws InvalidCredentialsException
    {
        UserModel user = checkCredentials(login, password);
        this.userService.setCurrentUser(user);
        return user;
    }


    public UserModel checkCredentials(String login, String password) throws InvalidCredentialsException
    {
        UserModel user = null;
        try
        {
            user = this.userService.getUserForUID(login);
            if(checkCustomerAnonymousAndAnonymousLoginDisable(user))
            {
                throw buildInvalidCredentialsException("Anonymous login is disabled");
            }
            if(user.isLoginDisabled() || (user
                            .getDeactivationDate() != null && user.getDeactivationDate().toInstant().isBefore(Instant.now())))
            {
                throw buildInvalidCredentialsException();
            }
            if(!this.passwordEncoderService.isValid(user, password))
            {
                throw buildInvalidCredentialsException();
            }
        }
        catch(UnknownIdentifierException e)
        {
            throw buildInvalidCredentialsException();
        }
        catch(CannotDecodePasswordException e)
        {
            throw buildInvalidCredentialsException();
        }
        catch(PasswordEncoderNotFoundException e)
        {
            throw buildInvalidCredentialsException();
        }
        return user;
    }


    public void logout()
    {
        getSessionService().closeCurrentSession();
    }


    protected InvalidCredentialsException buildInvalidCredentialsException()
    {
        return buildInvalidCredentialsException("invalid credentials");
    }


    protected InvalidCredentialsException buildInvalidCredentialsException(String message)
    {
        return new InvalidCredentialsException(message);
    }


    private boolean checkCustomerAnonymousAndAnonymousLoginDisable(UserModel user)
    {
        return (user instanceof de.hybris.platform.core.model.user.CustomerModel && this.userService
                        .isAnonymousUser(user) && Config.getBoolean("login.anonymous.always.disabled", true));
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setPasswordEncoderService(PasswordEncoderService passwordEncoderService)
    {
        this.passwordEncoderService = passwordEncoderService;
    }
}
