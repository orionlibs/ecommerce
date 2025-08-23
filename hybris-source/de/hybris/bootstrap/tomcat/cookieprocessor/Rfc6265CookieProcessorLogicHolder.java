package de.hybris.bootstrap.tomcat.cookieprocessor;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class Rfc6265CookieProcessorLogicHolder
{
    private static final AtomicReference<CookieHandler> COOKIE_HANDLER_HOLDER = new AtomicReference<>();
    private static final AtomicReference<LogHandler> LOG_HANDLER_HOLDER = new AtomicReference<>();


    public static Optional<CookieHandler> getCookieHandler()
    {
        return Optional.ofNullable(COOKIE_HANDLER_HOLDER.get());
    }


    public static void setCookieHandler(CookieHandler cookieHandler)
    {
        COOKIE_HANDLER_HOLDER.set(cookieHandler);
    }


    public static Optional<LogHandler> getLogHandler()
    {
        return Optional.ofNullable(LOG_HANDLER_HOLDER.get());
    }


    public static void setLogHandler(LogHandler logHandler)
    {
        LOG_HANDLER_HOLDER.set(logHandler);
    }
}
