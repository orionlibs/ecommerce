package de.hybris.platform.servicelayer.session;

import de.hybris.platform.servicelayer.session.impl.DefaultSession;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MockSession extends DefaultSession
{
    private long sessionIdCounter = 1L;
    private final Map<String, Object> attributes = new ConcurrentHashMap<>();
    private final String sessionId;


    public String getSessionId()
    {
        return this.sessionId;
    }


    public MockSession()
    {
        this.sessionId = String.valueOf(this.sessionIdCounter++);
    }


    public <T> Map<String, T> getAllAttributes()
    {
        return Collections.unmodifiableMap((Map)this.attributes);
    }


    public <T> T getAttribute(String name)
    {
        return (T)this.attributes.get(name);
    }


    public void setAttribute(String name, Object value)
    {
        this.attributes.put(name, value);
    }
}
