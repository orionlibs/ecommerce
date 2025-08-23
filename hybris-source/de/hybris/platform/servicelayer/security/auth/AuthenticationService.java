package de.hybris.platform.servicelayer.security.auth;

import de.hybris.platform.core.model.user.UserModel;

public interface AuthenticationService
{
    UserModel checkCredentials(String paramString1, String paramString2) throws InvalidCredentialsException;


    UserModel login(String paramString1, String paramString2) throws InvalidCredentialsException;


    void logout();
}
