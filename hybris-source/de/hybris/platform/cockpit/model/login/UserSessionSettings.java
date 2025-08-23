package de.hybris.platform.cockpit.model.login;

import de.hybris.platform.core.model.user.UserModel;
import java.util.Locale;
import java.util.TimeZone;

public class UserSessionSettings
{
    private final Locale locale;
    private final TimeZone timeZone;
    private final UserModel user;


    public UserSessionSettings(UserModel user, Locale loc, TimeZone timezone)
    {
        this.user = user;
        this.locale = loc;
        this.timeZone = timezone;
    }


    public Locale getLocale()
    {
        return this.locale;
    }


    public TimeZone getTimeZone()
    {
        return this.timeZone;
    }


    public UserModel getUser()
    {
        return this.user;
    }
}
