package de.hybris.platform.servicelayer.user.daos;

import de.hybris.platform.core.model.user.UserModel;

public interface UserDao
{
    UserModel findUserByUID(String paramString);
}
