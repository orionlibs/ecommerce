package de.hybris.platform.servicelayer.session.impl;

import de.hybris.platform.servicelayer.session.SessionService;
import java.util.UUID;

public class DefaultSessionTokenService
{
    private static final String SESSION_TOKEN_KEY = "_core_session_token_";
    private SessionService sessionService;


    public String getOrCreateSessionToken()
    {
        return (String)this.sessionService.getOrLoadAttribute("_core_session_token_", () -> UUID.randomUUID().toString());
    }


    public void setSessionToken(String token)
    {
        this.sessionService.setAttribute("_core_session_token_", token);
    }


    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
