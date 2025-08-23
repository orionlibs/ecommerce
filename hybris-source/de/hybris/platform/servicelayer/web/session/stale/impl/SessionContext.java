package de.hybris.platform.servicelayer.web.session.stale.impl;

import java.util.Date;

public interface SessionContext
{
    String getSessionId();


    String getSessionUserId();


    String getSessionUserPassword();


    default String getSessionUserToken()
    {
        return "";
    }


    Object getSessionAttribute(String paramString);


    void setSessionAttribute(String paramString, Object paramObject);


    boolean isSessionUserLoginDisabled();


    Date getSessionUserDeactivationDate();
}
