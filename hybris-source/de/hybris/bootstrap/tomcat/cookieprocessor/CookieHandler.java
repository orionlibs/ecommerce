package de.hybris.bootstrap.tomcat.cookieprocessor;

import java.util.Optional;
import javax.servlet.http.Cookie;

public interface CookieHandler
{
    Optional<String> getSameSiteParameter(Cookie paramCookie);
}
