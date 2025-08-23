package de.hybris.platform.servicelayer.web.session.stale.impl;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.security.GeneratedPrincipal;
import de.hybris.platform.jalo.user.GeneratedUser;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.WebSessionFunctions;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RequestAndJaloBasedSessionContext implements SessionContext
{
    private final HttpServletRequest request;


    public RequestAndJaloBasedSessionContext(HttpServletRequest request)
    {
        this.request = Objects.<HttpServletRequest>requireNonNull(request, "request cannot be null.");
    }


    public String getSessionId()
    {
        return getHttpSession().<String>map(HttpSession::getId).orElse(null);
    }


    public String getSessionUserId()
    {
        return getJaloSessionUser().<String>map(GeneratedPrincipal::getUid).orElse(null);
    }


    public String getSessionUserPassword()
    {
        return getJaloSessionUser().<String>map(GeneratedUser::getEncodedPassword).orElse(null);
    }


    public String getSessionUserToken()
    {
        return getJaloSessionUser().<String>map(GeneratedUser::getRandomToken).orElse(null);
    }


    public boolean isSessionUserLoginDisabled()
    {
        return ((Boolean)getJaloSessionUser().<Boolean>map(GeneratedUser::isLoginDisabled).orElse(Boolean.valueOf(false))).booleanValue();
    }


    public Date getSessionUserDeactivationDate()
    {
        return getJaloSessionUser().<Date>map(GeneratedUser::getDeactivationDate).orElse(null);
    }


    public Object getSessionAttribute(String attributeName)
    {
        return getHttpSession().map(s -> {
            synchronized(s)
            {
                return s.getAttribute(attributeName);
            }
        }).orElse(null);
    }


    public void setSessionAttribute(String attributeName, Object attributeValue)
    {
        getHttpSession().ifPresent(s -> {
            synchronized(s)
            {
                s.setAttribute(attributeName, attributeValue);
            }
        });
    }


    private Optional<User> getJaloSessionUser()
    {
        return getHttpSession().map(WebSessionFunctions::tryGetJaloSession)
                        .filter(Predicate.not(JaloSession::isClosed))
                        .map(JaloSession::getUser);
    }


    private Optional<HttpSession> getHttpSession()
    {
        return Optional.ofNullable(this.request.getSession(false));
    }
}
