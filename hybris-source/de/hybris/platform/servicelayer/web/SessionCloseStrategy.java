package de.hybris.platform.servicelayer.web;

import javax.servlet.http.HttpSession;

public interface SessionCloseStrategy
{
    void closeSessionInHttpSession(HttpSession paramHttpSession);


    void setTimeoutOnHttpSessionCreation(HttpSession paramHttpSession);
}
