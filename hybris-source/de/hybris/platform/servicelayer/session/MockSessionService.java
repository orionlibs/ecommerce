package de.hybris.platform.servicelayer.session;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.impl.DefaultSessionService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MockSessionService extends DefaultSessionService
{
    private final ThreadLocal<Session> sessionTL = new ThreadLocal<>();
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();


    public synchronized Session getCurrentSession()
    {
        Session session = this.sessionTL.get();
        if(session != null)
        {
            return session;
        }
        session = createNewSession();
        return session;
    }


    public Session getSession(String id)
    {
        return null;
    }


    public Session createNewSession()
    {
        Session session = createSession();
        this.sessionTL.set(session);
        this.sessions.put(session.getSessionId(), session);
        return session;
    }


    public Object executeInLocalView(SessionExecutionBody body)
    {
        return body.execute();
    }


    public void closeSession(Session session)
    {
        this.sessions.remove(session.getSessionId());
        this.sessionTL.set(null);
    }


    public Session createSession()
    {
        return (Session)new MockSession();
    }


    public Object executeInLocalView(SessionExecutionBody body, UserModel model)
    {
        return body.execute();
    }


    public <T> T getOrLoadAttribute(String name, SessionService.SessionAttributeLoader<T> loader)
    {
        T result = (T)getAttribute(name);
        if(result == null)
        {
            result = (T)getAttribute(name);
            if(result == null)
            {
                result = (T)loader.load();
                setAttribute(name, result);
            }
        }
        return result;
    }
}
