package de.hybris.platform.servicelayer.user;

import de.hybris.platform.core.model.user.UserModel;

public interface UserNetCheckingStrategy
{
    boolean isNetUser(UserModel paramUserModel);
}
