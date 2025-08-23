package de.hybris.platform.servicelayer.security.spring;

import de.hybris.platform.core.model.user.UserModel;
import org.springframework.security.core.Authentication;

public interface UserToAuthenticationConverter
{
    Authentication convert(UserModel paramUserModel);
}
