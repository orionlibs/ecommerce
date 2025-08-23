package de.hybris.platform.samlsinglesignon;

import de.hybris.platform.core.model.user.UserModel;
import java.util.Collection;

public interface SSOUserService
{
    UserModel getOrCreateSSOUser(String paramString1, String paramString2, Collection<String> paramCollection);
}
