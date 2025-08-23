package de.hybris.platform.samlsinglesignon;

import de.hybris.platform.core.model.user.UserModel;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SamlLoginService
{
    void storeLoginToken(HttpServletResponse paramHttpServletResponse, UserModel paramUserModel, String paramString);


    Optional<String> getRedirectionUrl(HttpServletRequest paramHttpServletRequest);
}
