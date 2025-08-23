package de.hybris.platform.servicelayer.session;

import java.util.Map;

public interface Session
{
    void setAttribute(String paramString, Object paramObject);


    <T> T getAttribute(String paramString);


    <T> Map<String, T> getAllAttributes();


    String getSessionId();


    void removeAttribute(String paramString);
}
